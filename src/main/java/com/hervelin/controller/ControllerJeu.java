package com.hervelin.controller;
import com.hervelin.model.*;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;

public class ControllerJeu implements ControlledScreen {
    ScreensController myController;
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;
    private boolean isCaseDejaSelectionnee = false;
    private Case caseDejaSelectionnee;
    private int mode=0;


    public Plateau plateau;
    public int nombreCaseX = 40;
    public int nombreCaseY = 40;
    private ArrayList<Joueur> listeDesJoueurs;
    private ArrayList<Case> openList;
    private ArrayList<Case> ListPortee = new ArrayList<Case>();
    private ArrayList<Case> shoot = new ArrayList<Case>();
    private ArrayList<Case> build = new ArrayList<Case>();
    private ArrayList<Case> casesDansExplosion = new ArrayList<Case>();
    private HBox listeArmeCoffre = new HBox();
    private Image imageExplosion = new Image("images/TextureExplosion40-min.png");
    private Image fixeCoffre = new Image("images/Solid_war/GIF/Coffre14_2-min.png");
    private Pane backOuvrirCoffre = new Pane();
    private Pane paneOuvrirCoffre = new Pane();


    @FXML
    public GridPane gridPlateau, gridContainer;
    @FXML
    public ScrollPane scrollPlateau;
    @FXML
    public GridPane anchorMain;
    @FXML
    public Button buil, move, boutonFinDuTour, boutonOuvrirArme, boutonOuvrirArmure, boutonOuvrirPopo, boutonLancerPM, boutonLancerPA;
    @FXML
    public ListView<Arme> listArmes;
    @FXML
    public Text nomJoueur;
    @FXML
    public Text nomJoueur2;
    @FXML
    public ImageView imageJoueur, imageJoueur2;
    @FXML
    public ProgressBar ptStructure;
    @FXML
    public Pane pane = new Pane();
    @FXML
    public VBox vBoxLancers, vBoxSanteArmure, vBoxSanteArmureJoueurAdverse;
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
        joueur1.ajouterArme(new Bazooka("images/nop.png"));
        joueur1.ajouterArme(new Bazooka("images/nop.png"));
        joueur1.ajouterArme(new Bazooka("images/nop.png"));
        joueur1.ajouterArme(new Bazooka("images/nop.png"));

        joueur2.ajouterArme(new Bazooka("images/nop.png"));
        joueur2.ajouterArme(new Bazooka("images/nop.png"));
        joueur2.ajouterArme(new Bazooka("images/nop.png"));
        joueur2.ajouterArme(new Bazooka("images/nop.png"));

        affichageDuJoueur(plateau.turnPlayer);
        mettreAjourSanteArmure();
        definitionCaseDuPlateau(null);
        initialiserRessources();

        Glow glow = new Glow(0.5);
        Reflection reflection=new Reflection();
        reflection.setInput(glow);

        //Boutons de sélection du mode
        Image img=new Image("images/build.png");
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        buil.setGraphic(imageView);
        buil.setEffect(reflection);
        Image img2=new Image("images/move.png");
        ImageView imageView2 = new ImageView(img2);
        imageView2.setPreserveRatio(true);
        move.setGraphic(imageView2);
        move.setEffect(reflection);

        //Bouton Fin du tour
        Image img3=new Image("images/FinDuTour.png");
        ImageView imageView3 = new ImageView(img3);
        imageView2.setPreserveRatio(true);
        boutonFinDuTour.setGraphic(imageView3);
        boutonFinDuTour.setEffect(reflection);

        //Boutons ouvrir
        Image img4=new Image("images/OuvrirCoffreicon-min.png");
        ImageView imageView4 = new ImageView(img4);
        imageView2.setPreserveRatio(true);
        boutonOuvrirArme.setGraphic(imageView4);
        boutonOuvrirArme.setEffect(reflection);
        Image img5=new Image("images/Ouvriricon-min.png");
        ImageView imageView5 = new ImageView(img5);
        imageView5.setPreserveRatio(true);
        boutonOuvrirPopo.setGraphic(imageView5);
        boutonOuvrirPopo.setEffect(reflection);
        Image img6=new Image("images/Ouvriricon-min.png");
        ImageView imageView6 = new ImageView(img6);
        imageView6.setPreserveRatio(true);
        boutonOuvrirArmure.setGraphic(imageView6);
        boutonOuvrirArmure.setEffect(reflection);


