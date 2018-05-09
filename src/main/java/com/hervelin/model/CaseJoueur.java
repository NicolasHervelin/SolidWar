package com.hervelin.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class CaseJoueur extends Case {
    private Joueur joueur;
    private Image img;

    public CaseJoueur(Joueur j,Image img) {
        super(j.getPosition(),"CaseJoueur",Color.ORANGE);
        joueur=j;
        this.img=img;

    }

    public Image getImg(){
        return img;
    }

}
