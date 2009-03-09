package se.umu.cs.geneticReanimation;

public interface Creature extends Cloneable {
    public double getFitness();

    public void setFitness(double fitness);

    public double[] getGenotype();
}
