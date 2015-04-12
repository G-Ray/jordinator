package org.eidd.jordinator;

public class  Coordonnee {
	
	//Coordonnees initiales
	/*public static int x = 0;
	public static int y = 1;
	public static int last_x = 0;
	public static int last_y = 1;*/

	public static void update_coor(String sample) {

		/*switch(sample) {
			case "red": y=1; break;
			case "yellow": y=3; break;
			case "green": x=1; break;
			case "blue": x=3; break;
			case "white": 
				if(x<2) x=0;
				else x=4;
				break;
			case "black":
				if( x<1 || x>3 ) y=2;
				break;
		}*/
		float x = Deplacements.getX();
		float y = Deplacements.getY();

		switch(sample) {
			case "red": Deplacements.setY(Config.DISTANCE_Y); break;
			case "yellow": Deplacements.setY(3 * Config.DISTANCE_Y); break;
			case "green": Deplacements.setX(Config.DISTANCE_X); break;
			case "blue": Deplacements.setX(3 * Config.DISTANCE_X); break;
			case "white":
				if(x<Config.DISTANCE_X) Deplacements.setX(0);
				else Deplacements.setX(4 * Config.DISTANCE_X);
				break;
			case "black":
				if( x<Config.DISTANCE_X || x>3*Config.DISTANCE_X )
					Deplacements.setY(2 * Config.DISTANCE_Y);
				else if( y<Config.DISTANCE_Y || y>3*Config.DISTANCE_Y )
					Deplacements.setX(2 * Config.DISTANCE_X);
				break;
		}
	}
}
