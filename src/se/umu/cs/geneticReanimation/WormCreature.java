package se.umu.cs.geneticReanimation;

import net.phys2d.raw.World;
import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;

public class WormCreature implements Creature {
    private double fitness;
    private Brain brain;
    private World world;

    public WormCreature(World world, double[] genotype) {
        this.world = world;
        this.brain = new HopfieldNeuralNet(genotype);
    }

    public WormCreature(World world) {
        this.world = world;
        
        // TODO: decide from input output
        int genotypeSize = 5 * 5;
        double[] newGenotype = new double[genotypeSize];
        for (int i = 0; i<genotypeSize; i++) {
            newGenotype[i] = ((Math.random()*2)-1);
        }
        this.brain = new HopfieldNeuralNet(newGenotype);
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
