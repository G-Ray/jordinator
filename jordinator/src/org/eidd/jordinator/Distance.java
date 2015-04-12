package org.eidd.jordinator;


import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;

public class Distance {

	public static boolean obstacle;
	public static double distance = 999;
	static float frequency =  2;// 2 sample per second
	static float[] sample;
	static SampleProvider sp;

	public static void init() {
		obstacle = false;
		EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		try {
			sp = new PublishFilter(sonicSensor.getDistanceMode(), "Ultrasonic readings", frequency);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sample = new float[sp.sampleSize()];

		DistanceThread dt = new DistanceThread();
		dt.start();
	}
	
	private static class DistanceThread extends Thread {
		public void run() {
			while(true){
				sp.fetchSample(sample, 0);
				distance = sample[0];

				if(distance < Config.DISTANCE_COLLISION)
					obstacle = true;
				else obstacle = false;
			}
		}
	}
}
