package com.hervelin.model;


import javafx.scene.image.Image;

public class CaseArmure extends Case {
    private int volume;

    public CaseArmure(Position p, int volume) {
        super(p, "CaseArmure",new Image("images/TextureBouclierBleu.png"));
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
