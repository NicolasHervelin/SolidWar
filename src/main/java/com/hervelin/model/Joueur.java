package com.hervelin.model;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Joueur {
    private String name;
    private Position position;
    private ArrayList<Arme> armes = new ArrayList<>(5);
    private int ptMouvement;
    private ArrayList<Case> listPortee;
    private int ptAttaque;
    private int ptSante;
    private int ptArmure;
    private Image imageJoueur;
    private int brique;
    public Case caseSauvegarde;
    public Arme armeSelectionnee;
    public int indexArmeSelectionnee;

    public Joueur(String name, Position p, Image image) {
        this.name = name;
        this.position=p;
        this.ptMouvement = 0;
        this.ptAttaque = 0;
        this.ptSante = 100;
        this.ptArmure = 0;
        this.imageJoueur = image;
        this.brique=0;
        this.caseSauvegarde = null;
        Couteau armeDeBase = new Couteau("images/Solid_war/COFFRE/ARMES/COUTEAU/CLASSE1.png");
        armes.add(armeDeBase);
        this.armeSelectionnee = armes.get(0);
        this.indexArmeSelectionnee = 0;
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

    public void ajouterArme(Arme arme) {armes.add(arme); }

    public void remplacerArme(Arme arme, Arme a){
        int index=armes.lastIndexOf(arme);
        armes.remove(arme);
        armes.add(index,a);
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

    public int getBrique() {
        return brique;
    }

    public void setBrique(int brique) {
        this.brique = brique;
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


    public int getPtArmure() {
        return ptArmure;
    }

    public void setPtArmure(int ptArmure) {
        this.ptArmure = ptArmure;
    }

    public ArrayList<Case> getListPortee() {
        return listPortee;
    }

    public void setListPortee(ArrayList<Case> listPortee) {
        this.listPortee = listPortee;
    }
}
