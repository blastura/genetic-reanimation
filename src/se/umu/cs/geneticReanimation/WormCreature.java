package se.umu.cs.geneticReanimation;

import java.util.ArrayList;
import java.util.List;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.DistanceJoint;
import net.phys2d.raw.Joint;
import net.phys2d.raw.SpringyAngleJoint;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;

public class WormCreature implements Creature {
    private double fitness;
    private Brain brain;
    private List<Body> bodyList;
    private List<Joint> jointList;
    // Effectors
    private SpringyAngleJoint[] saj;
    
    public WormCreature(double[] genotype) {
        this.brain = new HopfieldNeuralNet(genotype);
        this.bodyList = new ArrayList<Body>();
        this.jointList = new ArrayList<Joint>();
        initBody();
    }
    
    public WormCreature() {
        // TODO: decide from input output
        this.bodyList = new ArrayList<Body>();
        this.jointList = new ArrayList<Joint>();
        initBody();
        
        int genotypeSize = (int) Math.pow(bodyList.size(), 2); //saj.length * saj.length;
        double[] newGenotype = new double[genotypeSize];
        for (int i = 0; i<genotypeSize; i++) {
            newGenotype[i] = ((Math.random()*2)-1);
        }
        this.brain = new HopfieldNeuralNet(newGenotype);
    }
    
    public void connectToWorld(World world) {
        for (Body b : bodyList) {
            world.add(b);
        }
        for (Joint j : jointList) {
            world.add(j);
        }        
    }

    private void initBody() {
        // Make schneyk
        float sWidth = 20f;
        float sHeight = 10f;
        float spaceing = 10f;
        int sections = 5;

        saj = new SpringyAngleJoint[sections-1];
        
        Body segment = null;
        Body prev_segment;

        for(int i = 0; i < sections; i++) {
            prev_segment = segment;
            segment = new Body("Segment", new Box(sWidth, sHeight), 1);
            segment.setPosition((sWidth + spaceing)*i, 420);
            bodyList.add(segment);
            if(i > 0) {
                Vector2f fixpoint1 = new Vector2f(sWidth/2f, 0);
                Vector2f fixpoint2 = new Vector2f(-sWidth/2f, 0);
                DistanceJoint dj = new DistanceJoint(prev_segment, segment,
                                                     fixpoint1, fixpoint2, spaceing);
                jointList.add(dj);

                saj[i-1] = new SpringyAngleJoint(prev_segment, segment,
                                                 fixpoint1, fixpoint2, 1000f, 0f);
                jointList.add(saj[i-1]);
            }
        }
    }
    
    public void act() {
        double[] inputs = new double[bodyList.size()];
        for (int i = 0, length = inputs.length; i < length; i++) {
            inputs[i] = bodyList.get(i).getPosition().getY();
        }

        brain.setInputs(inputs);
        brain.step();
        brain.step();
        double[] outputs = brain.getOutputs();
        
        // Affect worm
        for (int i = 0, length = outputs.length; i < length && i < saj.length; i++) {
            // TODO: jointList out of bounds, warn or check?
            saj[i].setOriginalAngle((float) ((outputs[i] * Math.PI)));
        }
    }
    
    public double getFitness() {
        //this.fitness = getMidPosition(hipRight, hipLeft).getX();
        return this.fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double[] getGenotype() {
        return brain.getGenotype();
    }

	public double getXPosition() {
		return (double)bodyList.get(bodyList.size()-1).getPosition().getX();
	}

    private Vector2f getMidPosition(Body b1, Body b2) {
        Vector2f v1 = (Vector2f) b1.getPosition();
        Vector2f v2 = (Vector2f) b2.getPosition();

        // TODO: check calculations for lines where x1 != x2 or y1 != y2
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        return new Vector2f(v1.x - (dx / 2), v1.y - (dy / 2));
    }
}
