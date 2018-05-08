package com.hervelin.model;

import javafx.scene.paint.Color;

public class CaseMur extends Case {
    private int ptStructure;

    public CaseMur(Position p) {
        super(p,"CaseMur",Color.BLACK);
        this.ptStructure = 50;
    }

    public int getPtStructure() {
        return ptStructure;
    }

    public void setPtStructure(int ptStructure) {
        this.ptStructure = ptStructure;
    }
}
