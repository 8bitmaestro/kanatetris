package com.eightbitmaestro.kanatetris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.eightbitmaestro.kanatetris.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//default resolution is 640x480, scale appropriately
//		config.width = 1280;
//		config.height = 960;
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
