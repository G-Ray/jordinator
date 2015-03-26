package org.eidd.jordinator;

import java.io.IOException;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.PilotProps;

/**
 * Classe pour tous les deplacements
 * @author Geoffrey et Komel
 *
 */
public class Deplacements {

	private static RegulatedMotor leftMotor;
	private static RegulatedMotor rightMotor;
	private float wheelDiameter;
	private float trackWidth;
	boolean reverse;
	private static DifferentialPilot robot;

	public Deplacements() {
    	PilotProps pp = new PilotProps();
    	try {
			pp.loadPersistentValues();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			return;
		}

    	wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, Config.WHEELDIAMETER));
    	trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, Config.TRACKWIDTH));

    	System.out.println("Wheel diameter is " + wheelDiameter);
    	System.out.println("Track width is " + trackWidth);

    	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, Config.PORT_LEFTMOTOR));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, Config.PORT_RIGHTMOTOR));
    	reverse = Boolean.parseBoolean(pp.getProperty(PilotProps.KEY_REVERSE,"false"));
    	
    	robot = new DifferentialPilot(wheelDiameter,trackWidth,leftMotor,rightMotor,reverse);

    	robot.setAcceleration(6000);
		robot.setTravelSpeed(50); // cm/sec
		robot.setRotateSpeed(45); // deg/sec
	}
	
	public void avancer(double distance) {
		robot.travel(distance, true);
	}

	public void reculer(double distance) {
		robot.travel(-distance);
	}
	
	public void stop() {
		robot.stop();
	}
	
	public void rotationGauche(double angle) {
		robot.rotate(angle);
	}

	public void rotationDroite(double angle) {
		robot.rotate(-angle);
	}

	public void arcAvantGauche(double turnRate) {
		robot.steer(turnRate);
	}
	public void arcAvantDroite(double turnRate) {
		robot.steer(-turnRate);
	}
	public void arcArriereGauche(double turnRate) {
		robot.steerBackward(turnRate);
	}
	public void arcArriereDroite(double turnRate) {
		robot.steerBackward(-turnRate);
	}
}
