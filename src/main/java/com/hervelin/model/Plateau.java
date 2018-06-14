package com.hervelin.model;

import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;

import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.util.DataManager;

public class Plateau {
    private ArrayList<Case> casesDuPlateau = new ArrayList<>();
    private ArrayList<Joueur> listeDeJoueurs = new ArrayList<>(4);
    private ArrayList<CaseMur> listeDeMurs = new ArrayList<>();

    private ArrayList<Case> ListPortee = new ArrayList<>();
    private ArrayList<Case> openList = new ArrayList<>();
    private ArrayList<Case> shoot = new ArrayList<>();
    private ArrayList<Case> build = new ArrayList<>();
    private ArrayList<Case> explosion = new ArrayList<>();

    private int xTaille;
    private int yTaille;
    private Position randomPosition1;
    private Position randomPosition2;
    private Position randomPosition3;
    private Position randomPosition4;

    private static final String CHEMIN_FICHIER_CSV = "donneesDeJeu.csv";
    private static final String FILE_HEADER = "Points de vie du turnPlayer, Points d'armure du turnPlayer, Points d'attaque du turnPlayer, Points de mouvement du turnPlayer, Points de Brique du turnPlayer, Mouvement max possible, Nombre d'armes possédées, Meilleure arme dispo, Coffre à portée, Popo à portée, Armure à portée, Joueur adverse à portée, Joueur adverse à portée de tir, Joueur adverse à portée d'explosion, turnPlayer à portée de tir, turnPlayer à portée d'explosion, Construction possible, Action réalisée ";
    private static final String VIRGULE_DELIMITEUR = ",";
    private static final String LIGNE_SEPARATEUR = "\n";


    public Joueur turnPlayer;


    public Plateau(int xTaille, int yTaille, String joueur1, String joueur2) {
        this.xTaille = xTaille;
        this.yTaille = yTaille;
        randomPosition1 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        randomPosition2 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        if(isDejaExistant("2"))
            randomPosition2 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        /*imageJoueur1 = new Image("images/Perso1minimizedColor.png");
        imageJoueur2 = new Image("images/Perso2minimizedColor.png");
        System.out.println(joueur1);
        listeDeJoueurs.add(new Joueur(joueur1, randomPosition1, imageJoueur1));
        listeDeJoueurs.add(new Joueur(joueur2, randomPosition2, imageJoueur2));*/

        //Pour les test de bico sur AWT
        listeDeJoueurs.add(new Joueur(joueur1, randomPosition1, null));
        listeDeJoueurs.add(new Joueur(joueur2, randomPosition2, null));
        initialize();
    }

    public Plateau(int xTaille, int yTaille, String joueur1, String joueur2, String joueur3) {
        this.xTaille = xTaille;
        this.yTaille = yTaille;
        randomPosition1 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        randomPosition2 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        randomPosition3 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        if(isDejaExistant("3")) {
            while (isDejaExistant("3")) {
                randomPosition1 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
                randomPosition2 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
                randomPosition3 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
            }
        }
        listeDeJoueurs.add(new Joueur(joueur1, randomPosition1, null));
        listeDeJoueurs.add(new Joueur(joueur2, randomPosition2, null));
        listeDeJoueurs.add(new Joueur(joueur3, randomPosition3, null));
        initialize();
    }

    public Plateau(int xTaille, int yTaille, String joueur1, String joueur2, String joueur3, String joueur4) {
        this.xTaille = xTaille;
        this.yTaille = yTaille;
        randomPosition1 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        randomPosition2 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        randomPosition3 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        randomPosition4 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        if(isDejaExistant("4")) {
            while (isDejaExistant("4")) {
                randomPosition1 = new Position(1 + (int) (Math.random() * ((xTaille - 1) + 1)), 1 + (int) (Math.random() * ((yTaille - 1) + 1)));
                randomPosition2 = new Position(1 + (int) (Math.random() * ((xTaille - 1) + 1)), 1 + (int) (Math.random() * ((yTaille - 1) + 1)));
                randomPosition3 = new Position(1 + (int) (Math.random() * ((xTaille - 1) + 1)), 1 + (int) (Math.random() * ((yTaille - 1) + 1)));
                randomPosition4 = new Position(1 + (int) (Math.random() * ((xTaille - 1) + 1)), 1 + (int) (Math.random() * ((yTaille - 1) + 1)));
            }
        }
        listeDeJoueurs.add(new Joueur(joueur1, randomPosition1, null));
        listeDeJoueurs.add(new Joueur(joueur2, randomPosition2, null));
        listeDeJoueurs.add(new Joueur(joueur3, randomPosition3, null));
        listeDeJoueurs.add(new Joueur(joueur4, randomPosition4, null));
        initialize();
    }

    ArrayList<Case> getCasesDuPlateau() {
        return casesDuPlateau;
    }

    public int size() {
        return casesDuPlateau.size();
    }

    public Case get(int index) {
        return casesDuPlateau.get(index);
    }

    public void add(Case c) {
        casesDuPlateau.add(c);
    }

    public Case remove(int index) {
        return casesDuPlateau.remove(index);
    }

