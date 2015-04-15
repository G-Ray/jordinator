package org.eidd.jordinator;

public class  Coordonnee {

	public static void update_coor(String sample) {

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
