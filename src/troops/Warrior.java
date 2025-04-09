package com.mygdx.civ.troops;


import com.mygdx.civ.players.Player;
import com.mygdx.civ.map.Grid;


public class Warrior extends Troop{

    public Warrior(int row, int col, Grid grid, Player player) {
        super(row, col, grid, player);
        this.setName("Warrior");
        this.setCanAttack(true);
        this.setDamage(20);
    }

    public void updateTroop() {
        if (this.getPlayer().getScience().getTechnologies().get("Metallurgie") == 1) {
            this.setDamage(40);
        }
    }
    
}
