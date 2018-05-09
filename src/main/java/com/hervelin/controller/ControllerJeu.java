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

    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;
    private boolean isCaseDejaSelectionnee = false;
    private Case caseDejaSelectionnee;

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
        String nombreDeJoueurs = "2";

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
                //System.out.println(plateau.getCaseByPosition(new Position(1, 1)).getCouleur());
                break;
            case "3" :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2, nomJoueur3);
                break;
            case "4" :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2, nomJoueur3, nomJoueur4);
                break;
            default :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2);
                break;
        }

        listeDesJoueurs = plateau.getListeDeJoueurs();

        /*Image imgCoffre = new Image("images/TextureCoffre.png", nombreCaseX/1.5, nombreCaseY/1.5, true, true);
        Image imgMur = new Image("images/TextureMur.png", nombreCaseX/1.5, nombreCaseY/1.5, true, true);
        Image imgPopo = new Image("images/TexturePopoBleue.png", nombreCaseX/1.5, nombreCaseY/1.5, true, true);
        Image imgArmure = new Image("images/TextureBouclierBleu.png", nombreCaseX/1.5, nombreCaseY/1.5, true, true);
        Image imgWhite = new Image("images/TextureCaseNormale.png", nombreCaseX/1.5, nombreCaseY/1.5, true, true);*/
        //ArrayList<CaseJoueur> listeDeJoueurs=plateau.getListeDeJoueurs();

        //Définition des cases du plateau
        for (int row = 1; row <= nombreCaseX; row++) {
            for (int col = 1; col <= nombreCaseY; col ++) {
                Button bouton = new Button();
                bouton.setStyle("-fx-padding:2 2 2 2;");
                Position positionActuelle = new Position(row,col);
                setImagePourLesBoutons(bouton, plateau.getCaseByPosition(positionActuelle).getImg());
                bouton.setPrefWidth(nombreCaseX/1.5);
                bouton.setPrefHeight(nombreCaseY/1.5);
                bouton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event){
                        AnalysePosition(positionActuelle);
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

        //Gestion clic sur les cases
        /*gridPlateau.setOnMouseClicked(event -> {
            event.consume();
        });*/

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
        if(isCaseJoueur(position)) {
            System.out.println("c'est un joueur");

            /*if(isCaseDejaSelectionnee) {
                //attaquer le joueur
                isCaseDejaSelectionnee = false;
            }
            else {
                caseDejaSelectionnee = new Case()
                isCaseDejaSelectionnee = true;

            }*/
        }
        else {
            if(isCaseDejaSelectionnee);

        }
    }



    public boolean isCaseDuJoueurActuel(Position position) {
        return false;
    }

    public boolean isCaseDunAutreJoueur(Position position) {
        return false;
    }


    public boolean isCaseJoueur(Position position) {
        if(plateau.getCaseByPosition(position).getType().equals("CaseJoueur"))
            return true;
        return false;
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
