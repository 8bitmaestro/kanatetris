package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class MoveableEntity extends Entity{
	
	Vector2 velocity;
	private static final float SPEED = 5;

	public MoveableEntity(Vector2 position, float width, float height) {
		super(position, width, height);
		velocity = new Vector2(0,0);
	}
	
	public void setVelocity(Vector2 velocity){
		this.velocity = velocity.cpy();
	}
	
	public void update(){
		position.add(velocity.cpy().scl(Gdx.graphics.getDeltaTime() * SPEED * 5));
		bounds.x = position.x+(width/4);
		bounds.y = position.y+(height/4);
	}
}
