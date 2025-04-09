package com.mygdx.civ;

import com.mygdx.civ.map.Tile;

import com.mygdx.civ.map.Grid;

import com.mygdx.civ.players.PlayerManager;

import com.mygdx.civ.troops.Troop;

import com.mygdx.civ.IsometricRenderer;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class IsometricRenderer {

    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    private Texture grass;
    private Texture water;

    private Texture mont;
    private Texture ville;
    private Texture plage;

    private Texture ressourceADessiner;
    private PlayerManager playerManager;
    private Texture foret;
    private Texture[] exploitation;
    private Texture[] ressources;
    private Texture[] villeUI;
    




    public IsometricRenderer(PlayerManager playerManager) {
        grass = new Texture(Gdx.files.internal("tile_024.png"));
        water = new Texture(Gdx.files.internal("tile_104.png"));

        mont = new Texture(Gdx.files.internal("montagne_30.png"));
        ville = new Texture(Gdx.files.internal("villle.png"));
        plage = new Texture(Gdx.files.internal("sand.png"));

        foret = new Texture(Gdx.files.internal("Foret.png"));
        ressources = new Texture[2];
        for (int i = 0; i < 2; i++) {
            ressources[i] = new Texture(Gdx.files.internal("ressource" + i + ".png"));
        }
        villeUI = new Texture[6];
        for (int i = 0; i < 2; i++) {
            villeUI[i] = new Texture(Gdx.files.internal("VilleUI" + i + ".png"));
        }

        exploitation = new Texture[5];
        for (int i = 0; i < 5; i++) {
            exploitation[i] = new Texture(Gdx.files.internal("Exploitation" + i + ".png"));
        }

        this.playerManager = playerManager;



    }
    /* Méthode qui permet de dessiner les tuiles de la grille 
     * @param batch : le SpriteBatch qui permet de dessiner les tuiles
     * @param grid : la grille de jeu
    */
    public void renderGround(SpriteBatch batch, Grid grid)  {
        int l = grid.getTaille();
        for (int row = l - 1; row >= 0; row--) {
            for (int col = l - 1; col >= 0; col--) {
                //if (this.playerManager.getCurrentPlayer().getVision()[row][col] == 1) {
                    Tile currentTile = grid.getGrille()[row][col];
                    float x = (col - row) * (TILE_WIDTH / 2);
                    float y = (col + row) * (TILE_HEIGHT / 3.8f);
                    if (currentTile.getBiome() == Tile.Biome.Eau) {
                        batch.draw(water, x, y, TILE_WIDTH, TILE_HEIGHT);
                    } 

                    if (currentTile.getBiome() == Tile.Biome.Montagne) {
                        batch.draw(grass, x, y, TILE_WIDTH, TILE_HEIGHT);
                    } 
                    
                    if (currentTile.getBiome() == Tile.Biome.Plage) {
                        batch.draw(plage, x, y, TILE_WIDTH, TILE_HEIGHT);
                    } 

                    if (currentTile.getBiome() == Tile.Biome.Plaine) {
                        batch.draw(grass, x, y, TILE_WIDTH, TILE_HEIGHT);
                    }
                        
                    if (currentTile.getRessource() == Tile.Ressource.Foret) {
                        if (currentTile.getExploite()[4] == 0) {
                        batch.draw(foret, x, y, TILE_WIDTH, TILE_HEIGHT);
                        } else {
                            renderExploitation(batch, x, y, currentTile, 4);
                        }

                    } 
                //}
            }
         }            
    }


    /* Méthode qui permet de dessiner les villes, les ressources, et les unités sur la grille
     * @param batch : le SpriteBatch qui permet de dessiner les villes et les unités
     * @param grid : la grille de jeu
    */
    public void renderSprites(SpriteBatch batch, Grid grid) {  
        int l = grid.getTaille();
        for (int row2 = l - 1; row2 >= 0; row2--) {
            for (int col2 = l - 1; col2 >= 0; col2--) {
                Tile currentTile = grid.getGrille()[row2][col2];
                float x = (col2 - row2) * (TILE_WIDTH / 2);
                float y = (col2 + row2) * (TILE_HEIGHT / 3.8f);
                //if (this.playerManager.getCurrentPlayer().getVision()[row2][col2] == 1) {
                    if (currentTile.getBiome() == Tile.Biome.Montagne) {
                        batch.draw(mont, x + 4, y + 8, TILE_WIDTH - 8, TILE_HEIGHT - 8);
                    } 

                    boolean exploite = false;
                    int type = 0;
                    for (int i = 0; i < 5; i++) {
                        if (currentTile.getExploite()[i] == 1){
                            exploite = true;
                            type = i;
                        }
                    }

                    if(currentTile.getRessource() != Tile.Ressource.Rien && currentTile.getRessource() != Tile.Ressource.Foret && !exploite){
                        this.renderRessource(batch, x, y, currentTile.getRessource(), currentTile);
                    
                    } else if (exploite) {
                        renderExploitation(batch, x, y, currentTile, type);
                        
                    }

                    if (currentTile.getEstVille()) {
                        int joueur = currentTile.getCity().getPlayer().getNumber();
                        Texture iciVilleUI = villeUI[joueur];
                        batch.draw(iciVilleUI, x, y, TILE_WIDTH, TILE_HEIGHT);

                        if (currentTile.getEstCentre() ) {
                            batch.draw(ville, x + 2, y + 9, TILE_WIDTH - 3, TILE_HEIGHT - 3);
                        }
                    }

                    
                    if (currentTile.getUnitePresente()){
                        Troop troop = currentTile.getTroop();
                        troop.renderTroop(batch, x, y);
                    }
                    
                    
                    
                //}
                
            }
        }
    }
    /* Méthode qui permet de dessiner les ressources sur la grille
    * @param batch : le SpriteBatch qui permet de dessiner les ressources
    * @param x : la position x de la ressource
    * @param y : la position y de la ressource
    * @param ressource : la ressource à dessiner
    */
    public void renderRessource(SpriteBatch batch, float x, float y, Tile.Ressource ressource, Tile tile) {
        int index = ressource.ordinal();
        ressourceADessiner = ressources[index];
        batch.draw(ressourceADessiner, x + 6, y + 12, 16, 16);

    }

    public void renderExploitation(SpriteBatch batch, float x, float y, Tile tile, int num) {
        int index = num;
        Texture exploitationADessiner = exploitation[index];
        System.out.println("" + exploitationADessiner);
        batch.draw(exploitationADessiner, x, y, TILE_HEIGHT, TILE_WIDTH);  
    }

    

}


       

       




