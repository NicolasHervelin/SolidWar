package com.hervelin.model;

import javafx.scene.image.Image;

public class CasePopo extends Case {
    private int volume;

    public static final int VOLUME_PETIT = 10;
    public static final int VOLUME_MOYEN = 25;
    public static final int VOLUME_GRAND = 50;

    public CasePopo(Position p, int volume) {
        super(p,"CasePopo",new Image("images/TextureBouclierBleu.png"));
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
