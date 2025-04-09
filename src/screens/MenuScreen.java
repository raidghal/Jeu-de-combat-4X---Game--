package com.mygdx.civ.screens;


import com.mygdx.civ.screens.MenuScreen;

import com.mygdx.civ.CivGame;



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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Table;






public class MenuScreen extends ScreenAdapter {


    private CivGame game;
    SpriteBatch batch;
	OrthographicCamera camera;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
	private Stage stage;
	private TextButton boutonJeu;
    private TextButton boutonQuitter;

    private Music music;
    private Texture background;


	public MenuScreen(SpriteBatch batch, CivGame game) {
		this.batch = batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
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
        boutonJeu = new TextButton("Jouer", skin);
        boutonJeu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionScreen(batch, game, music));
            }
        });

        //bouton pour quitter le jeu
        boutonQuitter = new TextButton("Quitter", skin);
        boutonQuitter.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        //on crée une table pour placer les boutons
        Table table = new Table();
        table.setFillParent(true); 
        stage.addActor(table);

        //on ajoute les boutons à la table
        table.add(boutonJeu).width(200).height(100).pad(10); 
        table.row(); 
        //table.add(boutonCharger).width(200).height(100).pad(10);
        table.row();
        table.add(boutonQuitter).width(200).height(100).pad(10);

        //on ajoute de la musique
        music = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3")); 
        music.setLooping(true); 
        music.setVolume(0.3f);
        music.play(); 
		


		
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
		
	

    @Override
    public void dispose() {
        batch.dispose();
    }

}