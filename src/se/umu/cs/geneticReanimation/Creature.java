package se.umu.cs.geneticReanimation;

public class Creature {
	private double fitness;
	private Brain brain;
	//private List<Joint> effectors;
	
	public Creature(double[] genotype) {
		brain = new Brain(genotype);
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
