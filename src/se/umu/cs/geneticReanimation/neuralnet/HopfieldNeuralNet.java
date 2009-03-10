package se.umu.cs.geneticReanimation.neuralnet;

import java.util.Arrays;

public class HopfieldNeuralNet implements Brain { 
    private double weightMatrix[][];
    private double[] nodes;
    private double[] inputs;
    
    public HopfieldNeuralNet(final double[] genotype) {
        setGenotype(genotype);
        // TODO:
    }
    
    public HopfieldNeuralNet(int size) {
        this.weightMatrix = new double[size][size];
        // TODO: create random weights
        this.nodes = new double[size];
        this.inputs = new double[size];
    }

    public void setGenotype(final double[] genotype) {
        double sqrt = Math.sqrt(genotype.length);
        if (sqrt % 1 != 0 || genotype.length < 4) {
            String errorMsg = "Genotype length must be bigger than 3 and have "
                + "an integer root. Example valid length: 4, 9, 16";
            throw new IllegalArgumentException(errorMsg);
        }
        int size = (int) sqrt;
        this.weightMatrix = new double[size][size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                weightMatrix[i][j] = genotype[count];
                count++;
            }
        }
        this.nodes = new double[size];
    }
    
    public double[] getGenotype() {
        double[] result = new double[(int) Math.pow(weightMatrix.length, 2)];
        int count = 0;
        for (double[] row : weightMatrix) {
            for (double value : row) {
                result[count] = value;
                count++;
            }
        }
        return result;
    }
    
    //     public void setWeightMatrix(final double weightMatrix[][]) {
    //         // Check values
    //         for (double[] row : weightMatrix) {
    //             for (double value: row) {
    //                 if (value < -1 || value > 1) {
    //                     throw new IllegalArgumentException("Values must be between -1 and 1");
    //                 }
    //             }
    //         }
    //         this.weightMatrix = weightMatrix;
    //     }
    
    public double[][] getWeightMatrix() {
        return this.weightMatrix;
    }

    public void setInputs(final double[] inputs) {
        if (this.inputs == null) {
            this.inputs = new double[inputs.length];
        }
        
        if (this.inputs.length != inputs.length) {
            throw new IllegalArgumentException("Input must be same length as previous input");
        }
        
        // TODO: Should we sigmoid inputs, or scale it in the simulation?
        for (int i = 0, lenght = inputs.length; i < lenght; i++) {
            this.inputs[i] = sigmoid(inputs[i]);
        }
    }
    
    /**
     * Update node values from current input and all neighbours.
     */
    public void step() {
        if (this.inputs == null) {
            throw new IllegalStateException("Input is not set");
        }
        
        double[] tmpNodes = new double[nodes.length];
        
        // Fill tmpNodes with values from input and nodes
        for (int i = 0, lenght = tmpNodes.length; i < lenght; i++) {
            // Add input if inputnode, input nodes come first
            if (i < inputs.length) {
                tmpNodes[i] += inputs[i];
            }
            
            // For every neighbour in nodes put value * weight in tmpNodes
            for (int j = 0; j < lenght; j++) {
                tmpNodes[i] += nodes[j] * weightMatrix[i][j];
            }
        }
        
        // Fill nodes with sigmoid(values from tmpNodes)
        for (int i = 0, lenght = nodes.length; i < lenght; i++) {
            this.nodes[i] = sigmoid(tmpNodes[i]);
        }
    }
    
    public double[] getOutputs() {
        // step();
        // TODO: size of output?
        return nodes;//Arrays.copyOf(nodes, nodes.length);
    }

    // TODO: test
    protected double sigmoid(double input) {
        return 2 / (1 + Math.pow(Math.E, (-2 * input))) - 1;
    }
}
