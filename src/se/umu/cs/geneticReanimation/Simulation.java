package se.umu.cs.geneticReanimation;

import java.util.List;

import net.phys2d.raw.World;

public class Simulation implements Runnable{
	
    // Constants
	private final int NROFGENERATIONS = 0;
    private final int POPULATIONSIZE = 0;
    private final double CROSSOVERRATE = 0;
    private final double MUTATIONRATE = 0;
    
    private World world;
    private GeneticAlgoritm ga;
    private List<Creature> population;
    private Creature currentCreature;
    
	public Simulation(World world) {
		this.world = world;
		this.ga = new GeneticAlgoritm(world, POPULATIONSIZE, CROSSOVERRATE, MUTATIONRATE);
		this.population = this.ga.createPopulation();
	}

	public void run() {
		for (int i = 0; i < NROFGENERATIONS; i++) {
			for (Creature creature : population) {
				simulate(creature);
				calculateFitness(creature);
			}
		}
		
	}
	
	private void calculateFitness(Creature creature) {
		// TODO Auto-generated method stub
		
	}

	private void simulate(Creature creature) {
		creature.act();
	}
}
