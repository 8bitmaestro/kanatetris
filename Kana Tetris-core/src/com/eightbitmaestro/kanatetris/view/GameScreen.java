package com.eightbitmaestro.kanatetris.view;

import com.badlogic.gdx.Screen;
import com.eightbitmaestro.kanatetris.MyGdxGame;

public class GameScreen implements Screen {

	MyGdxGame game;
	World world;
	WorldRenderer worldRenderer;
	
	public GameScreen(MyGdxGame game){
		this.game = game;
		world = new World(game);
		worldRenderer = new WorldRenderer(world);
	}
	
	@Override
	public void render(float delta) {
		world.update();
		worldRenderer.render();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
