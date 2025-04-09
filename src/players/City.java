package com.mygdx.civ.players;

import java.util.HashMap;
import java.lang.Math;

import com.mygdx.civ.map.Tile;
import com.mygdx.civ.map.Grid;
import com.mygdx.civ.players.City;
import com.mygdx.civ.troops.Archer;
import com.mygdx.civ.troops.Builder;
import com.mygdx.civ.troops.Settler;
import com.mygdx.civ.troops.Troop;
import com.mygdx.civ.troops.Warrior;



public class City {



    private Tile[] tiles;
    private String name;
    private int population;
    private boolean canDo;
    private HashMap<String, Integer> batimentConstruit = new HashMap<>();
    private HashMap<String, Integer> coutBatiment = new HashMap<>();

    private Grid grid;
    private Player player;

    private int production; 
    private int food; 

    private int buildingProductionTimer;
    private String currentlyBuilding;
    //Ajout de raid
    private String currentlyTraining; // Nouvelle attribut
    private int unitProductionTimer; // Nouvelle attribut
    //


    /* 
    * Constructeur pour la classe City
    * on assigne par la même occasion l'attribut City de la classe Tile pour chaque tuile de la ville
    * comme ça c'est facile en ayant une tuile de savoir a qui appartient la ville dessus
    *
    * @param tile : la tuile sur laquelle le centre est construit
    * @param grid : On passe la grille pour pouvoir accéder aux tuiles
    * @param player : le joueur qui possède la ville
     */

    public City(Tile tile, Grid grid, Player player) {
        int x = tile.getX();
        int y = tile.getY();
        tiles = new Tile[40];
        this.grid = grid;
        this.grid.getGrille()[x][y].setEstCentre(true);
        this.grid.getGrille()[x][y].setCity(this);
        this.grid.getGrille()[x][y].setRessource(Tile.Ressource.Rien);
        this.player = player;
        
        tiles[0] = grid.getGrille()[x][y];
        int[][] array = new int[8][2];
        array = grid.getVoisins(x,y);
        for (int i = 0; i < 8; i++) {
            if (array[i][0] >= 0 && array[i][0] < this.grid.getTaille() && array[i][1] >= 0 && array[i][1] < this.grid.getTaille()) {
                grid.getGrille()[array[i][0]][array[i][1]].setEstVille(true);
                grid.getGrille()[array[i][0]][array[i][1]].setCity(this);
                tiles[i + 1] = grid.getGrille()[array[i][0]][array[i][1]];

            }
        }

        Warrior warrior = new Warrior(x, y, grid, this.getPlayer());
        warrior.setCanRender(true);
        this.getPlayer().addTroop(warrior);
        this.getGrid().getGrille()[x][y].setUnitePresente(true);
        this.getGrid().getGrille()[x][y].setTroop(warrior);
        
        
        this.population = 1;

        this.buildingProductionTimer = 0;

        this.unitProductionTimer = 0;
        this.currentlyTraining = "Rien";

        this.name = "Ville" + 1;
        
        this.canDo = false;

        this.batimentConstruit.put("Moulin", 0);
        this.coutBatiment.put("Moulin", 10);

        this.batimentConstruit.put("Bibliothèque", 0);
        this.coutBatiment.put("Bibliothèque", 10);

        this.batimentConstruit.put("Marché", 0);
        this.coutBatiment.put("Marché", 10);

        this.batimentConstruit.put("Atelier", 0);
        this.coutBatiment.put("Atelier", 10);
    }

    public Tile[] getTiles() {
        return this.tiles;
    }

    public String getName() {
        return this.name;
    }

    public int getPopulation() {
        return this.population;
    }

