package com.hervelin.model;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Joueur {
    private String name;
    private Position position;
    private ArrayList<Arme> armes = new ArrayList<>();
    private int ptMouvement;
    private ArrayList<Case> listPortée;
    private int ptAttaque;
    private int ptSante;
    private int ptArmure;
    private Image imageJoueur;
    private int ptConstruction;

    public Joueur(String name, Position p, Image image) {
        this.name = name;
        this.position=p;
        this.ptMouvement = 0;
        this.ptAttaque = 0;
        this.ptSante = 100;
        this.ptArmure = 0;
        this.imageJoueur = image;
        this.ptConstruction = 0;

        Couteau armeDeBase = new Couteau();
        armes.add(armeDeBase);
    }

    public void deplacerX(int deplacementX) {

        position.x += deplacementX;
    }

    public void deplacerY(int deplacementY) {

        position.y += deplacementY;
    }

    public void deplacerXY(int deplacementX, int deplacementY) {
        deplacerX(deplacementX);
        deplacerY(deplacementY);
    }

    public void attaquerJoueur(Joueur other, int degats, boolean isLancerParfait) {
        if(isLancerParfait)
            other.setPtSante(other.getPtSante() - degats);
        else {
            if(other.getPtArmure() < degats) {
                other.setPtSante(other.getPtSante() - (degats - other.getPtArmure()));
                other.setPtArmure(0);
            }
            else {
                other.setPtArmure(other.getPtArmure() - degats);
            }
        }
    }

    public void attaquerMur(Mur mur, int degats, boolean isLancerParfait) {
        if(isLancerParfait) {
            mur.setPtStructure(0);
        }
        else {
            if(mur.getPtStructure() < degats)
                mur.setPtStructure(0);
            else
                mur.setPtStructure(mur.getPtStructure() - degats);
        }
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

    public Image getImageJoueur() {
        return imageJoueur;
    }

    public void setImageJoueur(Image imageJoueur) {
        this.imageJoueur = imageJoueur;
    }

    public int getPtConstruction() {
        return ptConstruction;
    }

    public void setPtConstruction(int ptConstruction) {
        this.ptConstruction = ptConstruction;
    }

    public int getPtArmure() {
        return ptArmure;
    }

    public void setPtArmure(int ptArmure) {
        this.ptArmure = ptArmure;
    }

    public ArrayList<Case> getListPortée() {
        return listPortée;
    }

    public void setListPortée(ArrayList<Case> listPortée) {
        this.listPortée = listPortée;
    }
}
