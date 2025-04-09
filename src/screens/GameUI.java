package com.mygdx.civ.screens;


import java.util.ArrayList;


import com.mygdx.civ.map.Tile;

import com.mygdx.civ.map.Grid;

import com.mygdx.civ.players.City;
import com.mygdx.civ.players.PlayerManager;

import com.mygdx.civ.troops.Settler;
import com.mygdx.civ.troops.Warrior;
import com.mygdx.civ.troops.Archer;

import com.mygdx.civ.troops.Builder;
import com.mygdx.civ.troops.Troop;
import com.mygdx.civ.CivGame;
import com.mygdx.civ.GameInputProcessor;
import com.mygdx.civ.IsometricRenderer;
import com.mygdx.civ.TurnManager;
import com.mygdx.civ.troops.State;


import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.Pixmap;





public class GameUI {

    private Stage stage;
    private Skin skin;
    private Texture C_overlay; 
    private Table tableBouton;
    private Table tableTour;
    private Table tableChoix;
    private Table tableCentreVille;
    private TextButton passerTour;
    private TextButton menu;
    private BitmapFont font;
    private LabelStyle labelStyle;
    private TurnManager turnManager;
    private CivGame game;
    private SpriteBatch batch;
    private Viewport viewport;
    private IsometricRenderer renderer;
    private Grid grid;
    private float overlayX;
    private float overlayY;
    private TextureRegionDrawable textureRegionDrawableBg;
    private State state;
    private GameScreen gameScreen;
    public static final int TILESIZE = 32;
    private PlayerManager playerManager;



    public GameUI(SpriteBatch batch, CivGame game, Viewport viewport, Grid grid, TurnManager turnManager, State state, GameScreen gameScreen, PlayerManager playerManager) {
        this.batch = batch;
        this.game = game;
        this.turnManager = turnManager;
        this.stage = new Stage(viewport, batch);
        this.skin = new Skin(Gdx.files.internal("skin/flat-earth-ui.json"));
        this.tableBouton = new Table();
        this.tableTour = new Table();;
        this.tableChoix = new Table();
        this.tableCentreVille = new Table();
        this.passerTour = new TextButton("Fin de tour", skin);
        this.menu = new TextButton("Menu", skin);
        this.font = new BitmapFont();
        this.grid = grid;
        this.C_overlay = new Texture(Gdx.files.internal("UI.png"));
        this.gameScreen = gameScreen;
        this.state = state;
        this.playerManager = playerManager;


        


    }


    public float getOverlayX() {
        return overlayX;
    }

    public float getOverlayY() {
        return overlayY;
    }

    public Texture getC_overlay() {
        return C_overlay;
    }

