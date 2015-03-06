package org.eidd.jordinator;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.utility.Delay;

public class EV3HelloWorld
{
    private static String[] melody = { "C4", "D4", "E4", "C4",
        "E4", "C4",   "E4"};

    public static void main(String[] args)
    {
        //System.out.println("Running...");
        GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
        final int SW = g.getWidth();
        final int SH = g.getHeight();
        Button.LEDPattern(0);

        g.setFont(Font.getLargeFont());
        g.drawString("Jordinator", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        Button.LEDPattern(0);
        Delay.msDelay(4000);
        g.clear();
        g.refresh();
    }
}
