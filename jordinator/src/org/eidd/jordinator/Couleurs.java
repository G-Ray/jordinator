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
	SampleProvider average;
	Port port;
	EV3ColorSensor colorSensor;
	private String color = "";

	public Couleurs() {
		port = LocalEV3.get().getPort(Config.PORT_COLOR);
		colorSensor = new EV3ColorSensor(port);
		average = new MeanFilter(colorSensor.getRGBMode(), 1);
		ColorThread thread = new ColorThread();
		calibrate();
		thread.start();
	}

	public String getColor() {
		return color;
	}

	public void calibrate() {
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
			Delay.msDelay(10000);
			System.exit(0);
		}
	}
	
	public String computeColor() {
		SampleProvider sp = new MeanFilter(colorSensor.getRGBMode(), 1);

		float[] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
		double minscal = Double.MAX_VALUE;
		String color = "";

		double scalaire = this.scalaire(sample, blue);

		if (scalaire < minscal) {
			minscal = scalaire;
			color = "blue";
		}
		
		scalaire = this.scalaire(sample, red);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "red";
		}
		
		scalaire = this.scalaire(sample, green);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "green";
		}
		
		scalaire = this.scalaire(sample, yellow);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "yellow";
		}

		scalaire = this.scalaire(sample, black);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "black";
		}

		scalaire = this.scalaire(sample, white);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "white";
		}

		scalaire = this.scalaire(sample, grey);
		if (scalaire < minscal) {
			minscal = scalaire;
			color = "grey";
		}

		System.out.println("The color is " + color + " \n");

		return color;
	}

	public boolean estBleu() {
		return(this.computeColor().equals("blue"));
	}

	public boolean estRouge() {
		return(this.computeColor().equals("red"));
	}

	public boolean estVert() {
		return(this.computeColor().equals("green"));
	}

	public boolean estJaune() {
		return(this.computeColor().equals("yellow"));
	}

	public boolean estNoir() {
		return(this.computeColor().equals("black"));
	}

	public boolean estBlanc() {
		return(this.computeColor().equals("white"));
	}

	public boolean estGris() {
		return(this.computeColor().equals("grey"));
	}
	
	private double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}

	private class ColorThread extends Thread {
		//Deplacements d = new Deplacements();
		public void run() {
			while(true){
				color = computeColor();
			} 
		}
	}

}