    public void initialize() {
        turnPlayer = listeDeJoueurs.get(0);
        creerPlateau();
        creerMurs();
        creerArmes();
        creerPopos();
        creerArmures();
        creerJoueurs();
        System.out.println("initialisation plateau Ok");
    }

    public void joueurSuivant(String nbJoueurs) {
        int indexDuJoueurActuel = listeDeJoueurs.indexOf(turnPlayer);
        if(indexDuJoueurActuel != (Integer.valueOf(nbJoueurs)-1)) {
            turnPlayer = listeDeJoueurs.get(indexDuJoueurActuel +1);
            if(turnPlayer.isIA()) {
                turnPlayer.setPtAttaque(turnPlayer.getPtAttaque() + lancerUnDe());
                turnPlayer.setPtMouvement(lancerDeuxDes());
            }
        }
        else {
            turnPlayer = listeDeJoueurs.get(0);
            if(turnPlayer.isIA()) {
                turnPlayer.setPtAttaque(lancerUnDe());
                turnPlayer.setPtMouvement(lancerDeuxDes());
            }
        }
    }

    public boolean isDejaExistant(String nbJoueurs) {
        switch(nbJoueurs) {
            case "2" :
                if(randomPosition1 == randomPosition2)
                    return true;
                break;

            case "3" :
                if(randomPosition1 == randomPosition2 || randomPosition1 == randomPosition3 || randomPosition2 == randomPosition3)
                    return true;
                break;

            case "4" :
                if(randomPosition1 == randomPosition2 || randomPosition1 == randomPosition3 || randomPosition1 == randomPosition4
                        || randomPosition2 == randomPosition3 || randomPosition2 == randomPosition4 || randomPosition3 == randomPosition4)
                    return true;
            default :
                if(randomPosition1 == randomPosition2)
                    return true;
                break;
        }
        return false;
    }

    public Case getCaseByPosition(Position p) {
        for (Case temp : casesDuPlateau) {
            if((temp.position.getX() == p.x) && (temp.position.getY() == p.y)) {
                return temp;
            }
        }
        return null;
    }

    public Case getCaseDown(Position p) {
        for (Case temp : casesDuPlateau) {
            if((temp.position.getX() == (p.x)+1) && (temp.position.getY() == p.y)) {
                return temp;
            }

        }
        return null;
    }
    public Case getCaseUp(Position p) {
        for (Case temp : casesDuPlateau) {
            if((temp.position.getX() == (p.x)-1) && (temp.position.getY() == p.y)) {
                return temp;
            }
        }
        return null;
    }
    public Case getCaseRight(Position p) {
        for (Case temp : casesDuPlateau) {
            if((temp.position.getX() == p.x) && (temp.position.getY() == (p.y)+1)) {
                return temp;
            }
        }
        return null;
    }
    public Case getCaseLeft(Position p) {
        for (Case temp : casesDuPlateau) {
            if((temp.position.getX() == p.x) && (temp.position.getY() == (p.y)-1)) {
                return temp;
            }
        }
        return null;
    }

    public void remplacerCase(Case origine, Case nouvelle) {
        int indexCaseOrigine = getIndexOfCase(origine);
        casesDuPlateau.set(indexCaseOrigine, nouvelle);
    }

    public int getIndexOfCase(Case laCase) {
        return casesDuPlateau.indexOf(laCase);
    }

    public Joueur getJoueurByPosition(Position p) {
        for (Joueur joueur : listeDeJoueurs) {
            if((joueur.getPosition().getX() == p.x) && (joueur.getPosition().getY() == p.y)) {
                return joueur;
            }
        }
        return null;
    }

    private void creerPlateau() {
        for(int i = 1; i <= xTaille; i++) {
            for(int j = 1; j <= yTaille; j++) {
                casesDuPlateau.add(new CaseNormale(new Position(i,j)));
            }
        }
    }

    private void creerMurs() {
        for(int i = 0; i <= xTaille*4; i++) {
            int xRandom = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            Case caseASupprimer = getCaseByPosition(new Position(xRandom,yRandom));
            casesDuPlateau.remove(caseASupprimer);
            CaseMur c=new CaseMur(new Mur(),new Position(xRandom,yRandom));
            casesDuPlateau.add(c);
            listeDeMurs.add(c);
        }
        for(int j=0;j<100;j++) {
            int xRandom = 1 + (int) (Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int) (Math.random() * ((yTaille - 1) + 1));
            for (int i = 0; i < 6; i++) {
                Case caseASupprimer = getCaseByPosition(new Position(xRandom, yRandom));
                casesDuPlateau.remove(caseASupprimer);
                CaseMur c=new CaseMur(new Mur(),new Position(xRandom, yRandom));
                casesDuPlateau.add(c);
                listeDeMurs.add(c);
                double alea = (Math.random() * ((1 + 1) -1));
                if(alea<0.6) {
                    double x = (Math.random() * ((1 + 1) - 1));
                    if (x < 0.5) x = 1;
                    else x = -1;
                    xRandom = (int) (xRandom + x);
                }else {
                    double y = (Math.random() * ((1 + 1) - 1));
                    if (y < 0.5) y = 1;
                    else y = -1;
                    yRandom = (int) (yRandom+y);
                }
            }
        }
    }

