package com.hervelin.model;

import java.awt.*;

public abstract class Case {
    public Position position;
    private Color couleur;
    private String type;


    public Case(Position p, String type,Color color) {
        position=p;
        this.type=type;
        couleur=color;
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
