package se.umu.cs.geneticReanimation;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.AngleJoint;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.Body;
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

public class Dog {

	/* Constants */ 
	float r = 20f;
	float bodyLength = 45f;
	float legWidth = 5f;
	float legHeight = 20f;
	float spaceing = 10f;
	float mass_hip = 10;
	float mass_thigh = 2f;
	float mass_leg = 2f;


	/* World */
	private World world;

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

	public Dog(World world) {
		this.world = world;
		initDog();
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
