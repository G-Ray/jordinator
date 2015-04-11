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

        while(true) {
        	boolean parcouru = true;
        	for(int i=0; i<5; i++)
        		for(int j=0; j<5; j++)
        			if(plateau[i][j] == 1) {
        				parcouru = false;
        				//x=i; y=j;
        				break;
        			}

        	//On a visité tous les noeuds
        	if (parcouru == true) {
        		System.out.println("PARCOURT TERMINE");
        		break;
        	}

        	//choisir le palais le plus près
        	//si pas de palais, MAJ visites et recommencer
        	//si palais, alors on marque
        	//if(Pinces.capture); //aller marquer
        	//if(Distance.obstacle); //Recalculer chemin

        	if(x==3 && y == 1) {
        		Deplacements.rotationGauche(77);
        		Deplacements.avancer(50);
        		Deplacements.rotationGauche(77);
        		y=2;
        	}
        	else if(y == 1) {
        		Deplacements.avancer(60);
        		x++;
        	}

        	if(x==1 && y == 2) {
        		Deplacements.rotationDroite(77);
        		Deplacements.avancer(50);
        		Deplacements.rotationDroite(77);
        		y=3;
        	}
        	else if(y == 2) {
        		Deplacements.avancer(60);
        		x--;
        	}

        	if(x==3 && y == 3) {
        		Deplacements.stop();
        	}

        	else if(y == 3) {
        		Deplacements.avancer(60);
        		x++;
        	}

        	System.out.println(x + ":" + y);
        	plateau[x][y] = 0;
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
