package com.hervelin.controller;

import com.hervelin.model.Joueur;
import com.hervelin.model.Plateau;
import com.hervelin.model.Position;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerJeu implements ControlledScreen {
    ScreensController myController;

    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;

    public Plateau plateau;
    public int nombreCaseX = 50;
    public int nombreCaseY = 50;

    @FXML
    public GridPane gridPlateau;


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

        for (int row = 1; row <= nombreCaseX; row++) {
            for (int col = 1; col <= nombreCaseY; col ++) {
                StackPane square = new StackPane();
                Button bouton = new Button();
                Rectangle rectangle = new Rectangle();
                Color color =  plateau.getCaseByPosition(new Position(row, col)).getCouleur();
                rectangle.setFill(color);
                gridPlateau.add(rectangle, col, row);
                rectangle.widthProperty().bind(gridPlateau.widthProperty().divide(nombreCaseX));
                rectangle.heightProperty().bind(gridPlateau.heightProperty().divide(nombreCaseY));
            }
        }

    }
}
