package com.mygdx.civ.map;

import java.util.Random;
import java.util.ArrayList;







/* la classe Grid permet de créer la grille de jeu qui va être utilisé dans tout le reste du code
 */
public class Grid {

    
    private Tile[][] grille; 
    private int taille;

    
    /* construction d'une grille a l'aide du nombre de case par coté
        * @param taille le nombre de case par coté
     */
    public Grid(int taille) {
        this.taille = taille;
        this.grille = new Tile[taille][taille];
        //on rempli la grille de case d'eau
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                this.grille[i][j] = new Tile(i, j, Tile.Biome.Eau);
            }
        }
    }

    public Tile[][] getGrille() {
        return this.grille;
    }

    public int getTaille() {
        return this.taille;
    }

    /* cette méthode prend la grille seulement remplie d'eau et la génère en utilisant un bruit de perlin, elle place aussi des fôrets et des ressources 
     */
    public void Generation() {
        Random rand = new Random();
        int seed = rand.nextInt(10000); //on génère un seed aléatoire
        float[][] noisemap = PerlinNoise.generatePerlinNoise(taille, taille, 3, 0.4f, seed); //on appelle la méthode de génération du bruit de perlin avec la seed ce qui assure une carte aléatoire

        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                //en fonction de la valeur de la noisemap on attribue les biomes, Eau, Plage, Plaine, Montagne
                if (noisemap[i][j] > 0.6) {
                    this.grille[i][j].setBiome(Tile.Biome.Eau);
                } 
                
                else {
                    if (noisemap[i][j] > 0.54)
                        this.grille[i][j].setBiome(Tile.Biome.Plage);
                    else {
                        if (noisemap[i][j] > 0.2) {
                            this.grille[i][j].setBiome(Tile.Biome.Plaine);
                            int chance = rand.nextInt(100);
                            if (chance < 1) {
                            this.creerForet(i,j); //la construction de foret est une méthode a part	sinon c'est trop lourd
                            }
                            else {
                            this.creerRessource(i,j);
                            }
                        }
                        else {
                        
                        this.grille[i][j].setBiome(Tile.Biome.Montagne);
                        }
                    }
                }
                
            }
        }
        // la deuxième boucle sert a éviter d'avoir des tuiles de sables seule au milieu de plaine 
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (this.grille[i][j].getBiome() == Tile.Biome.Plage) {
                    int[][] array = new int[8][2];
                    array = this.getVoisins(i,j);
                    boolean plainepartout = true;
                    int length = array.length;
                    for (int k = 0; k < length; k++) {
                        if (array[k][0] >= 0 && array[k][0] < this.taille && array[k][1] >= 0 && array[k][1] < this.taille ) {
                        if (this.grille[array[k][0]][array[k][1]].getBiome() == Tile.Biome.Plage || this.grille[array[k][0]][array[k][1]].getBiome() == Tile.Biome.Eau) {
                            plainepartout = false;
                        }
                        }
                        
                    }
                    if (plainepartout) {
                        this.grille[i][j].setBiome(Tile.Biome.Plaine);
                    }
                }
            }
        }
        
    }

    /* cette méthode permet de créer des forets sur la carte
     * @param i la position en x de la case
     * @param j la position en y de la case
     */
    public void creerForet(int i, int j) {
        this.grille[i][j].setRessource(Tile.Ressource.Foret);
        Random rand = new Random();
        int chance;
        int[][] array = new int[8][2];
        array = this.getVoisins(i,j);
        for (int k = 0; k < array.length; k++) {
            if (array[k][0] >= 0 && array[k][0] < this.taille && array[k][1] >= 0 && array[k][1] < this.taille) {
                chance = rand.nextInt(100);
                if (chance < 60) {
                    this.grille[array[k][0]][array[k][1]].setRessource(Tile.Ressource.Foret);
                }
            } 
        }
    }

    /* cette méthode permet de décider si une ressource est placée sur la carte
     * @param i la position en x de la case
     * @param j la position en y de la case
     */
    public void creerRessource(int i, int j) {
        Random rand = new Random();
        int chance = rand.nextInt(100);
        int index = rand.nextInt(3);
        if (chance < 2) {
            this.grille[i][j].setRessource(Tile.Ressource.values()[index]);
        }

    }

    public int[][] getVoisins(int i, int j) {
        int[][] voisins = new int[8][2];
        voisins[0][0] = i - 1;
        voisins[0][1] = j - 1;
        voisins[1][0] = i - 1;
        voisins[1][1] = j;
        voisins[2][0] = i - 1;
        voisins[2][1] = j + 1;
        voisins[3][0] = i;
        voisins[3][1] = j - 1;
        voisins[4][0] = i;
        voisins[4][1] = j + 1;
        voisins[5][0] = i + 1;
        voisins[5][1] = j - 1;
        voisins[6][0] = i + 1;
        voisins[6][1] = j;
        voisins[7][0] = i + 1;
        voisins[7][1] = j + 1;
        return voisins;

    }

    
    public ArrayList<int[]> getVoisinsList(int i, int j) {
        int[][] modificateur = new int[8][2];
        modificateur[0][0] =  - 1;
        modificateur[0][1] =  - 1;
        modificateur[1][0] =  - 1;
        modificateur[1][1] = 0;
        modificateur[2][0] =  - 1;
        modificateur[2][1] =  1;
        modificateur[3][0] = 0;
        modificateur[3][1] =  - 1;
        modificateur[4][0] = 0;
        modificateur[4][1] =  1;
        modificateur[5][0] =  1;
        modificateur[5][1] = - 1;
        modificateur[6][0] =  1;
        modificateur[6][1] = 0;
        modificateur[7][0] = 1;
        modificateur[7][1] = 1;

        ArrayList<int[]> voisins = new ArrayList<int[]>();


        for (int k = 0; k < 8; k++) {
            if (!(i + modificateur[k][0] < 0 || i + modificateur[k][0] >= this.taille || j + modificateur[k][1] < 0 || j + modificateur[k][1] >= this.taille)) {
                int[] couple = new int[2];
                couple[0] = i + modificateur[k][0];
                couple[1] = j + modificateur[k][1];
                voisins.add(couple);
            }
        }
        return voisins;

    }
}


