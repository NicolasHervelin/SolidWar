package com.hervelin.model;


import javafx.scene.image.Image;

public class CaseMur extends Case {
    private int ptStructure;

    public CaseMur(Position p) {
        super(p,"CaseMur",new Image("images/TextureMur.png"));
        this.ptStructure = 10;
    }

    public int getPtStructure() {
        return ptStructure;
    }

    public void setPtStructure(int ptStructure) {
        this.ptStructure = ptStructure;
    }
}
