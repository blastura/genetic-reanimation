package se.umu.cs.geneticReanimation;

import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;

public class WormCreature implements Creature { 
    private double fitness;
    private Brain brain;
    
    public WormCreature(double[] genotype) {
        this.brain = new HopfieldNeuralNet(genotype);
    }
    
    public WormCreature(int genotypeSize) {
        // TODO: 10 -> ?
        this.brain = new HopfieldNeuralNet(genotypeSize);
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
