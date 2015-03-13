package org.eidd.jordinator;


import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;


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

	public static void main(String[] args)
	{
		Bouton touch= new Bouton(SensorPort.S1);
		System.out.println("Appuyez sur le bouton");

		while(!touch.isPressed()){
			//On fait ce qu'on veut pendant que c'est pas touché
			Delay.msDelay(100);
		}

	}
}
