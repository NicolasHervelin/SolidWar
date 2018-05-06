package com.hervelin.controller;

import com.hervelin.model.Joueur;
import com.hervelin.model.Plateau;

public class ControllerJeu implements ControlledScreen {
    ScreensController myController;

    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;

    public Plateau plateau;
    public int nombreCaseX = 50;
    public int nombreCaseY = 50;


    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        initialize();
    }

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

    }
}
