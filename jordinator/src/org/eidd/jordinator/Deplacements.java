package org.eidd.jordinator;

import java.awt.Color;
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

    	spf = new ShortestPathFinder(map);
        nav = new Navigator(robot, position);
	}
	
	public static void goTo(float x, float y) {
        System.out.println("goTo " + x +":" + "y");
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

	public static void suivreLigne(String c) {
		int power = 50;
		long startTime;
		long time = 0;
		int boost = 5;
		int msLimit = 100;

		leftMotor.close();
		rightMotor.close();

		Port pLeft = LocalEV3.get().getPort(Config.PORT_LEFTMOTOR);
		Port pRight = LocalEV3.get().getPort(Config.PORT_RIGHTMOTOR);
		UnregulatedMotor left = new UnregulatedMotor(pLeft);
		UnregulatedMotor right = new UnregulatedMotor(pRight);
		
		left.setPower(power);

		left.forward();
		right.forward();
		
		double duree = 0;

		while(Couleurs.getColor() == "grey" || Couleurs.getColor() == c) {
			left.setPower(power);
			right.setPower(power);
			msLimit = 100;
			boolean sens = false;

			while(Couleurs.getColor() == "grey") {
				time = 0;

				right.setPower(power + boost);
				left.setPower(power - boost);
				startTime = System.currentTimeMillis();

				if(sens)
					duree = msLimit * 2;

				while(time < duree) {
					time = System.currentTimeMillis() - startTime;
					if(Couleurs.getColor() == c)
						break;
				}

				time = 0;

				//on essaye dans l'autre sens si ca n'a pas marché
				if(Couleurs.getColor() == "grey") {
					right.setPower(power - boost);
					left.setPower(power + boost);
					startTime = System.currentTimeMillis();

					if(!sens)
						duree = msLimit * 2;

					while(time < duree) {
						time = System.currentTimeMillis() - startTime;
						if(Couleurs.getColor() == c)
							break;
					}
				}

				msLimit += 100;
				sens = !sens;
			}
		}

		left.stop();
		right.stop();
		left.close();
		right.close();

		leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, Config.PORT_LEFTMOTOR));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, Config.PORT_RIGHTMOTOR));
	}

	public static void marquer() {
        System.out.println("Marquer...");
        double but_x = 240;
        double but_y = 75;

        try {
			nav.followPath(spf.findRoute(position.getPose(), new Waypoint(but_x, but_y)));
		} catch (DestinationUnreachableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        nav.waitForStop();

        Pinces.ouvrir();
        robot.travel(-10);
        Pinces.pincer();
        robot.travel(10);
        Pinces.ouvrir();
	}
}