package com.mygdx.civ.players;
import java.util.ArrayList;


import com.mygdx.civ.map.Tile;

import com.mygdx.civ.map.Grid;

import com.mygdx.civ.players.PlayerManager;

import com.mygdx.civ.troops.Settler;
import com.mygdx.civ.troops.Troop;
import com.mygdx.civ.CivGame;
import com.mygdx.civ.GameInputProcessor;
import com.mygdx.civ.IsometricRenderer;
import com.mygdx.civ.TurnManager;
import com.mygdx.civ.screens.EndScreen;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ScreenAdapter;


public class PlayerManager {

    private ArrayList<Player> players;
    private Player currentPlayer;
    private Grid grid;
    private CivGame game;
    private TurnManager turnManager;
    private SpriteBatch batch;

    /*
     * Constructeur de la classe PlayerManager 
     * @param taille le nombre de joueur
     * @param grid la grille de jeu
     */

    public PlayerManager(int taille, Grid grid, TurnManager turnManager, CivGame game, SpriteBatch batch) {
        players = new ArrayList<Player>();
        for (int i = 0; i < taille; i++) {
            players.add(new Player("Joueur" + i, i, grid.getTaille()));
        }
        this.grid = grid;
        this.turnManager = turnManager;
        this.game = game;
        this.batch = batch;
        
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }



    /* la méthode qui permet de placer les unités de départ de chaque joueur sur la grille de façon aléatoire
     */

    public void setUpGame() {
        for (int i = 0; i < players.size(); i++) {
            int row = (int) (Math.random() * grid.getTaille());
            int col = (int) (Math.random() * grid.getTaille());

            while (grid.getGrille()[row][col].getBiome() ==  Tile.Biome.Eau || grid.getGrille()[row][col].getUnitePresente() == true || grid.getGrille()[row][col].getBiome() == Tile.Biome.Montagne) {
                row = (int) (Math.random() * grid.getTaille());
                col = (int) (Math.random() * grid.getTaille());
            }

            Settler settler = new Settler(row, col, grid, players.get(i));
            settler.setCanRender(true);
            grid.getGrille()[row][col].setUnitePresente(true);
            grid.getGrille()[row][col].setTroop(settler);
            players.get(i).addTroop(settler);
            
            int [][] voisins = this.grid.getVoisins(row,col);
            players.get(i).getVision()[row][col] = 1;
            for (int l = 0; l < voisins.length; l++) {
                if (voisins[i][0] >= 0 && voisins[l][0] < this.grid.getTaille() && voisins[l][1] >= 0 && voisins[l][1] < this.grid.getTaille()){
                    players.get(i).getVision()[voisins[l][0]][voisins[l][1]] = 1;
                }
                int [][] voisins2 = this.grid.getVoisins(voisins[l][0], voisins[l][1]);
                for (int j = 0; j < voisins2.length; j++) {
                    if (voisins2[j][0] >= 0 && voisins2[j][0] < this.grid.getTaille() && voisins2[j][1] >= 0 && voisins2[j][1] < this.grid.getTaille()){
                        players.get(i).getVision()[voisins2[j][0]][voisins2[j][1]] = 1;
                    }
                    int [][] voisins3 = this.grid.getVoisins(voisins2[j][0], voisins2[j][1]);
                    for (int k = 0; k < voisins3.length; k++) {
                        if (voisins3[k][0] >= 0 && voisins3[k][0] < this.grid.getTaille() && voisins3[k][1] >= 0 && voisins3[k][1] < this.grid.getTaille()) {
                            players.get(i).getVision()[voisins3[k][0]][voisins3[k][1]] = 1;
                        }
                    }
                }
            }
        }
        currentPlayer = players.get(0);
        players.get(0).setUpTurn();
        
    }

    /* la méthode qui permet de passer au tour suivant
     */
    public void playTurns() {
        if (this.turnManager.getEndGame()){
            ScreenAdapter menuFin = new EndScreen(this.batch, maxScore(),  this.game);
            this.game.setScreen(menuFin);


        } else {
            players.get(turnManager.getWhoseTurn()).endTurn();
            turnManager.nextTurn();
            this.currentPlayer = players.get(turnManager.getWhoseTurn());
            players.get(turnManager.getWhoseTurn()).setUpTurn();
        }
        
    }

    /* La méthode qui permet de savoir le joueur ayant 
     * le plus grand score.
     */
    private Player maxScore(){
        int max = currentPlayer.getScore();
        Player bestScore = currentPlayer;
        for (int i = 0; i < players.size(); i++){
            if (max < players.get(i).getScore()){
                max = players.get(i).getScore();
                bestScore = players.get(i);
            }

        }

        return bestScore;

    }

}
    
    
