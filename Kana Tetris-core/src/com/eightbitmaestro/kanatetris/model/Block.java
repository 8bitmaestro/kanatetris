package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Block extends MoveableEntity{

	String color;
	boolean locked, shouldBeRemoved;
	int intPosition; //added for html
	float tetrisUnit = Gdx.graphics.getWidth()/10; //added for html
	
	public Block(Vector2 position, float width, float height, String color) {
		super(position, width, height);
		this.color = color;
		intPosition = (int) (position.y/tetrisUnit); //added for html
	}
	
	public String getColor(){
		return color;
	}
	
	public void fall(){
		if(!locked){
			position.y-=height;
			intPosition--; //added for html
		}
	}
	
	public void unfall(){
		if(!locked){
			position.y+=height;
			intPosition++; //added for html
		}
		locked = true;
	}
	
	public void moveLeft(){
		if(!locked){
			position.x-=width;
		}
	}
	
	public void moveRight(){
		if(!locked){
			position.x+=width;
		}
	}
	
	public boolean isLocked(){
		return locked;
	}
	
	public boolean shouldBeRemoved(){
		return shouldBeRemoved;
	}
	
	public void markForRemoval(){
		shouldBeRemoved = true;
	}
	
	float necessaryDisplacement = 0;
	public void setNecessaryDisplacement(float yAmount){
		necessaryDisplacement+=yAmount;
	}
	
	public void displace(){
		position.y+=necessaryDisplacement;
		int intDisplacement = (int) (necessaryDisplacement/tetrisUnit); //added for html
		intPosition += intDisplacement;//added for html
		necessaryDisplacement = 0;
		update();
	}
	
	public String toString(){
		return ("X: " + position.x + ", Y: " + position.y+ ", Color: " + color);
	}
	
	public int getSimplifiedPosition(){ //added for html
		return intPosition;
	}
	
}
