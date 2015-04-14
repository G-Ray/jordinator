package org.eidd.jordinator;

import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;

public class PaletProche {

	
	public static void main(String[] args) {
        
		double palet[] = new double[9];
		
		GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
        final int SW = g.getWidth();
        final int SH = g.getHeight();
        g.setFont(Font.getLargeFont());
        g.drawString("Jordinator", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        g.clear();
        g.refresh();
		int i;
		int j;
		double dist = 999999;
		int angle = 0;
		Distance.init();
		Deplacements.init();
		
		for (i=0; i<9; i++){
			Deplacements.rotationGauche(20);
			palet[i] = Distance.distance;
		}
				for (j=0; j<9; j++){
					if (palet[j] < dist){
						dist = palet[j];
						angle = j * 20; 
					}	
				}
		Deplacements.rotationDroite(180 - angle);
		Deplacements.avancer(dist * 100);
	}

}
