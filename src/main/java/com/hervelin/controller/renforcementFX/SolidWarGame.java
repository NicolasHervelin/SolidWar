package com.hervelin.controller.renforcementFX;

import com.hervelin.controller.ControllerJeu;
import com.hervelin.model.renforcement2.SwingAppletSolidWar;

import javax.swing.*;
import java.awt.*;

public class SolidWarGame extends Thread {
    long delay;
    SwingAppletSolidWar a;
    RLPolicyForSolidWar policy;
    SolidWarWorld world;
    ControllerJeu controllerJeu;
    static final int GREEDY=0, SMART=1; // type of mouse to use
    int mousetype = SMART;

    public boolean gameOn = false, single=false, gameActive, newInfo = false;

    public SolidWarGame(ControllerJeu controllerJeu, long delay, SolidWarWorld w, RLPolicyForSolidWar policy) {
        world = w;
        this.controllerJeu = controllerJeu;
        this.delay = delay;
        this.policy = policy;
    }

    /* Thread Functions */
    public void run() {
        System.out.println("--Game thread started");
        // start game
        try {
            while(true) {
                while(gameOn) {
                    gameActive = true;
                    resetGame();
                    //SwingUtilities.invokeLater(a); // draw initial state
                    runGame();
                    gameActive = false;
                    newInfo = true;
                    //SwingUtilities.invokeLater(a); // update state
                    sleep(delay);
                    controllerJeu.definitionCaseDuPlateau(null);
                }
                sleep(delay);
            }
        } catch (InterruptedException e) {
            System.out.println("interrupted.");
        }
        System.out.println("== Game finished.");
    }

    public void runGame() {
        while(!world.endGame()) {
            //System.out.println("Game playing. Making move.");
            int action=-1;
            if (mousetype == GREEDY) {
                //action = world.mouseAction();
                System.err.println("MouseType = GREEDY -- Not Implemented");
            } else if (mousetype == SMART) {
                action = policy.getBestAction(world.getState());
                System.err.println("MouseType = SMART");
            } else {
                System.err.println("Invalid mouse type:"+mousetype);
            }
            world.getNextState(action);

            //a.updateBoard();
            //SwingUtilities.invokeLater(a);

            try {
                sleep(delay);
            } catch (InterruptedException e) {
                System.out.println("interrupted.");
            }
        }
        //a.scoreJ1 += world.scoreJ1;
        //a.scoreJ2 += world.scoreJ2;

        // turn off gameOn flag if only single game
        if (single) gameOn = false;
    }

    public void interrupt() {
        super.interrupt();
        System.out.println("(interrupt)");
    }

    /* end Thread Functions */

    public void setPolicy(RLPolicyForSolidWar p) {	policy = p; }

    //public Position getJ1() { return world.j1.getPosition(); }
    //public Position getJ2() { return world.j2.getPosition(); }

    public Dimension getJ1() { return new Dimension(world.j1.getPosition().getX(), world.j1.getPosition().getY()); }
    public Dimension getJ2() { return new Dimension(world.j2.getPosition().getX(), world.j2.getPosition().getY()); }


    public void resetGame() {
        world.resetState();
    }
}
