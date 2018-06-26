package com.hervelin.NeuralNetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class NeuralNetwork {

    private NeuralLayer layers[];
    private double learningRate = 0.1;
    public int learningCycles = 0;

    public NeuralNetwork() {
        layers = new NeuralLayer[4];
        layers[0] = new NeuralLayer(17,17);
        layers[1] = new NeuralLayer(17,10);
        layers[2] = new NeuralLayer(10,10);
        layers[3] = new NeuralLayer(10,1);
    }

    public void reset() {
        layers = new NeuralLayer[4];
        layers[0] = new NeuralLayer(17,17);
        layers[1] = new NeuralLayer(17,10);
        layers[2] = new NeuralLayer(10,10);
        layers[3] = new NeuralLayer(10,1);
        learningCycles = 0;
    }

    public NeuralNetwork(File file) throws IOException {
        this.load(file);
    }

    public double[]
    outputs(double inputs[]) {
        for (int i = 0; i < getInputSize(); i++) {
            inputs = layers[i].outputs(inputs);
        }
        return inputs;
    }

    public int getInputSize() {
        return this.layers.length;
    }

    public int getOutputSize() {
        return this.layers[this.layers.length - 1].getSize();
    }

    public int getLayerCount() {
        return this.layers.length;
    }

    public void save(File file) throws IOException {
        FileWriter fstream = new FileWriter(file, false);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(Integer.toString(this.learningCycles));
        out.newLine();
        out.write(Integer.toString(getLayerCount()));
        out.newLine();
        for (int i = 0; i < getLayerCount(); i++) {
            layers[i].save(out);
        }
        out.close();
        fstream.close();
    }

    public void load(File file) throws IOException {
        FileReader fstream = new FileReader(file);
        BufferedReader in = new BufferedReader(fstream);
        this.learningCycles = Integer.parseInt(in.readLine());
        int size = Integer.parseInt(in.readLine());
        layers = new NeuralLayer[size];
        for (int i = 0; i < size; i++) {
            layers[i] = new NeuralLayer(in);
        }
        fstream.close();
    }

    public void learn(double[] expectedOutputs) {
        this.learningCycles++;
        double sum = 0;
        double error = 0;
        double weight = 0;

        //Last layer
        int lastLayer = layers.length - 1;
        for(int j = 0; j < layers[lastLayer].getSize(); j++) {

            for(int k = 0; k < layers[lastLayer].getNeuron(j).getInputSize(); k++){
                //Multiply by derivative of 1/(1+exp(-x))
                double delta = layers[lastLayer].getNeuron(j).lastOutput() * (1 - layers[lastLayer].getNeuron(j).lastOutput()) * (expectedOutputs[j] - layers[lastLayer].getNeuron(j).lastOutput());

                layers[lastLayer].getNeuron(j).setError(delta);
                layers[lastLayer].getNeuron(j).setWeight(k, layers[lastLayer].getNeuron(j).getWeight(k) + learningRate * layers[lastLayer].getNeuron(j).getError() * layers[lastLayer].getNeuron(j).getLastInput(k));
            }
        }

        //Other layers
        for(int i = layers.length - 2; i >= 0; i--){
            for(int j = 0; j < layers[i].getSize(); j++) {

                //Get error
                sum = 0;
                for(int k = 0; k < layers[i+1].getSize(); k++){
                    error = layers[i+1].getNeuron(k).getError();
                    weight = layers[i+1].getNeuron(k).getWeight(j);
                    sum += error * weight;
                }

                for(int k = 0; k < layers[i].getNeuron(j).getInputSize(); k++){
                    //Multiply by derivative of 1/(1+exp(-x))
                    double delta = layers[i].getNeuron(j).lastOutput() * (1 - layers[i].getNeuron(j).lastOutput()) * sum;

                    layers[i].getNeuron(j).setError(delta);
                    layers[i].getNeuron(j).setWeight(k, layers[i].getNeuron(j).getWeight(k) + learningRate * layers[i].getNeuron(j).getError() * layers[i].getNeuron(j).getLastInput(k));
                }
            }
        }
    }
}
