package com.hervelin.NeuralNetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Neuron {

    private double inputWeights[];
    private double lastInputs[];
    private double error = 0;
    private double lastOutput = 0;

    public Neuron(double inputWeights[]) {
        this.inputWeights = inputWeights;
    }

    public Neuron(BufferedReader in) throws IOException {
        this.load(in);
    }

    public int getInputSize() {
        return this.inputWeights.length;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double lastOutput() {
        return lastOutput;
    }

    public double getWeight(int previousNode) {
        return inputWeights[previousNode];
    }

    public void setWeight(int previousNode, double weight) {
        inputWeights[previousNode] = weight;
    }

    public double output(double inputs[]) {
        this.lastInputs = inputs;
        double out = 0;
        for (int i = 0; i < getInputSize(); i++) {
            out += inputs[i] * inputWeights[i];
        }
        this.lastOutput = convert(out);
        return lastOutput;
    }

    public void save(BufferedWriter out) throws IOException {
        out.write(Integer.toString(getInputSize()));
        out.newLine();
        for (int i = 0; i < getInputSize(); i++) {
            out.write(Double.toString(inputWeights[i]));
            out.newLine();
        }
    }

    public void load(BufferedReader in) throws IOException {
        int inputSize = Integer.parseInt(in.readLine());
        this.inputWeights = new double[inputSize];
        for (int i = 0; i < inputSize; i++) {
            this.inputWeights[i] = Double.parseDouble(in.readLine());
        }
    }

    private double convert(double input) {
        return ( 1.0 / ( 1.0 + Math.exp(-input)));
    }

    public double getLastInput(int k) {
        return lastInputs[k];
    }
}
