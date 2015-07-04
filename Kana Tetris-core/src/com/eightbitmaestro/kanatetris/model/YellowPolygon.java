package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.math.Vector2;

public class YellowPolygon extends Polygon{

	public YellowPolygon(Vector2 position, float width, float height) {
		super(position, width, height);
		color="yellow";
		blocks.add(new Block(position, width, height, color));
		blocks.add(new Block(new Vector2(position.x+width, position.y), width, height, color));
		blocks.add(new Block(new Vector2(position.x, position.y+height), width, height, color));
		blocks.add(new Block(new Vector2(position.x+width, position.y+height), width, height, color));
	}

	@Override
	public void rotateClockwise() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rotateCounterClockwise() {
		// TODO Auto-generated method stub
		
	}

}
