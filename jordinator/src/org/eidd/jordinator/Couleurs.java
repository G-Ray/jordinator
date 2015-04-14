package org.eidd.jordinator;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class Couleurs {

	private static float[] blue;
	private static float[] red;
	private static float[] green;
	private static float[] yellow;
	private static float[] black;
	private static float[] white;
	private static float[] grey;
	static SampleProvider average;
	static Port port;
	static EV3ColorSensor colorSensor;
	private static String color = "";

	public static void init() {
		port = LocalEV3.get().getPort(Config.PORT_COLOR);
		colorSensor = new EV3ColorSensor(port);
		average = new MeanFilter(colorSensor.getRGBMode(), 1);
		ColorThread thread = new ColorThread();
		calibrate();
		thread.start();
	}

	public static String getColor() {
		return color;
	}
	
	public static double getScalaire() {
		SampleProvider sp = new MeanFilter(colorSensor.getRGBMode(), 1);
		float[] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
		
	    return scalaire(sample);
	}
	
	public static double getScalaire(String color) {
		switch(color) {
		case "blue": return scalaire(blue);
		case "red": return scalaire(red);
		case "green": return scalaire(green);
		case "yellow": return scalaire(yellow);
		case "black": return scalaire(black);
		case "white": return scalaire(white);
		case "grey": return scalaire(grey);
		}
		return 0;
	}

	public static void calibrate() {
		try {
			colorSensor.setFloodlight(Color.WHITE);

			System.out.println("Press enter to calibrate blue...");
			Button.ENTER.waitForPressAndRelease();
			blue = new float[average.sampleSize()];
			average.fetchSample(blue, 0);

			System.out.println("Press enter to calibrate red...");
			Button.ENTER.waitForPressAndRelease();
			red = new float[average.sampleSize()];
			average.fetchSample(red, 0);
			
			System.out.println("Press enter to calibrate green...");
			Button.ENTER.waitForPressAndRelease();
			green = new float[average.sampleSize()];
			average.fetchSample(green, 0);
			
			System.out.println("Press enter to calibrate yellow...");
			Button.ENTER.waitForPressAndRelease();
		    yellow = new float[average.sampleSize()];
			average.fetchSample(yellow, 0);

			System.out.println("Press enter to calibrate black...");
			Button.ENTER.waitForPressAndRelease();
			black = new float[average.sampleSize()];
			average.fetchSample(black, 0);
			
			System.out.println("Press enter to calibrate white...");
			Button.ENTER.waitForPressAndRelease();
			white = new float[average.sampleSize()];
			average.fetchSample(white, 0);
			
			System.out.println("Press enter to calibrate grey...");
			Button.ENTER.waitForPressAndRelease();
			grey = new float[average.sampleSize()];
			average.fetchSample(grey, 0);
			//colorSensor.close();
			
		} catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(1000);
			System.exit(0);
		}
	}
	
	public static String computeColor() {
		SampleProvider sp = new MeanFilter(colorSensor.getRGBMode(), 1);

		float[] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
		double minscal = Double.MAX_VALUE;
		String color = "";

		double scalaire = scalaire(sample, blue);

		if (scalaire < minscal) {
			minscal = scalaire;
			color = "blue";
		}
		
		scalaire = scalaire(sample, red);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "red";
		}
		
		scalaire = scalaire(sample, green);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "green";
		}

		scalaire = scalaire(sample, yellow);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "yellow";
		}

		scalaire = scalaire(sample, black);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "black";
		}

		scalaire = scalaire(sample, white);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "white";
		}

		scalaire = scalaire(sample, grey);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "grey";
		}
		return color;
	}

	private static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}

	private static double scalaire(float[] v1) {
		return (v1[0] + v1[1] + v1[2]);
	}

	private static class ColorThread extends Thread {
		public void run() {
			while(true){
				color = computeColor();
				Coordonnee.update_coor(color);
			} 
		}
	}

}
