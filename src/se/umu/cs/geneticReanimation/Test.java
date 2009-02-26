package se.umu.cs.geneticReanimation;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.Line;
import net.phys2d.raw.strategies.QuadSpaceStrategy;
import processing.core.*;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.AngleJoint;
import net.phys2d.raw.SlideJoint;
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.SpringJoint;
import net.phys2d.math.ROVector2f;
import net.phys2d.raw.DistanceJoint;
import net.phys2d.raw.BasicJoint;
import net.phys2d.math.Matrix2f;
import net.phys2d.math.MathUtil;
import net.phys2d.raw.Joint;
import net.phys2d.raw.JointList;

public class Test extends PApplet {
    private World world = new World(new Vector2f(0.0f, 10.0f), 20, new QuadSpaceStrategy(20,5));
    private Body leftLeg;
    private Body rightLeg;;
    
    public static void main(String args[]) {
        PApplet.main(new String[] {"se.umu.cs.geneticReanimation.Test" }); // "--present"
    }

    @Override
    public void setup() {
        size(100, 300);
        setupWorld();
        smooth();
    }

    public void setupWorld() {
        world.clear();

        //Add static lines
        Body body1;
        body1 = new StaticBody("Ground", new Line(width,0));
        world.add(body1);

        body1 = new StaticBody("Ground", new Line(width,0));
        body1.setPosition(0,height);
        world.add(body1);

        body1 = new StaticBody("Ground", new Line(0,height));
        world.add(body1);

        body1 = new StaticBody("Ground", new Line(0,height));
        body1.setPosition(width,0);
        world.add(body1);

        this.leftLeg = new Body("LeftLeg", new Box(10.0f, 10.0f), 10);
        leftLeg.setPosition(10, 10);
        world.add(leftLeg);
        this.rightLeg = new StaticBody("RightLeg", new Box(10.0f, 10.0f));
        rightLeg.setPosition(25, 10);
        world.add(rightLeg);
        SlideJoint slideHip = new SlideJoint(leftLeg, rightLeg, new Vector2f(0, 0), new Vector2f(0, 0), 0.0f, 45.0f, 0f);
        AngleJoint angleHip = new AngleJoint(leftLeg, rightLeg, new Vector2f(0, 0), new Vector2f(0, 0), 0.0f, 90.0f, 1f);
        
        world.add(slideHip);
        world.add(angleHip);

        //Add free-falling ball
        for (int i=0; i<10; i++) {
            body1 = new Body("Ball", new Circle(3 + random(7)), 10);
            body1.setPosition(width/2 - 20 + random(5), height - i*30);
            world.add(body1);
            body1 = new Body("Ball", new Circle(3 + random(7)), 10);
            body1.setPosition(width/2 + 20 + random(5), height - i*30);
            world.add(body1);
        }
    }
    
    @Override
    public void mousePressed() {
        float random1 = (-1 + random(2)) * 10;
        float random2  = (-1 + random(2)) * 1000;
        //leftLeg.addForce(new Vector2f(random1, random2));
        leftLeg.adjustAngularVelocity(random1);
        println(random1 + ", " + random2);
    }

    @Override
    public void draw() {
        // If you do one step() per draw, it's totally slow-mo.
        // so we are doing 8. Adjust to taste.
        for (int i=0; i<1; i++) world.step();

        background(0);

        BodyList bodies = world.getBodies();
        int s = bodies.size();
        for (int i=0; i<s; i++) {
            drawBody(bodies.get(i));
        }

        JointList joints = world.getJoints();
        for (int i=0;i<joints.size();i++) {
            Joint joint = joints.get(i);
            
            drawJoint(joint);
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
        stroke(255,255,0);
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
        noStroke();
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
        stroke(255,255,0);
        strokeWeight(2);
        
        Vector2f[] pts = box.getPoints(body.getPosition(), body.getRotation());

        Vector2f v1 = pts[0];
        Vector2f v2 = pts[1];
        Vector2f v3 = pts[2];
        Vector2f v4 = pts[3];

        line((int) v1.x,(int) v1.y,(int) v2.x,(int) v2.y);
        line((int) v2.x,(int) v2.y,(int) v3.x,(int) v3.y);
        line((int) v3.x,(int) v3.y,(int) v4.x,(int) v4.y);
        line((int) v4.x,(int) v4.y,(int) v1.x,(int) v1.y);
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
			
            stroke(255, 0, 0);
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
			
            stroke(255, 0, 0);
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
}

