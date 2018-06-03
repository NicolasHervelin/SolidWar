package com.hervelin.model;

import javafx.scene.image.Image;

public class Pistolet extends Arme {
    public Pistolet(String image) {
        super("pistolet","droit",3,2,2,1,new Image(image));
    }
}
