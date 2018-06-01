package com.hervelin.model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;

public abstract class Case {

    public Position position;
    private Image img;
    private String type;
    private Button bouton;
    private int cout=0;

    //Pour affichage des case bonne résolution sans polluer la grille
    private Image image128coffre = new Image("images/TextureCoffre128-min.png");
    private Image image128armure = new Image("images/TextureBouclier128-min.png");
    private Image image128popo = new Image("images/TexturePopo128-min.png");

    public Case(Position p, String type, Image img) {
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

    public Image getImage128coffre() {
        return image128coffre;
    }

    public Image getImage128armure() {
        return image128armure;
    }

    public Image getImage128popo() {
        return image128popo;
    }

    public Image getImage128() {
        if(this.getType().equals("CaseArme"))
            return getImage128coffre();
        if(this.getType().equals("CaseArmure"))
            return getImage128armure();
        if(this.getType().equals("CasePopo"))
            return getImage128popo();
        return null;
    }
}
