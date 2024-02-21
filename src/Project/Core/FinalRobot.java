package Project.Core;

import java.util.Random;

public class FinalRobot {
	public int intelligence = 0;
	public int skill = 0;
	public int speed = 0;
	
	public FinalRobot(Random rng) {
		Cube[] bodyParts = new Cube[14];
		
		// Construct all 14 randomly so we get a random robot.
		for (int i = 0; i < bodyParts.length; i++) {
			bodyParts[i] = new Cube(rng, 30, 99);
		}
		
		this.constructRobot(bodyParts);
	}
	
	public FinalRobot(Cube[] bodyParts) {
		this.constructRobot(bodyParts);
	}
	
	/**
	 * Calculates the robot's statistics when body parts are given.
	 */
	private void constructRobot(Cube[] bodyParts) {
		// Intelligence = Sum of first 4 (body).
		for (int i = 0; i < 4; i++) {
			intelligence += bodyParts[i].yForce();			
		}
		
		// Skill = (LA + RA) / ArmBalance
		int leftArm = bodyParts[4].xForce() + bodyParts[5].xForce();
		int rightArm = bodyParts[6].xForce() + bodyParts[7].xForce();
		double armBalance = Math.max(leftArm, rightArm) / Math.min(leftArm, rightArm);
		skill = (int) ((leftArm + rightArm) / armBalance);
		
		// Speed = (LL + RL) / ArmBalance
		int leftLeg = bodyParts[8].yForce() + bodyParts[9].yForce() + bodyParts[10].yForce();
		int rightLeg = bodyParts[11].yForce() + bodyParts[12].yForce() + bodyParts[13].yForce();
		double legBalance = Math.max(leftLeg,  rightLeg) / Math.min(leftLeg,  rightLeg);
		speed = (int) ((leftLeg + rightLeg) / legBalance);
	}
}
