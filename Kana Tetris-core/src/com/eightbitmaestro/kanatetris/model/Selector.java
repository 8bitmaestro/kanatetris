package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Selector {
	
	boolean selected, animationFinished;
	Vector2 position;
	Vector2 circlePosition, crossPosition;
	float circleAlpha, crossAlpha;
	float offset = 64; //used to control how far away circle and cross move when alphad
	float speed = 200; //multiplied by Gdx.graphics.getDeltaTime() to control speed at which circle and cross move
	float alphaSpeed = 3; //multiplied by getDeltatime() to contorl speed at which alpha changes
	float timer;//used for animations
	
	public Selector(Vector2 position, boolean selected){
		this.position = position;
		this.selected = selected;
		circlePosition = new Vector2(position.cpy());
		crossPosition = new Vector2(position.cpy());
	}
	
	public void select(){
		selected = true;
	}
	
	public void unselect(){
		selected = false;
	}
	
	public void update(){
		timer+=Gdx.graphics.getDeltaTime();
		if (circleAlpha == 0){
			circlePosition.x = position.x-offset;
		}
		if (crossAlpha == 0){
			crossPosition.x = position.x+offset;
		}
		if (selected){
			if (circlePosition.x<position.x){
				circlePosition.x+=speed*Gdx.graphics.getDeltaTime();
			}
			if (circlePosition.x>position.x){
				circlePosition.x = position.x;
			}
			if (crossPosition.x< position.x+offset){
				crossPosition.x+=speed*Gdx.graphics.getDeltaTime();
			}
			if (crossPosition.x > position.x+offset){
				crossPosition.x = position.x+offset;
			}
			if (circleAlpha < 1){
				circleAlpha += alphaSpeed*Gdx.graphics.getDeltaTime();
			}
			if (circleAlpha > 1){
				circleAlpha = 1;
			}
			if (crossAlpha > 0){
				crossAlpha -= alphaSpeed*Gdx.graphics.getDeltaTime();
			}
			if (crossAlpha < 0){
				crossAlpha = 0;
			}
		}
		else{
			if (crossPosition.x > position.x){
				crossPosition.x-=speed*Gdx.graphics.getDeltaTime();
			}
			if (crossPosition.x < position.x){
				crossPosition.x = position.x;
			}
			if (circlePosition.x>position.x-offset){
				circlePosition.x -= speed*Gdx.graphics.getDeltaTime();
			}
			if (circlePosition.x<position.x-offset){
				circlePosition.x = position.x-offset;
			}
			if (crossAlpha < 1){
				crossAlpha += alphaSpeed*Gdx.graphics.getDeltaTime();
			}
			if (crossAlpha > 1){
				crossAlpha = 1;
			}
			if (circleAlpha > 0){
				circleAlpha -= alphaSpeed*Gdx.graphics.getDeltaTime();
			}
			if (circleAlpha < 0){
				circleAlpha = 0;
			}
		}
	}
	
	public float getTimer(){
		return timer;
	}
	
	public Vector2 getCirclePosition(){
		return circlePosition;
	}
	
	public float getCircleAlpha(){
		return circleAlpha;
	}
	
	public Vector2 getCrossPosition(){
		return crossPosition;
	}
	
	public float getCrossAlpha(){
		return crossAlpha;
	}
	
	public boolean isSelected(){
		return selected;
	}

}
