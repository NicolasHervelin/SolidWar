package com.hervelin.NeuralNetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class NeuralLayer {

    private Neuron neurons[];

    public NeuralLayer(int inputSize, int size) {
        neurons = new Neuron[size];
        for (int i = 0; i < size; i++) {
            double[] weights = new double[inputSize];
            for (int j = 0; j < inputSize; j++) {
                weights[j] = (Math.random()-0.5)*10;
            }
            neurons[i] = new Neuron(weights);
        }
    }

    public NeuralLayer(BufferedReader in) throws IOException {
        this.load(in);
    }

    public double[] outputs(double inputs[]) {
        double[] out = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            out[i] = neurons[i].output(inputs);
        }
        return out;
    }

    public int getSize() {
        return this.neurons.length;
    }

    public Neuron getNeuron(int i) {
        return neurons[i];
    }

    public void save(BufferedWriter out) throws IOException {
        out.write(Integer.toString(neurons.length));
        out.newLine();
        for (int i = 0; i < getSize(); i++) {
            neurons[i].save(out);
        }
    }

    public void load(BufferedReader in) throws IOException {
        int size = Integer.parseInt(in.readLine());
        this.neurons = new Neuron[size];
        for (int i = 0; i < size; i++) {
            this.neurons[i] = new Neuron(in);
        }
    }
}
