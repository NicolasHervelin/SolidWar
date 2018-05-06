package com.hervelin.model;

public class CaseMur extends Case {
    private int ptStructure;

    public CaseMur(int xPosition, int yPosition) {
        super(xPosition, yPosition);
        this.ptStructure = 50;
    }

    public int getPtStructure() {
        return ptStructure;
    }

    public void setPtStructure(int ptStructure) {
        this.ptStructure = ptStructure;
    }
}
