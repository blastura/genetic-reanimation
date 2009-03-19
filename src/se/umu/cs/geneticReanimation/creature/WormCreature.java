package se.umu.cs.geneticReanimation.creature;

import java.util.ArrayList;
import java.util.List;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.DistanceJoint;
import net.phys2d.raw.Joint;
import net.phys2d.raw.SpringyAngleJoint;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;

public class WormCreature implements Creature {
    private final float MAX_ANGLE_STEP = (float)(Math.PI/10);

    private double fitness;
    private Brain brain;
    private List<Body> bodyList;
    private List<Joint> jointList;

    // Effectors
    private BasicJoint[] saj;

    public WormCreature(double[] genotype) {
        this.brain = new HopfieldNeuralNet(genotype);
        initBody();
    }

    public WormCreature() {
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
        this.bodyList = new ArrayList<Body>();
        this.jointList = new ArrayList<Joint>();
        
        // Make schneyk
        float sWidth = 20f;
        float sHeight = 10f;
        float spaceing = 10f;
        int sections = 5;

        saj = new BasicJoint[sections-1];

        Body segment = null;
        Body prev_segment;

        for(int i = 0; i < sections; i++) {
            prev_segment = segment;
            segment = new Body("Segment", new Box(sWidth, sHeight), 1);
            segment.setPosition((sWidth + spaceing)*i-360, 420 - 250);
            bodyList.add(segment);
            if(i > 0) {
                Vector2f fixpoint1 = new Vector2f(sWidth/2f, 0);
                Vector2f fixpoint2 = new Vector2f(-sWidth/2f, 0);
                DistanceJoint dj = new DistanceJoint(prev_segment, segment,
                                                     fixpoint1, fixpoint2, spaceing);
                jointList.add(dj);

                saj[i-1] = new BasicJoint(prev_segment, segment, getMidPosition(prev_segment, segment));
                jointList.add(saj[i-1]);
            }
        }
        segment.adjustAngularVelocity(0.5f);
    }

    public void act() {
        double[] inputs = new double[bodyList.size()-1];
        for (int i = 0, length = inputs.length; i < length; i++) {
            float b1 = bodyList.get(i).getPosition().getY();
            float b2 = bodyList.get(i+1).getPosition().getY();
            inputs[i] = b1-b2;
        }

        brain.setInputs(inputs);
        brain.step();
        brain.step();
        double[] outputs = brain.getOutputs();

        // Affect worm
        for (int i = 0, length = outputs.length; i < length && i < bodyList.size(); i++) {
            float goal = (float)(outputs[i] * (Math.PI / 2));
            bodyList.get(i).adjustAngularVelocity(goal);
        }
    }

    public double getFitness() {
        return this.fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double[] getGenotype() {
        return brain.getGenotype();
    }

    public void setGenotype(double[] genotype) {
        // REPLACE BRAIN! MUAWHAHA
        this.brain = new HopfieldNeuralNet(genotype);
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

    @Override
    public Creature clone() {
        try {
            WormCreature result = (WormCreature) super.clone();
            result.initBody();

            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new Error("Object " + this.getClass().getName()
                            + " is not Cloneable");
        }
    }
}
