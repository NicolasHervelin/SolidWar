package com.hervelin.model;

import java.util.ArrayList;

public class Joueur {
    private String name;
    private Pion pion;
    private ArrayList<Arme> armes = new ArrayList<>();
    private int ptMouvement;
    private int ptAttaque;
    private int ptSante;

    public Joueur(String name) {
        this.name = name;
        this.pion = new Pion();
        this.ptMouvement = 0;
        this.ptAttaque = 0;
        this.ptSante = 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pion getPion() {
        return pion;
    }

    public void setPion(Pion pion) {
        this.pion = pion;
    }

    public ArrayList<Arme> getArmes() {
        return armes;
    }

    public void setArmes(ArrayList<Arme> armes) {
        this.armes = armes;
    }

    public void ajouterArme(Arme arme) {
        armes.add(arme);
    }

    public int getPtMouvement() {
        return ptMouvement;
    }

    public void setPtMouvement(int ptMouvement) {
        this.ptMouvement = ptMouvement;
    }

    public int getPtAttaque() {
        return ptAttaque;
    }

    public void setPtAttaque(int ptAttaque) {
        this.ptAttaque = ptAttaque;
    }

    public int getPtSante() {
        return ptSante;
    }

    public void setPtSante(int ptSante) {
        this.ptSante = ptSante;
    }

}
