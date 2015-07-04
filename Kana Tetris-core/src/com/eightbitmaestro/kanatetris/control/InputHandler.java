package com.eightbitmaestro.kanatetris.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.eightbitmaestro.kanatetris.view.World;

public class InputHandler implements InputProcessor{

	World world;
	
	public InputHandler(World world){
		this.world = world;
	}
	
	@Override
	public boolean keyDown(int arg0) {
		switch(arg0){
		case Keys.S:
			world.setSDown();
			if (world.isKanaEntered())
				world.moveAtIncreasedSpeed();
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		switch(arg0){
		//controls
		case Keys.A:
			if (world.isKanaEntered())
				world.moveLeft();
			else world.enterKey("a");
			break;
		case Keys.D:
			if (world.isKanaEntered())
			world.moveRight();
			else world.enterKey("d");
			break;
		case Keys.E:
			if (world.isKanaEntered())
			world.rotateClockwise();
			else world.enterKey("e");
			break;
		case Keys.Q:
			if (world.isKanaEntered())
			world.rotateCounterClockwise();
			else world.enterKey("q");
			break;
		case Keys.B:
			if(!world.isKanaEntered())
				world.enterKey("b");
			break;
		case Keys.C:
			if (!world.isKanaEntered())
				world.enterKey("c");
			break;
		case Keys.F:
			if (!world.isKanaEntered())
				world.enterKey("f");
			break;
		case Keys.G:
			if (!world.isKanaEntered())
				world.enterKey("g");
			break;
		case Keys.H:
			if (!world.isKanaEntered())
				world.enterKey("h");
			break;
		case Keys.I:
			if (!world.isKanaEntered())
				world.enterKey("i");
			break;
		case Keys.J:
			if (!world.isKanaEntered())
				world.enterKey("j");
			break;
		case Keys.K:
			if (!world.isKanaEntered())
				world.enterKey("k");
			break;
		case Keys.L:
			if (!world.isKanaEntered())
				world.enterKey("l");
			break;
		case Keys.M:
			if (!world.isKanaEntered())
				world.enterKey("m");
			break;
		case Keys.N:
			if (!world.isKanaEntered())
				world.enterKey("n");
			break;
		case Keys.O:
			if (!world.isKanaEntered())
				world.enterKey("o");
			break;
		case Keys.P:
			if (!world.isKanaEntered())
				world.enterKey("p");
			break;
		case Keys.R:
			if (!world.isKanaEntered())
				world.enterKey("r");
			break;
		case Keys.S:
			if (!world.canSBeEntered()){
				world.setSCanBeEntered();
				world.moveAtRegularSpeed();
				break;
			}
			if (!world.isKanaEntered())
				world.enterKey("s");
			else
				world.moveAtRegularSpeed();
			break;
		case Keys.T:
			if (!world.isKanaEntered())
				world.enterKey("t");
			break;
		case Keys.U:
			if (!world.isKanaEntered())
				world.enterKey("u");
			break;
		case Keys.V:
			if (!world.isKanaEntered())
				world.enterKey("v");
			break;
		case Keys.W:
			if (!world.isKanaEntered())
				world.enterKey("w");
			break;
		case Keys.X:
			if (!world.isKanaEntered())
				world.enterKey("x");
			break;
		case Keys.Y:
			if (!world.isKanaEntered())
				world.enterKey("y");
			break;
		case Keys.Z:
			if (!world.isKanaEntered())
				world.enterKey("z");
			break;
		case Keys.ENTER:
			if(!world.isKanaEntered())
				world.checkKana();
			break;
		case Keys.BACKSPACE:
			if(!world.isKanaEntered())
				world.backspace();
			break;
		case Keys.SPACE:
			world.clearAll();
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		world.moveMouse(x, Gdx.graphics.getHeight()-y);
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		//need to set mouse click position
		world.setMousePosition(x, Gdx.graphics.getHeight()-y);
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
