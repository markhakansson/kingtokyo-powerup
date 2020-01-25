package KingTokyo.monsters;

import java.util.ArrayList;
import java.util.Collections;

public class MonsterFactory {
    int minNumMonsters;
    int maxNumMonsters;

    public MonsterFactory() {
        minNumMonsters = 2;
        // The currently supported amount of monsters. Increment this if new monsters are added.
        maxNumMonsters = 3; 
    }

    /**
     * Creates a Monster from the specified name.
     * 
     * @param monsterName Name of the monster to create.
     * @return The Monster.
     */
    public Monster getMonster(String monsterName) {
        if(monsterName.equalsIgnoreCase("Gigazaur")) {
            Monster gigazaur = new MonsterGigazaur();
            gigazaur.setupEvolutionCards();
            return gigazaur;
        } else if (monsterName.equalsIgnoreCase("Kong")) {
            Monster kong = new MonsterKong();
            kong.setupEvolutionCards();
            return kong;
        } else if (monsterName.equalsIgnoreCase("Alienoid")) {
            Monster alienoid = new MonsterAlienoid();
            alienoid.setupEvolutionCards();
            return alienoid;
        }

        // Add monsters here

        return null;
    }    

    /**
     * Creates a random ArrayList of Monsters.
     * @param numMonsters Number of monsters to create.
     * @return ArrayList<Monster> Monster list.
     */
    public ArrayList<Monster> getMonsters(int numMonsters) {
        ArrayList<Monster> monsters = generateAllMonsters(); 
        Collections.shuffle(monsters);

        if(numMonsters < minNumMonsters || numMonsters > maxNumMonsters) {
            return monsters;
        }

        // Returns a subslice with the wanted number of monsters
        monsters = new ArrayList<Monster>(monsters.subList(0, numMonsters));

        return monsters;
    }

    private ArrayList<Monster> generateAllMonsters() {
        ArrayList<Monster> monsters = new ArrayList<Monster>();
        monsters.add(getMonster("Gigazaur"));
        monsters.add(getMonster("Kong"));
        monsters.add(getMonster("Alienoid"));
        // Add monsters here
        
        return monsters;
    }
}