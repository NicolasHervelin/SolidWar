package com.hervelin.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class CaseJoueur extends Case {
    private Joueur joueur;

    public CaseJoueur(Joueur j, Image img) {
        super(j.getPosition(),"CaseJoueur",img);
        joueur=j;
    }
    public Joueur getJoueur() {
        return joueur;
    }
}
