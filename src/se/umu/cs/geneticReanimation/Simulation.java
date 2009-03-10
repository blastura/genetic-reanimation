package se.umu.cs.geneticReanimation;

import java.util.List;

import processing.core.PApplet;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.strategies.QuadSpaceStrategy;

public class Simulation implements Runnable {
	
    // Constants
	private final int NROFGENERATIONS = 1;
    private final int POPULATIONSIZE = 5;
    private final double CROSSOVERRATE = 0;
    private final double MUTATIONRATE = 0;
    private final int LIFESPAN = 1000;
    
    private ProcessingView view;
    private World world;
    private GeneticAlgoritm ga;
    private List<Creature> population;
    private Creature currentCreature;
    
	public Simulation(ProcessingView a) {
		view = a;
		setupWorld();
		
		// Setup simulation
		this.ga = new GeneticAlgoritm(POPULATIONSIZE, CROSSOVERRATE, MUTATIONRATE);
		this.population = this.ga.createPopulation();
	}

	
    private void setupWorld() {
        this.world = new World(new Vector2f(0.0f, 10.0f), 20, new QuadSpaceStrategy(20,5));
        view.size(1600 / 2, 1000 / 2);
        world.clear();
        addGround();
    }
    
    private void addGround() {
        //Add ground
        Body body1;
        body1 = new StaticBody("Ground", new Box(view.width * 10, 100));
        body1.setPosition(view.width / 2, view.height - 10);
        world.add(body1);    	
    }

	public void run() {
		for (int i = 0; i < NROFGENERATIONS; i++) {
			System.out.println("Generation " + (i+1) + " is starting...");
			for (Creature creature : population) {
				creature.connectToWorld(world);
				simulate(creature);
				calculateFitness(creature);
			}
			System.out.println("Generation " + (i+1) + " is done.");
		}
		System.out.println("Simulation ended.");
	}
	
	public World getWorld() {
		return this.world;
	}
	
	private void calculateFitness(Creature creature) {
		// TODO Auto-generated method stub
		
	}

	private void simulate(Creature creature) {
        // If you do one step() per draw, it's totally slow-mo.
        // so we are doing 8. Adjust to taste.
        for (int step=0; step < LIFESPAN; step++) {
        	world.step();
        	creature.act();
        }
        System.out.println("Life ended...");
	}
}
