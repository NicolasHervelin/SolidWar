package com.hervelin.controller;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    static String Menu_ID = "MENU";
    static String Menu_FILE = "/menu.fxml";

    static String Jeu_ID = "JEU";
    static String Jeu_FILE = "/jeu.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {

        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(Menu_ID, Menu_FILE);
        mainContainer.setScreen(Menu_ID);

        int width = (int) Screen.getPrimary().getBounds().getWidth();
        int height = (int) Screen.getPrimary().getBounds().getHeight();

        Group root = new Group();
        root.getChildren().addAll(mainContainer.stack);
        //primaryStage.getIcons().add(new Image("images/fond.png"));
        primaryStage.setTitle("The Game");
        //primaryStage.getIcons().add(new Image("images/fond.png"));
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add("/style.css");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
