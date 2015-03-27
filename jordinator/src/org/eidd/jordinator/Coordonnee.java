package org.eidd.jordinator;

public class Coordonnee {
	
	public int x=0;
	public int y=50;
	int X_MAX=220;
	int Y_MAX=200;
	
	public void Coordonne(String sample){
		
		if(sample=="red"){
		    y=50;
		}
		else if(sample=="yellow"){
		    y=150;    
		}
		else if(sample=="green"){
		    x=60;
		}
		else if(sample=="blue"){
		    x=180;
		}
		else if(sample=="white"){
		    if(x<120){
		        x=0;
		    }else{
		        x=220;
		    }
		}
		else if(sample=="black"){
		    if(x<110){
		        y=100;
		    }else if(x>110){
		        y=100;
		    }else{
		        x=120;
		    }
		}
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
