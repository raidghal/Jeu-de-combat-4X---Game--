package com.mygdx.civ.troops;

import com.mygdx.civ.map.Tile;

import com.mygdx.civ.map.Grid;
import com.mygdx.civ.players.Player;


import com.mygdx.civ.troops.Troop;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class Troop {

    private String name;
    private int health;
    private int damage;
    private int price;
    private int production;
    private int row;
    private int col;
    Grid grid;
    private Player player;
    private boolean canMove;
    private boolean canRender;
    private boolean canAttack;

    public Troop(int row, int col, Grid grid, Player player) {
        this.health = 100;
        this.row = row;
        this.col = col;
        this.name = "Troop";
        this.grid = grid;
        this.player = player;
        this.canMove = true;
        this.price = 10;
        this.production = 10;
        this.canRender = false;
        this.canAttack = false;
        this.damage = 0;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean getCanMove() {
        return this.canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public int getPrice() {
        return this.price;
    }

    public int getProduction() {
        return this.production;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean getCanRender() {
        return this.canRender;
    }

    public void setCanRender(boolean canRender) {
        this.canRender = canRender;
    }

    public boolean getCanAttack() {
        return this.canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return this.damage;
    }


    /* Déplace l'unité sur une nouvelle tuile
    * @param tile la tuile sur laquelle l'unité doit se déplacer
     */
    public void move(Tile tile) {
        //on enlève l'unité de la tuile actuelle
        this.grid.getGrille()[this.row][this.col].setUnitePresente(false);
        this.grid.getGrille()[this.row][this.col].setTroop(null);

        //on met à jour la position de l'unité
        this.row = tile.getX();
        this.col = tile.getY();

        //on ajoute l'unité à la nouvelle tuile
        this.grid.getGrille()[this.row][this.col].setUnitePresente(true);
        this.grid.getGrille()[this.row][this.col].setTroop(this);
        this.canMove = false;

        int [][] voisins = this.grid.getVoisins(this.row, this.col);
        player.getVision()[this.row][this.col] = 1;
        for (int i = 0; i < voisins.length; i++) {
            if (voisins[i][0] >= 0 && voisins[i][0] < this.grid.getTaille() && voisins[i][1] >= 0 && voisins[i][1] < this.grid.getTaille()){
                player.getVision()[voisins[i][0]][voisins[i][1]] = 1;
            }
            int [][] voisins2 = this.grid.getVoisins(voisins[i][0], voisins[i][1]);
            for (int j = 0; j < voisins2.length; j++) {
                if (voisins2[j][0] >= 0 && voisins2[j][0] < this.grid.getTaille() && voisins2[j][1] >= 0 && voisins2[j][1] < this.grid.getTaille()){
                    player.getVision()[voisins2[j][0]][voisins2[j][1]] = 1;
                }
                int [][] voisins3 = this.grid.getVoisins(voisins2[j][0], voisins2[j][1]);
                for (int k = 0; k < voisins3.length; k++) {
                    if (voisins3[k][0] >= 0 && voisins3[k][0] < this.grid.getTaille() && voisins3[k][1] >= 0 && voisins3[k][1] < this.grid.getTaille()) {
                        player.getVision()[voisins3[k][0]][voisins3[k][1]] = 1;
                    }
                }
            }
        }

        //Si le joueur rentre dans une ville où il n'y pas d'unité alors il prend la ville
        if (tile.getEstCentre()){
            tile.getCity().getPlayer().removeCity(tile.getCity());
            this.player.addCity(tile.getCity());
            this.grid.getGrille()[tile.getX()][tile.getY()].getCity().setPlayer(this.player);
        } 
    }

    /* Affiche l'unité sur la carte
    * @param batch le SpriteBatch sur lequel on dessine
    * @param x la position x de l'unité
    * @param y la position y de l'unité
     */
    public void renderTroop(SpriteBatch batch, float x, float y) {
        //cette méthode permet d'éviter 1000 if else
        if (this.canRender) {
        Texture icon = new Texture(Gdx.files.internal(this.name + ".png"));
        batch.draw(icon, x + 8, y + 8, 16, 16);
        }
    }

    /* Attaque une autre unité
    */
    public void attack(Troop troop, int modificateur) {
        troop.setHealth(troop.getHealth() - damage*modificateur);
        if (troop.getHealth() <= 0) {
            troop.getPlayer().removeTroop(troop);
            this.grid.getGrille()[troop.getRow()][troop.getCol()].setTroop(null);
            this.grid.getGrille()[troop.getRow()][troop.getCol()].setUnitePresente(false);
            this.row = troop.getRow();
            this.col = troop.getCol();

            //Si le joueue bat une troupe ennemi qui se trouvait sur une case se trouvant
            //dans une ville alors le joueur prend cett ville
            //et le joueur dont les troupes ont été battus perd cette ville 
            if (this.grid.getGrille()[troop.getRow()][troop.getCol()].getEstCentre()) {
                troop.getPlayer().removeCity(this.grid.getGrille()[troop.getRow()][troop.getCol()].getCity() );
                this.player.addCity(this.grid.getGrille()[troop.getRow()][troop.getCol()].getCity());
                this.grid.getGrille()[troop.getRow()][troop.getCol()].getCity().setPlayer(this.player);
            }
            
        }
        this.canAttack = false;
    }

    public void updateTroop() {
        return;
    }
    
}
