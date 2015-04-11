package org.eidd.jordinator;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;

/**
 * Classe pour les actions des pinces
 * @author Geoffrey et Komel
 *
 */
public class Pinces {

	private static RegulatedMotor pickMotor;
	public static boolean capture;

	public static void init() {
		capture = false;
		pickMotor = Motor.A;
		pickMotor.setAcceleration(6000);
		pickMotor.setSpeed((int) pickMotor.getMaxSpeed());
		PalaisThread pt = new PalaisThread();
		pt.start();
	}

	public static void pincer() {
		pickMotor.rotate(-800);
		capture = true;
	}

	public static void ouvrir() {
		pickMotor.rotate(800);
		capture = false;
	}

	/*
	 * Pincer à chaque fois qu'on touche quelque chose
	 */
	private static class PalaisThread extends Thread {
		public void run() {
			Port port = LocalEV3.get().getPort(Config.PORT_BUTTON);
			Bouton bouton = new Bouton(port);
			while(true){
				// on vérifie que c'est bien un palais
				if(bouton.isPressed() && Distance.distance > 10)
					Pinces.pincer();
			}
		}
	}
}
