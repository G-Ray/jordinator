package org.eidd.jordinator;

import java.io.IOException;

import org.jfree.chart.plot.dial.DialPointer.Pin;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.geometry.Line;
import lejos.robotics.geometry.Point;
import lejos.robotics.geometry.Rectangle;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.pathfinding.ShortestPathFinder;
import lejos.utility.Delay;
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
	public static int orientation; // 0 face au but
	private static Navigator nav;
	private static PoseProvider position;
	private static ShortestPathFinder spf;
	private static LineMap map;

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

    	robot.setAcceleration(Config.ACCELERATION);
		robot.setTravelSpeed(Config.TRAVEL_SPEED); // cm/sec
		robot.setRotateSpeed(Config.ROTATE_SPEED); // deg/sec

    	Line [] lines = new Line[4];
    	//lines[0] = new Line(0, 0, 150, 0);
    	//lines[0] = new Line(150, 0, 150, 180);
    	//lines[0] = new Line(150, 180, 0, 180);
    	//lines[0] = new Line(150, 180, 0, 0);

    	lines [0] = new Line(0, 0, 240, 0);   //left >> right bottom
    	lines [1] = new Line(240, 0, 240, 200);      // down >> up
    	lines [2] = new Line(240, 200, 0, 200);
    	lines [3] = new Line(0, 200, 0, 0);   //full rectangle 
    	Rectangle bounds = new Rectangle(0, 0, 240, 200);

    	map = new LineMap(lines, bounds);
	    position = new OdometryPoseProvider(robot);

    	spf = new ShortestPathFinder(map);
        nav = new Navigator(robot, position);
        leftMotor.setSpeed(12);
        rightMotor.setSpeed(20);
	}
	
	public static void goTo(float x, float y) {
        System.out.println("Planning path...");
        //try {
			//nav.followPath(spf.findRoute(position.getPose(), new Waypoint(x, y)));
			nav.goTo(x, y);
			while(nav.isMoving()) {
				if(Pinces.capture) {
					nav.stop();
					marquer();
					return;
				}
			}
		/*} catch (DestinationUnreachableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        nav.waitForStop();
	}
	
	public static void setX(float x) {
		Pose p = position.getPose();
		p.setLocation(x, p.getY());
	}

	public static void setY(float y) {
		Pose p = position.getPose();
		p.setLocation(p.getX(), y);
	}
	
	public static float getX() {
		return position.getPose().getX();
	}
	
	public static float getY() {
		return position.getPose().getY();
	}

	public static boolean isMoving() {
		return robot.isMoving();
	}
	
	public static void avancer(double distance) {
		robot.travel(distance, true);
	}

	public static void reculer(double distance) {
		robot.travel(-distance, true);
	}

	public static void stop() {
		robot.stop();
	}
	
	public static void gauche() {
		robot.rotate(90);
		robot.travel(60);
	}

	public static void droite() {
		robot.rotate(90);
		robot.travel(60);
	}
	
	public static void haut() {
		robot.travel(60);
	}
	
	public static void bas() {
		robot.rotate(180);
		robot.travel(60);
	}
	
	public static void rotationGauche(double angle) {
		robot.rotate(angle);
		orientation -= angle;
	}

	public static void rotationDroite(double angle) {
		robot.rotate(-angle);
		orientation += angle;
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
		/*robot.travel(2);
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

		if(lignePleine) {
			robot.forward();
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
		
		while(Couleurs.getColor() != arret) {

			if(Couleurs.getColor() == color) {
				robot.forward();
				while(robot.isMoving()) {
					if(Pinces.capture) {
						Deplacements.marquer();
						return;
					}
				}
			}
	
			while(Couleurs.getColor() != color) {
				boolean found = false;
				int turnRate = -50;
				int angle = 2;
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
					robot.rotate(angle, true);
					while(robot.isMoving()) {
						if(Couleurs.getColor() == color) {
							found = true;
							robot.stop();
							break;
						}
					}
					angle *= -2;
				}
			}
		}
	}
	
	/*public static void marquer() {
		robot.rotate(-orientation); // on se remet face aux buts
		robot.forward();
		while(robot.isMoving()) {
			if(Couleurs.getColor() == "white") {
				avancer(5);
				Pinces.ouvrir(); //BUT !!!
				reculer(5);
				robot.rotate(180);
			}
		}
		orientation = 180;
		robot.stop();
	}*/
	public static void marquer() {
        System.out.println("Planning path...");
        double but_x = 220;
        double but_y = 75;
        try {
			nav.followPath(spf.findRoute(position.getPose(), new Waypoint(but_x, but_y)));
		} catch (DestinationUnreachableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        nav.waitForStop();
        Pinces.ouvrir();
        //nav.goTo(position.getPose().getX()-10, position.getPose().getY());

        robot.travel(-10);
        Pinces.pincer();
        robot.travel(10);
        Pinces.ouvrir();
	}
	
	public static void avancerLigne() {
		robot.forward();
		while(Couleurs.getColor() != "grey");
		robot.stop();
	}
}
