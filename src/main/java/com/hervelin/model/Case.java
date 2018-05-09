package com.hervelin.model;

import javafx.scene.image.Image;

public abstract class Case {
    public Position position;
    private Image img;
    private String type;


    public Case(Position p, String type,Image img) {
        position=p;
        this.type=type;
        this.img=img;
    }

    public String getType() {
        return this.type;
    }

    public Image getImg(){
        return img;
    }

    public void setImg(Image img){
        this.img=img;
    }

}
