package com.eightbitmaestro.kanatetris.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.eightbitmaestro.kanatetris.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
               // return new GwtApplicationConfiguration(480, 320);
        	return new GwtApplicationConfiguration(640, 480);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new MyGdxGame();
        }
}