package com.mygdx.civ.screens;



import com.mygdx.civ.map.Grid;

import com.mygdx.civ.players.PlayerManager;
import com.mygdx.civ.screens.GameScreen;


import com.mygdx.civ.CivGame;
import com.mygdx.civ.GameInputProcessor;
import com.mygdx.civ.IsometricRenderer;
import com.mygdx.civ.TurnManager;
import com.mygdx.civ.troops.State;





import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.badlogic.gdx.InputMultiplexer;

import com.badlogic.gdx.audio.Music;









public class GameScreen extends ScreenAdapter {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int TILESIZE = 32;
    private IsometricRenderer renderer;
    private Grid grid;
    private GameInputProcessor inputProcessor;
    private GameUI gameUI;
    private InputMultiplexer multiplexer;
    private PlayerManager playerManager;
    private TurnManager turnManager;
    private State state;
    private Music music;

    private CivGame game;
    private boolean isNewGame;






    
    public GameScreen(SpriteBatch batch, CivGame game, int taille) {
        this.batch = batch;
        this.grid = new Grid(taille * 20);
        this.camera = new OrthographicCamera(WIDTH, HEIGHT);
        this.state = new State();
        this.turnManager = new TurnManager(50, taille*2);
        this.game = game;
        this.playerManager = new PlayerManager(taille*2, grid, turnManager, game, batch);
        this.renderer = new IsometricRenderer(playerManager);
        this.gameUI = new GameUI(batch, game, new FitViewport(WIDTH, HEIGHT), grid, turnManager, state, this, playerManager);
        this.inputProcessor = new GameInputProcessor(camera, gameUI, grid, state);
        grid.Generation();
        isNewGame = true;
        

    }

    /*
     * la méthode est appellée lorsque l'écran est affiché pour la première fois, elle met
     * en place tout ce qui est nécessaire pour le jeu, notamment l'InputMultiplexer qui permet de gérer 
     * les inputs de l'utilisateur
     */
    @Override public void show() {
        music = Gdx.audio.newMusic(Gdx.files.internal("game.mp3")); // Replace with your music file
        music.setLooping(true); // Set the music to loop
        music.setVolume(0.3f);
        music.play(); // Start playing the music

        if (isNewGame) {
            playerManager.setUpGame(); //on setup le jeu (éventuellement si on implémente des sauvegardes faudra pas l'appeler a chaque fois)
            for (int i = 1; i < playerManager.getPlayers().size(); i++) {
                playerManager.getPlayers().get(i).endTurn();
            }   
        }

        gameUI.show(); //on affiche l'interface de jeu

        // on ajoute les inputProcessor de l'interface de jeu et de la caméra dans un InputMultiplexer
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameUI.getStage());
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        





    }

    /*
     * la méthode est appellée lorsque l'écran a chaque frame, elle met a jour ce qui est affiché
     * à l'écran en fonction du déplacement de la caméra et de l'input d'un joueur
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(250f/255f, 206f/255f, 235f/255f, 1); // on définit la couleur de fond
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined); // on définit la matrice de projection de la caméra
        
        camera.update(); // on met a jour la caméra
        
        //tout ce qui est dans le batch.begin() et batch.end() est affiché a l'écran
        batch.begin();
        renderer.renderGround(batch, grid); 
        batch.draw(gameUI.getC_overlay(), -gameUI.getOverlayX(), gameUI.getOverlayY(), 32, 32);
        renderer.renderSprites(batch, grid);  
        batch.end();

        gameUI.render();
        
 
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public Grid getGrid() {
        return grid;
    }

    public Music getMusic() {
        return music;
    }
    

    
}