        afficherListeArmes();
        listArmes.getSelectionModel().select(plateau.turnPlayer.indexArmeSelectionnee);
    }


    /*****
     *  GridPlateau  *
     *****/

    //Définition des cases du plateau
    private void definitionCaseDuPlateau(Position positionAConserver) {
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
    private void setImagePourLesBoutons(Button bouton, Image img) {
        ImageView imageView = new ImageView(img);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(plateau.getxTaille() / 1.5);
        imageView.setFitHeight(plateau.getyTaille() / 1.5);
        bouton.setGraphic(imageView);
    }

    //Ajoute une bodure colorée sur une liste de cases
    private void coloration(Color color, ArrayList<Case> closedList){
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

    public void afficherListeArmes(){
        listArmes.getItems().removeAll();
        listArmes.getItems().setAll(plateau.turnPlayer.getArmes());
        listArmes.getSelectionModel().select(plateau.turnPlayer.indexArmeSelectionnee);
        listArmes.setCellFactory(new ArmeCellFactory());
        listArmes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (mode){
                case 1:
                    clean_pathfinding(ListPortee);
                    break;
                case 2:
                    clean_pathfinding(shoot);
                    break;
                case 3:
                    clean_pathfinding(build);
                    break;
                default:break;
            }
            mode=2;
            if(newValue != null) {
                afficherArme(newValue);
                shoot_pathfinding(newValue);
                plateau.turnPlayer.indexArmeSelectionnee=listArmes.getSelectionModel().getSelectedIndex();
                System.out.println(newValue);
                System.out.println(plateau.turnPlayer.indexArmeSelectionnee);

            }
        });
    }

    //Fait apparaître une image d'explosion sur une liste de cases
    private void afficherExplosion(ArrayList<Case> listeDeCases){
        for (Case temp:listeDeCases) {
            Button bouton = temp.getBouton();
            setImagePourLesBoutons(bouton, imageExplosion);
            temp.setBouton(bouton);
        }
    }

    //Fait disparaître l'image d'explosion et applique les dégats d'explosion
    private void decolorationApresExplosion(Position positionExplosion, int degats, ArrayList<Case> listeDeCases, Arme armeUtilisee, ArrayList<Integer> listeDesLancers) {
        for (Case temp : listeDeCases) {
            Button bouton = temp.getBouton();
            setImagePourLesBoutons(temp.getBouton(), temp.getImg());
            temp.setBouton(bouton);
        }
        plateau.appliquerDegatsExplosion(positionExplosion , armeUtilisee, degats, listeDesLancers);
        remplacerLesMursDetruits(listeDeCases);
        shoot_pathfinding(armeUtilisee);
      /*  ArrayList<Case> l=plateau.casesDansExplosion(listArmes.getSelectionModel().getSelectedItem());
        clean_pathfinding(shoot);
        coloration(Color.BLACK,l);*/
    }


    /*****
     *  Fin GridPlateau  *
     *****/



    /*****
     *  Analyse des cases *
     *****/

    //Clic sur une case
    private void AnalysePosition(Position position) {
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
            case 3:
                switch (analyse.getType()) {
                    case "CaseNormale":
                        if(build.contains(analyse) && plateau.turnPlayer.getBrique()>=10){
                            construireMur(analyse);
                        }
                }break;

        }
        switch (analyse.getType()) {
            case "CaseJoueur":
                Joueur j = plateau.getJoueurByPosition(position);
                if (j != plateau.turnPlayer) {
                    affichageDuJoueur2(j);
                }
                break;
            case "CaseMur":
                afficherMur(analyse);
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

    /*****
     *  Fin Analyse des cases *
     *****/



    /*****
     *  PathFinding *
     *****/

    // Affiche en rouge les cases où le joueur peut tirer
    private void clean_pathfinding(ArrayList<Case> l){
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

    private void build_pathfinding(){
        build = plateau.buildPathFindingPlateau();
        coloration(Color.GREEN,build);
    }

    private void shoot_pathfinding(Arme arme){
        shoot = plateau.shootPathFindingPlateau(arme);
        coloration(Color.RED,shoot);
    }

    // Affiche en bleu les cases où le joueur peut se déplacer
    private void pathfinding(){
        ListPortee = plateau.pathFindingPlateau();
        coloration(Color.BLUE,ListPortee);
    }

    /*****
     *  Fin PathFinding *
     *****/


    /*****
     *  Attaques et constructions *
     *****/

    //Gestion des attaques en fonction de l'arme
    private void attaque(Case analyse, Arme armeSelectionnee) {
        if(plateau.isPointsAttaqueSuffisants(armeSelectionnee.getPa())) {
            if(analyse.getType().equals("CaseJoueur") || analyse.getType().equals("CaseMur")) {
                if(armeSelectionnee.getName().equals("bazooka") || armeSelectionnee.getName().equals("grenade")) {
                    ArrayList<Integer> listeDesLancers = obtenirPointsDommage(armeSelectionnee.getDmg_dés());
                    int dommageTotal = 0;
                    for(Integer lancer : listeDesLancers) {
                        dommageTotal += lancer;
                    }
                    ArrayList<Case> listDesCasesDansLeRayon = plateau.casesDansLeRayon(analyse.getPosition(), armeSelectionnee.getRayon(), new ArrayList<>());
                    explosion(analyse.getPosition(), dommageTotal, listDesCasesDansLeRayon, armeSelectionnee, listeDesLancers);
                }
                else {
                    ArrayList<Integer> listeDesLancers = obtenirPointsDommage(armeSelectionnee.getDmg_dés());
                    int dommageTotal = 0;
                    for(Integer lancer : listeDesLancers) {
                        dommageTotal += lancer;
                    }
                    plateau.appliquerDegatsClassiques(analyse, armeSelectionnee, dommageTotal, listeDesLancers);
                    if(analyse.getType().equals("CaseMur")) {
                        CaseMur caseMur = (CaseMur) analyse;
                        if(caseMur.getMur().getPtStructure() <= 0) {
                            detruireMur(caseMur);
                        }
                    }
                }
            }
            else {
                if(armeSelectionnee.getName().equals("bazooka") || armeSelectionnee.getName().equals("grenade")) {
                    ArrayList<Integer> listeDesLancers = obtenirPointsDommage(armeSelectionnee.getDmg_dés());
                    int dommageTotal = 0;
                    for(Integer lancer : listeDesLancers) {
                        dommageTotal += lancer;
                    }
                    ArrayList<Case> listDesCasesDansLeRayon = plateau.casesDansLeRayon(analyse.getPosition(), armeSelectionnee.getRayon(), new ArrayList<>());
                    explosion(analyse.getPosition(), dommageTotal, listDesCasesDansLeRayon, armeSelectionnee, listeDesLancers);
                }
            }
            mettreAjourInfoRessources();
        }
        else {
            System.out.println("Points d'attaque insuffisants !!");
            mettreAjourInfoRessources();
        }
    }

    //Appelle les méthodes permettant l'explosion visuelle et la répartition des dégats
    private void explosion(Position positionExplosion, int degats, ArrayList<Case> listeDesCasesQuiExplosent, Arme armeUtilisee, ArrayList<Integer> listeDesLancers) {
        afficherExplosion(listeDesCasesQuiExplosent);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(800),
                ae -> {
                    decolorationApresExplosion(positionExplosion, degats, listeDesCasesQuiExplosent, armeUtilisee, listeDesLancers);
                    mettreAjourInfoRessources();
                }));
        timeline.play();
    }

    //Remplace les murs détruits par une case normale
    private void remplacerLesMursDetruits(ArrayList<Case> listeDeCases) {
        for(Case c : listeDeCases){
            if(c.getType().equals("CaseMur")) {
                CaseMur caseMur = (CaseMur) c;
                if(caseMur.getMur().getPtStructure() <= 0) {
                    detruireMur(caseMur);
                }
            }
        }
    }

    //Construit un mur et actualise
    private void construireMur(Case c){
        CaseMur cm = new CaseMur(new Mur(),c.getPosition());
        plateau.construireMur(c, cm);
        definitionCaseDuPlateau(cm.getPosition());
        afficherMur(cm);
        clean_pathfinding(build);
        build_pathfinding();
        mettreAjourInfoRessources();
    }

    //Détruit un mur et l'actualise
    private void detruireMur(CaseMur caseMur){
        CaseNormale caseNormale = new CaseNormale(caseMur.getPosition());
        plateau.detruireMur(caseMur, caseNormale);
        definitionCaseDuPlateau(caseNormale.getPosition());
        clean_pathfinding(shoot);
        shoot_pathfinding(listArmes.getSelectionModel().getSelectedItem());
        mettreAjourInfoRessources();
    }

    /*****
     *  Fin Attaques et construction *
     *****/



    /*****
     *  Déplacements *
     *****/

    //Déplace le joueur actuelle et actualise les case
    private void deplacerPionJoueur(Position destination) {
        cacherBoutonOuvrir();
        //PathTransition pathTransition = new PathTransition();

        //Transition déplacement pion du joueur

        Case caseDestination = plateau.getCaseByPosition(destination);
        plateau.deplacementDuJoueur(destination);

        plateau.turnPlayer.setPtMouvement(plateau.turnPlayer.getPtMouvement() - caseDestination.getCout());
        if(plateau.turnPlayer.caseSauvegarde != null && plateau.turnPlayer.caseSauvegarde.getType().equals("CaseArme") && plateau.isPointsAttaqueSuffisants(1))
            boutonOuvrirArme.setVisible(true);
        if(plateau.turnPlayer.caseSauvegarde != null && plateau.turnPlayer.caseSauvegarde.getType().equals("CasePopo") && plateau.isPointsAttaqueSuffisants(1))
            boutonOuvrirPopo.setVisible(true);
        if(plateau.turnPlayer.caseSauvegarde != null && plateau.turnPlayer.caseSauvegarde.getType().equals("CaseArmure") && plateau.isPointsAttaqueSuffisants(1))
            boutonOuvrirArmure.setVisible(true);
        definitionCaseDuPlateau(plateau.turnPlayer.getPosition());
        clean_pathfinding(ListPortee);
        pathfinding();
        mettreAjourInfoRessources();
    }

    /*****
     *  Fin déplacements *
     *****/



    /*****
     *  Lancers de dés *
     *****/

    //Lancer les dés pour obtenir les PM
    private void obtenirPointsDeMouvement() {
        int lancer1 = plateau.lancerUnDe();
        int lancer2 = plateau.lancerUnDe();
        ArrayList<Integer> listeDesLancers = new ArrayList<>();
        listeDesLancers.add(lancer1);
        listeDesLancers.add(lancer2);

        mettreAjourLeLancer(listeDesLancers);
        plateau.turnPlayer.setPtMouvement(lancer1+lancer2);
    }

    //Lancer les dés pour obtenir les PA
    private void obtenirPointsAttaque() {
        ArrayList<Integer> listeDesLancers = new ArrayList<>();
        int lancer = plateau.lancerUnDe();
        listeDesLancers.add(lancer);
        mettreAjourLeLancer(listeDesLancers);
        plateau.turnPlayer.setPtAttaque(plateau.turnPlayer.getPtAttaque() + lancer);
    }

    //Lance les dés pour le nombre de dégats à infliger
    private ArrayList<Integer> obtenirPointsDommage(int nombreDeLancers) {
        ArrayList<Integer> listeDesLancers = new ArrayList<>();
        for(int i = 0; i < nombreDeLancers; i++) {
            listeDesLancers.add(plateau.lancerUnDe());
        }
        mettreAjourLeLancer(listeDesLancers);
        return listeDesLancers;
    }

    //Clic sur le bouton "Prends tes PM"
    public void lancerPM() {
        obtenirPointsDeMouvement();
        mettreAjourInfoRessources();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> supprimerLesDes()));
        timeline.play();
        boutonLancerPM.setVisible(false);
        boutonLancerPA.setVisible(true);
    }

    //Clic sur le bouton "Prends tes PA"
    public void lancerPA() {
        obtenirPointsAttaque();
        mettreAjourInfoRessources();
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2000),
                ae -> supprimerLesDes()));
        timeline.play();
        boutonLancerPA.setVisible(false);
    }

    //supprime l'affichage de dés en cours
    private void supprimerLesDes() {
        hBoxLancers1.getChildren().clear();
        hBoxLancers2.getChildren().clear();
    }

    /*****
     *  Fin Lancers de dés *
     *****/



    /*****
     *  Mise à jour de l'ATH *
     ****/

    //Affichage d'une arme
    private void afficherArme(Arme arme){
        supprimerSanteArmureJoueurAdverse();
        imageJoueur2.setImage(arme.getImage());
        nomJoueur2.setText(arme.getName());
        ptStructure.setVisible(false);

    }

    //Affichage pour une case mur
    private void afficherMur(Case cm){
        supprimerSanteArmureJoueurAdverse();
        CaseMur caseMur = (CaseMur) cm;
        imageJoueur2.setImage(caseMur.getImg());
        nomJoueur2.setText("Mur");
        ptStructure.setProgress((double)caseMur.getMur().getPtStructure() /10);
        if(caseMur.getMur().getPtStructure() == 0) {
            CaseNormale caseNormale = new CaseNormale(caseMur.getPosition());
            afficherCaseNormale(caseNormale);
            ptStructure.setVisible(false);
        }
        else
            ptStructure.setVisible(true);
    }

    //Affichage pour une case normale
    private void afficherCaseNormale(Case cn){
        supprimerSanteArmureJoueurAdverse();
        imageJoueur2.setImage(null);
        nomJoueur2.setText(null);
        ptStructure.setVisible(false);
    }

    //Affichage pour une case popo
    private void afficherPopo(Case Popo){
        supprimerSanteArmureJoueurAdverse();
        imageJoueur2.setImage(Popo.getImage128());
        nomJoueur2.setText("Potion");
        ptStructure.setVisible(false);
    }

    //Affichage pour une case arme
    private void afficherCoffre(Case coffre){
        supprimerSanteArmureJoueurAdverse();
        imageJoueur2.setImage(coffre.getImage128());
        nomJoueur2.setText("Coffre");
        ptStructure.setVisible(false);
    }

    //Affichage pour une case armure
    private void afficherArmure(Case armure){
        supprimerSanteArmureJoueurAdverse();
        imageJoueur2.setImage(armure.getImage128());
        nomJoueur2.setText("Armure");
        ptStructure.setVisible(false);
    }

    private void afficherSanteArmureJoueurAdverse(Joueur joueur) {
        vBoxSanteArmureJoueurAdverse.getChildren().clear();
        vBoxSanteArmureJoueurAdverse.getChildren().add(new ColoredProgressBar("ArmureProgressBar", (double)joueur.getPtArmure() /100));
        vBoxSanteArmureJoueurAdverse.getChildren().add(new ColoredProgressBar("SanteProgressBar", (double)joueur.getPtSante() /100));
    }

    private void supprimerSanteArmureJoueurAdverse() {
        vBoxSanteArmureJoueurAdverse.getChildren().clear();
    }

    //Affichage du joueur actuel
    private void affichageDuJoueur(Joueur joueur) {
        nomJoueur.setText(joueur.getName());
        imageJoueur.setImage(joueur.getImageJoueur());
    }

    //Affichage d'un autre joueur
    private void affichageDuJoueur2(Joueur joueur) {
        nomJoueur2.setText(joueur.getName());
        imageJoueur2.setImage(joueur.getImageJoueur());
        afficherSanteArmureJoueurAdverse(joueur);
    }

    //Affichage du dernier lancer obtenu
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

    //Affichage des progressBar de santé et armure
    private void mettreAjourSanteArmure() {
        vBoxSanteArmure.getChildren().clear();
        vBoxSanteArmure.getChildren().add(new ColoredProgressBar("ArmureProgressBar", (double)plateau.turnPlayer.getPtArmure() /100));
        vBoxSanteArmure.getChildren().add(new ColoredProgressBar("SanteProgressBar", (double)plateau.turnPlayer.getPtSante() /100));
    }

    //Affichage des ressources dispo (PM, PA, Brique)
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
        Text nombreDeBrique = new Text(String.valueOf(plateau.turnPlayer.getBrique()));
        nombreDeBrique.setFont(font);
        hBoxBrique.getChildren().add(nombreDeBrique);

    }

    //Affichage des icones ressources
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

    //Class pour les progressBar
    class ColoredProgressBar extends ProgressBar {
        ColoredProgressBar(String styleClass, double progress) {
            super(progress);
            getStyleClass().add(styleClass);
        }
    }

    /*****
     *  Fin Mise à jour de l'affichage *
     *****/



    /*****
     *  Control des boutons (mode, fin de tour, ouvrir, etc.) *
     *****/

    //Mise a jour des infos suite au changement de joueur
    private void update() {
        affichageDuJoueur(plateau.turnPlayer);
        mettreAjourSanteArmure();
        mettreAjourInfoRessources();
        nomJoueur2.setText(null);
        imageJoueur2.setImage(null);
        cacherBoutonOuvrir();
        afficherListeArmes();
        listArmes.getSelectionModel().select(plateau.turnPlayer.indexArmeSelectionnee);
        boutonLancerPM.setVisible(true);
        plateau.getCaseByPosition(plateau.turnPlayer.getPosition()).getBouton().requestFocus();
        afficherCaseNormale(new CaseNormale(new Position(1,1)));
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

    //Clic sur le bouton "MOVE", active le mode déplacement
    public void move(){
        switch (mode){
            case 1:
                clean_pathfinding(ListPortee);
                break;
            case 2:
                clean_pathfinding(shoot);
                break;
            case 3:
                clean_pathfinding(build);
                break;
            default:break;
        }
        mode=1;
        pathfinding();
    }

    //Clic sur le bouton "BUILD", active le mode construction
    public void build(){
        switch (mode){
            case 1:
                clean_pathfinding(ListPortee);
                break;
            case 2:
                clean_pathfinding(shoot);
                break;
            case 3:
                clean_pathfinding(build);
                break;
            default:break;
        }
        mode=3;
        build_pathfinding();
    }

    //Clic sur le bouton "OuvrirCoffre"
    public void ouvrirCoffre() {
        fermerCoffre();
        int lancer1;
        int lancer2;
        if (plateau.isPointsAttaqueSuffisants(1)) {
            plateau.turnPlayer.setPtAttaque(plateau.turnPlayer.getPtAttaque() - 1);
            lancer1 = plateau.lancerUnDeAquatreChiffres();
            lancer2 = plateau.lancerUnDeAneufChiffres();
            Arme arme = plateau.tirageArme(lancer1, lancer2);
            //  afficherListeArmes();
            mettreAjourInfoRessources();

            FadeTransition fade = new FadeTransition(Duration.millis(800), gridContainer);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();

            //Ombre sur le gridPlateau
            backOuvrirCoffre.setPrefWidth(1200);
            backOuvrirCoffre.setPrefHeight(1200);
            BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, new CornerRadii(1), new Insets(0, 0, 0, 0));
            Background background = new Background(backgroundFill);
            backOuvrirCoffre.setBackground(background);
            backOuvrirCoffre.setOpacity(0.7);

            //Pane au centre (contient les animations du coffre
            paneOuvrirCoffre.setMaxWidth(900);
            paneOuvrirCoffre.setMaxHeight(720);
            BackgroundFill backgroundFill2 = new BackgroundFill(new ImagePattern(fixeCoffre), new CornerRadii(1), new Insets(5, 100, 15, 100));
            Background background2 = new Background(backgroundFill2);
            paneOuvrirCoffre.setBackground(background2);

            //Effet Ultraviolet autour
            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(0f);
            dropShadow.setOffsetY(0f);
            dropShadow.setWidth(100);
            dropShadow.setHeight(100);
            dropShadow.setColor(Color.rgb(127, 26, 229));
            paneOuvrirCoffre.setEffect(dropShadow);

            //Gif animé du coffre
            Image gifCoffre = new Image("images/Solid_war/GIF/GIFCoffreOfficiel.gif");
            ImageView gifImageView = new ImageView(gifCoffre);
            gifImageView.setPreserveRatio(true);
            gifImageView.setFitWidth(700);
            gifImageView.setFitHeight(760);
            gifImageView.setLayoutX(100);
            gifImageView.setLayoutY(5);

            Font font = new Font("Autumn Regular", 20);
            String styleDefault = "-fx-border-width:2px; -fx-border-radius:3; -fx-radius:3; -fx-border-color:#7F1AE5; -fx-background-color:#000000; -fx-text-fill:#FFD700;";
            String styleHover = "-fx-border-width:2px; -fx-border-color:#7F1AE5; -fx-border-radius:3; -fx-radius:3; -fx-background-color:#FFD700; -fx-text-fill:#000000;";

            Button button2 = new Button();
            button2.setText("Laisser");
            button2.setPrefSize(150, 50);
            button2.setStyle(styleDefault);
            button2.setFont(font);
            button2.setLayoutX(250);
            button2.setLayoutY(200);
            button2.setOnAction(e -> fermerCoffre());
            button2.setOnMouseEntered(e -> button2.setStyle(styleHover));
            button2.setOnMouseExited(e -> button2.setStyle(styleDefault));


            Button button = new Button();
            button.setText("Prendre");
            button.setPrefSize(150, 50);
            button.setStyle(styleDefault);
            button.setFont(font);
            button.setWrapText(true);
            button.setLayoutX(500);
            button.setLayoutY(200);
            button.setOnAction(e -> {
                PrendreArme(arme);
                listeArmeCoffre.setDisable(false);
                listeArmeCoffre.setOpacity(1.0);
            });
            button.setOnMouseEntered(e -> button.setStyle(styleHover));
            button.setOnMouseExited(e -> button.setStyle(styleDefault));

            paneOuvrirCoffre.getChildren().add(gifImageView);
            //Avec transformation
            //TransitionForArme(armeObtenue, 330, 370, 270, 330 - 150);

            TransitionForArme(arme, 300, 365, 350, 430 - 150);

            gridContainer.getChildren().add(backOuvrirCoffre);
            gridContainer.getChildren().add(paneOuvrirCoffre);


            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(900),
                    ae -> {
                        paneOuvrirCoffre.getChildren().remove(gifImageView);
                        paneOuvrirCoffre.getChildren().add(button);
                        paneOuvrirCoffre.getChildren().add(button2);
                        afficherListeArmesCoffre();
                        if (listeArmeCoffre.getChildren().size() == 5) {
                            button.setText("Remplacer");
                        }
                    }));
            timeline.play();

        }

    }

    public void remplacer_arme(int i,Arme arme){
        listeArmeCoffre.getChildren().get(i).setOnMouseClicked(mouseEvent -> {
            plateau.turnPlayer.remplacerArme(plateau.turnPlayer.getArmes().get(i),arme);
            // afficherListeArmesCoffre();
            afficherListeArmes();
            fermerCoffre();
        });

    }

    public void PrendreArme(Arme arme){
        if(plateau.turnPlayer.getArmes().size()<5) {
            plateau.turnPlayer.ajouterArme(arme);
            fermerCoffre();
            afficherListeArmes();
        }else {
            listeArmeCoffre.setOpacity(1);
            for(int i=0;i<5;i++){
                remplacer_arme(i,arme);
            }
        }
    }

    public void afficherListeArmesCoffre(){
        paneOuvrirCoffre.getChildren().remove(listeArmeCoffre);

        listeArmeCoffre = new HBox();
        listeArmeCoffre.setLayoutX(185);
        listeArmeCoffre.setLayoutY(20);

        int i = 0;

        for (Arme a : plateau.turnPlayer.getArmes()) {
            ImageView img = new ImageView(a.getImage());
            img.setPreserveRatio(true);
            img.setFitWidth(100);
            img.setFitHeight(100);
            listeArmeCoffre.getChildren().add(img);
            i++;
        }

        setDropShadowsListeArmes(plateau.turnPlayer.getArmes().size());
        listeArmeCoffre.setDisable(true);
        listeArmeCoffre.setOpacity(0.2);
        paneOuvrirCoffre.getChildren().add(listeArmeCoffre);
    }

    public void remplacerArme(Arme a, Arme newarme){

    }

    private void setDropShadowsListeArmes(int nombreDarme) {
        DropShadow dropShadowOnHover = new DropShadow();
        dropShadowOnHover.setOffsetX(0f);
        dropShadowOnHover.setOffsetY(0f);
        dropShadowOnHover.setColor(Color.rgb(255,215,0));
        for(int i = 0; i < nombreDarme; i++) {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.GRAY);
            dropShadow.setOffsetX(-2 + i);
            dropShadow.setOffsetY(-2);
            ImageView imageView = (ImageView) listeArmeCoffre.getChildren().get(i);
            imageView.setEffect(dropShadow);
            imageView.setOnMouseEntered(e -> imageView.setEffect(dropShadowOnHover));
            imageView.setOnMouseExited(e -> imageView.setEffect(dropShadow));
        }
    }

    public void fermerCoffre() {
        listeArmeCoffre.getChildren().clear();
        paneOuvrirCoffre.getChildren().remove(listeArmeCoffre);
        paneOuvrirCoffre.getChildren().clear();
        backOuvrirCoffre.getChildren().clear();
        gridContainer.getChildren().remove(backOuvrirCoffre);
        gridContainer.getChildren().remove(paneOuvrirCoffre);
        cacherBoutonOuvrir();
        plateau.turnPlayer.caseSauvegarde = null;
        definitionCaseDuPlateau(plateau.turnPlayer.getPosition());
    }

    //Clic sur le bouton "OuvrirPopo"
    public void ouvrirPopo() {
        plateau.turnPlayer.setPtAttaque(plateau.turnPlayer.getPtAttaque()-1);
        plateau.turnPlayer.setPtSante(plateau.turnPlayer.getPtSante()+plateau.lancerTroisDes());
        mettreAjourSanteArmure();
        mettreAjourInfoRessources();
        cacherBoutonOuvrir();
        plateau.turnPlayer.caseSauvegarde = null;
    }

    //Clic sur le bouton "OuvrirArmure"
    public void ouvrirArmure() {
        plateau.turnPlayer.setPtAttaque(plateau.turnPlayer.getPtAttaque()-1);
        plateau.turnPlayer.setPtArmure(plateau.turnPlayer.getPtArmure()+plateau.lancerTroisDes());
        mettreAjourSanteArmure();
        mettreAjourInfoRessources();
        cacherBoutonOuvrir();
        plateau.turnPlayer.caseSauvegarde = null;
    }

    //Supprime l'affichage de tous les boutons ouvrir
    private void cacherBoutonOuvrir() {
        boutonOuvrirArme.setVisible(false);
        boutonOuvrirArmure.setVisible(false);
        boutonOuvrirPopo.setVisible(false);
    }

    /*****
     *  Fin Control des boutons *
     *****/


    private void TransitionForArme(Arme armeObtenue, double layoutXFrom, double layoutXTo, double layoutYFrom, double layoutYTo) {
        ImagePattern imagePatternArme = new ImagePattern(armeObtenue.getImage());
        Rectangle rectangle = new Rectangle(175, 175);
        rectangle.setFill(imagePatternArme);

        paneOuvrirCoffre.getChildren().add(rectangle);
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1200), rectangle);
        translateTransition.setFromX(layoutXFrom);
        translateTransition.setToX(layoutXTo);
        translateTransition.setFromY(layoutYFrom);
        translateTransition.setToY(layoutYTo);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(3800), rectangle);
        fadeTransition.setFromValue(0.0f);
        fadeTransition.setToValue(1.0f);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(true);
        translateTransition.play();
        fadeTransition.play();


        //EFFETS
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(10.0f);
        innerShadow.setOffsetY(-25.0f);
        innerShadow.setRadius(35);
        innerShadow.setColor(Color.WHITE);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0f);
        dropShadow.setOffsetY(-2.0f);
        dropShadow.setColor(Color.LIGHTGRAY);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(110.0f);
        Lighting l = new Lighting();
        l.setLight(light);
        l.setSurfaceScale(45.0f);

        PerspectiveTransform e = new PerspectiveTransform();
        e.setUlx(-20);     // Upper-left point
        e.setUly(105);
        e.setUrx(165);    // Upper-right point
        e.setUry(120);
        e.setLlx(-35);      // Lower-left point
        e.setLly(240);
        e.setLrx(155);    // Lower-right point
        e.setLry(280);

        l.setContentInput(innerShadow);
        l.setContentInput(dropShadow);
        rectangle.setEffect(l);

        Timeline clignotement = new Timeline(new KeyFrame(
                Duration.millis(1200),
                ae -> {
                    FadeTransition clignote = new FadeTransition(Duration.millis(400), rectangle);
                    clignote.setFromValue(1.0f);
                    clignote.setToValue(0.0f);
                    clignote.setCycleCount(6);
                    clignote.setAutoReverse(true);
                    clignote.play();

                }));
        clignotement.play();
    }

    private boolean isCaseEstDansLeRayon(Position position, ArrayList<Case> listeDesCasesQuiExplosent) {
        for(Case caseQuiExplose : listeDesCasesQuiExplosent) {
            if(caseQuiExplose.getPosition().getX() == position.getX() && caseQuiExplose.getPosition().getY() == position.getY())
                return true;
        }
        return false;
    }


    /*****
     *  Gestion actions du menu *
     *****/

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

    /*****
     *  Fin Gestion actions du menu *
     *****/
}
