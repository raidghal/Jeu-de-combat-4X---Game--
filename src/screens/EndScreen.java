
package com.mygdx.civ.screens;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;


import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Label;



import com.mygdx.civ.players.Player;

import com.mygdx.civ.CivGame;
;


public class EndScreen extends ScreenAdapter{

   
    SpriteBatch batch;
	OrthographicCamera camera;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
	private Stage stage;
    private TextButton boutonQuitter;
    private Player winnerPlayer;
    private CivGame game;
    private Texture background = new Texture(Gdx.files.internal("background.png"));


	public EndScreen(SpriteBatch batch, Player winnerPlayer, CivGame game) {
		this.batch = batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
        this.winnerPlayer = winnerPlayer;
        this.game = game;
        
	}

	@Override
	public void show() {
        //on setup le stage avec les boutons d'interaction
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        background = new Texture(Gdx.files.internal("background.png"));
        
        //bouton pour lancer le jeu et passer à l'écran de choix des options
        boutonQuitter = new TextButton("Quitter", skin);
        boutonQuitter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(batch, game));
            }
        });

        
        Label label = new Label("le joueur :" + winnerPlayer.getNumber() + "a gagné", skin);
        label.setPosition(WIDTH / 2 - label.getWidth() / 2, HEIGHT / 2 + 50);

        stage.addActor(label);
        stage.addActor(boutonQuitter);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(250f/255f, 206f/255f, 235f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined); // utiliser la deuxième caméra pour dessiner l'image
        
        

        batch.begin();
        batch.draw(background, 0, 0, 800, 480);
        batch.end();

        stage.act(delta);
        stage.draw();

        camera.update();
    }


}
