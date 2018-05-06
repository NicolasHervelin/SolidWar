package com.hervelin.controller;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    static String Menu_ID = "MENU";
    static String Menu_FILE = "/menu.fxml";

    static String Jeu_ID = "JEU";
    static String Jeu_FILE = "/jeu.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {

        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Jeu_ID, Jeu_FILE);
        mainContainer.setScreen(Jeu_ID);

        Group root = new Group();
        root.getChildren().addAll(mainContainer.stack);
        //primaryStage.getIcons().add(new Image("images/fond.png"));
        primaryStage.setTitle("The Game");
        //primaryStage.getIcons().add(new Image("images/fond.png"));
        Scene scene = new Scene(root, 890, 690);
        scene.getStylesheets().add("/style.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
