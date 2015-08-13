package com.eightbitmaestro.kanatetris.model;

import com.badlogic.gdx.math.Vector2;

public class IndigoPolygon extends Polygon{

	public IndigoPolygon(Vector2 position, float width, float height) {
		super(position, width, height);
		color = "indigo";
		blocks.add(new Block(position, width, height, color));
		blocks.add(new Block(new Vector2(position.x+width, position.y), width, height, color));
		blocks.add(new Block(new Vector2(position.x-width, position.y), width, height, color));
		blocks.add(new Block(new Vector2(position.x-width, position.y+height), width, height, color));
	}

	@Override
	public void rotateClockwise() {
		if(rotation==0){
			moveBlock(1,-1,-1);
			moveBlock(2,1,1);
			moveBlock(3,2,0);
			rotation=1;
		}
		else if(rotation==1){
			moveBlock(1,-1,1);
			moveBlock(2,1,-1);
			moveBlock(3,0,-2);
			rotation=2;
		}
		else if(rotation==2){
			moveBlock(1,1,1);
			moveBlock(2,-1,-1);
			moveBlock(3,-2,0);
			rotation=3;
		}
		else if(rotation==3){
			moveBlock(1,1,-1);
			moveBlock(2,-1,1);
			moveBlock(3,0,2);
			rotation=0;
		}
		
	}

	@Override
	public void rotateCounterClockwise() {
		if(rotation==1){
			moveBlock(1,1,1);
			moveBlock(2,-1,-1);
			moveBlock(3,-2,0);
			rotation=0;
		}
		else if(rotation==2){
			moveBlock(1,1,-1);
			moveBlock(2,-1,1);
			moveBlock(3,0,2);
			rotation=1;
		}
		else if(rotation==3){
			moveBlock(1,-1,-1);
			moveBlock(2,1,1);
			moveBlock(3,2,0);
			rotation=2;
		}
		else if(rotation==0){
			moveBlock(1,-1,1);
			moveBlock(2,1,-1);
			moveBlock(3,0,-2);
			rotation=3;
		}
		
	}

}
