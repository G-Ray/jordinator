package org.eidd.jordinator;


import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;

import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;

public class Distance {
	public static void main(String[] args) throws IOException {
		float frequency = 1; // 1 sample per second
		EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		SampleProvider sp = new PublishFilter(sonicSensor.getDistanceMode(), "Ultrasonic readings", frequency);
		float[] sample = new float[sp.sampleSize()];
		while(Button.ESCAPE.isUp()) {
		
			sp.fetchSample(sample, 0);
			LCD.clear(4);
			LCD.drawString("Distance: " + sample[0],0,4);
			
		}
		
		sonicSensor.close();
	}
}
