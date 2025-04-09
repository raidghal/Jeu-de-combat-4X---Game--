
package com.mygdx.civ;

import java.lang.Math;

import com.mygdx.civ.map.Tile;

import com.mygdx.civ.map.Grid;

import com.mygdx.civ.screens.GameUI;

import com.mygdx.civ.GameInputProcessor;

import com.mygdx.civ.troops.State;


import com.badlogic.gdx.Input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;






public class GameInputProcessor implements InputProcessor {
    private OrthographicCamera camera;

    private Vector3 position;
    private GameUI gameUI;
    private Grid grid;

    private State state;
    public static final int TILESIZE = 32;
    



    public GameInputProcessor(OrthographicCamera camera, GameUI gameUI, Grid grid, State state) {
        this.camera = camera;
        this.gameUI = gameUI;
        this.state = state;
        this.grid = grid;


    }

    /* Méthode qui permet de bouger la caméra en faisant un "drag" avec la souris
     * @param screenX la position x de la souris
     * @param screenY la position y de la souris
     * @param pointer (pas utile)
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            float x = Gdx.input.getDeltaX() * camera.zoom;
            float y = Gdx.input.getDeltaY() * camera.zoom;
            camera.translate(-x, y);
            return true;
        }
        return false;
    }

    public boolean keyDown (int keycode) {
        return false;
     }
  
     public boolean keyUp (int keycode) {
        return false;
     }
  
     public boolean keyTyped (char character) {
        return false;
     }
     
    /* Méthode qui permet de gérer tout les clics de souris, c'est a dire dans tout les cas afficher les informations de 
     * la tuile sur laquelle on a cliqué, et dans le cas ou on cherche a déplacer une troupe on gère le déplacement.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
 
        if (button == Input.Buttons.LEFT) {
            
            position = camera.unproject(new Vector3(screenX, screenY, 0));

            float row = ((position.x / (TILESIZE/ 2) + position.y / (TILESIZE / 3.8f)) / 2) - 1.5f;
            float col = ((position.y / (TILESIZE / 3.8f) - (position.x / (TILESIZE / 2))) / 2) - 0.5f;
            col = Math.round(col);
            row = Math.round(row);

            gameUI.afficherChoix((int) row, (int) col, position.x, position.y);


            if (state.getIsMoving() && col >= 0 && col < grid.getTaille() && row >= 0 && row < grid.getTaille()) { 
                Tile currentTile = grid.getGrille()[state.getCol()][state.getRow()];
                Tile nextTile = grid.getGrille()[(int) col][(int) row];

                if (nextTile.getTroop() == null && nextTile.getBiome() != Tile.Biome.Eau 
                && nextTile.getBiome() != Tile.Biome.Montagne &&
                nextTile.distance(currentTile) <= 2) {

                grid.getGrille()[state.getCol()][state.getRow()].getTroop().move(grid.getGrille()[(int) col][(int) row]);
                state.stopMoving();
                }
            } else {
                if (state.getIsAttacking() && col >= 0 && col < grid.getTaille() && row >= 0 && row < grid.getTaille()) {
                    Tile currentTile = grid.getGrille()[state.getCol()][state.getRow()];
                    Tile nextTile = grid.getGrille()[(int) col][(int) row];

                    if (nextTile.distance(currentTile) <= 2 && nextTile.getTroop() != null && nextTile.getTroop().getPlayer() != currentTile.getTroop().getPlayer()) {
                        if (nextTile.distance(currentTile) == 1) {
                            currentTile.getTroop().attack(nextTile.getTroop(), 1);
                            state.stopAttacking();
                        } else if (nextTile.distance(currentTile) == 2 && currentTile.getTroop().getName() == "Archer"){
                            
                            currentTile.getTroop().attack(nextTile.getTroop(), 2);
                            state.stopAttacking();
                        
                        }
                    }
                }
            }
            return true;
        }
        else { 

        }

        
        

        return false;
    }
    
  
     public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
     }
     
     /* gère l'overlay qui permet de voir la case qu'on est en train de sélectionner 
      */
     public boolean mouseMoved (int x, int y) {

        position = camera.unproject(new Vector3(x, y, 0));


        float row = ((position.x / (TILESIZE / 2) + position.y / (TILESIZE / 3.8f)) / 2) - 1.5f;
        float col = ((position.y / (TILESIZE / 3.8f) - (position.x / (TILESIZE / 2))) / 2) - 0.5f;
        col = Math.round(col);
        row = Math.round(row);
        gameUI.setPosOverlay((int) row, (int) col, position.x, position.y);

        return false;
        }
    
    
    /* gère le zoom de la souris en fonction du scroll
    */
    @Override
    public boolean scrolled(float amountx, float amounty) {
        
        camera.zoom += amounty * 0.1f; 
        if (camera.zoom < 0.2f) camera.zoom = 0.2f;
        camera.update();
        return true;
    }

     public boolean touchCancelled (int screenX, int screenY, int pointer, int button) {
        return false;
     }


}
    

