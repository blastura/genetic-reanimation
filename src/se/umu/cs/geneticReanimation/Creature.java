package se.umu.cs.geneticReanimation;

import net.phys2d.raw.World;

// Extend Clonable?
public interface Creature {
    public void connectToWorld(World world);
    public void act();
    public double getFitness();
    public void setFitness(double fitness);
	public double getXPosition();
    public double[] getGenotype();
	public void setGenotype(double[] genotype);
}
