package com.hervelin.controller;

import com.hervelin.model.*;
import com.sun.javafx.tk.TKSceneListener;
import javafx.animation.PathTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;

public class ControllerJeu implements ControlledScreen {
    ScreensController myController;
    public Joueur turnPlayer;
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;
    private boolean isCaseDejaSelectionnee = false;
    private Case caseDejaSelectionnee;

     //zoom factor

    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(2000);
    private final DoubleProperty mouseXProperty = new SimpleDoubleProperty();
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
    @FXML
    public ListView<Arme> listArmes;
    @FXML
    public Text nomJoueur;
    @FXML
    public ImageView imageJoueur;
    @FXML
    public Pane pane = new Pane();

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
        turnPlayer = listeDesJoueurs.get(0);
        turnPlayer.ajouterArme(new Bazooka());


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
        listArmes.getItems().setAll(turnPlayer.getArmes());
        listArmes.setCellFactory(new ArmeCellFactory());
        listArmes.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Arme>() {
            @Override
            public void changed(ObservableValue<? extends Arme> observable, Arme oldValue, Arme newValue) {
                AlertBox.afficherDetailArme(newValue);
            }
        });
        affichageDuJoueur(turnPlayer);
        obtenirPointsDeMouvement();
        definitionCaseDuPlateau();

    }

    //Définition des cases du plateau
    public void definitionCaseDuPlateau() {
        gridPlateau.getChildren().removeAll();
        for (int row = 1; row <= plateau.getxTaille(); row++) {
            for (int col = 1; col <= plateau.getyTaille(); col++) {
                Button bouton = new Button();
                bouton.setStyle("-fx-padding:2 2 2 2;");
                Position positionActuelle = new Position(row,col);
                setImagePourLesBoutons(bouton, plateau.getCaseByPosition(positionActuelle).getImg());
                bouton.setPrefWidth(plateau.getxTaille()/1.5);
                bouton.setPrefHeight(plateau.getyTaille()/1.5);
                bouton.setOnAction(event -> AnalysePosition(positionActuelle));
                if (isCaseEstAPortee(positionActuelle)) {
                    InnerShadow borderGlow = new InnerShadow();
                    borderGlow.setOffsetX(0f);
                    borderGlow.setOffsetY(0f);
                    borderGlow.setColor(Color.BLUE);
                    bouton.setEffect(borderGlow); //Apply the borderGlow effect to the JavaFX node
                }
                else {
                    bouton.setEffect(null);
                }
                GridPane.setColumnIndex(bouton, col);
                GridPane.setRowIndex(bouton, row);
                gridPlateau.add(bouton, col, row);
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

    //Clicl sur une case
    public void AnalysePosition(Position position) {
        if(isCaseJoueur(position)) {
            Joueur j=plateau.getJoueurByPosition(position);
            if (j!=turnPlayer){
                affichageDuJoueur(j);
                listArmes.setVisible(false);
            }else {
                affichageDuJoueur(j);
                listArmes.setVisible(true);
            }
            }else if(isCaseNormale(position)) {
            if(isCaseEstAPortee(position)) {
                deplacerPionJoueur(position);
            }else {
                nomJoueur.setText(plateau.getCaseByPosition(position).getType());
                listArmes.setVisible(false);
                imageJoueur.setImage(plateau.getCaseByPosition(position).getImg());
            }
        }else {
            nomJoueur.setText(plateau.getCaseByPosition(position).getType());
            listArmes.setVisible(false);
            imageJoueur.setImage(plateau.getCaseByPosition(position).getImg());
        }
    }

    public boolean isCaseEstAPortee(Position position) {
        double distance = plateau.calculDeDistance(turnPlayer.getPosition(), position);
        if(distance <= turnPlayer.getPtMouvement())
            return true;
        return false;
    }


    public boolean isCaseJoueur(Position position) {
        if(plateau.getCaseByPosition(position).getType().equals("CaseJoueur"))
            return true;
        return false;
    }


    public boolean isCaseNormale(Position position) {
        if(plateau.getCaseByPosition(position).getType().equals("CaseNormale"))
            return true;
        return false;
    }


    private void affichageDuJoueur(Joueur joueur) {
        nomJoueur.setText(joueur.getName());
        imageJoueur.setImage(joueur.getImageJoueur());
        listArmes.setVisible(true);


    }

    private void obtenirPointsDeMouvement() {
        String messageDeLancer = "Résultats obtenus : ";
        int lancer1 = plateau.lancerUnDe();
        int lancer2 = plateau.lancerUnDe();
        ArrayList<Integer> listeDesLancers = new ArrayList<>();
        listeDesLancers.add(lancer1);
        listeDesLancers.add(lancer2);

        AlertBox.afficherLancer(listeDesLancers, messageDeLancer);
        turnPlayer.setPtMouvement(lancer1+lancer2);
    }

    private void deplacerPionJoueur(Position destination) {
        Position positionInitiale = turnPlayer.getPosition();
        //PathTransition pathTransition = new PathTransition();

        //Transition déplacement pion du joueur

        //remplace par case normale
        Case caseOrigine = plateau.getCaseByPosition(turnPlayer.getPosition());
        Case caseOrigineNouvelle = new CaseNormale(turnPlayer.getPosition());
        plateau.remplacerCase(caseOrigine, caseOrigineNouvelle);

        //remplace par case joueur
        Case caseDestination = plateau.getCaseByPosition(destination);
        turnPlayer.setPosition(destination);
        Case caseDestinationNouvelle = new CaseJoueur(turnPlayer, turnPlayer.getImageJoueur());
        plateau.remplacerCase(caseDestination, caseDestinationNouvelle);

        int distanceParcourue = destination.distance(positionInitiale, destination);
        turnPlayer.setPtMouvement(turnPlayer.getPtMouvement() - distanceParcourue);

        definitionCaseDuPlateau();
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
