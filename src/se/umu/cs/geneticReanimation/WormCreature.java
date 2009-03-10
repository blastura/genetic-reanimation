package se.umu.cs.geneticReanimation;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.AngleJoint;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.Body;
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

import se.umu.cs.geneticReanimation.neuralnet.Brain;
import se.umu.cs.geneticReanimation.neuralnet.HopfieldNeuralNet;

public class WormCreature implements Creature {
    private double fitness;
    private Brain brain;
    private World world;

    /* Constants */
    private float r = 20f;
    private float bodyLength = 45f;
    private float legWidth = 5f;
    private float legHeight = 20f;
    private float spaceing = 10f;
    private float mass_hip = 10;
    private float mass_thigh = 2f;
    private float mass_leg = 2f;

    /* Body parts */
    // Hip
    private Body hipLeft;
    private Body hipRight;
    // Thigh
    private Body thighLeft;
    private Body thighRight;
    // Leg
    private Body legLeft;
    private Body legRight;
    // Paw
    private Body pawLeft;
    private Body pawRight;


    public WormCreature(World world, double[] genotype) {
        this.world = world;
        this.brain = new HopfieldNeuralNet(genotype);
    }

    public WormCreature(World world) {
        this.world = world;

        // TODO: decide from input output
        int genotypeSize = 5 * 5;
        double[] newGenotype = new double[genotypeSize];
        for (int i = 0; i<genotypeSize; i++) {
            newGenotype[i] = ((Math.random()*2)-1);
        }
        this.brain = new HopfieldNeuralNet(newGenotype);
        initDog();
    }
    
    public void act() {
        double[] inputs = {hipRight.getPosition().getY(),
                           hipLeft.getPosition().getY(),
                           legLeft.getPosition().getX(),                           
                           legRight.getPosition().getY()};
        brain.setInputs(inputs);
        brain.step();
        brain.step();
        double[] output = brain.getOutputs();
        
        // Affect worm
        legLeft.adjustAngularVelocity((float) output[2] * 2);
        legRight.adjustAngularVelocity((float) output[2] * 2);
    }
    
    public double getFitness() {
        this.fitness = getMidPosition(hipRight, hipLeft).getX();
        return this.fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double[] getGenotype() {
        return brain.getGenotype();
    }

    private void initDog() {
        /* Make bodyparts */
        hipLeft = new Body("", new Circle(r), 10);
        hipLeft.setPosition(0, 0);
        world.add(hipLeft);

        this.thighLeft = new Body("thighLeft", new Box(legWidth, legHeight), 10);
        thighLeft.setPosition(0, r + legHeight / 2 + spaceing);
        world.add(thighLeft);

        legLeft = new Body("legLeft", new Box(legWidth, legHeight), 10);
        legLeft.setPosition(0, r + (legHeight / 2) + legHeight + spaceing * 2);
        world.add(legLeft);

        hipRight = new Body("", new Circle(r), 10);
        hipRight.setPosition(bodyLength, 0);
        world.add(hipRight);

        thighRight = new Body("thighRight", new Box(legWidth, legHeight), 10);
        thighRight.setPosition(bodyLength, r + legHeight / 2 + spaceing);
        world.add(thighRight);

        legRight = new Body("thighRight2", new Box(legWidth, legHeight), 10);
        legRight.setPosition(bodyLength, r + (legHeight / 2) + legHeight + spaceing * 2);
        world.add(legRight);

        /* Joints */
        BasicJoint hipLeftJoint = new BasicJoint(hipLeft, thighLeft,
                                                 (Vector2f) hipLeft.getPosition());
        world.add(hipLeftJoint);

        BasicJoint leftKneeJoint = new BasicJoint(thighLeft, legLeft,
                                                  getMidPosition(thighLeft, legLeft));
        world.add(leftKneeJoint);

        AngleJoint leftKneeAngleJoint = new AngleJoint(thighLeft, legLeft,
                                                       new Vector2f(),
                                                       new Vector2f(),
                                                       (float) Math.PI / 2,
                                                       0f);
        world.add(leftKneeAngleJoint);

        BasicJoint hipRightJoint = new BasicJoint(hipRight, thighRight,
                                                  (Vector2f) hipRight.getPosition());
        world.add(hipRightJoint);

        BasicJoint rightKneeJoint = new BasicJoint(thighRight, legRight,
                                                   getMidPosition(thighRight, legRight));

        world.add(rightKneeJoint);

        // Connect hips
        FixedJoint backbone = new FixedJoint(hipLeft, hipRight);
        world.add(backbone);
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
