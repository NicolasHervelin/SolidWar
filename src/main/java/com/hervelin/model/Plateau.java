package com.hervelin.model;

import java.util.ArrayList;

public class Plateau {
    private ArrayList<Case> casesDuPlateau = new ArrayList<>();
    private int xTaille;
    private int yTaille;

    public Plateau(int xTaille, int yTaille) {
        this.xTaille = xTaille;
        this.yTaille = yTaille;
        creerPlateau();
        creerMurs();
        creerArmes();
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

    public Case getCaseByPosition(int xPosition, int yPosition) {
        for (Case temp : casesDuPlateau) {
            if((temp.getXPosition() == xPosition) && (temp.getYPosition() == yPosition)) {
                return temp;
            }
        }
        return null;
    }

    private void creerPlateau() {
        for(int i = 1; i <= xTaille; i++) {
            for(int j = 1; j <= yTaille; j++) {
                casesDuPlateau.add(new CaseNormale(i, j));
            }
        }
    }

    private void creerMurs() {
        for(int i = 0; i <= xTaille; i++) {
            int xRandom = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            Case caseASupprimer = getCaseByPosition(xRandom,yRandom);
            casesDuPlateau.remove(caseASupprimer);
            casesDuPlateau.add(new CaseMur(xRandom,yRandom));
        }
    }

    private void creerArmes() {
        for(int i = 0; i <= xTaille/5; i++) {
            int xRandom = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            Case caseASupprimer = getCaseByPosition(xRandom,yRandom);
            if(caseASupprimer.getType().equals(Case.CASE_NORMALE)) {
                casesDuPlateau.remove(caseASupprimer);
                casesDuPlateau.add(new CaseArme(xRandom,yRandom));
            }
        }
    }

    private void creerPopos() {
        for(int i = 0; i <= xTaille/15; i++) {
            int xRandom = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            Case caseASupprimer = getCaseByPosition(xRandom,yRandom);
            if(caseASupprimer.getType().equals(Case.CASE_NORMALE)) {
                casesDuPlateau.remove(caseASupprimer);
                casesDuPlateau.add(new CasePopo(xRandom,yRandom,CasePopo.VOLUME_PETIT));
            }
        }

        for(int i = 0; i <= xTaille/15; i++) {
            int xRandom = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            Case caseASupprimer = getCaseByPosition(xRandom,yRandom);
            if(caseASupprimer.getType().equals(Case.CASE_NORMALE)) {
                casesDuPlateau.remove(caseASupprimer);
                casesDuPlateau.add(new CasePopo(xRandom,yRandom,CasePopo.VOLUME_MOYEN));
            }
        }

        for(int i = 0; i <= xTaille/15; i++) {
            int xRandom = 1 + (int)(Math.random() * ((xTaille - 1) + 1));
            int yRandom = 1 + (int)(Math.random() * ((yTaille - 1) + 1));

            Case caseASupprimer = getCaseByPosition(xRandom,yRandom);
            if(caseASupprimer.getType().equals(Case.CASE_NORMALE)) {
                casesDuPlateau.remove(caseASupprimer);
                casesDuPlateau.add(new CasePopo(xRandom,yRandom,CasePopo.VOLUME_GRAND));
            }
        }
    }
}
