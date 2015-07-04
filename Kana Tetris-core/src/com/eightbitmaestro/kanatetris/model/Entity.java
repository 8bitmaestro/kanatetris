package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Entity {
	
	Vector2 position;
	float width, height;
	Rectangle bounds;
	
	public Entity(Vector2 position, float width, float height){
		this.position = position;
		this.width = width;
		this.height = height;
		bounds = new Rectangle(position.x+(width/4), position.y+height/4, width*0.75f, height*0.75f);
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public Rectangle getBounds(){
		return bounds;
	}

}
