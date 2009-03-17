package se.umu.cs.geneticReanimation;

import java.io.File;
import net.phys2d.math.MathUtil;
import net.phys2d.math.Matrix2f;
import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.AngleJoint;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.DistanceJoint;
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.Joint;
import net.phys2d.raw.JointList;
import net.phys2d.raw.SlideJoint;
import net.phys2d.raw.SpringJoint;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.Line;
import processing.core.*;

public class ProcessingView extends PApplet {

    private Simulation s;
    private boolean recording = false;
	public double fitness_roevare = 0.0;

    private static File generationFile;
    
    // Default values
    public static int NROFGENERATIONS = 50;
    public static int POPULATIONSIZE = 10;
    public static double CROSSOVERRATE = 0.7;
    public static double MUTATIONRATE = 0.01;
    public static int LIFESPAN = 4000;
    public static boolean RECORDBEST = false;
    public static String MOVIEPATH = "";
    public static boolean SAVE_BEST_TO_FILE = true;

    public static void main(String args[]) {
        parseParameters(args);
        PApplet.main(new String[] {"se.umu.cs.geneticReanimation.ProcessingView"}); // "--present"
    }

    @Override
    public void setup() {
        println("Processing starts...");
        smooth();
        if (generationFile != null) {
            this.s = new Simulation(this, generationFile);
        } else {
            this.s = new Simulation(this);
        }
        new Thread(s).start();
        // Prevent draw from looping
        // Update view with redraw()
        noLoop();
		PFont font = createFont("Helvectica", 12); 
		textFont(font); 
    }

    private static void parseParameters(String[] args) {
        for(String arg : args) {
            if(arg.charAt(0) == '-') {
                // By catching the NumberFormatExceptions we make it possible
                // to check the value of a parameter by just giving the parameter name, ex: -p
                switch(arg.charAt(1)) {
                case 'f':
                    generationFile = new File(arg.substring(2));
                    break;
                case 'n':
                    try { NROFGENERATIONS = argIntVal(arg); } catch(NumberFormatException e) {}
                    System.out.println("Number of generations: " + NROFGENERATIONS);
                    break;
                case 'p':
                    try { POPULATIONSIZE = argIntVal(arg); } catch(NumberFormatException e) {}
                    System.out.println("Population size: " + POPULATIONSIZE);
                    break;
                case 'c':
                    try { CROSSOVERRATE = argDoubleVal(arg); } catch(NumberFormatException e) {}
                    System.out.println("Crossover rate: " + CROSSOVERRATE);
                    break;
                case 'm':
                    try { MUTATIONRATE = argDoubleVal(arg); } catch(NumberFormatException e) {}
                    System.out.println("Mutation rate: " + MUTATIONRATE);
                    break;
                case 'l':
                    try { LIFESPAN = argIntVal(arg); } catch(NumberFormatException e) {}
                    System.out.println("Lifespan: " + LIFESPAN);
                    break;
                case 'r':
                    try { RECORDBEST = argBooleanVal(arg); } catch(NumberFormatException e) {}
                    System.out.println("Record best: " + RECORDBEST);
				case 'v':
					MOVIEPATH = arg.substring(2);
                    System.out.println("Movie path: " + MOVIEPATH);

					
                default:
                }
            }
        }
    }

    private static int argIntVal(String arg) throws NumberFormatException {
        return Integer.parseInt(arg.substring(2));
    }

    private static boolean argBooleanVal(String arg) throws NumberFormatException {
        return (argIntVal(arg) > 0);
    }

    private static double argDoubleVal(String arg) throws NumberFormatException {
        return Double.parseDouble(arg.substring(2));
    }

    public void setRecording(boolean b) {
        this.recording  = b;
    }


    @Override
    public void mousePressed() {
        //         float random1 = (-1 + random(2)) * 100;
        //         float random2  = (-1 + random(2)) * 1000;
        //leftLeg.addForce(new Vector2f(mouseX * 100, mouseY * 100));
        //leftLeg.adjustAngularVelocity(- (mouseX - (width / 2)));
        //println(random1 + ", " + random2);
    }

    @Override
    public void draw() {
        try {
            World world = s.getWorld();

            // Reposition center
            pushMatrix();
            translate(width / 2, 0);

            background(19, 21, 28);

            BodyList bodies = world.getBodies();
            for (int i = 0, length = bodies.size(); i < length; i++) {
                drawBody(bodies.get(i));
            }

            JointList joints = world.getJoints();
            for (int i = 0, length = joints.size(); i < length; i++) {
                drawJoint(joints.get(i));
            }
            
			text(String.valueOf(fitness_roevare), width/2-100, 10);
			
			popMatrix();
            if (recording) {
                s.getMovie().addFrame();
            }
        } catch (IndexOutOfBoundsException e) {

        }
    }


