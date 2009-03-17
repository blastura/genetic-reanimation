package se.umu.cs.geneticReanimation;

import java.util.*;
import se.umu.cs.geneticReanimation.creature.Creature;
import se.umu.cs.geneticReanimation.creature.WormCreature;

public class GeneticAlgoritm {
    // private static int populationSize;
    private static double crossoverRate;
    private static double mutationRate;
    //private List<Creature> population;
    
    public GeneticAlgoritm (double crossoverRate,
                            double mutationRate) {
        GeneticAlgoritm.crossoverRate = crossoverRate;
        GeneticAlgoritm.mutationRate = mutationRate;
    }

    /**
     * Creates a new population
     * @param popSize int for size of the new population
     * @return a List of the new population
     */
    public List<Creature> createPopulation(final int populationSize) {
        List<Creature> population = new ArrayList<Creature>();
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
    public final List<Creature> createNextGeneration(final List<Creature> oldPopulation) {
        int populationSize = oldPopulation.size();
        ArrayList<Creature> newPopulation = new ArrayList<Creature>();
        
        Creature bestCreature = oldPopulation.get(0);
        double bestFitness = bestCreature.getFitness();
        for (Creature creature : oldPopulation) {
            if (bestFitness < creature.getFitness()) {
                bestCreature = creature;
                bestFitness = bestCreature.getFitness();
            }
        }
        newPopulation.add(bestCreature);
        

        Creature parent1;
        Creature parent2;
        for (int i = 1; i <= (populationSize * crossoverRate); i += 2) {
            parent1 = tournamentSelection(oldPopulation);
            parent2 = tournamentSelection(oldPopulation);
            newPopulation.add(crossover(parent1, parent2));
            newPopulation.add(crossover(parent2, parent1));
        }

        for (int i = newPopulation.size(); i < populationSize; i++) {
            parent1 = tournamentSelection(oldPopulation);
            Creature newParent = new WormCreature(parent1.getGenotype());
            newPopulation.add(newParent);
        }

        for (int i = 0; i < populationSize; i++) {
            mutate(newPopulation.get(i));
        }

        return newPopulation;
    }

    private static Creature mutate(Creature creature) {
        double[] genotype = creature.getGenotype();
        for (int i = 0, length = genotype.length; i < length; i++) {
            if (mutationRate > Math.random()) {
                genotype[i] = (Math.random() * 2 - 1);
            }
        }
        creature.setGenotype(genotype);
        return creature;
    }

    private static Creature crossover(Creature parent1, Creature parent2) {
        double[] parent2Genotype = parent2.getGenotype();
        int genotypeSize = parent2Genotype.length;

        double[] childGenotype = parent1.getGenotype();
        for (int i = (int)(Math.random()*genotypeSize); i<genotypeSize; i++) {
            childGenotype[i]=parent2Genotype[i];
        }
        Creature child = new WormCreature(childGenotype);
        return child;
    }

    private static Creature tournamentSelection(final List<Creature> population) {
        int populationSize = population.size();
        Creature bestParent = null;
        for (int i = 0; i<3; i++) {
            Creature parent = population.get((int) (Math.random() * populationSize));
            if (bestParent == null || parent.getFitness() >= bestParent.getFitness()) {
                bestParent = parent;
            }
        }
        return bestParent;
    }

    private static Creature createNewCreature() {
        return new WormCreature();
    }
}
