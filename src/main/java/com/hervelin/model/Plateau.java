package com.hervelin.model;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Plateau {
    private ArrayList<Case> casesDuPlateau = new ArrayList<>();
    private ArrayList<Joueur> listeDeJoueurs = new ArrayList<>(4);
    private ArrayList<CaseMur> listeDeMurs = new ArrayList<>();


    private int xTaille;
    private int yTaille;
    private Image imageJoueur1;
    private Image imageJoueur2;
    private Image imageJoueur3;
    private Image imageJoueur4;
    private Position randomPosition1;
    private Position randomPosition2;
    private Position randomPosition3;
    private Position randomPosition4;

    public  Joueur turnPlayer;


    public Plateau(int xTaille, int yTaille, String joueur1, String joueur2) {
        this.xTaille = xTaille;
        this.yTaille = yTaille;
        randomPosition1 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        randomPosition2 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        if(isDejaExistant("2"))
            randomPosition2 = new Position(1 + (int)(Math.random() * ((xTaille - 1) + 1)),1 + (int)(Math.random() * ((yTaille - 1) + 1)));
        imageJoueur1 = new Image("images/Perso1minimizedColor.png");
        imageJoueur2 = new Image("images/Perso2minimizedColor.png");
        System.out.println(joueur1);
        listeDeJoueurs.add(new Joueur(joueur1, randomPosition1, imageJoueur1));
        listeDeJoueurs.add(new Joueur(joueur2, randomPosition2, imageJoueur2));
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
        imageJoueur1 = new Image("images/Perso1minimizedColor.png");
        imageJoueur2 = new Image("images/Perso2minimizedColor.png");
        imageJoueur3 = new Image("images/Perso3minimizedColor.png");
        listeDeJoueurs.add(new Joueur(joueur1, randomPosition1, imageJoueur1));
        listeDeJoueurs.add(new Joueur(joueur2, randomPosition2, imageJoueur2));
        listeDeJoueurs.add(new Joueur(joueur3, randomPosition3, imageJoueur3));
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
        imageJoueur1 = new Image("images/Perso1minimizedColor.png");
        imageJoueur2 = new Image("images/Perso2minimizedColor.png");
        imageJoueur3 = new Image("images/Perso3minimizedColor.png");
        imageJoueur4 = new Image("images/Perso4minimizedColor.png");
        listeDeJoueurs.add(new Joueur(joueur1, randomPosition1, imageJoueur1));
        listeDeJoueurs.add(new Joueur(joueur2, randomPosition2, imageJoueur2));
        listeDeJoueurs.add(new Joueur(joueur3, randomPosition3, imageJoueur3));
        listeDeJoueurs.add(new Joueur(joueur4, randomPosition4, imageJoueur4));
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
    }

    public void joueurSuivant(String nbJoueurs) {
        int indexDuJoueurActuel = listeDeJoueurs.indexOf(turnPlayer);
        if(indexDuJoueurActuel != (Integer.valueOf(nbJoueurs)-1)) {
            turnPlayer = listeDeJoueurs.get(indexDuJoueurActuel +1);
        }
        else {
            turnPlayer = listeDeJoueurs.get(0);
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
        //int xOrigine = origine.position.getX();
        //int yOrigine = origine.position.getY();
        //int xNouvelle = nouvelle.position.getX();
        //int yNouvelle = nouvelle.position.getY();

        int indexCaseOrigine = getIndexOfCase(origine);
        //System.out.println("Index Origine : " + getIndexOfCase(origine));

        //System.out.println("index remplace :"+ indexCaseOrigine);
        casesDuPlateau.set(indexCaseOrigine, nouvelle);
        //System.out.println("Index Destination : " + getIndexOfCase(nouvelle));

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
                casesDuPlateau.add(new CaseJoueur(joueur, joueur.getImageJoueur()));
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
        if(isPointsAttaqueSuffisants(arme.getPa())) {
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
        }
        else {
            System.out.print("Points d'attaque insuffisantes !");
        }
    }

    public void attaquerMur(Mur mur, Arme arme, int degats, boolean isLancerParfait) {
        if(isPointsAttaqueSuffisants(arme.getPa())) {
            if (isLancerParfait) {
                mur.setPtStructure(0);
            } else {
                if (mur.getPtStructure() < degats)
                    mur.setPtStructure(0);
                else
                    mur.setPtStructure(mur.getPtStructure() - degats);
            }
        }
        else {
            System.out.print("Points d'attaque insuffisantes !");
        }
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
        //if(isPointsAttaqueSuffisants(arme.getPa())) {
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
        //}
    }

    public void appliquerDegatsClassiques(Case caseSelectionnee, Arme arme, int degats, ArrayList<Integer> listeDeLancers) {
        //if (isPointsAttaqueSuffisants(arme.getPa())) {
            if (caseSelectionnee.getType().equals("CaseJoueur")) {
                CaseJoueur caseJoueur = (CaseJoueur) caseSelectionnee;
                attaquerJoueur(caseJoueur.getJoueur(), arme, degats, isLancerParfait(listeDeLancers));
            }
            if (caseSelectionnee.getType().equals("CaseMur")) {
                CaseMur caseMur = (CaseMur) caseSelectionnee;
                attaquerMur(caseMur.getMur(), arme, degats, isLancerParfait(listeDeLancers));
                Mur mur = caseMur.getMur();
            }
            turnPlayer.setPtAttaque(turnPlayer.getPtAttaque() - arme.getPa());
        //}
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
}
