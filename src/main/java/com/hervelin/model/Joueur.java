package com.hervelin.model;


import javafx.scene.image.Image;

import java.util.ArrayList;

public class Joueur {
    private String name;
    private Position position;
    private Image img;
    private ArrayList<Arme> armes = new ArrayList<>();
    private int ptMouvement;
    private int ptAttaque;
    private int ptSante;

    public Joueur(String name,Position p,Image img) {
        this.name = name;
        this.img=img;
        this.position=p;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position p){
        position=p;
    }
    public Image getImg(){
        return img;
    }
}
