package com.mygdx.civ.players;

import java.util.HashMap;

public class Science {

    private int totalSciencePoints; // Points de science accumulés
    private HashMap<String, Integer> technologies; // Technologies découvertes
    private HashMap<String, Integer> technologyCosts; // Coûts en points de science des technologies

    public Science() {
        this.totalSciencePoints = 0;
        this.technologies = new HashMap<>();
        this.technologyCosts = new HashMap<>();
        
        // Initialisation des technologies et leurs coûts
        initializeTechnologies();
    }

    private void initializeTechnologies() {
        // Exemple de technologies
        technologies.put("Ecriture", 0);
        technologies.put("Metallurgie", 0);
        technologies.put("Construction", 0);
        technologies.put("Ingénierie", 0);

        technologyCosts.put("Ecriture", 50);
        technologyCosts.put("Metallurgie", 100);
        technologyCosts.put("Construction", 200);
        technologyCosts.put("Ingénierie", 300);
    }

    public int getTotalSciencePoints() {
        return totalSciencePoints;
    }

    public void addSciencePoints(int points) {
        this.totalSciencePoints += points;
    }

    public void updateTechScience(){
        if (this.totalSciencePoints >= 50 && this.totalSciencePoints < 100){
            researchTechnology("Ecriture");
        } else if(this.totalSciencePoints >= 100 && this.totalSciencePoints < 200){
            researchTechnology("Metallurgie");
        } else if(this.totalSciencePoints >= 100 && this.totalSciencePoints < 200){
            researchTechnology("Construction");
        } else {
            researchTechnology("Ingénierie");
        }
    }

    public void researchTechnology(String techName) {
        if (technologies.containsKey(techName) && technologies.get(techName) == 0) {
            int cost = technologyCosts.get(techName);
            if (totalSciencePoints >= cost) {
                totalSciencePoints -= cost;
                technologies.put(techName, 1);

            }
        }

    }

    public int isTechnologyResearched(String techName) {
        return technologies.getOrDefault(techName,0);
    }

    public HashMap<String, Integer> getTechnologies() {
        return technologies;
    }

}



