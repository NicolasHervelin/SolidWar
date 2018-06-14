package com.hervelin.model.FX;

import javafx.scene.image.Image;

public class Grenade extends Arme {
    public Grenade(String image) {
        super("grenade","droit",4, 1,2,0,new Image(image));
    }
}
