package com.hervelin.controller;

import com.hervelin.model.*;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.BoundsAccessor;
import com.sun.javafx.tk.TKSceneListener;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ControllerJeu implements ControlledScreen {
    ScreensController myController;
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;
    private boolean isCaseDejaSelectionnee = false;
    private Case caseDejaSelectionnee;
    private int mode=0;

    final static String MOUVEMENT = "Obtenir les PM";
    final static String PA = "Obtenir les PA";
    final static String DEGATS = "Obtenir les dommages";
    final static String COULEURDARME = "Définir la puissance";

    public Plateau plateau;
    public int nombreCaseX = 40;
    public int nombreCaseY = 40;
    private ArrayList<Joueur> listeDesJoueurs;
    private ArrayList<Case> openList;
    private ArrayList<Case> ListPortee = new ArrayList<Case>();
    private ArrayList<Case> shoot = new ArrayList<Case>();
    private ArrayList<Case> casesDansExplosion = new ArrayList<Case>();
    private Image imageExplosion = new Image("images/TextureExplosion40-min.png");
    private String need = MOUVEMENT;

    @FXML
    public GridPane gridPlateau;
    @FXML
    public ScrollPane scrollPlateau;
    @FXML
    public GridPane anchorMain;
    @FXML
    public Button fight,move, boutonFinDuTour, boutonOuvrir, boutonLancerPM, boutonLancerPA;
    @FXML
    public ListView<Arme> listArmes;
    @FXML
    public Text nomJoueur;
    @FXML
    public Text nomJoueur2;
    @FXML
    public ImageView imageJoueur, imageJoueur2;
    @FXML
    public Pane pane = new Pane();
    @FXML
    public VBox vBoxLancers, vBoxSanteArmure;
    @FXML
    public HBox hBoxLancers1, hBoxLancers2, hBoxInfoRessources, hBoxPA, hBoxPM, hBoxBrique;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        setUp();
    }

    private void setUp() {
        String nomJoueur1 = myController.getData("joueur1");
        String nomJoueur2 = myController.getData("joueur2");
        String nomJoueur3 = myController.getData("joueur3");
        String nomJoueur4 = myController.getData("joueur4");

        //Initialisation du plateau
        switch (myController.getData("nbjoueurs")) {
            case "2 joueurs" :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2);
                joueur1 = plateau.getListeDeJoueurs().get(0);
                joueur2 = plateau.getListeDeJoueurs().get(1);
                break;
            case "3 joueurs" :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2, nomJoueur3);
                joueur1 = plateau.getListeDeJoueurs().get(0);
                joueur2 = plateau.getListeDeJoueurs().get(1);
                joueur3 = plateau.getListeDeJoueurs().get(2);
                break;
            case "4 joueurs" :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2, nomJoueur3, nomJoueur4);
                joueur1 = plateau.getListeDeJoueurs().get(0);
                joueur2 = plateau.getListeDeJoueurs().get(1);
                joueur3 = plateau.getListeDeJoueurs().get(2);
                joueur4 = plateau.getListeDeJoueurs().get(3);
                break;
            default :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2);
                joueur1 = plateau.getListeDeJoueurs().get(0);
                joueur2 = plateau.getListeDeJoueurs().get(1);
                break;
        }

        listeDesJoueurs = plateau.getListeDeJoueurs();
        plateau.turnPlayer.ajouterArme(new Bazooka());

        listArmes.getItems().setAll(plateau.turnPlayer.getArmes());
        listArmes.setCellFactory(new ArmeCellFactory());
        listArmes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Arme>() {
            @Override
            public void changed(ObservableValue<? extends Arme> observable, Arme oldValue, Arme newValue) {
             //   AlertBox.afficherDetailArme(newValue);
                afficherArme(newValue);
                switch (mode){
                    case 1:
                        clean_pathfinding(ListPortee);
                        break;
                    case 2:
                        clean_pathfinding(shoot);
                        break;
                    default:break;
                }
                mode=2;
                shoot_pathfinding(newValue);
            }
        });
        affichageDuJoueur(plateau.turnPlayer);
        mettreAjourSanteArmure();
        definitionCaseDuPlateau(null);
        initialiserRessources();

        Glow glow = new Glow(0.5);
        Reflection reflection=new Reflection();
        reflection.setInput(glow);
        Image img=new Image("images/target.png");
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        fight.setGraphic(imageView);
        fight.setEffect(reflection);
        Image img2=new Image("images/move.png");
        ImageView imageView2 = new ImageView(img2);
        imageView2.setPreserveRatio(true);
        move.setGraphic(imageView2);
        move.setEffect(reflection);

        Image img3=new Image("images/FinDuTour.png");
        ImageView imageView3 = new ImageView(img3);
        imageView2.setPreserveRatio(true);
        boutonFinDuTour.setGraphic(imageView3);
        boutonFinDuTour.setEffect(reflection);
        Image img4=new Image("images/Ouvrir.png");
        ImageView imageView4 = new ImageView(img4);
        imageView2.setPreserveRatio(true);
        boutonOuvrir.setGraphic(imageView4);
        boutonOuvrir.setEffect(reflection);

    }


    public void afficherArme(Arme arme){
        imageJoueur2.setImage(arme.getImage());
        nomJoueur2.setText(arme.getName());
    }

    public void afficherMur(Case mur){
        imageJoueur2.setImage(mur.getImg());
        nomJoueur2.setText("Mur");
    }

    public void afficherCaseNormale(Case cn){
        imageJoueur2.setImage(cn.getImg());
        nomJoueur2.setText("Case");
    }

    public void afficherPopo(Case Popo){
        imageJoueur2.setImage(Popo.getImage128());
        nomJoueur2.setText("Potion");
    }

    public void afficherCoffre(Case coffre){
        imageJoueur2.setImage(coffre.getImage128());
        nomJoueur2.setText("Coffre");
    }

    public void afficherArmure(Case armure){
        imageJoueur2.setImage(armure.getImage128());
        nomJoueur2.setText("Armure");
    }

    public void move(){
        if(mode==2){
            clean_pathfinding(shoot);
        }
        mode=1;
        pathfinding();
    }

    public void fight(){
        if(mode==1){
            clean_pathfinding(ListPortee);
        }
        mode=2;
        listArmes.getSelectionModel().select(0);
        shoot_pathfinding(listArmes.getSelectionModel().getSelectedItem());
    }

    //Définition des cases du plateau
    public void definitionCaseDuPlateau(Position positionAConserver) {
        gridPlateau.getChildren().clear();
        for (int row = 1; row <= plateau.getxTaille(); row++) {
            for (int col = 1; col <= plateau.getyTaille(); col++) {
                Button bouton = new Button();
                bouton.setStyle("-fx-padding:2 2 2 2;");
                Position positionActuelle = new Position(row,col);
                Case caseActuelle =plateau.getCaseByPosition(positionActuelle);
                setImagePourLesBoutons(bouton, caseActuelle.getImg());
                bouton.setPrefWidth(plateau.getxTaille()/1.5);
                bouton.setPrefHeight(plateau.getyTaille()/1.5);
                bouton.setOnAction(event -> AnalysePosition(positionActuelle));
                GridPane.setColumnIndex(bouton, col);
                GridPane.setRowIndex(bouton, row);
                gridPlateau.add(bouton, col, row);
                caseActuelle.setBouton(bouton);
                if(positionAConserver != null && positionAConserver.getX() == row && positionAConserver.getY() == col) {
                    bouton.requestFocus();
                }
            }
        }
    }

    //Assigne à chaque bouton l'image correspondante
    public void setImagePourLesBoutons(Button bouton, Image img) {
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(plateau.getxTaille() / 1.5);
        imageView.setFitHeight(plateau.getyTaille() / 1.5);
        bouton.setGraphic(imageView);
    }


    // Affiche en rouge les cases où le joueur peut tirer
    public void clean_pathfinding(ArrayList<Case> l){
        if(l!=null) {
            for (Case temp : l) {
                InnerShadow borderGlow = new InnerShadow();
                Button bouton = temp.getBouton();
                borderGlow.setOffsetX(0f);
                borderGlow.setOffsetY(0f);
                borderGlow.setColor(null);
                bouton.setEffect(null); //Apply the borderGlow effect to the JavaFX node
                temp.setBouton(bouton);
            }
        }
        l.clear();
    }

    public void shoot_pathfinding(Arme arme){
        switch(arme.getTypeTir()){
            case "droit":
                int row;
                Position depart=plateau.turnPlayer.getPosition();
                Case next;
                int i=0;
                openList=new ArrayList<Case>();
                shoot=new ArrayList<Case>();
                Case positionactuelle;
                openList.add(plateau.getCaseByPosition(depart));
                while (openList.size()>0) {
                    positionactuelle = openList.get(openList.size() - 1);
                    openList.remove(positionactuelle);
                    shoot.add(positionactuelle);
                    next=plateau.getCaseLeft(positionactuelle.getPosition());
                    if (next != null && next.getPosition().getY()>=depart.getY()-arme.getPortée() && i==0) {
                        if (next.getType()=="CaseMur"){
                            i=1;
                        }
                        openList.add(next);
                    }
                }
                i=0;
                openList.add(plateau.getCaseByPosition(depart));
                while (openList.size()>0) {
                    positionactuelle = openList.get(openList.size() - 1);
                    openList.remove(positionactuelle);
                    shoot.add(positionactuelle);
                    next=plateau.getCaseRight(positionactuelle.getPosition());
                    if (next != null && next.getPosition().getY()<=depart.getY()+arme.getPortée() && i==0) {
                        if (next.getType()=="CaseMur"){
                            i=1;
                        }
                        openList.add(next);
                    }
                }
                i=0;
                openList.add(plateau.getCaseByPosition(depart));
                while (openList.size()>0) {
                    positionactuelle = openList.get(openList.size() - 1);
                    openList.remove(positionactuelle);
                    shoot.add(positionactuelle);
                    next=plateau.getCaseUp(positionactuelle.getPosition());
                    if (next != null && next.getPosition().getX()>=depart.getX()-arme.getPortée() && i==0) {
                        if (next.getType()=="CaseMur"){
                            i=1;
                        }
                        openList.add(next);
                    }
                }
                i=0;
                openList.add(plateau.getCaseByPosition(depart));
                while (openList.size()>0) {
                    positionactuelle = openList.get(openList.size() - 1);
                    openList.remove(positionactuelle);
                    shoot.add(positionactuelle);
                    next=plateau.getCaseDown(positionactuelle.getPosition());
                    if (next != null && next.getPosition().getX()<=depart.getX()+arme.getPortée() && i==0) {
                        if (next.getType()=="CaseMur"){
                            i=1;
                        }
                        openList.add(next);
                    }
                }
                coloration(Color.RED,shoot);
                break;
        }
    }

    // Affiche en bleu les cases où le joueur peut se déplacer

    public void pathfinding(){
        openList=new ArrayList<Case>();
        ListPortee=new ArrayList<Case>();
        Case positionactuelle;
        int i;
        Case Right;
        Case Left;
        Case Up;
        Case Down;
        openList.add(plateau.getCaseByPosition(plateau.turnPlayer.getPosition()));
        while (openList.size()>0) {
            positionactuelle=openList.get(openList.size()-1);
            openList.remove(positionactuelle);
            ListPortee.add(positionactuelle);
            i=positionactuelle.getCout();
            Right=plateau.getCaseRight(positionactuelle.getPosition());
            Left=plateau.getCaseLeft(positionactuelle.getPosition());
            Up=plateau.getCaseUp(positionactuelle.getPosition());
            Down=plateau.getCaseDown(positionactuelle.getPosition());
            AnalyseCase(Right,i);
            AnalyseCase(Left,i);
            AnalyseCase(Up,i);
            AnalyseCase(Down,i);
        }
        coloration(Color.BLUE,ListPortee);
    }

    public void coloration(Color color, ArrayList<Case> closedList){
        for (Case temp:closedList) {
            InnerShadow borderGlow = new InnerShadow();
            Button bouton =temp.getBouton();
            borderGlow.setOffsetX(0f);
            borderGlow.setOffsetY(0f);
            borderGlow.setColor(color);
            bouton.setEffect(borderGlow);//Apply the borderGlow effect to the JavaFX node
            temp.setBouton(bouton);
        }
    }

    //Code pour le pathfinding
    public void AnalyseCase(Case nextCase, int i){
        if (nextCase!=null && !openList.contains(nextCase) && !ListPortee.contains(nextCase) &&  nextCase.getType() != "CaseMur") {
            if(nextCase.getCout()>0){
                nextCase.setCout(0);
            }
            nextCase.setCout(i+1);
            if(nextCase.getCout()<=plateau.turnPlayer.getPtMouvement()) {
                openList.add(nextCase);
            }else nextCase.setCout(0);
        }else if (openList.contains(nextCase) && (i+1)<nextCase.getCout()){
            nextCase.setCout(i+1);
        }else if (ListPortee.contains(nextCase) && (i+1)<nextCase.getCout()){
            ListPortee.remove(nextCase);
            nextCase.setCout(i+1);
            openList.add(nextCase);
        }
    }

    //Clic sur une case
    public void AnalysePosition(Position position) {
        Case analyse=plateau.getCaseByPosition(position);
        Arme armeSelectionnee = listArmes.getSelectionModel().getSelectedItem();
        //A checker
        switch (mode) {
            case 1:
                switch (analyse.getType()) {
                    case "CaseJoueur":
                        Joueur j = plateau.getJoueurByPosition(position);
                        if (j != plateau.turnPlayer) {
                            affichageDuJoueur2(j);
                        }
                        break;
                    case "CaseNormale":
                        if (ListPortee.contains(analyse)) {
                            deplacerPionJoueur(position);
                        }
                        break;
                    case "CaseArme":
                        if (ListPortee.contains(analyse)) {
                            deplacerPionJoueur(position);
                            //ouvrirCoffre();
                        }
                        break;
                    case "CasePopo":
                        if (ListPortee.contains(analyse)) {
                            deplacerPionJoueur(position);
                           // prendrePotion();
                        }
                        break;
                    case "CaseArmure":
                        if (ListPortee.contains(analyse)) {
                            deplacerPionJoueur(position);
                            // prendreArmure();
                        }
                        break;
                }
                break;
            case 2:
                switch (analyse.getType()) {
                    case "CaseJoueur":
                        Joueur j = plateau.getJoueurByPosition(position);
                        if(shoot.contains(analyse)){
                            if (j != plateau.turnPlayer) {
                          //      Tirer(j);
                                attaque(analyse, armeSelectionnee);
                            }
                        }break;
                    case "CaseMur":
                        if(shoot.contains(analyse)){
                         //   TirerMur(analyse);
                            attaque(analyse, armeSelectionnee);
                        }
                        break;
                    case "CaseNormale":
                        if(shoot.contains(analyse)) {

                            attaque(analyse, armeSelectionnee);
                        }
                        break;
                    case "CaseArme":
                        if(shoot.contains(analyse)) {
                            attaque(analyse, armeSelectionnee);
                        }
                        break;
                    case "CasePopo":
                        if(shoot.contains(analyse)) {
                            attaque(analyse, armeSelectionnee);
                        }
                        break;
                    case "CaseArmure":
                        if(shoot.contains(analyse)) {
                            attaque(analyse, armeSelectionnee);
                        }
                        break;
                }
                break;
        }
        switch (analyse.getType()) {
            case "CaseJoueur":
                Joueur j = plateau.getJoueurByPosition(position);
                if (j != plateau.turnPlayer) {
                    affichageDuJoueur2(j);
                }
                break;
            case "CaseNormale":
                afficherCaseNormale(analyse);
                break;
            case "CaseArme":
                afficherCoffre(analyse);
                break;
            case "CasePopo":
                afficherPopo(analyse);
                break;
            case "CaseArmure":
                afficherArmure(analyse);
                break;
        }
    }

    private void attaque(Case analyse, Arme armeSelectionnee) {
        if(analyse.getType().equals("CaseJoueur") || analyse.getType().equals("CaseMur")) {
            if(armeSelectionnee.getName().equals("bazooka") || armeSelectionnee.getName().equals("grenade")) {
                ArrayList<Integer> listeDesLancers = obtenirPointsDommage(armeSelectionnee.getDmg_dés());
                int dommageTotal = 0;
                for(Integer lancer : listeDesLancers) {
                    dommageTotal += lancer;
                }
                plateau.appliquerDegatsExplosion(analyse.getPosition(), armeSelectionnee, dommageTotal, listeDesLancers);
                ArrayList<Case> listDesCasesDansLeRayon = plateau.casesDansLeRayon(analyse.getPosition(), armeSelectionnee.getRayon(), new ArrayList<>());
                animationExplosion(listDesCasesDansLeRayon, armeSelectionnee);
            }
            else {
                ArrayList<Integer> listeDesLancers = obtenirPointsDommage(armeSelectionnee.getDmg_dés());
                int dommageTotal = 0;
                for(Integer lancer : listeDesLancers) {
                    dommageTotal += lancer;
                }
                plateau.appliquerDegatsClassiques(analyse, armeSelectionnee, dommageTotal, listeDesLancers);
            }
        }
        else {
            if(armeSelectionnee.getName().equals("bazooka") || armeSelectionnee.getName().equals("grenade")) {
                ArrayList<Integer> listeDesLancers = obtenirPointsDommage(armeSelectionnee.getDmg_dés());
                int dommageTotal = 0;
                for(Integer lancer : listeDesLancers) {
                    dommageTotal += lancer;
                }
                plateau.appliquerDegatsExplosion(analyse.getPosition(), armeSelectionnee, dommageTotal, listeDesLancers);
                ArrayList<Case> listDesCasesDansLeRayon = plateau.casesDansLeRayon(analyse.getPosition(), armeSelectionnee.getRayon(), new ArrayList<>());
                animationExplosion(listDesCasesDansLeRayon, armeSelectionnee);
            }
        }
    }

    private void affichageDuJoueur(Joueur joueur) {
        nomJoueur.setText(joueur.getName());
        imageJoueur.setImage(joueur.getImageJoueur());
    }
    private void affichageDuJoueur2(Joueur joueur) {
        nomJoueur2.setText(joueur.getName());
        imageJoueur2.setImage(joueur.getImageJoueur());
    }

    private void obtenirPointsDeMouvement() {
        int lancer1 = plateau.lancerUnDe();
        int lancer2 = plateau.lancerUnDe();
        ArrayList<Integer> listeDesLancers = new ArrayList<>();
        listeDesLancers.add(lancer1);
        listeDesLancers.add(lancer2);

        mettreAjourLeLancer(listeDesLancers);
        plateau.turnPlayer.setPtMouvement(lancer1+lancer2);
    }

    private void obtenirPointsAttaque() {
        ArrayList<Integer> listeDesLancers = new ArrayList<>();
        int lancer = plateau.lancerUnDe();
        listeDesLancers.add(lancer);
        mettreAjourLeLancer(listeDesLancers);
        plateau.turnPlayer.setPtAttaque(plateau.turnPlayer.getPtAttaque() + lancer);
    }

    private ArrayList<Integer> obtenirPointsDommage(int nombreDeLancers) {
        ArrayList<Integer> listeDesLancers = new ArrayList<>();
        for(int i = 0; i < nombreDeLancers; i++) {
            listeDesLancers.add(plateau.lancerUnDe());
        }
        mettreAjourLeLancer(listeDesLancers);
        return listeDesLancers;
    }

    private void mettreAjourLeLancer(ArrayList<Integer> listeDesLancers) {
        hBoxLancers1.getChildren().clear();
        hBoxLancers2.getChildren().clear();
        if(listeDesLancers.size() <= 2) {
            for(int lancer : listeDesLancers) {
                hBoxLancers1.getChildren().add(new ImageView(new Image("images/ResultatLancer" + lancer + ".png", 72, 72, true, true)));
            }
        }
        else {
            for(int i = 0; i < 2; i++) {
                hBoxLancers1.getChildren().add(new ImageView(new Image("images/ResultatLancer" + listeDesLancers.get(i) + ".png", 72, 72, true, true)));
            }
            for(int i = 2; i < listeDesLancers.size(); i++) {
                hBoxLancers2.getChildren().add(new ImageView(new Image("images/ResultatLancer" + listeDesLancers.get(i) + ".png", 72, 72, true, true)));
            }
        }

    }

    private void mettreAjourSanteArmure() {
        vBoxSanteArmure.getChildren().clear();
        vBoxSanteArmure.getChildren().add(new ColoredProgressBar("ArmureProgressBar", (double)plateau.turnPlayer.getPtArmure() /100));
        vBoxSanteArmure.getChildren().add(new ColoredProgressBar("SanteProgressBar", (double)plateau.turnPlayer.getPtSante() /100));
    }

    private void mettreAjourInfoRessources() {
        hBoxPA.getChildren().clear();
        hBoxPM.getChildren().clear();
        hBoxBrique.getChildren().clear();
        Font font = new Font("Autumn Regular", 14);

        //Pour les PA
        Rectangle paIcon = new Rectangle();
        paIcon.setWidth(20);
        paIcon.setHeight(20);
        paIcon.setFill(new ImagePattern(new Image("images/PAicon-min.png")));
        hBoxPA.getChildren().add(paIcon);
        Text nombreDePA = new Text(String.valueOf(plateau.turnPlayer.getPtAttaque()));
        nombreDePA.setFont(font);
        hBoxPA.getChildren().add(nombreDePA);

        //Pour les PM
        Rectangle pmIcon = new Rectangle();
        pmIcon.setWidth(20);
        pmIcon.setHeight(20);
        pmIcon.setFill(new ImagePattern(new Image("images/PMicon-min.png")));
        hBoxPM.getChildren().add(pmIcon);
        Text nombreDePM = new Text(String.valueOf(plateau.turnPlayer.getPtMouvement()));
        nombreDePM.setFont(font);
        hBoxPM.getChildren().add(nombreDePM);

        //Pour les briques
        Rectangle briqueIcon = new Rectangle();
        briqueIcon.setWidth(20);
        briqueIcon.setHeight(20);
        briqueIcon.setFill(new ImagePattern(new Image("images/BRIQUEicon-min.png")));
        hBoxBrique.getChildren().add(briqueIcon);
        Text nombreDeBrique = new Text(String.valueOf(plateau.turnPlayer.getPtConstruction()));
        nombreDeBrique.setFont(font);
        hBoxBrique.getChildren().add(nombreDeBrique);

    }

    private void initialiserRessources() {
        Rectangle paIcon = new Rectangle();
        paIcon.setWidth(20);
        paIcon.setHeight(20);
        paIcon.setFill(new ImagePattern(new Image("images/PAicon-min.png")));
        hBoxPA.getChildren().add(paIcon);

        Rectangle pmIcon = new Rectangle();
        pmIcon.setWidth(20);
        pmIcon.setHeight(20);
        pmIcon.setFill(new ImagePattern(new Image("images/PMicon-min.png")));
        hBoxPM.getChildren().add(pmIcon);

        Rectangle briqueIcon = new Rectangle();
        briqueIcon.setWidth(20);
        briqueIcon.setHeight(20);
        briqueIcon.setFill(new ImagePattern(new Image("images/BRIQUEicon-min.png")));
        hBoxBrique.getChildren().add(briqueIcon);
    }

    class ColoredProgressBar extends ProgressBar {
        ColoredProgressBar(String styleClass, double progress) {
            super(progress);
            getStyleClass().add(styleClass);
        }
    }

    private void deplacerPionJoueur(Position destination) {
        Position positionInitiale = plateau.turnPlayer.getPosition();
        //PathTransition pathTransition = new PathTransition();

        //Transition déplacement pion du joueur

        //remplace par case normale
        Case caseOrigine = plateau.getCaseByPosition(plateau.turnPlayer.getPosition());
        Case caseOrigineNouvelle = new CaseNormale(plateau.turnPlayer.getPosition());
        plateau.remplacerCase(caseOrigine, caseOrigineNouvelle);

        //remplace par case joueur
        Case caseDestination = plateau.getCaseByPosition(destination);
        plateau.turnPlayer.setPosition(destination);
        Case caseDestinationNouvelle = new CaseJoueur(plateau.turnPlayer, plateau.turnPlayer.getImageJoueur());
        plateau.remplacerCase(caseDestination, caseDestinationNouvelle);

        //int distanceParcourue = destination.distance(positionInitiale, destination);
        plateau.turnPlayer.setPtMouvement(plateau.turnPlayer.getPtMouvement() - caseDestination.getCout());
        definitionCaseDuPlateau(plateau.turnPlayer.getPosition());
        clean_pathfinding(ListPortee);
        pathfinding();
    }

    private void update() {
        affichageDuJoueur(plateau.turnPlayer);
        mettreAjourSanteArmure();
        mettreAjourInfoRessources();
        boutonLancerPM.setVisible(true);
    }

    public void ouvrir() {

    }

    public void lancerPM() {
        obtenirPointsDeMouvement();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> supprimerLesDes()));
        timeline.play();
        boutonLancerPM.setVisible(false);
        boutonLancerPA.setVisible(true);
    }

    public void lancerPA() {
        obtenirPointsAttaque();
        mettreAjourInfoRessources();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> supprimerLesDes()));
        timeline.play();
        boutonLancerPA.setVisible(false);
    }

    private void supprimerLesDes() {
        hBoxLancers1.getChildren().clear();
        hBoxLancers2.getChildren().clear();
    }

    //FIN DU TOUR ET CHANGEMENT DE JOUEUR ACTIF
    public void finDuTour() {
        plateau.turnPlayer.setPtMouvement(0);
        plateau.joueurSuivant(myController.getData("nbjoueurs").substring(0,1));
        //gridPlateau.getChildren().get(plateau.getIndexOfCase(plateau.getCaseByPosition(plateau.turnPlayer.getPosition()))).requestFocus();
        clean_pathfinding(ListPortee);
        clean_pathfinding(shoot);
        update();
    }

    private boolean isCaseEstDansLeRayon(Position position, ArrayList<Case> listeDesCasesQuiExplosent) {
        for(Case caseQuiExplose : listeDesCasesQuiExplosent) {
            if(caseQuiExplose.getPosition().getX() == position.getX() && caseQuiExplose.getPosition().getY() == position.getY())
                return true;
        }
        return false;
    }

    private void afficherExplosion(ArrayList<Case> listeDeCases){
        for (Case temp:listeDeCases) {
            Button bouton = temp.getBouton();
            setImagePourLesBoutons(bouton, imageExplosion);
            temp.setBouton(bouton);
        }
    }

    private void decolorationApresExplosion(ArrayList<Case> listeDeCases, Arme armeUtilisee) {
        for (Case temp : listeDeCases) {
            Button bouton = temp.getBouton();
            setImagePourLesBoutons(temp.getBouton(), temp.getImg());
            temp.setBouton(bouton);
        }
        shoot_pathfinding(armeUtilisee);
    }

    private void animationExplosion(ArrayList<Case> listeDesCasesQuiExplosent, Arme armeUtilisee) {
        afficherExplosion(listeDesCasesQuiExplosent);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> decolorationApresExplosion(listeDesCasesQuiExplosent, armeUtilisee)));
        timeline.play();
    }

    //Boutton permettant de retourner au menu
    public void backToMenu() {
        myController.loadScreen(Main.Menu_ID, Main.Menu_FILE);
        myController.setScreen(Main.Menu_ID);
    }

    //Boutton permettant de redémarrer la partie
    public void backToJeu() {
        myController.loadScreen(Main.Jeu_ID, Main.Jeu_FILE);
        myController.setScreen(Main.Jeu_ID);
    }
}