    public boolean getCanDo() {
        return this.canDo;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCanDo(boolean canDo) {
        this.canDo = canDo;
    }

    public String getCurrentlyTraining() {
        return this.currentlyTraining;
    }

    public int getProduction() {
        return this.production;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public int getFood() {
        return this.food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public String getCurrentlyBuilding() {
        return this.currentlyBuilding;
    }


    /*
     * Ajoute une tuile à la ville
     * @param tile la tuile à ajouter
     */
    public void addTile(Tile tile) {
        for (int i = 0; i < 40; i++) {
            if (this.tiles[i] == null) {
                this.tiles[i] = tile;
                break;
            }
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;

    }

    public int[] getInfoCity() {

        int[] info = new int[6];

        for (int i = 0; i < 40; i++) {
            
            if (this.tiles[i] == null) {
                break;
            }

            if (this.tiles[i].getExploite()[0] == 1) {
                info[0]++;
            }

            if (this.tiles[i].getExploite()[1] == 1) {
                info[1]++;
            }

            if (this.tiles[i].getExploite()[2] == 1) {
                info[2]++;
            }

            if (this.tiles[i].getExploite()[3] == 1) {
                info[3]++;
            }

            if (this.tiles[i].getExploite()[4] == 1) {
                info[4]++;
            }

            if (this.tiles[i].getBiome() == Tile.Biome.Plaine) {
                info[5]++;
            }

           
        }
        return info;
    }

    /* Calcule la quantité d'or qu'une ville produit a chaque tour ce qui dépend
     * de sa population et des aménagements dans la ville
     * @return la quantité d'or produite
     */
    public int getGold() {
        return 5 + 2*this.population + this.batimentConstruit.get("Marché")*5;
    }

    public int getScience() {
        return 5 + 2*this.population + this.batimentConstruit.get("Bibliothèque")*5 + 5*this.player.getScience().getTechnologies().get("Ecriture");
    }



    public Grid getGrid() {
        return this.grid;
    }

    public HashMap<String, Integer> getBatimentConstruit() {
        return this.batimentConstruit;
    }

    public void incrementProductionTimer() {
        this.buildingProductionTimer += this.getProduction();
    }

    public void updateParameters() {

        int[] cityInfo = getInfoCity();

        int nbFermes = cityInfo[0];
        int nbCarriere = cityInfo[1];
        int nbMines = cityInfo[2];
        int nbPature = cityInfo[3];
        int nbScierie = cityInfo[4];
        int nbPlaine = cityInfo[5];

        int nbFermier = this.population/2 + 1;
        int nbDansFermes = Math.min(nbFermes, nbFermier);
        nbFermier -= nbDansFermes;
        int nbsurplaine = Math.min(nbPlaine, nbFermier);

        this.food = 5 + this.batimentConstruit.get("Moulin")*10 + nbsurplaine + nbDansFermes*5 + nbPature*3;
        this.population = this.food/5 + 5*nbPature;
        this.production = 2*this.population + this.batimentConstruit.get("Atelier")*10 + (Math.min(nbMines, (int) this.population/2)*3) + nbCarriere*3 + nbScierie*3 + nbPature*3 + this.getPlayer().getScience().getTechnologies().get("Ingénierie")*5;


        
        
    }

    //Raid
    public void startTraining(String unitType) {
        this.currentlyTraining = unitType;
        this.unitProductionTimer = 0; // Initialiser le timer de production
    }

    public void startBuilding(String buildingType) {
        this.currentlyBuilding = buildingType;
        this.buildingProductionTimer = 0; // Initialiser le timer de production
    }

    public void updateProduction() {
        updateBuildingProduction();
        updateUnitTraining();
    }

    private void updateBuildingProduction() {
        if (currentlyBuilding != null) {
            buildingProductionTimer = buildingProductionTimer + production;
            // Logique de construction des bâtiments
            if (buildingProductionTimer >= coutBatiment.get(currentlyBuilding)) {
                // Ajouter le bâtiment à la ville
                batimentConstruit.put(currentlyBuilding, 1);
                currentlyBuilding = null;
                buildingProductionTimer = 0;
            }
        }
    }

    private void updateUnitTraining() {
        if (currentlyTraining != null) {
            unitProductionTimer = unitProductionTimer + production;
            // Logique de formation des unités
            if (unitProductionTimer >= getProduction(currentlyTraining)) {
                // Ajouter l'unité à la ville ou au joueur
                addUnitToCity(currentlyTraining);
                currentlyTraining = null;
                unitProductionTimer = 0;
            }
        }
    }

    //raid 
    public void addUnitToCity(String unitType) {
        // Logique pour ajouter l'unité formée à la ville ou au joueur
        Troop unit;
        switch (unitType) {
            case "Warrior":
                unit = new Warrior(tiles[0].getX(), tiles[0].getY(), grid, player); // Vous devez passer les paramètres corrects
                break;
            case "Archer":
                unit = new Archer(tiles[0].getX(), tiles[0].getY(), grid, player); // Vous devez passer les paramètres corrects
                break;
            case "Builder":
                unit = new Builder(tiles[0].getX(), tiles[0].getY(), grid, player); // Vous devez passer les paramètres corrects
                break;
            case "Settler":
                unit = new Settler(tiles[0].getX(), tiles[0].getY(), grid, player); // Vous devez passer les paramètres corrects
                break;
            default:
                unit = null;
                break;
        }
        if (unit != null) {
            player.addTroop(unit);
            unit.setCanRender(true);
            grid.getGrille()[unit.getRow()][unit.getCol()].setUnitePresente(true);
            grid.getGrille()[unit.getRow()][unit.getCol()].setTroop(unit);
        }
    }

    public int getProduction(String unitType) {
        // Logique pour obtenir le coût de production d'une unité
        switch (unitType) {
            case "Warrior":
                return 5;
            case "Archer":
                return 10;
            case "Builder":
                return 15;
            case "Settler":
                return 20;
            default:
                return 0;
        }
    }
}
