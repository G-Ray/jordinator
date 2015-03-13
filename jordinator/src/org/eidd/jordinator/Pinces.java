package org.eidd.jordinator;

import lejos.hardware.motor.Motor;

import lejos.robotics.RegulatedMotor;

/**
 * Classe pour tous les deplacements
 * @author Geoffrey et Komel
 *
 */
public class Pinces {

	private static RegulatedMotor pickMotor;

	public Pinces() {
		pickMotor = Motor.A;
		pickMotor.setAcceleration(6000);
		pickMotor.setSpeed((int) pickMotor.getMaxSpeed());
	}

	public void pincer() {
		pickMotor.rotate(-800);
	}

	public void ouvrir() {
		pickMotor.rotate(800);
	}

}
