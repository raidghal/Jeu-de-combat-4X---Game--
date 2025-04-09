package com.mygdx.civ.troops;
import java.util.ArrayList;

import com.mygdx.civ.map.Tile;

import com.mygdx.civ.players.Player;
import com.mygdx.civ.map.Grid;
import com.mygdx.civ.players.City;


import com.mygdx.civ.troops.Settler;


public class Settler extends Troop {

    public Settler(int row, int col, Grid grid, Player player) {
        super(row, col, grid, player);
        this.setName("Settler");
        this.setCanAttack(false);
    }


    /* Permet de fonder une ville  */
    public void settle(ArrayList<City> cities) {

        Tile currentTile = this.grid.getGrille()[this.getRow()][this.getCol()];

        if (currentTile.getEstVille() == false) {

            City city = new City(currentTile, this.grid, this.getPlayer());
            cities.add(city);
            
            
            currentTile.setTroop(null);
            currentTile.setUnitePresente(false);
            this.getPlayer().removeTroop(this);
            
        }
    
    }
}