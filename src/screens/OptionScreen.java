package com.mygdx.civ.screens;


import com.mygdx.civ.screens.OptionScreen;

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




public class OptionScreen extends ScreenAdapter {


    private CivGame game;
    SpriteBatch batch;
	OrthographicCamera camera;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
	private Stage stage;
	private TextButton boutonPetit;
    private TextButton boutonMoyen;
    private TextButton boutonGrand;
    private TextButton boutonMenu;
    private int taille;
    private Music music;
    private Texture background;


	public OptionScreen(SpriteBatch batch, CivGame game, Music music) {
		this.batch = batch;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
        this.game = game;
        this.music = music;
	}

	@Override
	public void show() {

        //Création du stage et du skin
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        background = new Texture(Gdx.files.internal("background.png"));

        //Création des boutons
        boutonMenu = new TextButton("Menu", skin);
        boutonPetit = new TextButton("Petite", skin);
        boutonMoyen = new TextButton("Moyenne", skin);
        boutonGrand = new TextButton("Grande", skin);

        //chaque bouton passe une taille différente au gameScreen
        boutonPetit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                taille = 1;
                music.stop();
                game.setScreen(new GameScreen(batch, game, taille));
            }
        });

        boutonMoyen.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                taille = 2;
                music.stop();
                game.setScreen(new GameScreen(batch, game, taille));
            }
        });

        boutonGrand.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                taille = 3;
                music.stop();
                game.setScreen(new GameScreen(batch, game, taille));
            }
        });

        //bouton pour retourner au menu
        boutonMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MenuScreen(batch, game));
            }
        });



        //ajout des boutons au stage
        Table table = new Table();
        table.setFillParent(true); // This makes the table fill the whole screen
        stage.addActor(table);

        table.add(boutonPetit).width(200).height(100).pad(10);
        table.add(boutonMoyen).width(200).height(100).pad(10);
        table.add(boutonGrand).width(200).height(100).pad(10);
        table.row(); // Add a new row
        table.add(boutonMenu).width(200).height(100).pad(10);
		


		
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