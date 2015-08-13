package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Polygon {
	
	Array<Block> blocks;
	String color;
	Vector2 startingPosition;
	boolean locked;
	float width, height;
	int rotation;
	
	public Polygon(Vector2 position, float width, float height){
		startingPosition = position;
		blocks = new Array<Block>();
		this.width = width;
		this.height = height;
	}
	
	public Array<Block> getBlocks(){
		return blocks;
	}
	
	public void setWidth(float width){
		this.width = width;
	}
	
	public void setHeight(float height){
		this.height = height;
	}
	
	public void moveBlock(int blockID, int unitsX, int unitsY){
		blocks.get(blockID).getPosition().x+=(unitsX*width);
		blocks.get(blockID).getPosition().y+=(unitsY*height);
		blocks.get(blockID).updateBounds();
	}
	
	public void rotateClockwise(){};
	public void rotateCounterClockwise(){};

}
