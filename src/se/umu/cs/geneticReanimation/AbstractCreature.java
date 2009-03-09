package se.umu.cs.geneticReanimation;

import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;

public class AbstractCreature implements Creature { 
    private double fitness;
    private Brain brain;

    public AbstractCreature(double[] genotype) {
        this.brain = new HopfieldNeuralNet(genotype);
    }
    
    public AbstractCreature(int genotypeSize) {
        // TODO: 10 -> ?
        this.brain = new HopfieldNeuralNet(genotypeSize);
    }
    
    
    public double getFitness() {
        return this.fitness;
    }
    
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    public double[] getGenotype() {
        return brain.getGenotype();
    }
}
