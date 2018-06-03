package com.hervelin.controller;

import com.hervelin.model.*;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;


class AlertBox {

    static void afficherLancer(ArrayList<Integer> listeDesLancers, String message) {
        Stage messageBox = new Stage();
        messageBox.initStyle(StageStyle.TRANSPARENT);

        messageBox.initModality(Modality.APPLICATION_MODAL);
        messageBox.setMinWidth(350);
        messageBox.setMinHeight(200);

        Label l = new Label(message);
        l.setTextFill(Color.GRAY);
        l.setStyle("-fx-font: 22 'Autumn Regular';" +
                "-fx-text-alignment: center;");

        VBox container = new VBox(10);
        container.getChildren().add(l);
        for(int lancer : listeDesLancers) {
            container.getChildren().add(new ImageView(new Image("images/ResultatLancer" + lancer + ".png", 40, 40, true, true)));
        }
        container.setAlignment(Pos.BOTTOM_RIGHT);
        container.setMinWidth(850);
        container.setMinHeight(300);

        // fill background with java
        BackgroundFill fill = new BackgroundFill(Color.TRANSPARENT, new CornerRadii(1), new Insets(0, 0, 0, 0));
        container.setBackground(new Background(fill));

        // fadeIn
        FadeTransition fade = new FadeTransition(Duration.seconds(1), container);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
        Scene scene = new Scene(container);
        messageBox.setScene(scene);
        // transparence
        scene.setFill(null);
        messageBox.initStyle(StageStyle.TRANSPARENT);

        // close auto
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished( event -> messageBox.close() );
        delay.play();
        // show popup
        messageBox.show();
    }



    /*static void afficherArme(int lancer1, int lancer2) {
        Stage messageBox = new Stage();
        messageBox.initStyle(StageStyle.TRANSPARENT);

        messageBox.initModality(Modality.APPLICATION_MODAL);
        messageBox.setMinWidth(400);
        messageBox.setMinHeight(400);


        Label l = new Label(arme.getName());
        l.setTextFill(Color.RED);
        l.setStyle("-fx-font: 30 'Autumn Regular';" +
                "-fx-text-alignment: center;");

        VBox container = new VBox(10);
        container.getChildren().add(l);
        container.getChildren().add(new ImageView(arme.getImage()));
        container.setAlignment(Pos.BOTTOM_RIGHT);
        container.setMinWidth(850);
        container.setMinHeight(300);

        // fill background with java
        BackgroundFill fill = new BackgroundFill(Color.TRANSPARENT, new CornerRadii(1), new Insets(0, 0, 0, 0));
        container.setBackground(new Background(fill));

        // fadeIn
        FadeTransition fade = new FadeTransition(Duration.seconds(1), container);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
        Scene scene = new Scene(container);
        messageBox.setScene(scene);
        // transparence
        scene.setFill(null);
        messageBox.initStyle(StageStyle.TRANSPARENT);

        // close auto
        scene.setOnMouseClicked(event -> messageBox.close());
        // show popup
        messageBox.show();
    }*/
}