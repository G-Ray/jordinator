package org.eidd.jordinator;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;

public class Calibrage {

	public static void main(String[] args) {
		try {
			
			boolean again = true;
			
			
			Port port = LocalEV3.get().getPort("S4");
			EV3ColorSensor colorSensor = new EV3ColorSensor(port);
			SampleProvider average = new MeanFilter(colorSensor.getRGBMode(), 1);
			colorSensor.setFloodlight(Color.WHITE);
			
			System.out.println("Press enter to calibrate blue...");
			Button.ENTER.waitForPressAndRelease();
			float[] blue = new float[average.sampleSize()];
			average.fetchSample(blue, 0);
			
			
			System.out.println("Press enter to calibrate red...");
			Button.ENTER.waitForPressAndRelease();
			float[] red = new float[average.sampleSize()];
			average.fetchSample(red, 0);
			
			System.out.println("Press enter to calibrate green...");
			Button.ENTER.waitForPressAndRelease();
			float[] green = new float[average.sampleSize()];
			average.fetchSample(green, 0);
			
			System.out.println("Press enter to calibrate yellow...");
			Button.ENTER.waitForPressAndRelease();
			float[] yellow = new float[average.sampleSize()];
			average.fetchSample(yellow, 0);

			System.out.println("Press enter to calibrate black...");
			Button.ENTER.waitForPressAndRelease();
			float[] black = new float[average.sampleSize()];
			average.fetchSample(black, 0);
			
			System.out.println("Press enter to calibrate white...");
			Button.ENTER.waitForPressAndRelease();
			float[] white = new float[average.sampleSize()];
			average.fetchSample(white, 0);
			
			System.out.println("Press enter to calibrate grey...");
			Button.ENTER.waitForPressAndRelease();
			float[] grey = new float[average.sampleSize()];
			average.fetchSample(grey, 0);
			
			
			while (again) {
				float[] sample = new float[average.sampleSize()];
				System.out.println("\n Enter to detect a color");
				Button.ENTER.waitForPressAndRelease();
				average.fetchSample(sample, 0);
				double minscal = Double.MAX_VALUE;
				String color = "";
				
				double scalaire = Calibrage.scalaire(sample, blue);
				//Button.ENTER.waitForPressAndRelease();
				//System.out.println(scalaire);
				
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "blue";
				}
				
				scalaire = Calibrage.scalaire(sample, red);
				//System.out.println(scalaire);
				//Button.ENTER.waitForPressAndRelease();
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "red";
				}
				
				scalaire = Calibrage.scalaire(sample, green);
				//System.out.println(scalaire);
				//Button.ENTER.waitForPressAndRelease();
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "green";
				}
				
				scalaire = Calibrage.scalaire(sample, yellow);
				//System.out.println(scalaire);
				//Button.ENTER.waitForPressAndRelease();
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "yellow";
				}
				
				scalaire = Calibrage.scalaire(sample, black);
				//System.out.println(scalaire);
				//Button.ENTER.waitForPressAndRelease();
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "black";
				}
				
				scalaire = Calibrage.scalaire(sample, white);
				//System.out.println(scalaire);
				//Button.ENTER.waitForPressAndRelease();
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "white";
				}
				
				scalaire = Calibrage.scalaire(sample, grey);
				//System.out.println(scalaire);
				//Button.ENTER.waitForPressAndRelease();
				if (scalaire < minscal) {
					minscal = scalaire;
					color = "grey";
				}
				
				System.out.println("The color is " + color + " \n");
				System.out.println("Enter or Escape  \n");
				Button.waitForAnyPress();
				if(Button.ESCAPE.isDown()) {
					colorSensor.setFloodlight(false);
					again = false;
				}
			}
			
			
		} catch (Throwable t) {
			t.printStackTrace();
			Delay.msDelay(10000);
			System.exit(0);
		}
	}
	
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}

}