    /*
     * Affiche les boutons du jeu
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);

        tableBouton.setFillParent(true);
        tableBouton.top().left();
        tableBouton.add(menu).width(150).height(30).pad(10);
        tableBouton.add(passerTour).width(150).height(30).pad(10);
        stage.addActor(tableBouton);


        //on setup le bouton qui permet de passer son tour
        passerTour.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerManager.playTurns();
                tableTour.clear();
                tableTour.add(new Label("Turn: " + turnManager.getTurn(), labelStyle)).pad(10);
                tableTour.row();
                tableTour.add(new Label("Joueur " + turnManager.getWhoseTurn(), labelStyle)).pad(10);
                tableTour.row();
                tableTour.add(new Label("Gold : " + playerManager.getCurrentPlayer().getGold(), labelStyle)).pad(10);
                tableTour.row();
                tableTour.add(new Label("Score : " + playerManager.getCurrentPlayer().getScore(), labelStyle)).pad(10);
                tableTour.row();
                tableTour.add(new Label("Science : " + playerManager.getCurrentPlayer().getScience().getTotalSciencePoints(),labelStyle)).pad(10);
            }

        });

        //on setup le bouton qui permet de revenir au menu
        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.getMusic().stop();
                game.setScreen(new MenuScreen(batch, game));
            }
        });


        //on setup le label qui affiche le tour actuel
        tableTour.setFillParent(true);
        tableTour.top().right(); 


        font.getData().setScale(1.5f);
        labelStyle = new LabelStyle(font, Color.GREEN);
        tableTour.add(new Label("Turn: " + turnManager.getTurn(), labelStyle)).pad(10);
        tableTour.row();
        tableTour.add(new Label("Joueur " + turnManager.getWhoseTurn(), labelStyle)).pad(10);
        tableTour.row();
        tableTour.add(new Label("Gold : " + playerManager.getPlayers().get(0).getGold(), labelStyle)).pad(10);
        tableTour.row();
        tableTour.add(new Label("Score : " + playerManager.getPlayers().get(0).getScore(), labelStyle)).pad(10);
        tableTour.row();
        tableTour.add(new Label("Science : " + playerManager.getCurrentPlayer().getScience().getTotalSciencePoints(),labelStyle)).pad(10);
        stage.addActor(tableTour);

    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    /*
     * cette fonction sert uniquement a obtenir la position de la tuile survolée par la souris
     * @param row : la ligne de la tuile
     * @param col : la colonne de la tuile
     * @param mousex : la position en x de la souris
     * @param mousey : la position en y de la souris
     */
    public void setPosOverlay(int row, int col, float mousex, float mousey) {
        this.overlayX = (col - row) * (TILESIZE / 2);
        this.overlayY = (col + row) * (TILESIZE / 3.8f);  
    }



    
    /*Affiche les informations de la tuile cliquée par la souris et permet d'interagir avec 
     * les villes et unités qui se trouve potentiellement sur la tuile
     * @param row : la ligne de la tuile
     * @param col : la colonne de la tuile
     * @param mousex : la position en x de la souris
     * @param mousey : la position en y de la souris
     */
    public void afficherChoix(int row, int col, float mousex, float mousey) {


        //on crée la table qui va afficher les informations de la tuile et toutes les options d'interaction
        tableCentreVille.clear();
        tableCentreVille.remove();
        tableChoix.clear();
        tableChoix.setPosition(stage.getWidth() - tableChoix.getWidth(), stage.getHeight() / 2 - tableChoix.getHeight() / 2 - 100);
        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.TEAL);
        bgPixmap.fill();
        textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        tableChoix.setSize(150, 400);
        tableChoix.setBackground(textureRegionDrawableBg);

