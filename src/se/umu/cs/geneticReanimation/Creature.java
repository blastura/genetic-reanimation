package se.umu.cs.geneticReanimation;

import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;

public class Creature {
    private double fitness;
    private Brain brain;
    //private List<Joint> effectors;

    public Creature(int genotypeSize) {
        this.brain = new HopfieldNeuralNet(genotypeSize);
        //effectors = gooooon;

    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double[] getGenotype() {
        return brain.getGenotype();
    }
}
