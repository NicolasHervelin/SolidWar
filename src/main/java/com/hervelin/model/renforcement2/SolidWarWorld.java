package com.hervelin.model.renforcement2;

import com.hervelin.model.*;
import javafx.geometry.Pos;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SolidWarWorld implements RLWorld{
    public static final String ANSI_GREEN = "\u001B[32m";

    public Plateau plateau;
    private int lowPointDeSante; //0 si false et 1 si true
    private int lowPointDarmure; //0 si false et 1 si true
    private int lowPointDattaque; //0 si false et 1 si true
    private int lowPointDeMouvement; //0 si false et 1 si true
    private int coffreAportee; //0 si false et 1 si true
    private int popoAportee; //0 si false et 1 si true
    private int armureAportee; //0 si false et 1 si true
    private int joueurAportee; //0 si false et 1 si true
    private int joueurAporteeDeTir; //0 si false et 1 si true
    private int joueurAporteeDexplosion; //0 si false et 1 si true
    private int turnPlayerAporteeDeTir; //0 si false et 1 si true
    private int turnPlayerAporteeDexplosion; //0 si false et 1 si true
    private int constructionPossible; //0 si false et 1 si true
    /** On peut enlever 2 paramètres d'état pour passer de 8192 états possibles à 2048 **/

    private int bx, by;

    public Joueur j1, j2;
    public int scoreJ1 = 0, scoreJ2 = 0;
    private int xJ1, yJ1;
    private int xJ2, yJ2;

    //private int nouvelleArmeReward = 10, gainDeVieReward = 15, gainDarmureReward = 10;
    //Modifier récompense pour pas que le bot ne fasse que finir son tour.
    //Mettre une récompense à chaque action (genre 1 et 0 pour fin de tour)
    public int recompenseDeKill = 1000;
    public int punitionDeMort = 100;
    public int recompensePourUneAction = 1;
    public int recompensePourAvoirPlusDePV = 1;
    public int punitionPourAvoirMoinsDePV = 1;

    static final int NUM_OBJECTS_SOLID_WAR=13; //Les paramètres de l'état
    static final int NUM_ACTIONS_SOLID_WAR=17;  //Les actions possibles
                                                //----Attaque----
                                                //0:attaque
                                                //----Sélectionner arme----
                                                //1:arme1  2:arme2  3:arme3  4:arme4  5:arme5
                                                //----Déplacements----
                                                //6:haut  7:bas  8:gauche  9:droite
                                                //----Construire----
                                                //10:haut  11:bas  12:gauche  13:droite
                                                //----Objets----
                                                //14:prendre  15:laisser
                                                //----Fin du tour----
                                                //16:findetour

    //A chaque fois que l'agent fait une action, on met à jour l'état.
    //Lorsque que les pts de

    static final int WALL_TRIALS_SOLID_WAR=100;
    static final double INIT_VALS_SOLID_WAR=0;

    int[] stateArray;
    double waitingReward;

    public SolidWarWorld(Plateau plateau) {
        this.plateau = plateau;

        bx = plateau.getxTaille();
        by = plateau.getyTaille();

        plateau.getListeDeJoueurs().get(0).setIA(true);
        plateau.getListeDeJoueurs().get(1).setIA(true);

        plateau.getListeDeJoueurs().get(0).setPtAttaque(plateau.lancerUnDe());
        plateau.getListeDeJoueurs().get(0).setPtMouvement(plateau.lancerDeuxDes());
        plateau.getListeDeJoueurs().get(1).setPtAttaque(plateau.lancerUnDe());
        plateau.getListeDeJoueurs().get(1).setPtMouvement(plateau.lancerDeuxDes());

        j1 = plateau.getListeDeJoueurs().get(0);
        j2 = plateau.getListeDeJoueurs().get(1);

        System.out.println("Joueur 1 pt d'attaque : " + j1.getPtAttaque());
        System.out.println("Joueur 1 pt de mouvement : " + j1.getPtMouvement());
        System.out.println("Joueur 2 pt d'attaque : " + j2.getPtAttaque());
        System.out.println("Joueur 2 pt de mouvement : " + j2.getPtMouvement());

        xJ1 = j1.getPosition().getX();
        yJ1 = j1.getPosition().getY();
        xJ2 = j2.getPosition().getX();
        yJ2 = j2.getPosition().getY();

        lowPointDeSante = mapperLeBooleen(false);
        lowPointDarmure = mapperLeBooleen(false);
        lowPointDattaque = mapperLeBooleen(false);
        lowPointDeMouvement = mapperLeBooleen(false);
        coffreAportee = mapperLeBooleen(plateau.isCoffreAPortee());
        popoAportee = mapperLeBooleen(plateau.isPopoAPortee());
        armureAportee = mapperLeBooleen(plateau.isArmureAPortee());
        joueurAportee = mapperLeBooleen(plateau.isJoueurAPortee());
        joueurAporteeDeTir = mapperLeBooleen(plateau.isJoueurAPorteeDeTir());
        joueurAporteeDexplosion = mapperLeBooleen(false);
        turnPlayerAporteeDeTir = mapperLeBooleen(plateau.isTurnPlayerAPorteeDeTir());
        turnPlayerAporteeDexplosion = mapperLeBooleen(false);
        constructionPossible = mapperLeBooleen(false);

    }

    public int mapperLeBooleen(boolean bool) {
        int result;
        if(!bool)
            result = 0;
        else
            result = 1;
        return result;
    }

    public boolean mapperLentier(int entier) {
        return (entier == 1);
    }

    /******* RLWorld interface functions ***********/
    public int[] getDimension() {
        int[] retDim = new int[NUM_OBJECTS_SOLID_WAR+1];
        retDim[0] = 2;
        retDim[1] = 2;
        retDim[2] = 2;
        retDim[3] = 2;
        retDim[4] = 2;
        retDim[5] = 2;
        retDim[6] = 2;
        retDim[7] = 2;
        retDim[8] = 2;
        retDim[9] = 2;
        retDim[10] = 2;
        retDim[11] = 2;
        retDim[12] = 2;
        retDim[NUM_OBJECTS_SOLID_WAR] = NUM_ACTIONS_SOLID_WAR;

        for(int i = 0; i < retDim.length; i++) {
            System.out.println("retDim["+i+"] : " + retDim[i]);
        }

        return retDim;
    }

    public Joueur otherJoueur() {
        if(plateau.turnPlayer == j1)
            return j2;
        else
            return j1;
    }

    public boolean isAttaqueAutorisee() {
        Arme arme = plateau.turnPlayer.armeSelectionnee;
        return (plateau.turnPlayer.getPtAttaque() >= arme.getPa());
    }

    public boolean isSelectionDeLarmeAutorisee(int indexArme) {
        return (indexArme <= plateau.turnPlayer.getArmes().size()-1);
    }

    public boolean isDeplacementPossible() {
        //System.err.println("posX : " + plateau.turnPlayer.getPosition().getX());
        //System.err.println("posY : " + plateau.turnPlayer.getPosition().getY());
        //System.err.println("Points de mouvement : " + plateau.turnPlayer.getPtMouvement());
        return (plateau.turnPlayer.getPtMouvement() >= 1);
    }

    public boolean isDeplacementAutorise(Position position, int deltaX, int deltaY) {
        //System.err.println("posX : " + plateau.turnPlayer.getPosition().getX());
        //System.err.println("posY : " + plateau.turnPlayer.getPosition().getY());
        //System.err.println("isDeplacementPossible : " + isDeplacementPossible());
        if(isDeplacementPossible()
                && (position.getX() + deltaX <= plateau.getxTaille() && position.getX() + deltaX >= 1)
                && (position.getY() + deltaY <= plateau.getyTaille() && position.getY() + deltaY >= 1)) {
            Position positionCaseDestination = new Position(position.getX()+deltaX, position.getY()+deltaY);
            Case caseDestination =  plateau.getCaseByPosition(positionCaseDestination);
            if((plateau.getListeDeJoueurs().get(0).getPosition().getX() == positionCaseDestination.getX()
                    && plateau.getListeDeJoueurs().get(0).getPosition().getY() == positionCaseDestination.getY())
                    || (plateau.getListeDeJoueurs().get(1).getPosition().getX() == positionCaseDestination.getX()
                        && plateau.getListeDeJoueurs().get(1).getPosition().getY() == positionCaseDestination.getY())
                    || caseDestination.getType().equals("CaseMur")) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isConstructionAutorisee(Position position, int deltaX, int deltaY) {
        Case caseAconstruire = plateau.getCaseByPosition(new Position(position.getX()+deltaX, position.getY()+deltaY));
        return (plateau.isConstructionPossible() && plateau.buildPathFindingPlateau().contains(caseAconstruire));
    }

    public boolean isInteractionAutorisee() {
        return (plateau.turnPlayer.caseSauvegarde != null);
    }

    // given action determine next state
    public int[] getNextState(int action) {
        // action est l'action de l'agent (voir description action possible en haut)
        if(plateau.getListeDeJoueurs().get(0).getPosition().getX() == plateau.getListeDeJoueurs().get(1).getPosition().getX()
                && plateau.getListeDeJoueurs().get(0).getPosition().getY() == plateau.getListeDeJoueurs().get(1).getPosition().getY()) {
            System.out.println("\u001B[34m" + "Position des deux joueurs identique BBBBBUUUUUUUUUUUGGGGGGGGGGGG" + "\u001B[37m");
        }
            if(action == 0) {
                //ATTAQUE
                if(mapperLentier(joueurAporteeDeTir)) {
                    System.out.println(ANSI_GREEN + "--------------------Portée de tir----------------------" + "\u001B[37m");
                    System.out.println(ANSI_GREEN + "-----------------------" + "Position X J1 : " + plateau.getListeDeJoueurs().get(0).getPosition().getX()  + "\u001B[37m");
                    System.out.println(ANSI_GREEN + "-----------------------" + "Position Y J1 : " + plateau.getListeDeJoueurs().get(0).getPosition().getY()  + "\u001B[37m");
                    System.out.println(ANSI_GREEN + "-----------------------" + "Position X J2 : " + plateau.getListeDeJoueurs().get(1).getPosition().getX() + "\u001B[37m");
                    System.out.println(ANSI_GREEN + "-----------------------" + "Position Y J2 : " + plateau.getListeDeJoueurs().get(1).getPosition().getY() + "\u001B[37m");
                    Arme arme = plateau.turnPlayer.armeSelectionnee;
                    ArrayList<Integer> listeDesLancers = new ArrayList<>();
                    int degats = 0;
                    for(int i = 0; i < arme.getDmg_dés(); i++) {
                        int lancer = plateau.lancerUnDe();
                        listeDesLancers.add(lancer);
                        degats += lancer;
                    }
                    boolean dejaAttaque = false;
                    for (Case c : plateau.shootPathFindingPlateau(arme, plateau.turnPlayer)) {
                        if(!dejaAttaque && c.getType().equals("CaseJoueur") &&
                                (c.getPosition().getX() != plateau.turnPlayer.getPosition().getX() || c.getPosition().getY() != plateau.turnPlayer.getPosition().getY())){
                            plateau.appliquerDegatsClassiques(c, arme, degats, listeDesLancers);
                            System.out.println(ANSI_GREEN + "-----------------------" + "PV perdu " + "-------------------------" + "\u001B[37m");
                            System.out.println(ANSI_GREEN + "-----------------------" + "PV J1 " + plateau.getListeDeJoueurs().get(0).getPtSante() + "\u001B[37m");
                            System.out.println(ANSI_GREEN + "-----------------------" + "PV J2 " + plateau.getListeDeJoueurs().get(1).getPtSante()  + "\u001B[37m");
                            dejaAttaque = true;
                            //waitingReward = calcReward(action);
                        }
                    }
                }
                if(mapperLentier(joueurAporteeDexplosion)) {
                    System.out.println(ANSI_GREEN + "--------------------Portée d'explosion----------------------" + "\u001B[37m");
                    System.out.println(ANSI_GREEN + "-----------------------" + "Position X J1 : " + plateau.getListeDeJoueurs().get(0).getPosition().getX()  + "\u001B[37m");
                    System.out.println(ANSI_GREEN + "-----------------------" + "Position Y J1 : " + plateau.getListeDeJoueurs().get(0).getPosition().getY()  + "\u001B[37m");
                    System.out.println(ANSI_GREEN + "-----------------------" + "Position X J2 : " + plateau.getListeDeJoueurs().get(1).getPosition().getX() + "\u001B[37m");
                    System.out.println(ANSI_GREEN + "-----------------------" + "Position Y J2 : " + plateau.getListeDeJoueurs().get(1).getPosition().getY() + "\u001B[37m");
                    Arme arme = plateau.turnPlayer.armeSelectionnee;
                    ArrayList<Integer> listeDesLancers = new ArrayList<>();
                    int degats = 0;
                    for(int i = 0; i < arme.getDmg_dés(); i++) {
                        int lancer = plateau.lancerUnDe();
                        listeDesLancers.add(lancer);
                        degats += lancer;
                    }
                    boolean dejaAttaque = false;
                    for (Case c : plateau.shootPathFindingPlateau(arme, plateau.turnPlayer)) {
                        ArrayList<Case> casesDansLeRayon = plateau.casesDansLeRayon(c.getPosition(), arme.getRayon());
                        for (Case c2: casesDansLeRayon) {
                            if(!dejaAttaque && c2.getType().equals("CaseJoueur") &&
                                    (c2.getPosition().getX() != plateau.turnPlayer.getPosition().getX() || c2.getPosition().getY() != plateau.turnPlayer.getPosition().getY())){
                                plateau.appliquerDegatsExplosion(c.getPosition(), arme, degats, listeDesLancers);
                                System.out.println(ANSI_GREEN + "-----------------------" + "PV perdu " + "-------------------------" + "\u001B[37m");
                                System.out.println(ANSI_GREEN + "-----------------------" + "PV J1 " + plateau.getListeDeJoueurs().get(0).getPtSante() + "\u001B[37m");
                                System.out.println(ANSI_GREEN + "-----------------------" + "PV J2 " + plateau.getListeDeJoueurs().get(1).getPtSante()  + "\u001B[37m");
                                dejaAttaque = true;
                                //waitingReward = calcReward(action);
                            }
                        }
                    }
                }
                System.out.println("Action décidée -- Attaque");
            }
            else if(1 <= action && action <= 5) {
                //SELECTION D'ARME
                ArrayList<Arme> listeDesArmes = plateau.turnPlayer.getArmes();
                switch(action) {
                    case 1:
                        if(isSelectionDeLarmeAutorisee(0)) {
                            plateau.turnPlayer.armeSelectionnee = plateau.turnPlayer.getArmes().get(0);
                            //waitingReward = calcReward(action);
                            System.out.println("Action décidée -- sélection arme 1");

                        }
                        break;
                    case 2:
                        if(isSelectionDeLarmeAutorisee(1)) {
                            plateau.turnPlayer.armeSelectionnee = plateau.turnPlayer.getArmes().get(1);
                            //waitingReward = calcReward(action);
                            System.out.println("Action décidée -- sélection arme 2");
                        }
                        break;
                    case 3:
                        if(isSelectionDeLarmeAutorisee(2)) {
                            plateau.turnPlayer.armeSelectionnee = plateau.turnPlayer.getArmes().get(2);
                            //waitingReward = calcReward(action);
                            System.out.println("Action décidée -- sélection arme 3");
                        }
                        break;
                    case 4:
                        if(isSelectionDeLarmeAutorisee(3)) {
                            plateau.turnPlayer.armeSelectionnee = plateau.turnPlayer.getArmes().get(3);
                            //waitingReward = calcReward(action);
                            System.out.println("Action décidée -- sélection arme 4");
                        }
                        break;
                    case 5:
                        if(isSelectionDeLarmeAutorisee(4)) {
                            plateau.turnPlayer.armeSelectionnee = plateau.turnPlayer.getArmes().get(4);
                            //waitingReward = calcReward(action);
                            System.out.println("Action décidée -- sélection arme 5");
                        }
                        break;
                }
            }
            else if(6 <= action && action <= 9) {
                //DÉPLACEMENTS
                Position positionDuJoueur = plateau.turnPlayer.getPosition();
                int deltaX;
                int deltaY;
                switch(action) {
                    case 6:
                        //Haut
                        deltaX = 0;
                        deltaY = -1;
                        if(isDeplacementAutorise(positionDuJoueur, deltaX, deltaY)) {
                            System.err.println("-------Action VALIDÉE -- déplacement haut------");
                            plateau.deplacementDuJoueur(new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY));
                            //waitingReward = calcReward(action);
                        }
                        System.out.println("Action décidée -- déplacement haut");
                        break;
                    case 7:
                        //Bas
                        deltaX = 0;
                        deltaY = 1;
                        if(isDeplacementAutorise(positionDuJoueur, deltaX, deltaY)) {
                            System.err.println("-------Action VALIDÉE -- déplacement bas------");
                            plateau.deplacementDuJoueur(new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY));
                            //waitingReward = calcReward(action);
                        }
                        System.out.println("Action décidée -- déplacement bas");
                        break;
                    case 8:
                        //Gauche
                        deltaX = -1;
                        deltaY = 0;
                        if(isDeplacementAutorise(positionDuJoueur, deltaX, deltaY)) {
                            System.err.println("-------Action VALIDÉE -- déplacement gauche------");
                            plateau.deplacementDuJoueur(new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY));
                            //waitingReward = calcReward(action);
                        }
                        System.out.println("Action décidée -- déplacement gauche");
                        break;
                    case 9:
                        //Droite
                        deltaX = 1;
                        deltaY = 0;
                        if(isDeplacementAutorise(positionDuJoueur, deltaX, deltaY)) {
                            System.err.println("-------Action VALIDÉE -- déplacement droite------");
                            plateau.deplacementDuJoueur(new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY));
                            //waitingReward = calcReward(action);
                        }
                        System.out.println("Action décidée -- déplacement droite");
                        break;
                }
            }
            else if(10 <= action && action <= 13) {
                //CONSTRUCTION
                Position positionDuJoueur = plateau.turnPlayer.getPosition();
                int deltaX;
                int deltaY;
                switch(action) {
                    case 10:
                        //Haut
                        deltaX = 0;
                        deltaY = -1;
                        if(isConstructionAutorisee(positionDuJoueur, deltaX, deltaY)) {
                            Case caseAconstruire = plateau.getCaseByPosition(new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY));
                            plateau.construireMur(caseAconstruire, new CaseMur(new Mur(), new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY)));
                            //waitingReward = calcReward(action);
                        }
                        System.out.println("Action décidée -- Construire haut");
                        break;
                    case 11:
                        //Bas
                        deltaX = 0;
                        deltaY = 1;
                        if(isConstructionAutorisee(positionDuJoueur, deltaX, deltaY)) {
                            Case caseAconstruire = plateau.getCaseByPosition(new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY));
                            plateau.construireMur(caseAconstruire, new CaseMur(new Mur(), new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY)));
                            //waitingReward = calcReward(action);
                        }
                        System.out.println("Action décidée -- Construire bas");
                        break;
                    case 12:
                        //Gauche
                        deltaX = -1;
                        deltaY = 0;
                        if(isConstructionAutorisee(positionDuJoueur, deltaX, deltaY)) {
                            Case caseAconstruire = plateau.getCaseByPosition(new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY));
                            plateau.construireMur(caseAconstruire, new CaseMur(new Mur(), new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY)));
                            //waitingReward = calcReward(action);
                        }
                        System.out.println("Action décidée -- Construire gauche");
                        break;
                    case 13:
                        //Droite
                        deltaX = 1;
                        deltaY = 0;
                        if(isConstructionAutorisee(positionDuJoueur, deltaX, deltaY)) {
                            Case caseAconstruire = plateau.getCaseByPosition(new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY));
                            plateau.construireMur(caseAconstruire, new CaseMur(new Mur(), new Position(positionDuJoueur.getX()+deltaX, positionDuJoueur.getY()+deltaY)));
                            //waitingReward = calcReward(action);

                        }
                        System.out.println("Action décidée -- Construire droite");
                        break;
                }
            }
            else if(action == 14 || action == 15) {
                //INTÉRACTION AVEC LES OBJETS (Prendre ou laisser)
                Position positionDuJoueur = plateau.turnPlayer.getPosition();
                if(isInteractionAutorisee()) {
                    if(plateau.turnPlayer.caseSauvegarde.getType().equals("CaseArme")
                            && plateau.turnPlayer.getArmes().size() < 5) {
                        if(action == 14) {
                            System.err.println("------------Action VALIDÉE -- Arme Prise--------------");
                            int lancer1 = plateau.lancerUnDeAquatreChiffres();
                            int lancer2 = plateau.lancerUnDeAhuitChiffres();
                            Arme arme = plateau.tirageArme(lancer1, lancer2);
                            plateau.turnPlayer.ajouterArme(arme);
                            plateau.turnPlayer.caseSauvegarde = null;
                            //plateau.turnPlayer.armeSelectionnee = plateau.turnPlayer.getArmes().get(plateau.turnPlayer.getArmes().size()-1);
                           // waitingReward = calcReward(action);

                        }
                    }
                    else if(plateau.turnPlayer.caseSauvegarde.getType().equals("CasePopo")) {
                        if(action == 14) {
                            System.err.println("------------Action VALIDÉE -- Popo Prise--------------");
                            CasePopo casePopo = (CasePopo) plateau.turnPlayer.caseSauvegarde;
                            plateau.turnPlayer.setPtAttaque(plateau.turnPlayer.getPtAttaque()-1);
                            if(casePopo.getVolume() == CasePopo.VOLUME_PETIT)
                                plateau.turnPlayer.setPtSante(plateau.turnPlayer.getPtSante()+plateau.lancerUnDe());
                            if(casePopo.getVolume() == CasePopo.VOLUME_MOYEN)
                                plateau.turnPlayer.setPtSante(plateau.turnPlayer.getPtSante()+plateau.lancerDeuxDes());
                            if(casePopo.getVolume() == CasePopo.VOLUME_GRAND)
                                plateau.turnPlayer.setPtSante(plateau.turnPlayer.getPtSante()+plateau.lancerTroisDes());
                            if(plateau.turnPlayer.getPtSante() > 100)
                                plateau.turnPlayer.setPtSante(100);
                            plateau.turnPlayer.caseSauvegarde = null;
                            //waitingReward = calcReward(action);

                        }
                    }
                    else if(plateau.turnPlayer.caseSauvegarde.getType().equals("CaseArmure")) {
                        if(action == 14) {
                            System.err.println("------------Action VALIDÉE -- Armure Prise--------------");
                            CaseArmure caseArmure = (CaseArmure) plateau.turnPlayer.caseSauvegarde;
                            plateau.turnPlayer.setPtAttaque(plateau.turnPlayer.getPtAttaque()-1);
                            if(caseArmure.getVolume() == CaseArmure.VOLUME_PETIT)
                                plateau.turnPlayer.setPtSante(plateau.turnPlayer.getPtSante()+plateau.lancerUnDe());
                            if(caseArmure.getVolume() == CaseArmure.VOLUME_MOYEN)
                                plateau.turnPlayer.setPtSante(plateau.turnPlayer.getPtSante()+plateau.lancerDeuxDes());
                            if(caseArmure.getVolume() == CaseArmure.VOLUME_GRAND)
                                plateau.turnPlayer.setPtSante(plateau.turnPlayer.getPtSante()+plateau.lancerTroisDes());
                            if(plateau.turnPlayer.getPtArmure() > 100)
                                plateau.turnPlayer.setPtArmure(100);
                            plateau.turnPlayer.caseSauvegarde = null;
                            //waitingReward = calcReward(action);

                        }
                    }
                }
                System.out.println("Action décidée -- Prendre ou laisser objet");
            }
        else {
            //FINIR LE TOUR
            if(plateau.turnPlayer == plateau.getListeDeJoueurs().get(0)) {
                plateau.getListeDeJoueurs().get(1).setPtAttaque(plateau.getListeDeJoueurs().get(1).getPtAttaque() + plateau.lancerUnDe());
                plateau.getListeDeJoueurs().get(1).setPtMouvement(plateau.lancerDeuxDes());
                plateau.turnPlayer = plateau.getListeDeJoueurs().get(1);
            }
            else {
                plateau.getListeDeJoueurs().get(0).setPtAttaque(plateau.getListeDeJoueurs().get(0).getPtAttaque() + plateau.lancerUnDe());
                plateau.getListeDeJoueurs().get(0).setPtMouvement(plateau.lancerDeuxDes());
                plateau.turnPlayer = plateau.getListeDeJoueurs().get(0);
            }
            //waitingReward = calcReward(action);

            System.out.println("Action décidée -- Finir le tour");
        }

        lowPointDeSante = mapperLeBooleen(plateau.isLowPointDeSante());
        lowPointDarmure = mapperLeBooleen(plateau.isLowPointDarmure());
        lowPointDattaque = mapperLeBooleen(plateau.isLowPointDattaque());
        lowPointDeMouvement = mapperLeBooleen(plateau.isLowPointDeMouvement());
        coffreAportee = mapperLeBooleen(plateau.isCoffreAPortee());
        popoAportee = mapperLeBooleen(plateau.isPopoAPortee());
        armureAportee = mapperLeBooleen(plateau.isArmureAPortee());
        joueurAportee = mapperLeBooleen(plateau.isJoueurAPortee());
        joueurAporteeDeTir = mapperLeBooleen(plateau.isJoueurAPorteeDeTir());
        joueurAporteeDexplosion = mapperLeBooleen(plateau.isJoueurAPorteeDexplosion());
        turnPlayerAporteeDeTir = mapperLeBooleen(plateau.isTurnPlayerAPorteeDeTir());
        turnPlayerAporteeDexplosion = mapperLeBooleen(plateau.isTurnPlayerAPorteeDexplosion());
        constructionPossible = mapperLeBooleen(plateau.isConstructionPossible());

        waitingReward = calcReward(action);

        return getState();
    }

    public double getReward(int i) { return getReward(); }
    public double getReward() {	return waitingReward; }

    @Override
    public boolean validAction(int action) {
        //System.err.println("Action : " + action);
        //System.err.println("Joueur : " + plateau.turnPlayer.getName());
        //System.err.println("PositionX : " + j1.getPosition().getX());
        //System.err.println("PositionY : " + j1.getPosition().getY());
        if(!plateau.isFinDePartie()) {
            if(action == 0 && isAttaqueAutorisee()) {
                //System.err.println("Action : " + action);
                //System.err.println("Possible, return true");
                return true;
            }
            if((1 <= action && action <= 5) && isSelectionDeLarmeAutorisee(action)) {
                //System.err.println("Action : " + action);
                //System.err.println("Possible, return true");
                return true;
            }
            if(6 == action && isDeplacementAutorise(plateau.turnPlayer.getPosition(), 0, -1)) {
                //System.err.println("Action : " + action);
                //System.err.println("Possible, return true");
                return true;
            }
            if(7 == action && isDeplacementAutorise(plateau.turnPlayer.getPosition(), 0, 1))
                return true;
            if(8 == action && isDeplacementAutorise(plateau.turnPlayer.getPosition(), -1, 0))
                return true;
            if(9 == action && isDeplacementAutorise(plateau.turnPlayer.getPosition(), 1, 0))
                return true;
            if(10 == action && isConstructionAutorisee(plateau.turnPlayer.getPosition(), 0, -1))
                return true;
            if(11 == action && isConstructionAutorisee(plateau.turnPlayer.getPosition(), 0, 1))
                return true;
            if(12 == action && isConstructionAutorisee(plateau.turnPlayer.getPosition(), -1, 0))
                return true;
            if(13 == action && isConstructionAutorisee(plateau.turnPlayer.getPosition(), 1, 0))
                return true;
            if(14 == action || 15 == action && isInteractionAutorisee())
                return true;
            if(16 == action)
                return true;
        }
        else {
            System.err.println("00000000000000000000000 FIN DE PARTIE 00000000000000000000000");
        }

        return false;
    }

    public boolean endState() {
        return endGame();
    }

    public int[] resetState() {
        scoreJ1 = 0;
        scoreJ2 = 0;
        plateau = new Plateau(plateau.getxTaille(), plateau.getyTaille(), "J1", "J2");
        return getState();
    }

    public double getInitValues() { return INIT_VALS_SOLID_WAR; }
    /******* end RLWorld functions **********/


    public int[] getState() {
        // translates current state into boolean array
        stateArray = new int[NUM_OBJECTS_SOLID_WAR];
        stateArray[0] = lowPointDeSante;
        stateArray[1] = lowPointDarmure;
        stateArray[2] = lowPointDattaque;
        stateArray[3] = lowPointDeMouvement;
        stateArray[4] = coffreAportee;
        stateArray[5] = popoAportee;
        stateArray[6] = armureAportee;
        stateArray[7] = joueurAportee;
        stateArray[8] = joueurAporteeDeTir;
        stateArray[9] = joueurAporteeDexplosion;
        stateArray[10] = turnPlayerAporteeDeTir;
        stateArray[11] = turnPlayerAporteeDexplosion;
        stateArray[12] = constructionPossible;
        return stateArray;
    }

    public double calcReward(int action) {
        double newReward = 0;
        /*if(action == 15 || action == 16) {
            newReward += 0;
        }
        else if(action == 0) {
            newReward += 5;
        }
        else {
            newReward += recompensePourUneAction;
        }*/

        if(plateau.getListeDeJoueurs().get(1).isMort()) {
            scoreJ1++;
            newReward += recompenseDeKill;
        }
        if(plateau.getListeDeJoueurs().get(0).isMort()) {
            scoreJ2++;
            newReward -= punitionDeMort;
        }

        return newReward;
    }

    boolean endGame() {
        //return (((mx==hx)&&(my==hy)&& gotCheese) || ((cx==mx) && (cy==my)));
        return ((plateau.getListeDeJoueurs().get(0).getPtSante() <= 0) || (plateau.getListeDeJoueurs().get(1).getPtSante() <= 0));
    }

    /******** heuristic functions ***********/

    /******** end heuristic functions ***********/


    /******** wall generating functions **********/

    /******** wall generating functions **********/

}