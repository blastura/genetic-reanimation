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

public class Test extends PApplet {

	private World world = new World(new Vector2f(0.0f, 10.0f), 20, new QuadSpaceStrategy(20,5));

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "Test" });
	}


	public void setup() {
		size(100,300);
		setupWorld();
		smooth();
	}

	void setupWorld() {
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

	public void draw() { 
		  // If you do one step() per draw, it's totally slow-mo.
		  // so we are doing 8. Adjust to taste.
		  for (int i=0; i<1; i++) world.step();
		 
		  background(0);
		 
		  BodyList bodies = world.getBodies();
		  int s = bodies.size();
		  for (int i=0; i<s; i++) {
		    drawBody((Body)bodies.get(i));
		  }
		}

	/** render each physics body based on what shape it is */
	void drawBody(Body body) {
	  if (body.getShape() instanceof Circle) {
	     drawCircleBody(body, (Circle)body.getShape());
	  } else if (body.getShape() instanceof Line) {
	    drawLineBody(body,(Line) body.getShape());
	  } else {
	    throw new IllegalArgumentException(
	      "You need a draw method for this shape"); 
	  }
	}

	void drawLineBody(Body body, Line line) {
	    stroke(255,255,0);
	    strokeWeight(3);
	    float x = body.getPosition().getX();
	    float y = body.getPosition().getY();
	    float dx = line.getDX();
	    float dy = line.getDY();
	    line((int) x,(int) y,(int) (x+dx),(int) (y+dy));
	}
	
	/** Draw a circle with a red cross (to see rotation better) */
	void drawCircleBody(Body body, Circle circle) {
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

}

