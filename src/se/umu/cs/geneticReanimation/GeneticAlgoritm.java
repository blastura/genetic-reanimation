package se.umu.cs.geneticReanimation;

import java.util.*;
import net.phys2d.raw.World;

public class GeneticAlgoritm {
    private int populationSize;
    private double crossoverRate;
    private double mutationRate;
    private List<Creature> population;


    public GeneticAlgoritm (int populationSize,
                            double crossoverRate, double mutationRate) {
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    /**
     * Creates a new population
     * @param popSize int for size of the new population
     * @return a List of the new population
     */
    public List<Creature> createPopulation(){
        population = new ArrayList<Creature>();
        for (int i = 1; i<=populationSize; i++) {
            population.add(createNewCreature());
        }
        return population;
    }

    /**
     * Creates a new generation
     *
     * @param oldPopulation List over the old population
     * @return
     */
    public List createNextGeneration(List oldPopulation) {
        this.population = oldPopulation;
        ArrayList newPopulation = new ArrayList<Creature>();

        Creature parent1;
        Creature parent2;
        for (int i = 0; i <= (populationSize*crossoverRate); i+=2) {
            parent1 = tournamentSelection();
            parent2 = tournamentSelection();
            newPopulation.add(crossover(parent1, parent2));
            newPopulation.add(crossover(parent2, parent1));
        }

        for (int i = newPopulation.size(); i < populationSize; i++) {
            parent1 = tournamentSelection();
			Creature newParent = new WormCreature(parent1.getGenotype());
            newPopulation.add(newParent);
        }

        for (int i = 0; i < populationSize; i++) {
            mutate((Creature)newPopulation.get(i));
        }

        population = newPopulation;
		System.out.println(population.size());
        return population;
    }

    private void mutate(Creature creature) {
        double[] genotype = creature.getGenotype();
        for(int i = 0; i<populationSize; i++) {
            if (mutationRate>Math.random()) {
                genotype[i]= genotype[i] + (Math.random()/2 - 0.5);
            }
        }
		creature.setGenotype(genotype);
    }

    private Creature crossover(Creature parent1, Creature parent2) {
        double[] parent2Genotype = parent2.getGenotype();
        int genotypeSize = parent2Genotype.length;
        
        double[] childGenotype = parent1.getGenotype();
        for (int i = genotypeSize/2; i<genotypeSize; i++) {
            childGenotype[i]=parent2Genotype[i];
        }
        Creature child = new WormCreature(childGenotype);
        return child;
    }

    private Creature tournamentSelection() {
        Creature bestParent = null;
        for (int i = 0; i<3; i++) {
            Creature parent = population.get((int)(Math.random()*populationSize));
            if (bestParent == null || parent.getFitness() >= bestParent.getFitness()) {
                bestParent = parent;
            }
        }
        return bestParent;
    }

    private Creature createNewCreature() {
        return new WormCreature();
    }
}
