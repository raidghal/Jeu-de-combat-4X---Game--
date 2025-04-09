package com.mygdx.civ.players;

import java.util.ArrayList;
import com.mygdx.civ.troops.Troop;

public class Player {

    private String name;
    private int number;
    private int gold;
    private ArrayList<City> cities;
    private ArrayList<Troop> troops;
    private int[][] vision;
    private int score;
    private Science science; // Corrigé pour être un objet Science

    public Player(String name, int number, int taille) {
        this.name = name;
        this.number = number;
        this.science = new Science(); // Initialiser correctement l'objet Science
        this.cities = new ArrayList<City>();
        this.troops = new ArrayList<Troop>();
        this.gold = 0;
        this.vision = new int[taille][taille];
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                this.vision[i][j] = 0;
            }
        }
        this.science = new Science();
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public void removeCity(City city) {
        cities.remove(city);
    }

    public void addTroop(Troop troop) {
        troops.add(troop);
    }

    public void removeTroop(Troop troop) {
        troops.remove(troop);
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public ArrayList<Troop> getTroops() {
        return troops;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int[][] getVision() {
        return vision;
    }

    public void setVision(int[][] vision) {
        this.vision = vision;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addSciencePoints(int points) {
        this.science.addSciencePoints(points);
    }

    public Science getScience() {
        return this.science;
    }

    public void setUpTurn() {
        for (Troop troop : troops) {
            troop.setCanMove(true);
            incrementScience();
            this.science.updateTechScience(); // Mise à jour de la science
        }
        for (City city : cities) {
            city.setCanDo(true);
            city.updateProduction();
            city.updateParameters();
            this.gold += city.getGold();
        }
        
    }

    public void endTurn() {
        for (Troop troop : troops) {
            troop.setCanMove(false);
        }
        for (City city : cities) {
            city.setCanDo(false);
        }
    }

    public void incrementScience() {
        for (City city : cities) {
            this.science.addSciencePoints(city.getScience());
        }
    }
}
