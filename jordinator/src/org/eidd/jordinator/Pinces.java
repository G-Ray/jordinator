package org.eidd.jordinator;

import lejos.hardware.motor.Motor;

import lejos.robotics.RegulatedMotor;

/**
 * Classe pour les actions des pinces
 * @author Geoffrey et Komel
 *
 */
public class Pinces {

	private static RegulatedMotor pickMotor;

	public static void init() {
		pickMotor = Motor.A;
		pickMotor.setAcceleration(6000);
		pickMotor.setSpeed((int) pickMotor.getMaxSpeed());
	}

	public static void pincer() {
		pickMotor.rotate(-800);
	}

	public static void ouvrir() {
		pickMotor.rotate(800);
	}

}
