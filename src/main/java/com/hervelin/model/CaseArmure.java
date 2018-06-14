package com.hervelin.model;


import javafx.scene.image.Image;

public class CaseArmure extends Case {
    private int volume;

    public static final int VOLUME_PETIT = 10;
    public static final int VOLUME_MOYEN = 25;
    public static final int VOLUME_GRAND = 50;

    public CaseArmure(Position p, int volume) {
        super(p, "CaseArmure",null);
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
