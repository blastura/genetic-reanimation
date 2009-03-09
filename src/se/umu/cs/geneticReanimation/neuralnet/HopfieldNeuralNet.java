package se.umu.cs.geneticReanimation.neuralnet;

import java.util.List;

public class HopfieldNeuralNet implements Brain { 
    private double weightMatrix[][];
    private double[] nodes;
    private double[] inputs;
    
    public HopfieldNeuralNet(double[] genotype) {
        setGenotype(genotype);
        // TODO:
    }
    
    public HopfieldNeuralNet(int size) {
        this.weightMatrix = new double[size][size];
        // TODO: create random weights
        this.nodes = new double[size];
    }

    public void setGenotype(final double[] genotype) {
        // TODO:
    }
    
    public double[] getGenotype() {
        // TODO:
        return new double[1];
    }
    
    private void setWeightMatrix(final double weightMatrix[][]) {
        // Check values
        for (double[] row : weightMatrix) {
            for (double value: row) {
                if (value < -1 || value > 1) {
                    throw new IllegalArgumentException("Values must be between -1 and 1");
                }
            }
        }
        this.weightMatrix = weightMatrix;
    }
    
    public double[][] getWeightMatrix() {
        return this.weightMatrix;
    }

    public void setInputs(final double[] inputs) {
        for (int i = 0, lenght = inputs.length; i < lenght; i++) {
            this.inputs[i] = sigmoid(inputs[i]);
        }
    }
    
    /**
     * Update node values from current input and all neighbours.
     */
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
    
    public double[] getOutputs() {
        step();
        return new double[1];
    }

    // TODO: test
    protected double sigmoid(double input) {
        return 2 / (1 + Math.pow(Math.E, (-2 * input))) - 1;
    }
}
