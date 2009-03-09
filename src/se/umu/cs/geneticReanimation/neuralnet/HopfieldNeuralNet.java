package se.umu.cs.geneticReanimation;

import java.util.Arrays;

public class HopfieldNeuralNet implements NeuralNet { 
    private double weightMatrix[][];
    private double[] nodes;
    private double[] inputs;
    
    public HopfieldNeuralNet(int size) {
        this.weightMatrix = new double[size][size];
        this.nodes = new double[size];
    }

    public void setWeightMatrix(double weightMatrix[][]) {

    }
    
    public double[][] getWeightMatrix() {
        return this.weightMatrix;
    }

    public void setInputs(final double[] inputs) {
        for (int i = 0, lenght = inputs.length; i < lenght; i++) {
            this.inputs[i] = sigmoid(inputs[i]);
        }
    }
    
    private void step() {
        double[] tmpNodes = new double[nodes.length];
        
        // Fill tmpNodes with values from input and nodes
        for (int i = 0, lenght = tmpNodes.length; i < lenght; i++) {
            // Add input if inputnode, input nodes come first
            if (i < inputs.length) {
                tmpNodes[i] += inputs[i];
            }
            
            // Fore every neighbour add value * weight
            for (int j = 0; j < lenght; j++) {
                tmpNodes[i] += nodes[j] * weightMatrix[i][j];
            }
        }
        
        // Fill nodes with sigmoid(values from tmpNodes)
        for (int i = 0, lenght = nodes.length; i < lenght; i++) {
            nodes[i] = sigmoid(tmpNodes[i]);
        }
    }
    
    public double[] getOutput() {
        step();
        return new double[1];
    }

    // TODO: test
    protected double sigmoid(double input) {
        return 2 / (1 + Math.pow(Math.E, (-2 * input))) - 1;
    }
}
