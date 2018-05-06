package com.hervelin.model;

public abstract class Arme {
    private String name;
    private int portée;
    private int dmg_dés;
    private int Pa;

    public Arme(String name, int portée, int dmg_dés, int pa) {
        this.name = name;
        this.portée = portée;
        this.dmg_dés = dmg_dés;
        Pa = pa;
    }


    public String getType() {
        return this.getClass().getName();
    }

    public String getName() {
        return name;
    }

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
}



