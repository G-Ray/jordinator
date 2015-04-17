package org.eidd.jordinator;

import java.awt.Color;
import java.awt.Robot;
import java.io.IOException;

import org.jfree.chart.plot.dial.DialPointer.Pin;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
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
	public static int orientation; // 0 face au but
	private static Navigator nav;
	private static PoseProvider position;
	private static ShortestPathFinder spf;
	private static LineMap map;
	private static PilotProps pp;
	private static boolean premier = true;

	public static void init() {
    	pp = new PilotProps();
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

    	robot = new DifferentialPilot(Config.LEFT_WHEEL_DIAMETER, Config.RIGHT_WHEEL_DIAMETER, trackWidth, leftMotor, rightMotor, reverse);

    	robot.setAcceleration(Config.ACCELERATION);
		robot.setTravelSpeed(Config.TRAVEL_SPEED);// cm/sec
		robot.setRotateSpeed(Config.ROTATE_SPEED);// deg/sec

    	Line [] lines = new Line[4];

    	lines [0] = new Line(0, 0, 240, 0);   //left >> right bottom
    	lines [1] = new Line(240, 0, 240, 200);      // down >> up
    	lines [2] = new Line(240, 200, 0, 200);
    	lines [3] = new Line(0, 200, 0, 0);   //full rectangle 
    	Rectangle bounds = new Rectangle(0, 0, 240, 200);

    	map = new LineMap(lines, bounds);
	    position = new OdometryPoseProvider(robot);

    	//spf = new ShortestPathFinder(map);
       // nav = new Navigator(robot, position);
	}

	public static void goTo(float x, float y) {
        System.out.println("goTo " + x +":" + y);
        //float startHeading = nav.getPoseProvider().getPose().getHeading();
		//nav.goTo(x, y, startHeading - 5);
        nav.goTo(x, y);

		while(nav.isMoving()) {
			if(Distance.palaisPotentiel) {
				
			}
			if(Pinces.capture) {
				nav.stop();
				marquer();
				return;
			}
		}
		//nav.getPoseProvider().getPose().setHeading(startHeading);
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

	public static void suivreLigne(String c, long duree) {
		int angle;

		long startTime = System.currentTimeMillis();
		long endTime;
		long time = 0;

		while(time < duree || Distance.obstacle) {
			robot.forward();
			while(Couleurs.getColor() == c);

			angle = 2;
			robot.stop();

			while(Couleurs.getColor() != c) {
				robot.rotate(angle, true);
				while(robot.isMoving()) {
					if(Couleurs.getColor() == c) {
						nav.stop();
					}
				}
				angle *= -2;
			}

			endTime = System.currentTimeMillis();
			time = endTime - startTime;
		}
	}
	
	/*public static void suivreLigne(String c, String c2) {
		leftMotor.setAcceleration(300);
		rightMotor.setAcceleration(300);
		leftMotor.setSpeed(400);
		rightMotor.setSpeed(400);
		leftMotor.forward();
		rightMotor.forward();turnRate

		while(Couleurs.getColor() != c2) {
			rightMotor.setSpeed(400);
			rightMotor.forward();
			if(Couleurs.getColor() != c) {
				rightMotor.setSpeed(430);
				rightMotor.forward();
			}

	    	if(Pinces.capture) {
	        	robot.setAcceleration(Config.ACCELERATION);
	    		robot.setTravelSpeed(Config.TRAVEL_SPEED);// cm/sec
	    		robot.setRotateSpeed(Config.ROTATE_SPEED);// deg/sec
	    		robot.stop();
	    		marquer();
	    		return;
	    	}
		}

    	robot.setAcceleration(Config.ACCELERATION);
		robot.setTravelSpeed(Config.TRAVEL_SPEED);// cm/sec
		robot.setRotateSpeed(Config.ROTATE_SPEED);// deg/sec
		robot.stop();

    	avancer(15);
    	if(Pinces.capture)
    		marquer();
	}*/
	
	public static void suivreLigne(String c, String c2) {
		int angle;
		//float duree = 20000;

		/*long startTime = System.currentTimeMillis();
		long endTime;
		long time = 0;*/

		while(Couleurs.getColor() != c2) {
			robot.forward();
			while(Couleurs.getColor() == c) {
				if(Pinces.capture) {
					robot.stop();
					marquer();
					return;
				}
			}

			angle = 2;

			while(Couleurs.getColor() != c) {
				if(Pinces.capture) {
					robot.stop();
					marquer();
					return;
				}
				robot.rotate(angle, true);
				while(robot.isMoving()) {
					if(Couleurs.getColor() == c) {
						robot.stop();
					}
				}
				angle *= -2;
			}

			if(Pinces.capture) {
				robot.stop();
				marquer();
				return;
			}

			//endTime = System.currentTimeMillis();
			//time = endTime - startTime;
		}

    	avancer(10);
    	while(robot.isMoving())
	    	if(Pinces.capture)
	    		marquer();

		robot.stop();
	}

	public static void marquer() {
        System.out.println("Marquer...");

        if(!premier)
        	rotationGauche(orientation);
        
        orientation = 0;

        if(premier) { //on se decale
            rotationDroite(45);
            avancer(25);
            rotationGauche(45);
            premier = false;
        }

    	robot.setAcceleration(20);
		robot.setTravelSpeed(20);// cm/sec
		robot.setRotateSpeed(20);// deg/sec
        
		robot.forward();
		while(robot.isMoving()) {
			if(Couleurs.getColor() == "white")
				robot.stop();
		}

        robot.travel(25);
        Pinces.ouvrir();
        robot.travel(-10);
        
    	robot.setAcceleration(Config.ACCELERATION);
		robot.setTravelSpeed(Config.TRAVEL_SPEED);// cm/sec
		robot.setRotateSpeed(Config.ROTATE_SPEED);// deg/sec
        return;
	}
	
	public static void calibrer() {
		robot.rotate(360);
		float x, y;

		while(robot.isMoving()) {
			if(Couleurs.getColor() == "white") {
				suivreLigne("white", 25000);
			}
		}

		nav.getPoseProvider().getPose().setHeading(-90);
	}
}