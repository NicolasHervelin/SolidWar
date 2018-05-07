package com.hervelin.controller;

import com.hervelin.model.Joueur;
import com.hervelin.model.Plateau;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerJeu implements Initializable, ControlledScreen {
    ScreensController myController;

    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;

    public Plateau plateau;
    public int nombreCaseX = 50;
    public int nombreCaseY = 50;

    /*@FXML
    public GridPane gridPlateau;*/


    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        //initialize();
    }
    /*
    public void initialize() {
        String nomJoueur1 = "Joueur 1";
        String nomJoueur2 = "Joueur 2";
        String nomJoueur3 = "Joueur 3";
        String nomJoueur4 = "Joueur 4";
        String nombreDeJoueurs = "2";

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

        switch (nombreDeJoueurs) {
            case "2" :
                plateau = new Plateau(nombreCaseX, nombreCaseY, nomJoueur1, nomJoueur2);
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

        for (int row = 0; row < nombreCaseX; row++) {
            for (int col = 0; col < nombreCaseY; col ++) {
                StackPane square = new StackPane();
                String color ;
                if ((row + col) % 2 == 0) {
                    color = "white";
                } else {
                    color = "gray";
                }
                square.setStyle("-fx-background-color: "+color+";");
                gridPlateau.add(square, col, row);
            }
        }
        for (int i = 0; i < nombreCaseX; i++) {
            gridPlateau.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
            gridPlateau.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }

    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