    public ArrayList<CaseMur> getListeDeMurs() {
        return listeDeMurs;
    }
    public CaseMur getCaseMurByPosition(Position p){
        for (CaseMur c:listeDeMurs) {
            if (c.getPosition().getX()==p.getX() && c.getPosition().getY()==p.getY()) {
                return c;
            }
        }
        return null;
    }

    public void ajouterMur(CaseMur c){
        listeDeMurs.add(c);
    }
    public void retirerMur(CaseMur c){
        listeDeMurs.remove(c);
    }
    private void creerArmes() {
        for(int i = 0; i <= xTaille/2; i++) {
            int xRandom = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            Case caseASupprimer = getCaseByPosition(new Position(xRandom,yRandom));
            casesDuPlateau.remove(caseASupprimer);
            casesDuPlateau.add(new CaseArme(new Position(xRandom,yRandom)));
        }
    }

    private void creerPopos() {
        for(int i = 0; i <= xTaille/5; i++) {
            int xRandom1 = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom1 = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            int xRandom2 = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom2 = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            int xRandom3 = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom3 = 1 + (int)(Math.random() * ((yTaille - 1) + 1));


            Case caseASupprimer = getCaseByPosition(new Position(xRandom1,yRandom1));
            casesDuPlateau.remove(caseASupprimer);
            casesDuPlateau.add(new CasePopo(new Position(xRandom1,yRandom1),CasePopo.VOLUME_PETIT));

            caseASupprimer = getCaseByPosition(new Position(xRandom2,yRandom2));
            casesDuPlateau.remove(caseASupprimer);
            casesDuPlateau.add(new CasePopo(new Position(xRandom2,yRandom2),CasePopo.VOLUME_MOYEN));

            caseASupprimer = getCaseByPosition(new Position(xRandom3,yRandom3));
            casesDuPlateau.remove(caseASupprimer);
            casesDuPlateau.add(new CasePopo(new Position(xRandom3,yRandom3), CasePopo.VOLUME_GRAND));
        }
    }

    private void creerArmures() {
        for(int i = 0; i <= xTaille/15; i++) {
            int xRandom1 = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom1 = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            int xRandom2 = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom2 = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            int xRandom3 = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom3 = 1 + (int)(Math.random() * ((yTaille - 1) + 1));


            Case caseASupprimer = getCaseByPosition(new Position(xRandom1,yRandom1));
            casesDuPlateau.remove(caseASupprimer);
            casesDuPlateau.add(new CaseArmure(new Position(xRandom1,yRandom1),CasePopo.VOLUME_PETIT));

            caseASupprimer = getCaseByPosition(new Position(xRandom2,yRandom2));
            casesDuPlateau.remove(caseASupprimer);
            casesDuPlateau.add(new CaseArmure(new Position(xRandom2,yRandom2),CasePopo.VOLUME_MOYEN));

            caseASupprimer = getCaseByPosition(new Position(xRandom3,yRandom3));
            casesDuPlateau.remove(caseASupprimer);
            casesDuPlateau.add(new CaseArmure(new Position(xRandom3,yRandom3), CasePopo.VOLUME_GRAND));
        }
    }

    public void creerJoueurs() {
        for(Joueur joueur : listeDeJoueurs) {
            Case caseASupprimer = getCaseByPosition(joueur.getPosition());
            if(!caseASupprimer.getType().equals("CaseJoueur")) {
                casesDuPlateau.remove(caseASupprimer);
                casesDuPlateau.add(new CaseJoueur(joueur, null));
            }
        }
    }

    public int lancerUnDe() {
        int lance = 1 + (int)(Math.random() * ((6 - 1) + 1));
        return lance;
    }

    public int lancerDeuxDes() {
        int lance1 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        int lance2 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        return lance1 + lance2;
    }

    public int lancerTroisDes() {
        int lance1 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        int lance2 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        int lance3 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        return lance1 + lance2 + lance3;
    }

    public int lancerUnDeAtroisChiffres() {
        int lance = 1 + (int)(Math.random() * ((3 - 1) + 1));
        return lance;
    }

    public int lancerUnDeAquatreChiffres() {
        int lance = 1 + (int)(Math.random() * ((4 - 1) + 1));
        return lance;
    }

    public int lancerUnDeAneufChiffres() {
        int lance = 1 + (int)(Math.random() * ((9 - 1) + 1));
        return lance;
    }


    public int calculDeDistance(Position positionJoueur, Position caseCible) {
        int x_diff=caseCible.getX()-positionJoueur.getX();
        int y_diff=caseCible.getY()-positionJoueur.getY();
        if (x_diff<0){
            x_diff=Math.abs(x_diff);
        }
        if (y_diff<0){
            y_diff=Math.abs(y_diff);
        }
        return (x_diff+y_diff);
    }

