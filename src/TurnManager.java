package com.mygdx.civ;


import com.mygdx.civ.TurnManager;

public class TurnManager {

    private int turn;
    private int maxTurn;
    private boolean endGame;
    private int whoseTurn;
    private int playerCount;


    public TurnManager(int maxTurn, int playerCount) {
        this.maxTurn = maxTurn;
        this.playerCount = playerCount;
        this.turn = 0;
        this.whoseTurn = 0;

    }

    public TurnManager(int maxTurn, int playerCount, int turn, int currentPlayer) {
        this.maxTurn = maxTurn;
        this.playerCount = playerCount;
        this.turn = turn;
        this.whoseTurn = currentPlayer;

    }

    /* Methode qui gère le passage au tour des adversaires et l'incrémentation du tour
     */
    public void nextTurn() {
        if (whoseTurn == playerCount - 1) {
            whoseTurn = 0;
            turn++;
        } else {
            whoseTurn++;
        }

        if (turn == maxTurn) {
            endGame = true;   
        }
          
    }

    public int getTurn() {
        return turn;
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public int getMaxTurn() {
        return maxTurn;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public boolean getEndGame() {
        return endGame;
    }

    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }
    
}
