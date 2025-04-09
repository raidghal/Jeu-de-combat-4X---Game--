package com.mygdx.civ.troops;


import com.mygdx.civ.players.Player;
import com.mygdx.civ.map.Grid;


public class Archer extends Troop {

    public Archer(int row, int col, Grid grid, Player player) {
        super(row, col, grid, player);
        this.setName("Archer");
        this.setCanAttack(true);
        this.setDamage(10);
    }

    @Override
    public void attack(Troop troop, int modificateur) {
        troop.setHealth(troop.getHealth() - this.getDamage()*modificateur);
        if (troop.getHealth() <= 0) {
            troop.getPlayer().removeTroop(troop);
            this.grid.getGrille()[troop.getRow()][troop.getCol()].setTroop(null);
            this.grid.getGrille()[troop.getRow()][troop.getCol()].setUnitePresente(false);
        }
        this.setCanAttack(false);
    }

    public void updateTroop() {
        if (this.getPlayer().getScience().getTechnologies().get("Construction") == 1) {
            this.setDamage(40);
        }
    }
}


