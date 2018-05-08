package com.hervelin.model;

import javafx.scene.paint.Color;

public class CaseNormale extends Case {
    private boolean isMine;
    private String joueurProprioDeLaMine;

    public CaseNormale(Position p) {
        super(p,"CaseNormale",Color.WHITE);
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
