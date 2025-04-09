package com.mygdx.civ.map;

import com.mygdx.civ.map.Tile;
import com.mygdx.civ.players.City;
import com.mygdx.civ.troops.Troop;


public class Tile {
    
    //Attributes
    private int x;
    private int y;
    private Biome biome;
    private Ressource ressource;
    private boolean estVille;
    private boolean EstCentre;
    private int[] Exploite;
    private boolean unitepresente;
    private Troop troop;
    private City city;

    public enum Biome {
        Eau,
        Plage,
        Plaine,
        Montagne,
        Rien
    }
    
    public enum Ressource {
        Pierre,
        Betail,
        Foret,
        Rien
    }

    
    //Constructors
    public Tile(int x, int y, Biome biome) {
        this.x = x;
        this.y = y;
        this.biome = biome;
        this.ressource = Ressource.Rien;
        this.estVille = false;
        this.EstCentre = false;
        this.Exploite = new int[5];
        this.unitepresente = false;
        this.troop = null;
    }
    
    //Methods
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public Biome getBiome() {
        return this.biome;
    }
    
    public Ressource getRessource() {
        return this.ressource;
    }

    public boolean getEstVille() {
        return this.estVille;
    }

    public boolean getEstCentre() {
        return this.EstCentre;
    }

    public int[] getExploite() {
        return this.Exploite;
    }

    public boolean getUnitePresente() {
        return this.unitepresente;
    }

    public Troop getTroop() {
        return this.troop;
    }

    public City getCity() {
        return this.city;
    }
    
    public void setBiome(Biome biome) {
        this.biome = biome;
    }
    
    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
    }

    public void setEstVille(boolean estVille) {
        this.estVille = estVille;
    }

    public void setEstCentre(boolean EstCentre) {
        this.estVille = EstCentre;
        this.EstCentre = EstCentre;
    }

    public void setUnitePresente(boolean unitepresente) {
        this.unitepresente = unitepresente;
    }

    public void setTroop(Troop troop) {
        this.troop = troop;
    }

    public void setCity(City city) {
        this.city = city;
    }

    /* Méthode qui permet de calculer la distance entre deux tuiles a l'entier pret 
    * (c'est a dire que si la distance est 1.5 on renvoie 1 et donc que un cercle est un carré)
    * @param tile la tuile avec laquelle on veut calculer la distance
    * @return la distance entre les deux tuiles
    */
    public int distance(Tile tile) {
        return (int) Math.sqrt(Math.pow(this.getX() - tile.getX(), 2) + Math.pow(this.getY() - tile.getY(), 2));
    }

    /* Méthode qui permet de créer une ressource sur la tuile
    */
    public void creerRessource() {
        return;
    }

    


   


    
    


}