    public boolean isLancerParfait(ArrayList<Integer> listeDeLancers) {
        if(listeDeLancers.size() > 1) {
            Integer premierLancer = listeDeLancers.get(0);
            for(Integer lancer : listeDeLancers) {
                if(lancer != premierLancer)
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    public void attaquerJoueur(Joueur other, Arme arme, int degats, boolean isLancerParfait) {
        //if(isPointsAttaqueSuffisants(arme.getPa())) {
            if (isLancerParfait)
                other.setPtSante(other.getPtSante() - degats);
            else {
                if (other.getPtArmure() < degats) {
                    other.setPtSante(other.getPtSante() - (degats - other.getPtArmure()));
                    other.setPtArmure(0);
                } else {
                    other.setPtArmure(other.getPtArmure() - degats);
                }
            }
        //}
    }

    public void attaquerMur(Mur mur, Arme arme, int degats, boolean isLancerParfait) {
        //if(isPointsAttaqueSuffisants(arme.getPa())) {
        if (isLancerParfait) {
            mur.setPtStructure(0);
        } else {
            if (mur.getPtStructure() < degats)
                mur.setPtStructure(0);
            else
                mur.setPtStructure(mur.getPtStructure() - degats);
        }
        //}
    }


    public ArrayList<Case> casesDansLeRayon(Position positionExplosion, int rayon, ArrayList<Case> casesDansExplosion) {

        Case droite = getCaseRight(positionExplosion);
        Case gauche = getCaseLeft(positionExplosion);
        Case haut = getCaseUp(positionExplosion);
        Case bas = getCaseDown(positionExplosion);
        if(droite != null && !casesDansExplosion.contains(droite))
            casesDansExplosion.add(droite);
        if(gauche != null && !casesDansExplosion.contains(gauche))
            casesDansExplosion.add(gauche);
        if(haut != null && !casesDansExplosion.contains(haut))
            casesDansExplosion.add(haut);
        if(bas != null && !casesDansExplosion.contains(bas))
            casesDansExplosion.add(bas);
        if(rayon == 2) { //while i < rayon
            if(droite != null)
                casesDansLeRayon(droite.getPosition(), 1, casesDansExplosion);
            if(gauche != null)
                casesDansLeRayon(gauche.getPosition(), 1, casesDansExplosion);
            if(haut != null)
                casesDansLeRayon(haut.getPosition(), 1, casesDansExplosion);
            if(bas != null)
                casesDansLeRayon(bas.getPosition(), 1, casesDansExplosion);
        }
        return casesDansExplosion;
    }

    public boolean isPointsAttaqueSuffisants(int ptAttaqueNecessaires){
        if(turnPlayer.getPtAttaque() >= ptAttaqueNecessaires) {
            return true;
        }
        return false;
    }

    public boolean isBriqueSuffisantes() {
        if(turnPlayer.getBrique() >= 10) {
            return true;
        }
        return false;
    }

    public void appliquerDegatsExplosion(Position positionExplosion, Arme arme, int degats, ArrayList<Integer> listeDeLancers) {
        if(isPointsAttaqueSuffisants(arme.getPa())) {
            for(Case caseActuelle : casesDansLeRayon(positionExplosion, arme.getRayon(), new ArrayList<>())){
                if(caseActuelle.getType().equals("CaseJoueur")){
                    //Check pour infliger dégats aux joueurs présents dans l'explosion
                    CaseJoueur caseJoueur = (CaseJoueur) caseActuelle;
                    Joueur joueur = caseJoueur.getJoueur();
                    attaquerJoueur(joueur, arme, degats, isLancerParfait(listeDeLancers));
                }
                if(caseActuelle.getType().equals("CaseMur")){
                    //Check pour infliger dégats aux murs présents dans l'explosion
                    CaseMur caseMur = (CaseMur) caseActuelle;
                    Mur mur = caseMur.getMur();
                    attaquerMur(mur, arme, degats, isLancerParfait(listeDeLancers));
                }
            }
            turnPlayer.setPtAttaque(turnPlayer.getPtAttaque() - arme.getPa());
        }
        else {
            System.out.print("Points d'attaque insuffisants !");
        }
    }

    public void appliquerDegatsClassiques(Case caseSelectionnee, Arme arme, int degats, ArrayList<Integer> listeDeLancers) {
        if (isPointsAttaqueSuffisants(arme.getPa())) {
            if (caseSelectionnee.getType().equals("CaseJoueur")) {
                CaseJoueur caseJoueur = (CaseJoueur) caseSelectionnee;
                attaquerJoueur(caseJoueur.getJoueur(), arme, degats, isLancerParfait(listeDeLancers));
            }
            if (caseSelectionnee.getType().equals("CaseMur")) {
                CaseMur caseMur = (CaseMur) caseSelectionnee;
                Mur mur = caseMur.getMur();
                attaquerMur(mur, arme, degats, isLancerParfait(listeDeLancers));
            }
            turnPlayer.setPtAttaque(turnPlayer.getPtAttaque() - arme.getPa());
        }
        else {
            System.out.print("Points d'attaque insuffisants !");
        }
    }

    public void detruireMur(CaseMur caseMur, CaseNormale caseNormale) {
        remplacerCase(caseMur, caseNormale);
        retirerMur(caseMur);
        int lancer = lancerUnDe();
        turnPlayer.setBrique(turnPlayer.getBrique()+lancer);
    }

    public void construireMur(Case c, CaseMur cm){
        if(isBriqueSuffisantes()) {
            remplacerCase(c, cm);
            ajouterMur(cm);
            turnPlayer.setBrique(turnPlayer.getBrique()-10);
        }
        else {
            System.out.print("Briques insuffisantes !");
        }
    }

    public Arme tirageArme(int lancer1,int lancer2){
        Arme arme=null;
        switch (lancer2){
            case 1:
                arme=new Couteau(null);
                break;
            case 2:
                arme=new Fusil_assaut(null);
                break;
            case 3:
                arme=new Fusil_pompe(null);
                break;
            case 4:
                arme=new Bazooka(null);
                break;
            case 5:
                arme=new Pistolet(null);
                break;
            case 6:
                arme=new Mine(null);
                break;
            case 7:
                arme=new Sulfateuse(null);
                break;
            case 8:
                arme=new Sniper(null);
                break;
            case 9:
                arme=new Grenade(null);
                break;
        }
        return arme;
    }



    public void setCasesDuPlateau(ArrayList<Case> casesDuPlateau) {
        this.casesDuPlateau = casesDuPlateau;
    }

    public ArrayList<Joueur> getListeDeJoueurs() {
        return listeDeJoueurs;
    }

    public void setListeDeJoueurs(ArrayList<Joueur> listeDeJoueurs) {
        this.listeDeJoueurs = listeDeJoueurs;
    }

    public int getxTaille() {
        return xTaille;
    }

    public void setxTaille(int xTaille) {
        this.xTaille = xTaille;
    }

    public int getyTaille() {
        return yTaille;
    }

    public void setyTaille(int yTaille) {
        this.yTaille = yTaille;
    }

    public void deplacementDuJoueur(Position destination) {
        //remplace par case normale
        if(turnPlayer.caseSauvegarde != null) {
            remplacerCase(getCaseByPosition(turnPlayer.getPosition()), turnPlayer.caseSauvegarde);
            turnPlayer.caseSauvegarde = null;
        }
        else {
            Case caseOrigine = getCaseByPosition(turnPlayer.getPosition());
            Case caseOrigineNouvelle = new CaseNormale(turnPlayer.getPosition());
            remplacerCase(caseOrigine, caseOrigineNouvelle);
        }

        //remplace par case joueur
        if(!getCaseByPosition(destination).getType().equals("CaseNormale"))
            turnPlayer.caseSauvegarde = getCaseByPosition(destination);
        Case caseDestination = getCaseByPosition(destination);
        turnPlayer.setPosition(destination);
        Case caseDestinationNouvelle = new CaseJoueur(turnPlayer, null);
        remplacerCase(caseDestination, caseDestinationNouvelle);
        turnPlayer.setPtMouvement(turnPlayer.getPtMouvement() - caseDestination.getCout());
    }

    //Code pour le pathfinding
    private void AnalyseCase(Case nextCase, int i){
        if (nextCase!=null && !openList.contains(nextCase) && !ListPortee.contains(nextCase) &&  nextCase.getType() != "CaseMur") {
            if(nextCase.getCout()>0){
                nextCase.setCout(0);
            }
            nextCase.setCout(i+1);
            if(nextCase.getCout()<=turnPlayer.getPtMouvement()) {
                openList.add(nextCase);
            }else nextCase.setCout(0);
        }else if (openList.contains(nextCase) && (i+1)<nextCase.getCout()){
            nextCase.setCout(i+1);
        }else if (ListPortee.contains(nextCase) && (i+1)<nextCase.getCout()){
            ListPortee.remove(nextCase);
            nextCase.setCout(i+1);
            openList.add(nextCase);
        }
    }

    //Retourne les cases sur lesquelles le joueur actuel peut se déplacer
    public ArrayList<Case> pathFindingPlateau(Joueur joueur) {
        openList=new ArrayList<>();
        ListPortee=new ArrayList<>();
        Case positionactuelle;
        int i;
        Case Right;
        Case Left;
        Case Up;
        Case Down;
        openList.add(getCaseByPosition(joueur.getPosition()));
        while (openList.size()>0) {
            positionactuelle=openList.get(openList.size()-1);
            openList.remove(positionactuelle);
            ListPortee.add(positionactuelle);
            i=positionactuelle.getCout();
            Right=getCaseRight(positionactuelle.getPosition());
            Left=getCaseLeft(positionactuelle.getPosition());
            Up=getCaseUp(positionactuelle.getPosition());
            Down=getCaseDown(positionactuelle.getPosition());
            AnalyseCase(Right,i);
            AnalyseCase(Left,i);
            AnalyseCase(Up,i);
            AnalyseCase(Down,i);
        }
        return ListPortee;
    }

    //Retourne les cases constructibles par le joueur actuel
    public ArrayList<Case> buildPathFindingPlateau() {
        build=new ArrayList<>();
        Case positionactuelle;
        Case Right;
        Case Left;
        Case Up;
        Case Down;
        positionactuelle=getCaseByPosition(turnPlayer.getPosition());
        build.add(positionactuelle);
        Right=getCaseRight(positionactuelle.getPosition());
        Left=getCaseLeft(positionactuelle.getPosition());
        Up=getCaseUp(positionactuelle.getPosition());
        Down=getCaseDown(positionactuelle.getPosition());
        if(Right!=null && Right.getType().equals("CaseNormale")){
            build.add(Right);
        }
        if(Left!=null && Left.getType().equals("CaseNormale")){
            build.add(Left);
        }
        if(Up!=null && Up.getType().equals("CaseNormale")){
            build.add(Up);
        }
        if(Down!=null && Down.getType().equals("CaseNormale")){
            build.add(Down);
        }
        return build;
    }

    //Retourne le champs de tir d'une arme pour le joueur actuel en fonction
    public ArrayList<Case> shootPathFindingPlateau(Arme arme, Joueur joueur) {
        if(arme.getTypeTir().equals("droit")) {
            Position depart=joueur.getPosition();
            Case next;
            int i=0;
            openList=new ArrayList<>();
            shoot=new ArrayList<>();
            Case positionactuelle;
            openList.add(getCaseByPosition(depart));
            while (openList.size()>0) {
                positionactuelle = openList.get(openList.size() - 1);
                openList.remove(positionactuelle);
                shoot.add(positionactuelle);
                next=getCaseLeft(positionactuelle.getPosition());
                if (next != null && next.getPosition().getY()>=depart.getY()-arme.getPortée() && i==0) {
                    if (next.getType().equals("CaseMur")){
                        i=1;
                    }
                    openList.add(next);
                }
            }
            i=0;
            openList.add(getCaseByPosition(depart));
            while (openList.size()>0) {
                positionactuelle = openList.get(openList.size() - 1);
                openList.remove(positionactuelle);
                shoot.add(positionactuelle);
                next=getCaseRight(positionactuelle.getPosition());
                if (next != null && next.getPosition().getY()<=depart.getY()+arme.getPortée() && i==0) {
                    if (next.getType().equals("CaseMur")){
                        i=1;
                    }
                    openList.add(next);
                }
            }
            i=0;
            openList.add(getCaseByPosition(depart));
            while (openList.size()>0) {
                positionactuelle = openList.get(openList.size() - 1);
                openList.remove(positionactuelle);
                shoot.add(positionactuelle);
                next=getCaseUp(positionactuelle.getPosition());
                if (next != null && next.getPosition().getX()>=depart.getX()-arme.getPortée() && i==0) {
                    if (next.getType().equals("CaseMur")){
                        i=1;
                    }
                    openList.add(next);
                }
            }
            i=0;
            openList.add(getCaseByPosition(depart));
            while (openList.size()>0) {
                positionactuelle = openList.get(openList.size() - 1);
                openList.remove(positionactuelle);
                shoot.add(positionactuelle);
                next=getCaseDown(positionactuelle.getPosition());
                if (next != null && next.getPosition().getX()<=depart.getX()+arme.getPortée() && i==0) {
                    if (next.getType().equals("CaseMur")){
                        i=1;
                    }
                    openList.add(next);
                }
            }
            return shoot;
        }
        return null;
    }

    //Retourne la liste des cases qu'un joueur peut toucher avec une explosion
    private ArrayList<Case> explosionPathFindingPlateau(Arme arme, Joueur joueur) {
        /**                x
         *               x x x
         *             x x x x x
         *             x x x x x
         *         x x x x x x x x x
         *       x x x x x x x x x x x
         *     x x x x x x O x x x x x x      Exemple avec une arme explosif avec une portée de 4 et un rayon de 2
         *       x x x x x x x x x x x
         *         x x x x x x x x x
         *             x x x x x
         *             x x x x x
         *               x x x
         *                 x
         */
        if(arme.getRayon() > 0) {
            ArrayList<Case> listcasesdansExplosion=new ArrayList<>();
            for (Case c : shootPathFindingPlateau(arme, joueur)) {
                for (Case c2:casesDansLeRayon(c.getPosition(), arme.getRayon(), new ArrayList<>())) {
                    if(!listcasesdansExplosion.contains(c2)){
                        listcasesdansExplosion.add(c2);
                        //System.out.println("x :" +c2.getPosition().getX());
                        //System.out.println("y :" +c2.getPosition().getY());
                    }
                }
            }
            return listcasesdansExplosion;
        }
        return null;
    }

    //Retourn le nombre de déplacement maximum que le joueur actuel peut effectuer
    private int getDeplacementMax() {
        int coutMax = 0;
        ArrayList<Case> listeDesCasesAccessibles = pathFindingPlateau(turnPlayer);
        for(Case c : listeDesCasesAccessibles) {
            int coutCase = c.getCout();
            if(coutCase > coutMax) {
                coutMax = coutCase;
            }
        }
        return coutMax;
    }

    //Retourne le nombre d'armes possédées par le joueur actuel
    private int nombreArmesPossedees() {
        return turnPlayer.getArmes().size();
    }

    //Retourne le nom de la meilleure arme du joueur actuel
    private String meilleureArmePossedee() {
        String meilleureArme = "couteau";
        int puissanceMax = 0;
        int puissanceArme = 0;
        for (Arme arme : turnPlayer.getArmes()) {
            puissanceArme = (arme.getPortée() + arme.getRayon()) * arme.getDmg_dés(); //Détermine arbitrairement la puissance d'une arme
            if(puissanceArme > puissanceMax) {
                puissanceMax = puissanceArme;
                meilleureArme = arme.getName();
            }
        }
        return meilleureArme;
    }

    //Savoir si le turnPlayer est Low-life
    public boolean isLowPointDeSante() {
        return turnPlayer.getPtSante() < 50;
    }

    //Savoir si le turnPlayer est Low-Armure
    public boolean isLowPointDarmure() {
        return turnPlayer.getPtArmure() < 50;
    }

    //Savoir si le turnPlayer est Low niveau pts d'attaque
    public boolean isLowPointDattaque() {
        return turnPlayer.getPtAttaque() < 3;
    }

    //Savoir si le turnPlayer est Low niveau pts de mouvement
    public boolean isLowPointDeMouvement() {
        return turnPlayer.getPtMouvement() < 3;
    }

    //Savoir si un coffre est à portée du joueur actuel
    public boolean isCoffreAPortee() {
        ArrayList<Case> listeDesCasesAccessibles = pathFindingPlateau(turnPlayer);
        for (Case c : listeDesCasesAccessibles) {
            if(c.getType().equals("CaseArme")) {
                //System.out.println("Coffre à portée");
                return true;
            }
        }
        return false;
    }

    //Savoir si une popo est à portée du joueur actuel
    public boolean isPopoAPortee() {
        ArrayList<Case> listeDesCasesAccessibles = pathFindingPlateau(turnPlayer);
        for (Case c : listeDesCasesAccessibles) {
            if(c.getType().equals("CasePopo")) {
                //System.out.println("Popo à portée");
                return true;
            }
        }
        return false;
    }

    //Savoir si une armure est à portée du joueur actuel
    public boolean isArmureAPortee() {
        ArrayList<Case> listeDesCasesAccessibles = pathFindingPlateau(turnPlayer);
        for (Case c : listeDesCasesAccessibles) {
            if(c.getType().equals("CaseArmure")) {
                //System.out.println("Armure à portée");
                return true;
            }
        }
        return false;
    }

    //Savoir si un autre joueur est à portée du joueur actuel
    public boolean isJoueurAPortee() {
        ArrayList<Case> listeDesCasesAccessibles = pathFindingPlateau(turnPlayer);
        for (Case c : listeDesCasesAccessibles) {
            if(c.getType().equals("CaseJoueur") && c.getPosition() != turnPlayer.getPosition()) {
                //System.out.println("Joueur à portée de turnPlayer");
                return true;
            }
        }
        return false;
    }

    //Savoir si un autre joueur est à portée un de tir du joueur actuel
    public boolean isJoueurAPorteeDeTir() {
        ArrayList<Case> listeDesCasesAPorteeDeTir;
        //Avant de faire le renforcement
        /*
        for (Arme arme : turnPlayer.getArmes()) {
            if(arme.getRayon() == 0) {
                listeDesCasesAPorteeDeTir = shootPathFindingPlateau(arme, turnPlayer);
                for (Case c : listeDesCasesAPorteeDeTir) {
                    if(c.getType().equals("CaseJoueur") && c.getPosition() != turnPlayer.getPosition()) {
                        System.out.println("Joueur à portée de tir de turnPlayer");
                        return true;
                    }
                }
            }
        }*/

        //Pour le renforcement
        if(turnPlayer.armeSelectionnee.getRayon() == 0) {
            listeDesCasesAPorteeDeTir = shootPathFindingPlateau(turnPlayer.armeSelectionnee, turnPlayer);
            for (Case c : listeDesCasesAPorteeDeTir) {
                if(c.getType().equals("CaseJoueur") && c.getPosition() != turnPlayer.getPosition()) {
                    //System.out.println("Joueur à portée de tir de turnPlayer");
                    return true;
                }
            }
        }
        return false;
    }

    //Savoir si le joueur actuel est portée de tir d'un autre joueur
    public boolean isTurnPlayerAPorteeDeTir() {
        ArrayList<Case> listeDesCasesAPorteeDeTir;
        for (Joueur joueur : listeDeJoueurs) {
            if(joueur != turnPlayer) {
                for (Arme arme : joueur.getArmes()) {
                    if(arme.getRayon() == 0) {
                        listeDesCasesAPorteeDeTir = shootPathFindingPlateau(arme, joueur);
                        for (Case c : listeDesCasesAPorteeDeTir) {
                            if(c.getType().equals("CaseJoueur") && c.getPosition() == turnPlayer.getPosition()) {
                                System.out.println("TurnPlayer à portée de tir de " + joueur.getName());
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    //Savoir si un autre joueur est à portée d'explosion du joueur actuel
    public boolean isJoueurAPorteeDexplosion() {
        ArrayList<Case> listeDesCasesAPorteeDexplosion;
        //Avant de faire le renforcement
        /*
        for (Arme arme : turnPlayer.getArmes()) {
            if(arme.getRayon() > 0) {
                listeDesCasesAPorteeDexplosion = explosionPathFindingPlateau(arme, turnPlayer);
                for (Case c : listeDesCasesAPorteeDexplosion) {
                    if(c.getType().equals("CaseJoueur") && c.getPosition() != turnPlayer.getPosition()) {
                        System.out.println("Joueur à portée d'explosion de turnPlayer");
                        return true;
                    }
                }
            }
        }*/
        if(turnPlayer.armeSelectionnee.getRayon() > 0) {
            listeDesCasesAPorteeDexplosion = explosionPathFindingPlateau(turnPlayer.armeSelectionnee, turnPlayer);
            for (Case c : listeDesCasesAPorteeDexplosion) {
                if(c.getType().equals("CaseJoueur") && c.getPosition() != turnPlayer.getPosition()) {
                    //System.out.println("Joueur à portée d'explosion de turnPlayer");
                    return true;
                }
            }
        }
        return false;
    }

    //Savoir si le joueur actuel est à portée d'explosion d'un autre joueur
    public boolean isTurnPlayerAPorteeDexplosion() {
        ArrayList<Case> listeDesCasesAPorteeDexplosion;
        for (Joueur joueur : listeDeJoueurs) {
            if(joueur != turnPlayer) {
                for (Arme arme : joueur.getArmes()) {
                    if (arme.getRayon() > 0) {
                        listeDesCasesAPorteeDexplosion = explosionPathFindingPlateau(arme, joueur);
                        for (Case c : listeDesCasesAPorteeDexplosion) {
                            if (c.getType().equals("CaseJoueur") && c.getPosition() == turnPlayer.getPosition()) {
                                System.out.println("TurnPlayer à portée d'explosion de " + joueur.getName());
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;

    }

    //Savoir si le joueur actuel peut construire un mur
    public boolean isConstructionPossible() {
        return (turnPlayer.getBrique() >= 10 && buildPathFindingPlateau().size() >= 1);
    }

    public void testAppelFonctionsPourCSV() {
        System.out.println("Déplacement maximal : " +  getDeplacementMax()); //OK
        System.out.println("Construction possible : " + isConstructionPossible()); //OK
        isCoffreAPortee(); //OK
        isPopoAPortee(); //OK
        isArmureAPortee(); //OK
        isJoueurAPortee(); //OK
        isJoueurAPorteeDeTir(); //OK
        isJoueurAPorteeDexplosion(); //OK
        isTurnPlayerAPorteeDeTir(); //OK
        isTurnPlayerAPorteeDexplosion(); //OK
    }

    //Écrit les actions dans un fichier CSV
    public void ecrireActionCSV(String actionRealisee) {
        File fichierCSV = new File(CHEMIN_FICHIER_CSV);
        FileWriter fileWriter = null;

        if(fichierCSV.exists()) {
            try {
                fileWriter = new FileWriter(CHEMIN_FICHIER_CSV);

                fileWriter.append(String.valueOf(turnPlayer.getPtSante()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(turnPlayer.getPtArmure()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(turnPlayer.getPtAttaque()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(turnPlayer.getPtMouvement()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(turnPlayer.getBrique()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(getDeplacementMax()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(nombreArmesPossedees()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(meilleureArmePossedee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isCoffreAPortee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isPopoAPortee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isArmureAPortee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isJoueurAPortee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isJoueurAPorteeDeTir()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isJoueurAPorteeDexplosion()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isTurnPlayerAPorteeDeTir()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isTurnPlayerAPorteeDexplosion()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isConstructionPossible()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(actionRealisee));
                //...
                fileWriter.append(LIGNE_SEPARATEUR);

                System.out.println("Ecriture dans le fichier CSV réussie !!!");

            } catch (Exception e) {
                System.out.println("Erreur dans le CsvFileWriter !!!");
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("Erreur lors du flushing/closing pour le fileWriter !!!");
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                fileWriter = new FileWriter(CHEMIN_FICHIER_CSV);

                //Ajoute le header au fichier CSV
                fileWriter.append(FILE_HEADER.toString());

                //Ajoute une nouvelle ligne après le header
                fileWriter.append(LIGNE_SEPARATEUR);

                fileWriter.append(String.valueOf(turnPlayer.getPtSante()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(turnPlayer.getPtArmure()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(turnPlayer.getPtAttaque()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(turnPlayer.getPtMouvement()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(turnPlayer.getBrique()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(getDeplacementMax()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(nombreArmesPossedees()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(meilleureArmePossedee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isCoffreAPortee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isPopoAPortee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isArmureAPortee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isJoueurAPortee()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isJoueurAPorteeDeTir()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isJoueurAPorteeDexplosion()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isTurnPlayerAPorteeDeTir()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isTurnPlayerAPorteeDexplosion()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(isConstructionPossible()));
                fileWriter.append(VIRGULE_DELIMITEUR);
                fileWriter.append(String.valueOf(actionRealisee));
                //...
                fileWriter.append(LIGNE_SEPARATEUR);

            } catch (Exception e) {
                System.out.println("Erreur dans le CsvFileWriter !!!");
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println("Erreur lors du flushing/closing pour le fileWriter !!!");
                    e.printStackTrace();
                }
            }
        }

    }

    private boolean isJoueurMort(Joueur joueur) {
        return joueur.getPtSante() < 1;
    }

    public int nbJoueursRestants() {
        int nbJoueursMorts = 0;
        for (Joueur joueur : listeDeJoueurs) {
            if(isJoueurMort(joueur))
                nbJoueursMorts += 1;
        }
        return nbJoueursMorts;
    }

    public boolean isIlResteUnSeulJoueur() {
        return nbJoueursRestants() < 2;
    }

    public void finDePartie() {
        if(isIlResteUnSeulJoueur()) {
            for (Joueur joueur : listeDeJoueurs) {
                if(!isJoueurMort(joueur)) {
                    //Alors joueur est le gagnant
                    //AlertBox

                }
            }
        }
    }
}
