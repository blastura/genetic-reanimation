package se.umu.cs.geneticReanimation;

import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;
import net.phys2d.raw.World;

public class AbstractCreature implements Creature {
    private double fitness;
    private Brain brain;
    private World world;

    public AbstractCreature(World world, double[] genotype) {
        this.world = world;
        this.brain = new HopfieldNeuralNet(genotype);
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
