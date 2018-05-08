package com.hervelin.model;

import java.util.ArrayList;

public class Plateau {
    private ArrayList<Case> casesDuPlateau = new ArrayList<>();
    private ArrayList<Joueur> listeDeJoueurs = new ArrayList<>(4);
    private int xTaille;
    private int yTaille;
    private Position p1= new Position(1,1);
    private Position p2= new Position(xTaille,yTaille);
    private Position p3= new Position(1,yTaille);
    private Position p4= new Position(xTaille,1);


    public Plateau(int xTaille, int yTaille, String joueur1, String joueur2) {
        this.xTaille = xTaille;
        this.yTaille = yTaille;
        listeDeJoueurs.add(new Joueur(joueur1,p1));
        listeDeJoueurs.add(new Joueur(joueur2,p2));
        initialize();
    }

    public Plateau(int xTaille, int yTaille, String joueur1, String joueur2, String joueur3) {
        this.xTaille = xTaille;
        this.yTaille = yTaille;
        listeDeJoueurs.add(new Joueur(joueur1,p1));
        listeDeJoueurs.add(new Joueur(joueur2,p2));
        listeDeJoueurs.add(new Joueur(joueur3,p3));
        initialize();
    }

    public Plateau(int xTaille, int yTaille, String joueur1, String joueur2, String joueur3, String joueur4) {
        this.xTaille = xTaille;
        this.yTaille = yTaille;
        listeDeJoueurs.add(new Joueur(joueur1,p1));
        listeDeJoueurs.add(new Joueur(joueur2,p2));
        listeDeJoueurs.add(new Joueur(joueur3,p3));
        listeDeJoueurs.add(new Joueur(joueur4,p4));
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
        creerPlateau();
        creerMurs();
        creerArmes();
        creerPopos();
    }

    public Case getCaseByPosition(Position p) {
        for (Case temp : casesDuPlateau) {
            if((temp.position.getX() == p.x) && (temp.position.getY() == p.y)) {
                return temp;
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
            casesDuPlateau.add(new CaseMur(new Position(xRandom,yRandom)));
        }
        for(int j=0;j<100;j++) {
            int xRandom = 1 + (int) (Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int) (Math.random() * ((yTaille - 1) + 1));
            for (int i = 0; i < 6; i++) {
                Case caseASupprimer = getCaseByPosition(new Position(xRandom, yRandom));
                casesDuPlateau.remove(caseASupprimer);
                casesDuPlateau.add(new CaseMur(new Position(xRandom, yRandom)));
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
        for(int i = 0; i <= xTaille/15; i++) {
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

    private int lancerUnDe() {
        int lance = 1 + (int)(Math.random() * ((6 - 1) + 1));
        return lance;
    }

    private int lancerDeuxDes() {
        int lance1 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        int lance2 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        return lance1 + lance2;
    }

    private int lancerTroisDes() {
        int lance1 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        int lance2 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        int lance3 = 1 + (int)(Math.random() * ((6 - 1) + 1));
        return lance1 + lance2 + lance3;
    }

    private int lancerUnDeAtroisChiffres() {
        int lance = 1 + (int)(Math.random() * ((3 - 1) + 1));
        return lance;
    }

}
