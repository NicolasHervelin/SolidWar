package com.hervelin.model;

import java.awt.*;

public class Pion {
    private Position position;
    private Color couleur;

    public Pion(Position p, Color color) {
        position=p;
        couleur = color;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

}
