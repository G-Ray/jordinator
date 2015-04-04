package org.eidd.jordinator;

public class  Coordonnee {
	
	//Coordonnees en cm
	public static int x = 0;
	public static int y = 1;
	
	public void update_coor(String sample){
		
		if(sample == "red"){
		    y=1;
		}
		else if(sample == "yellow"){
		    y=3;
		}
		else if(sample == "green"){
		    x=1;
		}
		else if(sample == "blue"){
		    x=3;
		}
		else if(sample == "white"){
		    if(x<4){
		        x = 0;
		    }else{
		        x = 4;
		    }
		}
		/*else if(sample == "black"){
		    if(x<2){
		        y=2;
		    }else if(x>2){
		        y=2;
		    }else{
		        x=2;
		    }
		}*/
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
