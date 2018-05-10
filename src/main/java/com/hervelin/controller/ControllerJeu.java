package com.hervelin.controller;

import com.hervelin.model.*;
import com.sun.javafx.tk.TKSceneListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swt.FXCanvas;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControllerJeu implements ControlledScreen {
    ScreensController myController;

    public Joueur turnPlayer;

    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;
    private boolean isCaseDejaSelectionnee = false;
    private Case caseDejaSelectionnee;
    public String nombreDeJoueurs = "2";


    /**
     * The zoom factor.
     */
    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(2000);


    /**
     * The mouse X position.
     */
    private final DoubleProperty mouseXProperty = new SimpleDoubleProperty();

    /**
     * The mouse Y position.
     */
    private final DoubleProperty mouseYProperty = new SimpleDoubleProperty();


    public Plateau plateau;
    public int nombreCaseX = 40;
    public int nombreCaseY = 40;
    private ArrayList<Joueur> listeDesJoueurs;

    @FXML
    public GridPane gridPlateau;
    @FXML
    public ScrollPane scrollPlateau;
    @FXML
    public AnchorPane anchorMain;
    @FXML
    public Button finTourJoueur1, finTourJoueur2, finTourJoueur3, finTourJoueur4;


    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        setUp();
    }

    private void setUp() {
        String nomJoueur1 = "Joueur 1";
        String nomJoueur2 = "Joueur 2";
        String nomJoueur3 = "Joueur 3";
        String nomJoueur4 = "Joueur 4";

        double scaleX = gridPlateau.getScaleX(); // I only store this to be able to revert the changes
        double scaleY = gridPlateau.getScaleY();


        /*
        if(!myController.getData("NomDuJoueur1").equals(""))
            nomJoueur1 = myController.getData("NomDuJoueur1");
        if(!myController.getData("NomDuJoueur2").equals(""))
            nomJoueur2 = myController.getData("NomDuJoueur2");
        if(!myController.getData("NomDuJoueur3").equals(""))
            nomJoueur3 = myController.getData("NomDuJoueur3");
        if(!myController.getData("NomDuJoueur4").equals(""))
            nomJoueur4 = myController.getData("NomDuJoueur4");
        if(!myController.getData("NombreDeJoueurs").equals(""))
            nombreDeJoueurs = myController.getData("NombreDeJoueurs");
        */

        //Initialisation du plateau
        switch (nombreDeJoueurs) {
            case "2" :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2);
                joueur1 = plateau.getListeDeJoueurs().get(0);
                joueur2 = plateau.getListeDeJoueurs().get(1);
                //System.out.println(plateau.getCaseByPosition(new Position(1, 1)).getCouleur());
                break;
            case "3" :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2, nomJoueur3);
                joueur1 = plateau.getListeDeJoueurs().get(0);
                joueur2 = plateau.getListeDeJoueurs().get(1);
                joueur3 = plateau.getListeDeJoueurs().get(2);
                break;
            case "4" :
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
        turnPlayer = listeDesJoueurs.get(0);

        //Définition des cases du plateau
        for (int row = 1; row <= nombreCaseX; row++) {
            for (int col = 1; col <= nombreCaseY; col ++) {
                Button bouton = new Button();
                bouton.setStyle("-fx-padding:2 2 2 2;");
                Position positionActuelle = new Position(row,col);
                setImagePourLesBoutons(bouton, plateau.getCaseByPosition(positionActuelle).getImg());
                bouton.setPrefWidth(nombreCaseX/1.5);
                bouton.setPrefHeight(nombreCaseY/1.5);
                bouton.setOnAction(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent event) {
                        AnalysePosition(positionActuelle);
                        //setCaseDejaSelectionnee(positionActuelle);
                    }
                });
                gridPlateau.add(bouton, col, row);
            }
        }

        //Zoom sur le ScrollPane
        gridPlateau.setOnMouseMoved(event -> {
            mouseXProperty.set(event.getX());
            mouseYProperty.set(event.getY());
        });
        gridPlateau.setOnZoom(event -> {
            zoomProperty.set(zoomProperty.get() * event.getZoomFactor());
            gridPlateau.setScaleX(gridPlateau.getScaleX()*event.getZoomFactor());
            gridPlateau.setScaleY(gridPlateau.getScaleY()*event.getZoomFactor());
            event.consume();
        });

        affichageDuJoueur(joueur1);
        turnPlayer.setPtMouvement(7);
        colorerCasesAPortee();

    }

    //Assigne à chaque bouton l'image correspondante
    public void setImagePourLesBoutons(Button bouton, Image img) {
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(nombreCaseX / 1.5);
        imageView.setFitHeight(nombreCaseY / 1.5);
        bouton.setGraphic(imageView);
    }

    public void AnalysePosition(Position position) {
        System.out.println(position.getX());
        System.out.println(position.getY());
        if(isCaseEstAPortee(position)) {
            if(isCaseJoueur(position)) {
                System.out.println("c'est un joueur");
                if(isCaseDuJoueurActuel(position)) {
                    //Si le joueur clique sur son propre perso
                    affichageDuJoueur(turnPlayer);
                    turnPlayer.setPtMouvement(4);
                    colorerCasesAPortee();
                }
                if(isCaseDunAutreJoueur(position)) {
                    affichageDuJoueur(plateau.getJoueurByPosition(position));
                }

            }
        }
        else {
            if(isCaseDejaSelectionnee);

        }
    }

    public boolean isCaseEstAPortee(Position position) {
        Position positionJoueur = turnPlayer.getPosition();
        double ptMouvement = turnPlayer.getPtMouvement();
        double distance = plateau.calculDeDistance(positionJoueur, position);
        if(distance <= ptMouvement)
            return true;
        return false;
    }

    public boolean isCaseDuJoueurActuel(Position position) {
        if(position.getX() == turnPlayer.getPosition().getX() && position.getY() == turnPlayer.getPosition().getY())
            return true;
        return false;
    }

    public boolean isCaseDunAutreJoueur(Position position) {
        if(position.getX() != turnPlayer.getPosition().getX() || position.getY() != turnPlayer.getPosition().getY())
            return true;
        return false;
    }

    public Case getCaseDejaSelectionnee(){
        return caseDejaSelectionnee;
    }

    public void setCaseDejaSelectionnee(Position position){
        caseDejaSelectionnee = plateau.getCaseByPosition(position);
    }

    public boolean isCaseDejaSelectionnee() {
        if(caseDejaSelectionnee != null)
            return true;
        else
            return false;
    }

    public boolean isCaseJoueur(Position position) {
        if(plateau.getCaseByPosition(position).getType().equals("CaseJoueur"))
            return true;
        return false;
    }

    private void changerDeJoueur(Joueur actuel) {
        nonAffichageDuJoueur(actuel);
        turnPlayer = plateau.joueurSuivant(actuel, nombreDeJoueurs);
        affichageDuJoueur(turnPlayer);
        //update();
    }

    //Refresh les valeurs
    private void update() {
        colorerCasesAPortee();

    }

    public void colorerCasesAPortee() {
        int ptMouvement = turnPlayer.getPtMouvement();
        System.out.println("colorerCases");
        if(ptMouvement != 0) {
            for (int row = 1; row <= nombreCaseX; row++) {
                for (int col = 1; col <= nombreCaseY; col++) {
                    Position tempPosition = new Position(row,col);
                    int calculIndex = ((row-1) * nombreCaseX + col)-1;
                    Node node = gridPlateau.getChildren().get(calculIndex);
                    if (isCaseEstAPortee(tempPosition)) {
                        System.out.println(tempPosition);
                        DropShadow borderGlow= new DropShadow();
                        borderGlow.setOffsetY(0);
                        borderGlow.setOffsetX(0);
                        borderGlow.setColor(Color.BLUE);
                        borderGlow.setWidth((nombreCaseX/1.5));
                        borderGlow.setHeight((nombreCaseY/1.5));

                        node.setEffect(borderGlow); //Apply the borderGlow effect to the JavaFX node
                        //System.out.println(calculIndex);
                    }
                    else {
                        DropShadow borderGlow= new DropShadow();
                        borderGlow.setOffsetY(0);
                        borderGlow.setOffsetX(0);
                        borderGlow.setColor(Color.TRANSPARENT);
                        node.setEffect(borderGlow);
                    }
                }
            }
        }
    }

    private void affichageDuJoueur(Joueur joueur) {
        Button boutonFinDuTour = new Button("Fin du tour");
        boutonFinDuTour.setLayoutX(806);
        boutonFinDuTour.setLayoutY(700);
        boutonFinDuTour.setOnAction(event -> changerDeJoueur(joueur));
        anchorMain.getChildren().add(boutonFinDuTour);
    }

    private void nonAffichageDuJoueur(Joueur joueur) {
        //System.out.println(anchorMain.getChildren().get(0)); //MenuBar
        //System.out.println(anchorMain.getChildren().get(1)); //ScrollPane
        System.out.println(anchorMain.getChildren().get(2)); //Button
        anchorMain.getChildren().remove(2);
    }

    private void affichageDesInfosDeLaCase() {

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
