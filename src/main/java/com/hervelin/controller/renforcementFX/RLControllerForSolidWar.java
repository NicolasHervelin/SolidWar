package com.hervelin.controller.renforcementFX;

import com.hervelin.model.renforcement2.RLWorld;
import com.hervelin.model.renforcement2.SwingAppletSolidWar;

import javax.swing.*;

public class RLControllerForSolidWar extends Thread {
    public RLearnerForSolidWar learner;
    public int epochswaiting = 0, epochsdone = 0, totaldone = 0;
    long delay;
    int UPDATE_EPOCHS = 100;

    public boolean newInfo;

    public RLControllerForSolidWar(RLWorld world, long waitperiod) {
        // create RLearner
        learner = new RLearnerForSolidWar(world);
        delay = waitperiod;
    }

    public void run() {
        try {
            while(true) {
                if (epochswaiting > 0) {
                    System.out.println("Running "+epochswaiting+" epochs");
                    learner.running = true;
                    while(epochswaiting > 0) {
                        epochswaiting--;
                        epochsdone++;
                        learner.runEpoch();

                        if (epochswaiting % UPDATE_EPOCHS == 0) {

                        }
                            //SwingUtilities.invokeLater(a);
                    }
                    totaldone += epochsdone;
                    epochsdone = 0;
                    learner.running = false;

                    newInfo = true;

                    // inform applet we're finished
                    //SwingUtilities.invokeLater(a);
                }

                sleep(delay);
            }
        } catch (InterruptedException e) {
            System.out.println("Controller interrupted.");
        }
    }

    public void setEpisodes(int episodes) {
        System.out.println("Setting "+episodes+" episodes");
        this.epochswaiting += episodes;
    }
    public void stopLearner() {
        System.out.println("Stopping learner.");
        newInfo = false;
        epochswaiting = 0;
        totaldone += epochsdone;
        epochsdone = 0;

        // inform applet we're finished
        //SwingUtilities.invokeLater(a);

        learner.running = false;
    }

    public synchronized RLPolicyForSolidWar resetLearner() {
        totaldone = 0;
        epochsdone = 0;
        epochswaiting = 0;

        return learner.newPolicy();
    }
}
