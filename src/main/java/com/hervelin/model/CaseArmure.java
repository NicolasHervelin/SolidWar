package com.hervelin.model;

import javafx.scene.paint.Color;

public class CaseArmure extends Case {
    private int volume;

    public CaseArmure(Position p, int volume) {
        super(p, "CaseArmure", Color.LIGHTCYAN);
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
