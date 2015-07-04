package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.eightbitmaestro.kanatetris.view.World;

public class MenuText{
	boolean shouldMoveLeft, shouldMoveRight, shouldPlaySound = false, canPlaySound = true;
	private Vector2 position, velocity, target, startingPosition;
	World world;
	String sound;
	public MenuText(Vector2 position, Vector2 target, String sound, World world){
		this.position = position;
		startingPosition = position.cpy();
		this.target = target;
		velocity = new Vector2(0,0);
		this.world = world;
		this.sound = sound;
	}
	
	public void update(){
		if (shouldMoveLeft && (position.x > target.x)){
			position.x -= Gdx.graphics.getDeltaTime()*1000;
		}
		else if (shouldMoveLeft){
			shouldMoveLeft = false;
		}
		else if (shouldMoveRight && position.x < startingPosition.x){
			position.x += Gdx.graphics.getDeltaTime()*1000;
		}
		else if (shouldMoveRight){
			shouldMoveRight = false;
			canPlaySound = true;
		}
	}
	
	public void startMovingLeft(){
		shouldMoveLeft = true;
		shouldMoveRight = false;
		if (canPlaySound){
			world.playSound(sound);
			canPlaySound = false;
		}
	}
	
	public void startMovingRight(){
		shouldMoveRight = true;
		shouldMoveLeft = false;
		
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public boolean isMovingLeft(){
		return shouldMoveLeft;
	}

}
