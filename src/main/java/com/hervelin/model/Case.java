package com.hervelin.model;

import java.awt.*;

public abstract class Case {
    private int xPosition;
    private int yPosition;
    private Color couleur;

    public static final String CASE_NORMALE = "CaseNormale";
    public static final String CASE_MUR = "CaseMur";
    public static final String CASE_ARME = "CaseArme";
    public static final String CASE_POPO = "CasePopo";

    public Case() {

    }

    public Case(Position p) {
        this.xPosition = p.x;
        this.yPosition = p.y;
        switch(getType()) {
            case "CaseNormale" :
                this.couleur = Color.WHITE;
                break;

            case "CaseMur" :
                this.couleur = Color.BLACK;
                break;

            case "CaseArme" :
                this.couleur = Color.YELLOW;
                break;

            case "CasePopo" :
                this.couleur = Color.BLUE;
                break;

                default :
                    this.couleur = Color.WHITE;
                    break;

        }
    }


    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public String getType() {
        return this.getClass().getName();
    }

}
