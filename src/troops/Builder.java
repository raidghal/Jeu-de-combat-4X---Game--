package com.mygdx.civ.troops;

import com.mygdx.civ.map.Tile;

import com.mygdx.civ.players.Player;
import com.mygdx.civ.map.Grid;

public class Builder extends Troop{

    private int charge;

    public Builder(int row, int col, Grid grid, Player player) {
        super(row, col, grid, player);
        this.setName("Builder");
        this.charge = 3;
        this.setCanAttack(false);
    }

    public void build(Tile tile, int type){
        
        tile.getExploite()[type] = 1;
        charge --;
        this.setCanMove(false);
        
        if (charge == 0){
            this.getPlayer().removeTroop(this);
        }
    }

    
    
}
