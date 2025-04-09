package com.mygdx.civ.troops;


//La classe State permet de stocker l'état d'un objet (pour l'instant on ne l'utilise que pour les troupes) quand on a besoin des informations de 2 inputs 
//différents pour effectuer une action. Par exemple, pour déplacer une troupe, on a besoin de l'input sur le bouton "Move" et de l'input sur la case destination
public class State {

    private boolean isMoving; 
    private boolean isAttacking;
    private int col;
    private int row;

    public State() {
        this.isMoving = false;
        this.isAttacking = false;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

    public boolean getIsMoving() {
        return this.isMoving;
    }

    public boolean getIsAttacking() {
        return this.isAttacking;
    }

    public void setStateMove(int row, int col) {
        this.row = row; 
        this.col = col;
        this.isMoving = true;
    }

    public void setStateAttack(int row, int col) {
        this.row = row; 
        this.col = col;
        this.isAttacking = true;
    }

    public void stopMoving() {
        this.isMoving = false;
    }

    public void stopAttacking() {
        this.isAttacking = false;
    }




    
}
