package com.hervelin.controller;

import com.hervelin.model.*;
import com.sun.javafx.tk.TKSceneListener;
import javafx.animation.PathTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class ControllerJeu implements ControlledScreen {
    ScreensController myController;
    public Joueur turnPlayer;
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;
    private boolean isCaseDejaSelectionnee = false;
    private Case caseDejaSelectionnee;
    private int mode;

     //zoom factor

    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(2000);
    private final DoubleProperty mouseXProperty = new SimpleDoubleProperty();
    private final DoubleProperty mouseYProperty = new SimpleDoubleProperty();


    public Plateau plateau;
    public int nombreCaseX = 40;
    public int nombreCaseY = 40;
    private ArrayList<Joueur> listeDesJoueurs;
    private ArrayList<Case> openList;
    private ArrayList<Case> ListPortee = new ArrayList<Case>();
    private ArrayList<Case> shoot = new ArrayList<Case>();




    @FXML
    public GridPane gridPlateau;
    @FXML
    public ScrollPane scrollPlateau;
    @FXML
    public GridPane anchorMain;
    @FXML
    public Button fight,move;
    @FXML
    public ListView<Arme> listArmes;
    @FXML
    public Text nomJoueur,nomJoueur2;
    @FXML
    public ImageView imageJoueur, imageJoueur2;
    @FXML
    public Pane pane = new Pane();
    @FXML
    public VBox vBoxLancers;
    @FXML
    public HBox hBoxLancers;
    @FXML
    public VBox vBoxSanteArmure;

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
                clean_pathfinding(shoot);
                clean_pathfinding(ListPortee);
                shoot_pathfinding(newValue);
            }
        });
        affichageDuJoueur(turnPlayer);
        obtenirPointsDeMouvement();
        mettreAjourSanteArmure();
        definitionCaseDuPlateau(null);

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
        shoot_pathfinding(turnPlayer.getArmes().get(0));
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
        for (Case temp:l) {
            InnerShadow borderGlow = new InnerShadow();
            Button bouton =temp.getBouton();
            borderGlow.setOffsetX(0f);
            borderGlow.setOffsetY(0f);
            borderGlow.setColor(null);
            bouton.setEffect(null); //Apply the borderGlow effect to the JavaFX node
            temp.setBouton(bouton);
        }
        l.clear();
    }

    public void shoot_pathfinding(Arme arme){
        switch(arme.getTypeTir()){
            case "droit":
                int row;
                Position depart=turnPlayer.getPosition();
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
        openList.add(plateau.getCaseByPosition(turnPlayer.getPosition()));
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
            bouton.setEffect(borderGlow); //Apply the borderGlow effect to the JavaFX node
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
            if(nextCase.getCout()<=turnPlayer.getPtMouvement()) {
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
        switch (mode) {
            case 1:
                nomJoueur2.setText(plateau.getCaseByPosition(position).getType());
                imageJoueur2.setImage(plateau.getCaseByPosition(position).getImg());
                switch (plateau.getCaseByPosition(position).getType()) {
                    case "CaseJoueur":
                        Joueur j = plateau.getJoueurByPosition(position);
                        if (j != turnPlayer) {
                            affichageDuJoueur2(j);
                        }
                        break;
                    case "CaseNormale":
                        if (ListPortee.contains(plateau.getCaseByPosition(position))) {
                            deplacerPionJoueur(position);
                        }
                        break;
                    case "CaseArme":
                        if (ListPortee.contains(plateau.getCaseByPosition(position))) {
                            deplacerPionJoueur(position);
                            //ouvrirCoffre();
                        }
                        break;
                    case "CasePopo":
                        if (ListPortee.contains(plateau.getCaseByPosition(position))) {
                            deplacerPionJoueur(position);
                           // prendrePotion();
                        }
                        break;
                }
                break;
            case 2:
                nomJoueur2.setText(plateau.getCaseByPosition(position).getType());
                imageJoueur2.setImage(plateau.getCaseByPosition(position).getImg());
                switch (plateau.getCaseByPosition(position).getType()) {
                    case "CaseJoueur":
                        Joueur j = plateau.getJoueurByPosition(position);
                        if(shoot.contains(plateau.getCaseByPosition(position))){
                            if (j != turnPlayer) {
                          //      Tirer(j);
                            }
                        }break;
                    case "CaseMur":
                        if(shoot.contains(plateau.getCaseByPosition(position))){
                         //   TirerMur(position);
                        }break;
                }
                break;
            default:
                nomJoueur2.setText(plateau.getCaseByPosition(position).getType());
                imageJoueur2.setImage(plateau.getCaseByPosition(position).getImg());
                break;
        }
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
        turnPlayer.setPtMouvement(lancer1+lancer2);
    }

    private void mettreAjourLeLancer(ArrayList<Integer> listeDesLancers) {
        /*String messageDeLancer = "Dernier lancer";
        Label l = new Label(messageDeLancer);
        l.setTextFill(Color.GRAY);
        l.setStyle("-fx-font: 22 'Autumn Regular';" +
                "-fx-text-alignment: center;");*/

        vBoxLancers.getChildren().clear();
        //vBoxLancers.getChildren().add(l);
        hBoxLancers.getChildren().clear();
        for(int lancer : listeDesLancers) {
            hBoxLancers.getChildren().add(new ImageView(new Image("images/ResultatLancer" + lancer + ".png", 72, 72, true, true)));
        }
        // fill background with java
        BackgroundFill fill = new BackgroundFill(Color.TRANSPARENT, new CornerRadii(1), new Insets(0, 0, 0, 0));
        vBoxLancers.setBackground(new Background(fill));
        hBoxLancers.setBackground(new Background(fill));
        vBoxLancers.getChildren().add(hBoxLancers);
        vBoxLancers.setBorder(new Border(new BorderStroke(Color.GRAY, null, new CornerRadii(1), new BorderWidths(2))));
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

        //int distanceParcourue = destination.distance(positionInitiale, destination);
        turnPlayer.setPtMouvement(turnPlayer.getPtMouvement() - caseDestination.getCout());
        definitionCaseDuPlateau(turnPlayer.getPosition());
        clean_pathfinding(ListPortee);
        pathfinding();
    }

    private void mettreAjourSanteArmure() {
        vBoxSanteArmure.getChildren().clear();
        vBoxSanteArmure.getChildren().add(new ColoredProgressBar("ArmureProgressBar", turnPlayer.getPtArmure()/100));
        vBoxSanteArmure.getChildren().add(new ColoredProgressBar("SanteProgressBar", turnPlayer.getPtSante()/100));
    }

    class ColoredProgressBar extends ProgressBar {
        ColoredProgressBar(String styleClass, double progress) {
            super(progress);
            getStyleClass().add(styleClass);
        }
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