        //on try et on catch pour éviter les erreurs si on clique en dehors de la grille
        try {
            Tile currentTile = grid.getGrille()[col][row];
            tableChoix.add(new Label("" + currentTile.getBiome(), labelStyle)).pad(10); //on affiche le biome de la tuile
            tableChoix.row();

            //Si une ville est présente on affiche les informations de la ville et les options d'interaction
            if (currentTile.getEstVille()) {
                tableChoix.add(new Label(currentTile.getCity().getName(), labelStyle)).pad(10);
                tableChoix.row();
                tableChoix.add(new Label("Joueur " + currentTile.getCity().getPlayer().getNumber(), labelStyle)).pad(10);
                tableChoix.row();

                if(currentTile.getEstCentre()) {
                    tableCentreVille.setPosition(stage.getWidth() - tableCentreVille.getWidth() - 150, stage.getHeight() / 2 - tableCentreVille.getHeight() / 2);
                    tableCentreVille.setSize(150, 600);
                    tableCentreVille.setBackground(textureRegionDrawableBg);
                    tableCentreVille.add(new Label("Centre Ville", labelStyle)).pad(10);
                    tableCentreVille.row();
                    tableCentreVille.add(new Label("Population : " + currentTile.getCity().getPopulation(), labelStyle)).pad(10);
                    tableCentreVille.row();
                    tableCentreVille.add(new Label("Production : " + currentTile.getCity().getProduction(), labelStyle)).pad(10);
                    tableCentreVille.row();
                    tableCentreVille.add(new Label("Nourriture : " + currentTile.getCity().getFood(), labelStyle)).pad(10);
                    tableCentreVille.row();
                    tableCentreVille.add(new Label("Produit :" , labelStyle)).pad(10);
                    tableCentreVille.row();
                    tableCentreVille.add(new Label("" + currentTile.getCity().getCurrentlyBuilding(), labelStyle)).pad(10);
                    tableCentreVille.row();
                    tableCentreVille.add(new Label("" + currentTile.getCity().getCurrentlyTraining(), labelStyle)).pad(10);
                    tableCentreVille.row();
                    stage.addActor(tableCentreVille);
                    
                    
                }
            }

            if (currentTile.getEstCentre() && currentTile.getCity().getCanDo()) {
                TextButton production = new TextButton("Production", skin);
                tableChoix.add(production).width(150).height(30).pad(10);
                tableChoix.row();

                production.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        menuProduction(currentTile.getCity());
                        }
                    });
                
            }

            if (currentTile.getRessource() != null && currentTile.getRessource() != Tile.Ressource.Rien) {
                tableChoix.add(new Label(currentTile.getRessource().toString(), labelStyle)).pad(10);
                tableChoix.row();
            }
            

            //Si une unité est présente on affiche les informations de l'unité et les options d'interaction
            if (currentTile.getUnitePresente()) {
                //dans tout les cas on affiche le nom de l'unité et le joueur qui la possède
                tableChoix.add(new Label("" + currentTile.getTroop().getName() , labelStyle)).pad(10);
                tableChoix.row();

                if (!currentTile.getEstCentre()) {
                tableChoix.add(new Label("Joueur " + currentTile.getTroop().getPlayer().getNumber(), labelStyle)).pad(10);
                tableChoix.row();
                }
                tableChoix.add(new Label("PV : " + currentTile.getTroop().getHealth(), labelStyle)).pad(10);
                tableChoix.row();

                if (currentTile.getTroop().getCanMove()) {
                    //si l'unité est un settler on affiche le bouton pour la faire s'installer
                    if (currentTile.getTroop().getName() == "Settler" && currentTile.getTroop().getCanMove()){
                        TextButton settle = new TextButton("Settle", skin);
                        tableChoix.add(settle).width(100).height(30).pad(10);
                        settle.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                Troop troop = currentTile.getTroop();
                                ((Settler) troop).settle(troop.getPlayer().getCities());
                                tableChoix.clear();
                            }
                        } );
                    }

                    boolean exploite = false;
                    for (int i = 0; i < 2; i++) {
                        if (currentTile.getExploite()[i] == 1){
                            exploite = true;
                        }
                    }

                    if (!exploite && currentTile.getTroop().getName() == "Builder" && currentTile.getTroop().getCanMove()){
                            TextButton settle = new TextButton("Aménagement", skin);
                            tableChoix.add(settle).width(100).height(30).pad(10);
                            settle.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    if(currentTile.getCity().getPlayer() == currentTile.getTroop().getPlayer() && (currentTile.getBiome() == Tile.Biome.Plaine && currentTile.getRessource() == Tile.Ressource.Rien)) {
                                        tableChoix.clear();
                                        TextButton ferme = new TextButton("Ferme", skin);
                                        TextButton mine = new TextButton("Mine", skin);
                                        tableChoix.add(ferme).width(100).height(30).pad(10);
                                        tableChoix.row();
                                        tableChoix.add(mine).width(100).height(30).pad(10);

                                        ferme.addListener(new ClickListener() {
                                            @Override
                                            public void clicked(InputEvent event, float x, float y) {
                                                Troop troop = currentTile.getTroop();
                                                ((Builder) troop).build(currentTile, 0);
                                                tableChoix.clear();
                                            }
                                        });

                                        mine.addListener(new ClickListener() {
                                            @Override
                                            public void clicked(InputEvent event, float x, float y) {
                                                Troop troop = currentTile.getTroop();
                                                ((Builder) troop).build(currentTile, 1);
                                                tableChoix.clear();
                                            }
                                        });
                                    
                                    } else {
                                        tableChoix.clear();
                                        TextButton amenagement = new TextButton("Aménagement", skin);
                                        tableChoix.add(amenagement).width(100).height(30).pad(10);

                                        amenagement.addListener(new ClickListener() {
                                            @Override
                                            public void clicked(InputEvent event, float x, float y) {
                                                int index = currentTile.getRessource().ordinal();
                                                Troop troop = currentTile.getTroop();
                                                ((Builder) troop).build(currentTile, 2 + index);
                                                tableChoix.clear();

                                            }
                                        });


                                    }
                                }
                            });
                    }
                    
                    //si une unité ennemie est présente a coté on affiche le bouton pour attaquer
                    ArrayList<int[]> voisins = new ArrayList<int[]>();
                    voisins = this.grid.getVoisinsList(currentTile.getX(), currentTile.getY());

                    for (int i = 0; i < voisins.size(); i++) {
                        if (this.grid.getGrille()[voisins.get(i)[0]][voisins.get(i)[1]].getTroop() == null)
                            voisins.remove(i);
                        }



                    if (currentTile.getTroop().getCanAttack() && voisins.size() > 0) {
                        TextButton attack = new TextButton("Attack", skin);
                        tableChoix.row();
                        tableChoix.add(attack).width(100).height(30).pad(10);
                        attack.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                state.setStateAttack(row, col);
                                tableChoix.clear();
                            }
                        });
                    }
                    


                    
                    TextButton move = new TextButton("Move", skin);
                    tableChoix.row();
                    tableChoix.add(move).width(100).height(30).pad(10);
                    move.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {  
                            state.setStateMove(row, col); //la classe State permet de stocker la position de l'unité pour la passer à d'autres classes facilement
                            tableChoix.clear();
                        }
                    }); 
                } 
                
                
                

            }  
            
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("");
        }
        //on ajoute la table à l'interface
        stage.addActor(tableChoix);
    }
    

    public void effacerChoix() {
        tableChoix.remove();
    }

    public void menuProduction(City city) {
        tableChoix.clear();

        TextButton construction = new TextButton("Construction", skin);
        TextButton formation = new TextButton("Formation", skin);

        tableChoix.add(construction).width(150).height(30).pad(10);
        tableChoix.row();
        tableChoix.add(formation).width(150).height(30).pad(10);

        construction.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuConstruction(city);
            }
        });

        formation.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuFormation(city);
            }
        });  
    }

    public void menuFormation(City city) {
        tableChoix.clear();
        String[] formationsName = {"Settler", "Warrior", "Archer", "Builder"};
        Troop[] troupeAchat = {new Settler(0, 0, grid, city.getPlayer()), new Warrior(0, 0, grid, city.getPlayer()), new Archer(0, 0, grid, city.getPlayer()), new Builder(0, 0, grid, city.getPlayer())};
        TextButton[] formations = new TextButton[4];
        Table tableFormation = new Table();
        for (int i = 0; i < formationsName.length; i++) {
            formations[i] = new TextButton(formationsName[i], skin);
            tableChoix.add(formations[i]).width(150).height(30).pad(10);
            final int finalI = i;
        
            formations[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (city.getPlayer().getGold() >= troupeAchat[finalI].getPrice()) {
                        tableFormation.clear();
                        menuAchat(city, troupeAchat[finalI], tableFormation, troupeAchat);
                    } else {
                        TextButton entrainerButton = new TextButton("Entrainer", skin);
                        tableFormation.clear();
                        tableFormation.add(entrainerButton).width(150).height(30).pad(10);
                        tableFormation.setPosition(stage.getWidth() - tableFormation.getWidth() - 300, stage.getHeight() / 2 - tableFormation.getHeight() / 2);
                        stage.addActor(tableFormation);
        
                        
                        entrainerButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                city.startTraining(formationsName[finalI]);
                                tableFormation.clear();
                            }
                        });
                        tableFormation.add(entrainerButton).pad(10);
                        
                    }
                    
                }
            });
            tableChoix.row();
        }
        
    }    

    public void menuConstruction(City city) {

        tableChoix.clear();
        String[] constructionsName = {"Moulin", "Bibliothèque", "Marché", "Atelier"};
        TextButton[] constructions = new TextButton[city.getBatimentConstruit().size()];
        Table tableConstruction = new Table();

        for (int i = 0; i < city.getBatimentConstruit().size(); i++) {
            if (city.getBatimentConstruit().get(constructionsName[i]) == 0) {
                constructions[i] = new TextButton(constructionsName[i], skin);
                tableChoix.add(constructions[i]).width(150).height(30).pad(10);
                final int finalI = i;

                constructions[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                            tableConstruction.clear();
                            city.startBuilding(constructionsName[finalI]);
                            
                    }
                });
                tableChoix.row();
            }
        }
    }

    public void menuAchat(City city, Troop troop, Table tableFormation, Troop[] troupeAchat) {
        TextButton achat = new TextButton("Acheter", skin);
        tableFormation.add(achat).width(150).height(30).pad(10);
        tableFormation.setPosition(stage.getWidth() - tableFormation.getWidth() - 300, stage.getHeight() / 2 - tableFormation.getHeight() / 2);
        stage.addActor(tableFormation);
        achat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                city.getPlayer().setGold(city.getPlayer().getGold() - troop.getPrice());
                city.setCanDo(false);
                troop.move(city.getTiles()[0]);
                troop.setGrid(city.getGrid());
                troop.setPlayer(city.getPlayer());
                troop.setCanRender(true);
                city.getPlayer().addTroop(troop);
                city.getGrid().getGrille()[troop.getRow()][troop.getCol()].setUnitePresente(true);
                city.getGrid().getGrille()[troop.getRow()][troop.getCol()].setTroop(troop);
                tableFormation.clear();
            }
        });

        for (int i = 0; i < 4; i++) {
            city.getPlayer().removeTroop(troupeAchat[i]);
        }
    }
    

    public void effacerStage() {
        stage.clear();
    }
    

}





