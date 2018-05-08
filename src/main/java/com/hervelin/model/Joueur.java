package com.hervelin.model;

import java.awt.*;
import java.util.ArrayList;

public class Joueur {
    private String name;
    private Pion pion;
    private Position position;
    private ArrayList<Arme> armes = new ArrayList<>();
    private int ptMouvement;
    private int ptAttaque;
    private int ptSante;

    public Joueur(String name,Position p) {
        this.name = name;
        this.position=p;
        this.pion = new Pion(p,Color.BLUE);
        this.ptMouvement = 0;
        this.ptAttaque = 0;
        this.ptSante = 100;

        Couteau armeDeBase = new Couteau();
    }

    public void deplacerX(int deplacementX) {

        position.x += deplacementX;
    }

    public void deplacerY(int deplacementY) {

        position.y += deplacementY;
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
