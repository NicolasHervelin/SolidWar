package com.hervelin.model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;

public abstract class Case {

    public Position position;
    private Image img;
    private String type;
    private Button bouton;
    private int cout=0;


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

    public Position getPosition(){ return position;}

    public Button getBouton() {
        return bouton;
    }

    public void setBouton(Button bouton) {
        this.bouton = bouton;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }
}
