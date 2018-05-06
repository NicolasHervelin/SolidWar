package com.hervelin.model;

import java.awt.*;

public abstract class Case {
    private Position position;
    private Color couleur;
    private String type;

    
    public Case(Position p, String type) {
        position=p;
        this.type=type;
        switch(type) {
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
        return this.type;
    }

}
