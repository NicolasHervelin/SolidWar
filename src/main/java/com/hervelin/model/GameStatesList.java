package com.hervelin.model;

import com.hervelin.NeuralNetwork.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

public class GameStatesList {
    List<Double> ballX = new ArrayList<Double>();
    List<Double> ballY = new ArrayList<Double>();
    List<Double> ballYSpeed = new ArrayList<Double>();

    public void reset() {
        ballX.clear();
        ballY.clear();
        ballYSpeed.clear();
    }

    public void add(double ballX,double ballY,double ballYSpeed) {
        this.ballX.add(ballX);
        this.ballY.add(ballY);
        this.ballYSpeed.add(ballYSpeed);
    }

    public void learnAll(double finalPos, NeuralNetwork net) {
        for(int i = 0; i < this.ballX.size(); i++) {
            double out = net.outputs(new double[] {this.ballY.get(i), this.ballX.get(i), this.ballYSpeed.get(i)})[0];
            if (Math.abs(out-ballY.get(i))>0.2) {
                net.learn(new double[] {finalPos});
            }
        }
    }
}