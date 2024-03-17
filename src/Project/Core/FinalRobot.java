package Project.Core;

import java.util.Random;

public class FinalRobot {
	private int intelligence, skill, speed;
	private int leftArm, rightArm; 
	private int leftLeg, rightLeg; 
	
	public FinalRobot(Random rng) {
		Cube[] bodyParts = new Cube[14];
		
		// Construct all 14 randomly so we get a random robot.
		for (int i = 0; i < bodyParts.length; i++) {
			bodyParts[i] = new Cube(rng, 30, 99);
		}
		
		constructRobot(bodyParts);
	}
	
	public FinalRobot(Cube[] bodyParts) {
		constructRobot(bodyParts);
	}
	
	// Calculates the robot's statistics when body parts are given.
	private void constructRobot(Cube[] bodyParts) {
		bodyParts = fixBodyParts(bodyParts);
		
		// Intelligence = Sum of first 4 (body).
		for (int i = 0; i < 4; i++) {
			intelligence += bodyParts[i].yForce();			
		}
		
		// Skill = (LA + RA) / ArmBalance
		leftArm = bodyParts[4].xForce() + bodyParts[5].xForce();
		rightArm = bodyParts[6].xForce() + bodyParts[7].xForce();
		if (leftArm == 0 || rightArm == 0) {
			skill = 0;
		} else {
			double armBalance = Math.max(leftArm, rightArm) / Math.min(leftArm, rightArm);
			skill = (int) ((leftArm + rightArm) / armBalance);
		}
		
		// Speed = (LL + RL) / ArmBalance
		leftLeg = bodyParts[8].yForce() + bodyParts[9].yForce() + bodyParts[10].yForce();
		rightLeg = bodyParts[11].yForce() + bodyParts[12].yForce() + bodyParts[13].yForce();
		if (leftLeg == 0 || rightLeg == 0) {
			speed = 0;
		} else {			
			double legBalance = Math.max(leftLeg,  rightLeg) / Math.min(leftLeg,  rightLeg);
			speed = (int) ((leftLeg + rightLeg) / legBalance);
		}
	}
	
	private Cube[] fixBodyParts(Cube[] bodyParts) {
		Cube[] fixed = new Cube[14];
		for (int i = 0; i < 14; i++) {
			if (bodyParts[i] == null) fixed[i] = new Cube(0, 0);
			else fixed[i] = bodyParts[i];
		}
		return fixed;
	}
	
	public void renderStats(GameConsole console, int x, int y, int num) {
		console.print(x * 2, y, "Computer Robot " + num + " (CR" + num + ")");
		console.print(x * 2, y + 1, "In:" + intelligence + "  Sk:" + skill + "  SP:" + speed);
	}

	public int getIn() {
		return intelligence;
	}

	public int getSk() {
		return skill;
	}

	public int getSp() {
		return speed;
	}

	public int getLA() {
		return leftArm;
	}

	public int getRA() {
		return rightArm;
	}

	public int getLL() {
		return leftLeg;
	}

	public int getRL() {
		return rightLeg;
	}
}
