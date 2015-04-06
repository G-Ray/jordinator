package org.eidd.jordinator;

public class  Coordonnee {
	
	//Coordonnees initiales
	public static int x = 0;
	public static int y = 1;

	public static void update_coor(String sample){		
		switch(sample) {

		case "red": y=1; break;
		case "yellow": y=3; break;
		case "green": x=1; break;
		case "blue": x=3; break;
		case "white": 
			if(x<2) x=0;
			else x=4;
			break;
		case "black":
			if(x!=2) y=2;
			else x=2;
			break;
		}
	}
}
