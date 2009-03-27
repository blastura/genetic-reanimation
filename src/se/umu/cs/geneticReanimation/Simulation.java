package se.umu.cs.geneticReanimation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.strategies.QuadSpaceStrategy;

import processing.video.MovieMaker;

import se.umu.cs.geneticReanimation.creature.Creature;
import se.umu.cs.geneticReanimation.creature.WormCreature;

public class Simulation implements Runnable {

    private final boolean DRAW_GUI = true;
    private final int FPS = 60;

    private ProcessingView view;
    private World world;
    private GeneticAlgoritm ga;
    private List<Creature> population;
    private MovieMaker movie;

    public Simulation(ProcessingView view, File generationFile) {
        try {
            this.population = createPopulationFromFile(generationFile);
            this.view = view;
            initWorld();

            // Setup simulation
            this.ga = new GeneticAlgoritm(ProcessingView.CROSSOVERRATE,
                                          ProcessingView.MUTATIONRATE);
        } catch (FileNotFoundException e) {
            // TODO - fix error message
            e.printStackTrace();
        }
    }

    public Simulation(ProcessingView view) {
        this.view = view;
        initWorld();

        // Setup simulation
        this.ga = new GeneticAlgoritm(ProcessingView.CROSSOVERRATE,
                                      ProcessingView.MUTATIONRATE);
        this.population = this.ga.createPopulation(ProcessingView.POPULATIONSIZE);
    }

    /**
     * Initializes the world, sets size of frame and creates a Phys2d World.
     */
    private void initWorld() {
        this.world = new World(new Vector2f(0.0f, 10.0f),
                               20, new QuadSpaceStrategy(20,5));
        view.size(1600 / 2, 1000 / 2);
        resetWorld();
    }

    /**
     * Resets the world to a starting state. Clear world, adds a ground and a
     * wall.
     */
    private void resetWorld() {
        this.world.clear();

        //Add ground
        Body body;
        body = new StaticBody("Ground", new Box(view.width * 10, 100));
        body.setPosition(view.width / 2, view.height - 10);
        this.world.add(body);

        // Add left wall
        body = new StaticBody("Wall", new Box(20, 300));
        body.setPosition(-view.width/2, view.height-210);
        this.world.add(body);
    }

    /**
     * Start the Simulation Thread. Runs a loop for every Generation, specified
     * by ProcessingView.NROFGENERATIONS, which creates a population for every
     * generation and simulates every individual in that population.
     *
     * TODO: implements
     */
    public void run() {
        for (int i = 0; i < ProcessingView.NROFGENERATIONS; i++) {
            //System.out.println("Generation " + (i+1) + " is starting...");
            for (Creature creature : population) {
                creature.connectToWorld(world);
                simulate(creature);
                calculateFitness(creature);

                resetWorld();
            }

            // Record the best one
            if (ProcessingView.RECORDBEST) { recordBest(i); }
            if (ProcessingView.SAVE_POP_TO_FILE) { savePopulation(population, i); }

            Creature bestCreature = population.get(0);
            double bestFitness = bestCreature.getFitness();
            double minFitness = bestFitness;
            double sum = 0;
            int amount = 0;
            for (Creature creature : population) {
                if(!Double.valueOf(creature.getFitness()).isNaN()) {
                    sum += creature.getFitness();
                    amount++;
                }
                if(minFitness > creature.getFitness()) {
                    minFitness = creature.getFitness();
                }
                if (bestFitness < creature.getFitness()) {
                    bestCreature = creature;
                    bestFitness = bestCreature.getFitness();
                }
            }

            System.out.println("Medel:" + sum/amount);
            System.out.println("Max:" + bestFitness);
            System.out.println("Min:" + minFitness);


            //System.out.println("Generation " + (i+1) + " is done.");
            this.population = ga.createNextGeneration(this.population);
        }
        //System.out.println("Simulation ended.");
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

        Creature newBestCreature = new WormCreature(bestCreature.getGenotype());
        newBestCreature.connectToWorld(world);
        recordMovie(filename);
        simulate(newBestCreature, true);
        stopMovie();
        resetWorld();
    }

    private void savePopulation(List<Creature> population) {
        savePopulation(population, -1);
    }

    private void savePopulation(List<Creature> population, int generation) {
        try {
            File outFile = new File(ProcessingView.MOVIEPATH + "generation-"
                                    + generation + ".txt");
            FileOutputStream out = new FileOutputStream(outFile);
            PrintStream p = new PrintStream(out);
            // TODO: print stuff to p
            p.println("#   populationeSize");
            p.println("#   genotypeSize");
            p.println("# * fitness genotype");
            p.println(population.size());
            p.println(population.get(0).getGenotype().length);

            for (Creature creature : population) {
                // Requires: Fitness to be calculated
                p.print(creature.getFitness() + " ");
                for (double val : creature.getGenotype()) {
                    p.print(val + " ");
                }
                p.println();
            }
        } catch (FileNotFoundException e) {
            // TODO - fix error message
            e.printStackTrace();
        }
    }

    public World getWorld() {
        return this.world;
    }

    private void calculateFitness(Creature creature) {
        double fitness = creature.getXPosition()-120+360; // -worm length + worm startpos
        //System.out.println("Fitness: " + fitness);
        creature.setFitness(fitness);
    }

    private void simulate(Creature creature) {
        simulate(creature, false);
    }

    private void simulate(Creature creature, boolean force_gui) {
        //System.out.println("Simulating: " + encode(creature.getGenotype()));
        for (int step = 0; step < ProcessingView.LIFESPAN / 8; step++) {

            // Simulate world and createure more times than framerate, to avoid
            // totaly slow-mo
            for (int i = 0; i < 8; i++) {
                world.step();
                creature.act();
            }
            view.fitness_roevare = (creature.getXPosition()-120.0+360.0);

            if (DRAW_GUI || force_gui) {
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
        String fullname = view.MOVIEPATH + filename + ".mov";

        // Check if file exists
        File file = new File(fullname);
        if (file.exists()) {
            file.delete();
        }

        //     this.movie = new MovieMaker(view, view.width, view.height, fullname, 20);

        // Or, set specific compression and frame rate options
        int movieFps = 30;
        this.movie = new MovieMaker(view, view.width, view.height, fullname, movieFps,
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

    public List<Creature> createPopulationFromFile(File generationFile)
                throws FileNotFoundException {
        System.err.println("TODO: implement");
        System.out.println("File: " + generationFile.getPath());
        Scanner s = new Scanner(generationFile);
        s.nextLine(); // Comments
        s.nextLine(); // Comments
        s.nextLine(); // Comments
        int populationSize = s.nextInt();
        int genSize = s.nextInt();

        System.out.println("size " + genSize);
        List<Creature> result = new ArrayList<Creature>(populationSize);

        while (s.hasNextDouble()) {
            double fitness = s.nextDouble();
            double[] genotype = new double[genSize];

            for (int i = 0; i < genSize; i++) {
                double val = s.nextDouble();
                genotype[i] = val;
                System.out.print(val + " ");
            }
            // One genome is fetched
            result.add(new WormCreature(genotype));
            System.out.println("done");
        }
        System.out.println("Population: " + result);
        return result;
    }
}
