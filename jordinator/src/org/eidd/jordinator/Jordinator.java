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

        Deplacements deplacement = new Deplacements();
        deplacement.avancer(17);
        deplacement.reculer(17);
        deplacement.rotationDroite(90);
        deplacement.rotationGauche(90);
        deplacement.arcArriereDroite();
        deplacement.arcArriereGauche();
        deplacement.arcAvantDroite();
        deplacement.arcAvantGauche();
        System.out.println("Fin des deplacements");
        Delay.msDelay(5000);
        g.clear();
        g.refresh();
    }
}
