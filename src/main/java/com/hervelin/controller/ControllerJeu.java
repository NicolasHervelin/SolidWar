package com.hervelin.controller;

import com.hervelin.model.Joueur;
import com.hervelin.model.Plateau;
import com.hervelin.model.Position;
import com.sun.javafx.tk.TKSceneListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swt.FXCanvas;
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
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerJeu implements ControlledScreen {
    ScreensController myController;

    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;

    /**
     * The zoom factor.
     */
    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(1000);


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

        //scrollPlateau.setFitToWidth(true);
        //scrollPlateau.setFitToHeight(true);
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

        Image img;
        for (int row = 1; row <= nombreCaseX; row++) {
            for (int col = 1; col <= nombreCaseY; col ++) {
                StackPane square = new StackPane();
                Button bouton = new Button();
                Rectangle rectangle = new Rectangle();
                Position positionActuelle = new Position(row,col);
                Color color =  plateau.getCaseByPosition(positionActuelle).getCouleur();
                if(plateau.getCaseByPosition(positionActuelle).getType().equals("CaseNormale"))
                    rectangle.setFill(color);
                if(plateau.getCaseByPosition(positionActuelle).getType().equals("CaseArme")) {
                    img = new Image("images/TextureCoffre.png");
                    rectangle.setFill(new ImagePattern(img));
                }
                if(plateau.getCaseByPosition(positionActuelle).getType().equals("CaseMur")) {
                    img = new Image("images/TextureMur.png");
                    rectangle.setFill(new ImagePattern(img));
                }
                if(plateau.getCaseByPosition(positionActuelle).getType().equals("CasePopo")){
                    img = new Image("images/TexturePopoVerte.png");
                    rectangle.setFill(new ImagePattern(img));
                }
                if(plateau.getCaseByPosition(positionActuelle).getType().equals("CaseArmure")){
                    img = new Image("images/TexturePopoBleue.png");
                    rectangle.setFill(new ImagePattern(img));
                }

                gridPlateau.add(rectangle, col, row);

                rectangle.widthProperty().bind(gridPlateau.widthProperty().divide(nombreCaseX/1.5));
                rectangle.heightProperty().bind(gridPlateau.heightProperty().divide(nombreCaseY/1.5));
            }
        }
        /*EventHandler handler = new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println("Handling event " + event.getEventType());
                gridPlateau.setScaleX(gridPlateau.getScaleX()*event.getZoomFactor());
                gridPlateau.setScaleY(gridPlateau.getScaleY()*event.getZoomFactor());
                event.consume();
            }
        };*/
        //gridPlateau.addEventHandler(ZoomEvent.ZOOM_STARTED, handler);

        gridPlateau.setOnMouseMoved(event -> {
            mouseXProperty.set(event.getX());
            mouseYProperty.set(event.getY());
        });


        gridPlateau.setOnZoom(event -> {
            zoomProperty.set(zoomProperty.get() * event.getZoomFactor());

            gridPlateau.setScaleX(gridPlateau.getScaleX()*event.getZoomFactor());
            gridPlateau.setScaleY(gridPlateau.getScaleY()*event.getZoomFactor());
            //System.out.println(event.getZoomFactor());
            event.consume();
        });

    }



}
