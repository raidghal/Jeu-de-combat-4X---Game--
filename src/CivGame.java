package com.mygdx.civ;


import com.mygdx.civ.screens.MenuScreen;

import com.mygdx.civ.CivGame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;



public class CivGame extends Game {
	private SpriteBatch batch;
	
	
	/* la méthode permet de lancer le jeu fondamentalement 
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		DisplayMode displayMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(displayMode);
		setScreen(new MenuScreen(batch, this));
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		super.render();
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
