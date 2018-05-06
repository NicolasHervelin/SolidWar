package com.hervelin.model;

public class CaseNormale extends Case {
    private boolean isMine;
    private String joueurProprioDeLaMine;

    public CaseNormale(int xPosition, int yPosition) {
        super(xPosition, yPosition);
        this.isMine = false;
        this.joueurProprioDeLaMine = "";
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getJoueurProprioDeLaMine() {
        return joueurProprioDeLaMine;
    }

    public void setJoueurProprioDeLaMine(String joueurProprioDeLaMine) {
        this.joueurProprioDeLaMine = joueurProprioDeLaMine;
    }
}
