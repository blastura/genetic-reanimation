package se.umu.cs.geneticReanimation.neuralnet;

public interface Brain {
    public double[] getGenotype();
    public void setGenotype(final double[] genotype);
    public void setInputs(final double[] inputs);
    public double[] getOutputs();
    
    /**
     * Make brain think one step. Neural nets process all input from neighbours.
     */
    public void step();
}