    /** render each physics body based on what shape it is */
    private void drawBody(Body body) {
        if (body.getShape() instanceof Circle) {
            drawCircleBody(body, (Circle)body.getShape());
        } else if (body.getShape() instanceof Line) {
            drawLineBody(body,(Line) body.getShape());
        } else if (body.getShape() instanceof Box) {
            drawBoxBody(body, (Box) body.getShape());
        }
        else {
            throw new IllegalArgumentException("You need a draw method for this shape");
        }
    }

    private void drawLineBody(Body body, Line line) {
        stroke(203, 220, 239);
        strokeWeight(3);
        float x = body.getPosition().getX();
        float y = body.getPosition().getY();
        float dx = line.getDX();
        float dy = line.getDY();
        line((int) x,(int) y,(int) (x+dx),(int) (y+dy));
    }

    /** Draw a circle with a red cross (to see rotation better) */
    private void drawCircleBody(Body body, Circle circle) {
        float r = circle.getRadius();
        float rot = body.getRotation();

        pushMatrix();
        translate(body.getPosition().getX(), body.getPosition().getY());
        rotate(rot);
        //   noStroke();
        fill(255);
        ellipse(0,0,r*2,r*2);
        stroke(255,0,0);
        strokeWeight(2);
        line(-r+4,0,r-4,0);
        line(0,-r+4,0,r-4);
        popMatrix();
    }

    /**
     * TODO: Copied and edited from AbstractDemo
     * Draw a box in the world
     *
     * @param g The graphics contact on which to draw
     * @param body The body to be drawn
     * @param box The shape to be drawn
     */
    protected void drawBoxBody(Body body, Box box) {
        strokeWeight(1);

        if(body.getName().equals("Ground") || body.getName().equals("Wall")) {
            stroke(139, 120, 103);
            fill(50, 40, 40);
        } else {
            stroke(120, 219, 80);
            fill(40, 50, 40);
        }

        Vector2f[] pts = box.getPoints(body.getPosition(), body.getRotation());

        Vector2f v1 = pts[0];
        Vector2f v2 = pts[1];
        Vector2f v3 = pts[2];
        Vector2f v4 = pts[3];

        quad(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, v4.x, v4.y);
        //line((int) v1.x,(int) v1.y,(int) v2.x,(int) v2.y);
        //line((int) v2.x,(int) v2.y,(int) v3.x,(int) v3.y);
        //line((int) v3.x,(int) v3.y,(int) v4.x,(int) v4.y);
        //line((int) v4.x,(int) v4.y,(int) v1.x,(int) v1.y);
    }

