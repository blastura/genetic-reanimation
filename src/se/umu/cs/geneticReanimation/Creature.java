package se.umu.cs.geneticReanimation;

// Extend Clonable?
public interface Creature {
    public double getFitness();

    public void setFitness(double fitness);

    public double[] getGenotype();
}
