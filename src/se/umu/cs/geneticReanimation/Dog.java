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
		// Make dawg
		hipLeft = new Body("", new Circle(r), 10);
		hipLeft.setPosition(0, 0);
		world.add(hipLeft);

		this.thighLeft = new Body("thighLeft", new Box(legWidth, legHeight), 10);
		thighLeft.setPosition(0, r + legHeight / 2 + spaceing);
		world.add(thighLeft);

		Body thighLeft2 = new Body("thighLeft2", new Box(legWidth, legHeight), 10);
		thighLeft2.setPosition(0, r + (legHeight / 2) + legHeight + spaceing * 2);
		world.add(thighLeft2);

		Body rightHip = new Body("", new Circle(r), 10);
		rightHip.setPosition(bodyLength, 0);
		world.add(rightHip);

		this.thighRight = new Body("thighRight", new Box(legWidth, legHeight), 10);
		thighRight.setPosition(bodyLength, r + legHeight / 2 + spaceing);
		world.add(thighRight);

		Body thighRight2 = new Body("thighRight2", new Box(legWidth, legHeight), 10);
		thighRight2.setPosition(bodyLength, r + (legHeight / 2) + legHeight + spaceing * 2);
		world.add(thighRight2);

		BasicJoint hipLeftJoint = new BasicJoint(hipLeft, thighLeft,
				(Vector2f) hipLeft.getPosition());
		world.add(hipLeftJoint);

		BasicJoint leftKneeJoint = new BasicJoint(thighLeft, thighLeft2,
				getMidPosition(thighLeft, thighLeft2));
		world.add(leftKneeJoint);

		AngleJoint leftKneeAngleJoint = new AngleJoint(thighLeft, thighLeft2,
				new Vector2f(),
				new Vector2f(),
				(float) Math.PI / 2,
				0f);
		world.add(leftKneeAngleJoint);


		BasicJoint rightHipJoint = new BasicJoint(rightHip, thighRight,
				(Vector2f) rightHip.getPosition());
		world.add(rightHipJoint);        

		BasicJoint rightKneeJoint = new BasicJoint(thighRight, thighRight2,
				getMidPosition(thighRight, thighRight2));

		world.add(rightKneeJoint);

		// Connect hips
		FixedJoint backbone = new FixedJoint(hipLeft, rightHip);
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
