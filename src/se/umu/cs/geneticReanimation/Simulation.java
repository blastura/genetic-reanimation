package se.umu.cs.geneticReanimation;

import java.io.File;
import java.util.List;

//import com.sun.tools.javac.tree.Tree.Synchronized;

import processing.video.MovieMaker;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.strategies.QuadSpaceStrategy;

public class Simulation implements Runnable {

    private int NROFGENERATIONS;
    private int POPULATIONSIZE;
    private double CROSSOVERRATE;
    private double MUTATIONRATE;
    private int LIFESPAN;
    
	private String MOVIEPATH = "";

    private final boolean DRAW_GUI = true;
    private final int FPS = 600;
    
    private ProcessingView view;
    private World world;
    private GeneticAlgoritm ga;
    private List<Creature> population;
	private MovieMaker movie;

    public Simulation(ProcessingView view, int n, int p, double c, double m, int l) {
		NROFGENERATIONS = n;
		POPULATIONSIZE = p;
		CROSSOVERRATE = c;
		MUTATIONRATE = m;
		LIFESPAN = l;

        this.view = view;
        setupWorld();

        // Setup simulation
        this.ga = new GeneticAlgoritm(POPULATIONSIZE, CROSSOVERRATE, MUTATIONRATE);
        this.population = this.ga.createPopulation();
    }

    private void setupWorld() {
        this.world = new World(new Vector2f(0.0f, 10.0f),
                               20, new QuadSpaceStrategy(20,5));
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

		body1 = new StaticBody("Wall", new Box(20, 300));
		body1.setPosition(-view.width/2, view.height-210);
		world.add(body1);
	}

	public void run() {
		for (int i = 0; i < NROFGENERATIONS; i++) {
			System.out.println("Generation " + (i+1) + " is starting...");
			for (Creature creature : population) {
				creature.connectToWorld(world);
				simulate(creature);
				calculateFitness(creature);

				// Reset world
				world.clear();
				addGround();
			}
			
			// Record the best one
			//recordBest(i);
			
			System.out.println("Generation " + (i+1) + " is done.");
			this.population = ga.createNextGeneration(this.population);
		}
		System.out.println("Simulation ended.");
	}

	/**
	 * Finds the best creature of the current generation and 
	 * records a simulation with it 
	 * @param generation The index of the generation
	 */
	private void recordBest(int generation) {
		// Find the best
		Creature bestCreature = population.get(0);
		double bestFitness = bestCreature.getFitness();
		for (Creature creature : population) {
			if (bestFitness < creature.getFitness()) {
				bestCreature = creature;
				bestFitness = bestCreature.getFitness();
			}
		}
		
		String filename = "gen(" + generation + ")_fit(" + (int) bestCreature.getFitness() + ")";
		bestCreature.connectToWorld(world);
		recordMovie(filename);
		simulate(bestCreature);
		stopMovie();
	}

	public World getWorld() {
		return this.world;
	}

	private void calculateFitness(Creature creature) {
		double fitness = creature.getXPosition()-120+360; // -worm length + worm startpos
		System.out.println("Fitness: " + fitness);
		creature.setFitness(fitness);
	}

	private void simulate(Creature creature) {
		System.out.println("Simulating: " + encode(creature.getGenotype()));
		for (int step=0; step < LIFESPAN; step++) {
			world.step();
			creature.act();
			if (DRAW_GUI) {
				try {
					long waitTime = 1000 / FPS;
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					System.err.println("Simulate sleep interrupted");
				}
				view.redraw();                
			}
		}
	}

	private String encode(double[] genotype) {
		String s = "";
		for(double d : genotype) {
			s += String.valueOf(d) + " ";
		}
		return s;
	}

	private void recordMovie(String filename) {
		String fullname = MOVIEPATH + filename + ".mov";

		// Check if file exists 
		File file = new File(fullname);
		if (file.exists()) {
			file.delete();
		}

//		this.movie = new MovieMaker(view, view.width, view.height, fullname, 20);
		
		 // Or, set specific compression and frame rate options
		  this.movie = new MovieMaker(view, view.width, view.height, fullname, 30, 
		                      MovieMaker.ANIMATION, MovieMaker.HIGH);
		
		
		view.setRecording(true);
		System.out.println("Recording movie: >" + filename + ".mov<...");
		}
	
	private void stopMovie() {
		view.setRecording(false);
		this.movie.finish();
		System.out.println("Recording ended.");
	}
	
	public MovieMaker getMovie() {
		return this.movie;
	}

}
