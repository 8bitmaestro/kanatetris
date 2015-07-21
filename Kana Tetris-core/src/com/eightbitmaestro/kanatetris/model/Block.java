package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block extends MoveableEntity{

	String color;
	boolean locked, shouldBeRemoved;
	int intPosition; //added for html, Y coordinate expressed in integer relative to field
	float tetrisUnit = Gdx.graphics.getWidth()/10; //added for html
	
	public Block(Vector2 position, float width, float height, String color) {
		super(position, width, height);
		this.color = color;
		intPosition = (int) (position.y/tetrisUnit); //added for html
	}
	
	public void setWidth(float width){
		this.width = width;
	}
	
	public void setHeight(float height){
		this.height = height;
	}
	
	public void scale(){
		float oldTetrisUnit = tetrisUnit;
		System.out.println("Scaling block...");
		System.out.println("Old tetris unit was " + tetrisUnit);
		tetrisUnit = Gdx.graphics.getWidth()/10;
		System.out.println("New tetris unit is " + tetrisUnit);
		float scaleAmount = tetrisUnit/oldTetrisUnit;
		System.out.println("Scale amount is " + scaleAmount);
		System.out.println("old x position was " + position.x);
		position.x *= scaleAmount;
		System.out.println("new x position is " + position.x);
		System.out.println("old y int and float positions were " + intPosition + ", " + position.y);
		position.y = intPosition*tetrisUnit;
		System.out.println("new y float position is " + position.y);
		bounds = new Rectangle(position.x+(width/4), position.y+height/4, width*0.75f, height*0.75f);
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
