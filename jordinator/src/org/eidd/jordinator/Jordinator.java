package org.eidd.jordinator;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.Port;
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
        Delay.msDelay(2000);
        g.clear();
        g.refresh();

        // Init sensors (start threads)
        Deplacements.init();
        Distance.init();
        Couleurs.init();
        Pinces.init();
        Pinces.ouvrir();

        Deplacements.avancer(5000);
        while(Distance.distance > 30);
        System.out.println("Palais droit devant");
        Port p = LocalEV3.get().getPort(Config.PORT_BUTTON);
        Bouton b = new Bouton(p);

        while(!b.isPressed());

        Pinces.pincer();

        while(Couleurs.getColor() != "white");
        Deplacements.stop();
        
        b.close();
        g.clear();
        g.refresh();
    }
}
