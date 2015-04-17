package org.eidd.jordinator;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

public class Jordinator {
	private static int x;
	private static int y;

	public static void main(String[] args) {
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
		final int SW = g.getWidth();
		final int SH = g.getHeight();
		g.setFont(Font.getLargeFont());
		g.drawString("Jordinator", SW / 2, SH / 2, GraphicsLCD.BASELINE
				| GraphicsLCD.HCENTER);
		g.clear();
		g.refresh();
		Button.LEDPattern(5); // Jordinateur se reveille : Rouge clignotant

		// Init sensors - start threads
		Deplacements.init();
		Distance.init();
		Couleurs.init();
		Pinces.init();

		// TODO demander dans quel camp on commence
		//Pinces.ouvrir();
	
		while (true) { // Main game loop
			// premier coup
			Deplacements.suivreLigne("red", "white");

			if (Deplacements.orientation < 50 && Deplacements.orientation > -50) { // on
																					// a
																					// marqué
				Deplacements.rotationGauche(180); // on repart
				Deplacements.suivreLigne("red", "black");

				if (Deplacements.orientation < 50
						&& Deplacements.orientation > -50) { // on a marqué
					Deplacements.rotationGauche(180); // on repart
					Deplacements.suivreLigne("red", "black");
				}
			}

			Deplacements.rotationGauche(90);
			Deplacements.avancer(60); // ligne noire
			Deplacements.rotationGauche(90);
			Deplacements.suivreLigne("black", "white");

			if (Deplacements.orientation < 50 && Deplacements.orientation > -50) { // on
																					// a
																					// marqué
				Deplacements.rotationGauche(180); // on repart
				Deplacements.suivreLigne("black", "white");

				if (Deplacements.orientation < 50
						&& Deplacements.orientation > -50) { // on a marqué
					Deplacements.rotationGauche(180); // on repart
					Deplacements.suivreLigne("black", "white");
				}
			}

			Deplacements.rotationGauche(90);
			Deplacements.avancer(60); // ligne jaune
			Deplacements.rotationGauche(90);
			Deplacements.suivreLigne("yellow", "green");
			if (Deplacements.orientation < 50 && Deplacements.orientation > -50) { // on
																					// a
																					// marqué
				Deplacements.rotationGauche(180); // on repart
				Deplacements.suivreLigne("yellow", "green");

				if (Deplacements.orientation < 50
						&& Deplacements.orientation > -50) { // on a marqué
					Deplacements.rotationGauche(180); // on repart
					Deplacements.suivreLigne("yellow", "green");
				}
			}

			if (Couleurs.getColor() == "white") { // pas de palais sur la ligne
				Deplacements.rotationGauche(180);
				Deplacements.suivreLigne("black", "white");
			} else { // on a marque
				Deplacements.rotationGauche(180);
				Deplacements.suivreLigne("black", "white");

				if (Couleurs.getColor() == "white") {
					Deplacements.rotationGauche(180);
					Deplacements.suivreLigne("black", "white");
				} else { // on a marque
					Deplacements.rotationGauche(180);
					Deplacements.suivreLigne("black", "white");
					if (Couleurs.getColor() == "white") { // on retourne dans
															// notre camp
						Deplacements.rotationGauche(180);
						Deplacements.suivreLigne("black", "white");
					}
				}
			}
		}
	}

	private static void eviterObstacle() {
		if (y == 1 && Deplacements.orientation == 0)
			Deplacements.rotationGauche(90);
		else if (y == 1 && Deplacements.orientation == 180)
			Deplacements.rotationDroite(90);

		if (y == 2 && Deplacements.orientation == 0)
			Deplacements.rotationDroite(90);
		else if (y == 2 && Deplacements.orientation == 180)
			Deplacements.rotationGauche(90);

		if (y == 3 && Deplacements.orientation == 0)
			Deplacements.rotationDroite(90);
		else if (y == 3 && Deplacements.orientation == 180)
			Deplacements.rotationGauche(90);

		if (x == 1 && Deplacements.orientation == -90)
			Deplacements.rotationDroite(90);
		if (x == 1 && Deplacements.orientation == 90)
			Deplacements.rotationGauche(90);

		if (x == 2 && Deplacements.orientation == -90)
			Deplacements.rotationDroite(90);
		if (x == 2 && Deplacements.orientation == 90)
			Deplacements.rotationGauche(90);

		if (x == 3 && Deplacements.orientation == -90)
			Deplacements.rotationGauche(90);
		if (x == 3 && Deplacements.orientation == 90)
			Deplacements.rotationDroite(90);

		Deplacements.haut();
		;
	}
}
