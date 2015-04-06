package org.eidd.jordinator;


import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;


public class Bouton extends EV3TouchSensor
{
	public Bouton(Port port)
	{
		super(port);
	}

	public boolean isPressed()
	{
		float[] sample = new float[1];
		fetchSample(sample, 0);
		return sample[0] != 0;
	}

}
