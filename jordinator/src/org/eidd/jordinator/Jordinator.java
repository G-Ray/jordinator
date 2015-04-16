package org.eidd.jordinator;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.robotics.geometry.Rectangle;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.Path;
import lejos.utility.Delay;

public class Jordinator
{
	private static int x;
	private static int y;

    public static void main(String[] args)
    {
        GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
        final int SW = g.getWidth();
        final int SH = g.getHeight();
        g.setFont(Font.getLargeFont());
        g.drawString("Jordinator", SW/2, SH/2, GraphicsLCD.BASELINE|GraphicsLCD.HCENTER);
        g.clear();
        g.refresh();
        Button.LEDPattern(5); //Jordinateur se reveille : Rouge clignotant

        // Init sensors - start threads
        Deplacements.init();
        //Distance.init();
        Couleurs.init();
        //Pinces.init();

        float[][] palais = new float[][] {
      		  { 1, 1, 1},
      		  { 1, 1, 1},
      		  { 1, 1, 1}
      		};

        float target_x = 0;
        float target_y = 1;
        int palais_x = 0;
        int palais_y = 0;

        //TODO demander dans quel camp on commence
    	target_x = Config.DISTANCE_X;
    	target_y = Deplacements.getY();
    	palais_x = (int) (target_x / Config.DISTANCE_X);
    	palais_x = (int) (target_y / Config.DISTANCE_Y);

    	palais[palais_x][palais_y] = 0; //on a visité
    	Pinces.ouvrir();

        while(true) { //Main game loop
        	switch(Couleurs.getColor()) {
        	case "red": 
        		Deplacements.suivreLigne("red", "green");
        		Deplacements.rotationGauche(180);
        		Deplacements.suivreLigne("red", "white");
        		Deplacements.rotationGauche(90);
        		Deplacements.suivreLigne("white", 2);
        		Deplacements.rotationGauche(90);
        		Deplacements.suivreLigne("black", "green");
        		Deplacements.rotationGauche(180);
        		Deplacements.suivreLigne("black", "white");
        		Deplacements.rotationGauche(90);
        		Deplacements.suivreLigne("white", 2);
        		Deplacements.rotationGauche(90);
        		Deplacements.suivreLigne("yellow", "green")
        		Deplacements.rotationGauche(180);
        		Deplacements.suivreLigne("yellow", "white");
        		Deplacements.rotationDroite(90);
        		Deplacements.suivreLigne("white", 6);
        		Deplacements.rotationDroite(90);
        		
        		break;
        	
        	case "yellow":
        		Deplacements.suivreLigne("yellow", "blue");
        		Deplacements.rotationGauche(180);
        		Deplacements.suivreLigne("yellow", "white");
        		Deplacements.rotationGauche(90);
        		Deplacements.suivreLigne("white", 2);
        		Deplacements.rotationGauche(90);
        		Deplacements.suivreLigne("black", "blue");
        		Deplacements.rotationGauche(180);
        		Deplacements.suivreLigne("black", "white");
        		Deplacements.rotationGauche(90);
        		Deplacements.suivreLigne("white", 2);
        		Deplacements.rotationGauche(90);
        		Deplacements.suivreLigne("yellow", "blue");
        		Deplacements.rotationGauche(180);
        		Deplacements.suivreLigne("yellow", "white");
        		Deplacements.rotationDroite(90);
        		Deplacements.suivreLigne("white", 6);
        		Deplacements.rotationDroite(90);
        		break;

        	case "white":
        		/*Deplacements.rotationGauche(360);
        		while(Deplacements.isMoving()) {
        			if(Couleurs.getColor() != "white")
        				Deplacements.stop();
        		}
        		break;*/
        	}
        }
    }

    private static void eviterObstacle() {
		if(y == 1 && Deplacements.orientation == 0)
			Deplacements.rotationGauche(90);
		else if(y == 1 && Deplacements.orientation == 180)
			Deplacements.rotationDroite(90);
    	
		if(y == 2 && Deplacements.orientation == 0)
			Deplacements.rotationDroite(90);
		else if(y == 2 && Deplacements.orientation == 180)
			Deplacements.rotationGauche(90);
		
		if(y == 3 && Deplacements.orientation == 0)
			Deplacements.rotationDroite(90);
		else if(y == 3 && Deplacements.orientation == 180)
			Deplacements.rotationGauche(90);
		
		if(x==1 && Deplacements.orientation == -90)
			Deplacements.rotationDroite(90);
		if(x==1 && Deplacements.orientation == 90)
			Deplacements.rotationGauche(90);
		
		if(x==2 && Deplacements.orientation == -90)
			Deplacements.rotationDroite(90);
		if(x==2 && Deplacements.orientation == 90)
			Deplacements.rotationGauche(90);
		
		
		if(x==3 && Deplacements.orientation == -90)
			Deplacements.rotationGauche(90);
		if(x==3 && Deplacements.orientation == 90)
			Deplacements.rotationDroite(90);

		Deplacements.haut();;
    }
}
