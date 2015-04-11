package org.eidd.jordinator;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.port.Port;
import lejos.robotics.pathfinding.NavigationMesh;
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
        g.clear();
        g.refresh();

        // Init sensors - start threads
        Deplacements.init();
        Distance.init();
        Couleurs.init();
        Pinces.init();

        int[][] plateau = new int[][] {
    		  { 0, 0, 0, 0, 0},
    		  { 0, 1, 1, 1, 0},
    		  { 0, 1, 1, 1, 0},
    		  { 0, 1, 1, 1, 0},
    		  { 0, 0, 0, 0, 0}
    		};

        int[][] visites = new int[][] {
      		  { 0, 0, 0, 0, 0},
      		  { 0, 0, 0, 0, 0},
      		  { 0, 0, 0, 0, 0},
      		  { 0, 0, 0, 0, 0},
      		  { 0, 0, 0, 0, 0}
      		};

        int x = 0;
        int y = 1;

        /*
         * prochaines coordonnees à visiter
         * Obstacle detecté => calculer un autre chemin
         * palais détecté => prendre le chemin le plus court
         */

        while(true) { //game loop
        	boolean parcouru = true;
        	for(int i=0; i<5; i++)
        		for(int j=0; j<5; j++)
        			if(visites[i][j] == 1) {
        				parcouru = false;
        				//x=i; y=j;
        				break;
        			}

        	//On a visité tous les noeuds
        	if (parcouru == true) {
        		System.out.println("PARCOURT TERMINE");
        		break;
        	}

        	Deplacements.suivreLigne("red", "blue");
        	Deplacements.suivreLigne("blue", "yellow");
        	Deplacements.suivreLigne("yellow", "green");
        	Deplacements.suivreLigne("yellow", "black");

        	System.out.println(x + ":" + y);
        	visites[x][y] = 1;
        }

        g.clear();
        g.refresh();
        System.exit(0);
    }

    public void marquer() {
    	//calculer le chemin vers le but le plus court
    	//y aller
    }

    //se debrouille pour aller à des coordonnées spécifiques
    public void allerVers(int x, int y) {
    	
    }

    public void bfs() {
    	
    }
}
