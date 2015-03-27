package org.eidd.jordinator;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;

public class Jordinator
{
    public static void main(String[] args)
    {
        GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
        final int SW = g.getWidth();
        final int SH = g.getHeight();
        Button.LEDPattern(5); //Rouge clignotant

        g.setFont(Font.getLargeFont());
        g.drawString("Jordinator", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);

        /* On commence par calibrer les couleurs */

        /* On creer un état de jeu initial */

        /* Tests des déplacements standard */
        /*Deplacements deplacement = new Deplacements();
        deplacement.avancer(17);
        deplacement.reculer(17);
        deplacement.rotationDroite(90);
        deplacement.rotationGauche(90);
        deplacement.arcAvantDroite(100);
        deplacement.arcAvantGauche(100);
        deplacement.arcArriereDroite(100);
        deplacement.arcArriereGauche(100);*/
        
        /*Pinces p = new Pinces();
        p.pincer();
        p.ouvrir();
        Couleurs c = new Couleurs();
        c.calibrate();

        String couleur = c.getCouleur();*/
        g.clear();
        g.refresh();

       // Couleurs c = new Couleurs();
        //c.calibrate();
        Deplacements deplacement = new Deplacements();

        // Main  game loop
        boolean end = false;
        /*while(!end) {
        	deplacement.avancer(5); // on avance de 5 cm
        	if(c.getColor().equals("blue"))
        		end = true;
        }*/
        //deplacement.avancer(70000);

        //Delay.msDelay(5000);
        deplacement.avancer(5000);
        /*while(c.getColor() != "blue");
        deplacement.stop();
        deplacement.rotationGauche(90);
        deplacement.avancer(500000);

        while(c.getColor() != "yellow");
        deplacement.stop();
        deplacement.rotationGauche(90);
        deplacement.avancer(500000);

        while(c.getColor() != "white");
        deplacement.stop();*/
        Distance.init();
        while(Distance.distance>0.5);
        deplacement.stop();

        g.clear();
        g.refresh();
    }
}
