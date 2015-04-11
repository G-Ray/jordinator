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
	private static float wheelDiameter;
	private static float trackWidth;
	static boolean reverse;
	private static DifferentialPilot robot;
	private static String [] lignesPleines = {"white", "green", "blue", "black"};
	private static int orientation;

	public static void init() {
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

    	robot.setAcceleration(2000);
		robot.setTravelSpeed(30); // cm/sec
		robot.setRotateSpeed(30); // deg/sec
	}
	
	public static void avancer(double distance) {
		robot.travel(distance);
	}

	public static void reculer(double distance) {
		robot.travel(-distance);
	}
	
	public static void stop() {
		robot.stop();
	}
	
	public static void rotationGauche(double angle) {
		robot.rotate(angle);
	}

	public static void rotationDroite(double angle) {
		robot.rotate(-angle);
	}

	public static void arcAvantGauche(double turnRate) {
		robot.steer(turnRate);
	}

	public static void arcAvantDroite(double turnRate) {
		robot.steer(-turnRate);
	}

	public static void arcArriereGauche(double turnRate) {
		robot.steerBackward(turnRate);
	}

	public static void arcArriereDroite(double turnRate) {
		robot.steerBackward(-turnRate);
	}

	public static void suivreLigne(String color, String arret) {
		
		boolean lignePleine = false;
		for(String s : lignesPleines)
			if(s == color)
				lignePleine = true;

		//on verifie si on doit tourner pour commencer a suivre la ligne
		robot.travel(2);
		if(Couleurs.getColor() != color) {
			int angle = 100;
			robot.steer(150, 100, true);
			while(robot.isMoving()) {
				if(Couleurs.getColor() == color) {
					if(angle < 0)
						orientation -= 90;
					else orientation += 90;
					break;
				}
			}
		}

		/*if(lignePleine) {
			robot.travel(130, true);
			while(robot.isMoving()) {
				if(Couleurs.getColor() == color) {
					robot.rotate(3);
					robot.travel(130, true);
				}
				if(Couleurs.getColor() == arret) {
					robot.stop();
					return;
				}
			}
		}*/

		while(Couleurs.getColor() == color) {
			robot.forward();
			while(robot.isMoving()) {
				if(Pinces.capture) {
					Deplacements.marquer();
					return;
				}
			}
		}

		if(Couleurs.getColor() == arret) //on a atteint la couleur d'arret
			return;

		while(Couleurs.getColor() != color) {
			boolean found = false;
			int turnRate = -50;
			int angle = 3;
			if(!found) {
				robot.travel(2, true); // Essayons 2 cm plus loin
				while(robot.isMoving()) {
					if(Couleurs.getColor() == color) {
						found = true;
						break;
					}
				}
			}
			while(!found) {
				robot.steer(turnRate, angle, true);
				while(robot.isMoving()) {
					if(Couleurs.getColor() == color) {
						found = true;
						break;
					}
				}
				turnRate *= -1; //on change de sens de rotation
				angle += 3;
			}
		}
	}
	
	public static void marquer() {
		robot.rotate(-orientation); // on se remet face aux buts
		robot.forward();
		while(robot.isMoving()) {
			if(Couleurs.getColor() == "white") {
				avancer(10);
				Pinces.ouvrir(); //BUT !!!
				robot.rotate(180);
			}
		}
		orientation = 180;
		robot.stop();
	}
}
