package se.umu.cs.geneticReanimation;

import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;

public class WormCreature extends AbstractCreature { 
    
    public WormCreature(double[] genotype) {
        super(genotype);
    }
    
    public WormCreature(int genotypeSize) {
        super(genotypeSize);
    }
}
