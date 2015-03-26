package org.eidd.jordinator;


import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;

public class Distance {

	public static double distance = 999;
	float frequency = 1; // 1 sample per second
	float[] sample;
	SampleProvider sp;

	public Distance() throws IOException {
		EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		sp = new PublishFilter(sonicSensor.getDistanceMode(), "Ultrasonic readings", frequency);
		sample = new float[sp.sampleSize()];

		//sonicSensor.close();
		DistanceThread dt = new DistanceThread();
		dt.start();
	}
	
	private class DistanceThread extends Thread {
		public void run() {
			while(true){
				sp.fetchSample(sample, 0);
				System.out.println(distance);
				distance = sample[0];
			} 
		}
	}
}