    /**
     * TODO: Copied and edited from AbstractDemo
     * Draw a joint
     *
     * @param g The graphics contact on which to draw
     * @param j The joint to be drawn
     */
    public void drawJoint(Joint j) {
        if (j instanceof FixedJoint) {
            FixedJoint joint = (FixedJoint) j;

            stroke(255, 0, 0);
            float x1 = joint.getBody1().getPosition().getX();
            float x2 = joint.getBody2().getPosition().getX();
            float y1 = joint.getBody1().getPosition().getY();
            float y2 = joint.getBody2().getPosition().getY();

            line((int) x1,(int) y1,(int) x2,(int) y2);
        }

        if (j instanceof SlideJoint){
            SlideJoint joint = (SlideJoint) j;

            Body b1 = joint.getBody1();
            Body b2 = joint.getBody2();

            Matrix2f R1 = new Matrix2f(b1.getRotation());
            Matrix2f R2 = new Matrix2f(b2.getRotation());

            ROVector2f x1 = b1.getPosition();
            Vector2f p1 = MathUtil.mul(R1,joint.getAnchor1());
            p1.add(x1);

            ROVector2f x2 = b2.getPosition();
            Vector2f p2 = MathUtil.mul(R2,joint.getAnchor2());
            p2.add(x2);

            Vector2f im = new Vector2f(p2);
            im.sub(p1);
            im.normalise();



            stroke(255, 0, 0);
            line((int)p1.x,(int)p1.y,(int)(p1.x+im.x*joint.getMinDistance()),(int)(p1.y+im.y*joint.getMinDistance()));
            stroke(0, 255, 0);
            line((int)(p1.x+im.x*joint.getMinDistance()),(int)(p1.y+im.y*joint.getMinDistance()),(int)(p1.x+im.x*joint.getMaxDistance()),(int)(p1.y+im.y*joint.getMaxDistance()));
        }

        if (j instanceof AngleJoint){
            AngleJoint angleJoint = (AngleJoint)j;
            Body b1 = angleJoint.getBody1();
            Body b2 = angleJoint.getBody2();
            float RA = j.getBody1().getRotation() + angleJoint.getRotateA();
            float RB = j.getBody1().getRotation() + angleJoint.getRotateB();

            Vector2f VA = new Vector2f((float) Math.cos(RA), (float) Math.sin(RA));
            Vector2f VB = new Vector2f((float) Math.cos(RB), (float) Math.sin(RB));

            Matrix2f R1 = new Matrix2f(b1.getRotation());
            Matrix2f R2 = new Matrix2f(b2.getRotation());

            ROVector2f x1 = b1.getPosition();
            Vector2f p1 = MathUtil.mul(R1,angleJoint.getAnchor1());
            p1.add(x1);

            ROVector2f x2 = b2.getPosition();
            Vector2f p2 = MathUtil.mul(R2,angleJoint.getAnchor2());
            p2.add(x2);

            stroke(0, 255, 0);
            line((int)p1.x,(int)p1.y,(int)(p1.x+VA.x*20),(int)(p1.y+VA.y*20));
            line((int)p1.x,(int)p1.y,(int)(p1.x+VB.x*20),(int)(p1.y+VB.y*20));
        }

        if (j instanceof BasicJoint) {
            BasicJoint joint = (BasicJoint) j;

            Body b1 = joint.getBody1();
            Body b2 = joint.getBody2();

            Matrix2f R1 = new Matrix2f(b1.getRotation());
            Matrix2f R2 = new Matrix2f(b2.getRotation());

            ROVector2f x1 = b1.getPosition();
            Vector2f p1 = MathUtil.mul(R1,joint.getLocalAnchor1());
            p1.add(x1);

            ROVector2f x2 = b2.getPosition();
            Vector2f p2 = MathUtil.mul(R2,joint.getLocalAnchor2());
            p2.add(x2);

            stroke(255, 0, 0);
            line((int) x1.getX(), (int) x1.getY(), (int) p1.x, (int) p1.y);
            line((int) p1.x, (int) p1.y, (int) x2.getX(), (int) x2.getY());
            line((int) x2.getX(), (int) x2.getY(), (int) p2.x, (int) p2.y);
            line((int) p2.x, (int) p2.y, (int) x1.getX(), (int) x1.getY());
        }
        if(j instanceof DistanceJoint){
            DistanceJoint joint = (DistanceJoint) j;

            Body b1 = joint.getBody1();
            Body b2 = joint.getBody2();

            Matrix2f R1 = new Matrix2f(b1.getRotation());
            Matrix2f R2 = new Matrix2f(b2.getRotation());

            ROVector2f x1 = b1.getPosition();
            Vector2f p1 = MathUtil.mul(R1,joint.getAnchor1());
            p1.add(x1);

            ROVector2f x2 = b2.getPosition();
            Vector2f p2 = MathUtil.mul(R2,joint.getAnchor2());
            p2.add(x2);

            stroke(255, 120, 255);
            line((int) p1.getX(), (int) p1.getY(), (int) p2.x, (int) p2.y);
        }
        if (j instanceof SpringJoint) {
            SpringJoint joint = (SpringJoint) j;

            Body b1 = joint.getBody1();
            Body b2 = joint.getBody2();

            Matrix2f R1 = new Matrix2f(b1.getRotation());
            Matrix2f R2 = new Matrix2f(b2.getRotation());

            ROVector2f x1 = b1.getPosition();
            Vector2f p1 = MathUtil.mul(R1,joint.getLocalAnchor1());
            p1.add(x1);

            ROVector2f x2 = b2.getPosition();
            Vector2f p2 = MathUtil.mul(R2,joint.getLocalAnchor2());
            p2.add(x2);

            stroke(255, 0, 0);
            line((int) x1.getX(), (int) x1.getY(), (int) p1.x, (int) p1.y);
            line((int) p1.x, (int) p1.y, (int) p2.getX(), (int) p2.getY());
            line((int) p2.getX(), (int) p2.getY(), (int) x2.getX(), (int) x2.getY());
        }
    }

    public Vector2f getMidPosition(Body b1, Body b2) {
        Vector2f v1 = (Vector2f) b1.getPosition();
        Vector2f v2 = (Vector2f) b2.getPosition();

        // TODO: check calculations for lines where x1 != x2 or y1 != y2
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        return new Vector2f(v1.x - (dx / 2), v1.y - (dy / 2));
    }


}
