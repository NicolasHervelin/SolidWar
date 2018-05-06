package com.hervelin.model;

public class CaseMur extends Case {
    private int ptStructure;

    public CaseMur(Position p) {
        super(p,"CaseMur");
        this.ptStructure = 50;
    }

    public int getPtStructure() {
        return ptStructure;
    }

    public void setPtStructure(int ptStructure) {
        this.ptStructure = ptStructure;
    }
}
