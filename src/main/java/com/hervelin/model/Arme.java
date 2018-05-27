package com.hervelin.model;

import javafx.scene.image.Image;

public abstract class Arme {
    private String name;
    private String typeTir;
    private int portée;
    private int dmg_dés;
    private Image image;
    private int Pa;
    private int isReturnDamage;


    public Arme(String name,String typeTir, int portée, int dmg_dés, int pa,int isReturnDamage,Image img) {
        this.name = name;
        image=img;
        this.typeTir=typeTir;
        this.portée = portée;
        this.isReturnDamage=isReturnDamage;
        this.dmg_dés = dmg_dés;
        Pa = pa;
    }


    public String getType() {
        return this.getClass().getName();
    }

    public String getName() {
        return name;
    }

    public Image getImage(){ return image; }

    public void setImage(Image img){ image=img; }

    public void setName(String name) {
        this.name = name;
    }

    public int getPortée() {
        return portée;
    }

    public void setPortée(int portée) {
        this.portée = portée;
    }

    public int getDmg_dés() {
        return dmg_dés;
    }

    public void setDmg_dés(int dmg_dés) {
        this.dmg_dés = dmg_dés;
    }

    public int getPa() {
        return Pa;
    }

    public void setPa(int pa) {
        Pa = pa;
    }

    public int getIsReturnDamage() {
        return isReturnDamage;
    }

    public void setIsReturnDamage(int isReturnDamage) {
        this.isReturnDamage = isReturnDamage;
    }

    public String getTypeTir() {
        return typeTir;
    }
}



