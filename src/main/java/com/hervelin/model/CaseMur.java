package com.hervelin.model;


import javafx.scene.image.Image;

public class CaseMur extends Case {
    private Mur mur;

    public CaseMur(Mur mur,Position p) {
        super(p,"CaseMur",new Image("images/TextureMurMini-min.png"));
        this.mur=mur;
    }

    public Mur getMur() {
        return mur;
    }

    public void setMur(Mur mur) {
        this.mur = mur;
    }
}
