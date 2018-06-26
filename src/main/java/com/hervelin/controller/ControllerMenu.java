package com.hervelin.controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMenu implements Initializable, ControlledScreen {
    ScreensController myController;
    String test=new String("fff");

    @FXML
    private GridPane mainMenu;
    @FXML
    private ChoiceBox choixNbJoueur;
    @FXML
    private Pane pane2,pane1;
    @FXML
    private TextArea nomJoueur1,nomJoueur2,nomJoueur3,nomJoueur4;
    @FXML
    private Text titreJoueur2,titreJoueur3,titreJoueur4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      /*  SoundEffect.init();
        SoundEffect.volume = SoundEffect.Volume.LOW;  // un-mute*/
        choixNbJoueur.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if(choixNbJoueur.getItems().get((Integer) number2).equals("4 joueurs")){
                    titreJoueur2.setVisible(true);
                    nomJoueur2.setVisible(true);
                    titreJoueur3.setVisible(true);
                    nomJoueur3.setVisible(true);
                    titreJoueur4.setVisible(true);
                    nomJoueur4.setVisible(true);
                }else if(choixNbJoueur.getItems().get((Integer) number2).equals("3 joueurs")){
                    titreJoueur2.setVisible(true);
                    nomJoueur2.setVisible(true);
                    titreJoueur3.setVisible(true);
                    nomJoueur3.setVisible(true);
                    nomJoueur4.setVisible(false);
                    titreJoueur4.setVisible(false);
                }else if(choixNbJoueur.getItems().get((Integer) number2).equals("2 joueurs")){
                    titreJoueur2.setVisible(true);
                    nomJoueur2.setVisible(true);
                    titreJoueur3.setVisible(false);
                    nomJoueur3.setVisible(false);
                    nomJoueur4.setVisible(false);
                    titreJoueur4.setVisible(false);
                }else{
                    titreJoueur2.setVisible(false);
                    nomJoueur2.setVisible(false);
                    titreJoueur3.setVisible(false);
                    nomJoueur3.setVisible(false);
                    nomJoueur4.setVisible(false);
                    titreJoueur4.setVisible(false);
                }
            }
        });
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        setUp();
    }

    private void setUp() {
    }

    public void nextPane(){
        pane1.setVisible(false);
        pane2.setVisible(true);
    }



    public void returnPane(){
        pane2.setVisible(false);
        pane1.setVisible(true);
    }
    public void goToJeu() {
        myController.addData("nbjoueurs",choixNbJoueur.getValue().toString());
        myController.addData("joueur1",nomJoueur1.getText());
        myController.addData("joueur2",nomJoueur2.getText());
        myController.addData("joueur3",nomJoueur3.getText());
        myController.addData("joueur4",nomJoueur4.getText());
        myController.addData("is_bot_game","false");
        myController.loadScreen(Main.Jeu_ID, Main.Jeu_FILE);
        myController.setScreen(Main.Jeu_ID);
    }

    public void IA_Game(){
        myController.addData("nbjoueurs","IA");
        myController.loadScreen(Main.Jeu_ID, Main.Jeu_FILE);
        myController.setScreen(Main.Jeu_ID);
    }
}
