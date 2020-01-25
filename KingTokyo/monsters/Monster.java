package KingTokyo.monsters;

import java.util.ArrayList;
import KingTokyo.cards.*;

public abstract class Monster extends Player {
    public int maxHealth;
    public int currentHealth;
    public int energy;
    public int stars;
    public boolean inTokyo;
    public MonsterType monsterType;
    ArrayList<Card> cards;
    ArrayList<EvolutionCard> evoCards;

    public int armor;
    public int moreDamage;
    public int cardRebate;
    public int starsWhenAttacking;
    
    public Monster(String name, MonsterType monsterType) {
        maxHealth = 10;
        currentHealth = maxHealth;
        energy = 0;
        stars = 0;
        inTokyo = false;
        cards = new ArrayList<Card>();
        evoCards = new ArrayList<EvolutionCard>();
        setName(name);
    }
    
    /**
     * Setups this monster's evolution cards.
     * Creates them and inserts them to a seperate deck in their inventory.
     */
    public abstract void setupEvolutionCards();

    public void addArmor(int armor) {
        this.armor += armor;
    }

    public void addCardToHand(Card card) {
        cards.add(card);
    }

    public void addEnergy(int energy) {
        this.energy += energy;
    }

    public void addStars(int stars) {
        this.stars += stars;
    }

    public void addHealth(int health) {
        if((currentHealth + health) >= 10) {
            currentHealth = 10;
        } else {
            currentHealth += health;
        }
    }

    public int getArmor() {
        return armor;
    }

    public int getEnergy() {
        return energy;
    }

    public int getHealth() {
        return currentHealth;
    }

    public int getStars() {
        return stars;
    }

    public void setEnergy(int value) {
        energy = value;
    }

    public void setHealth(int value) {
        currentHealth = value;
    }

    public void setStars(int value) {
        stars = value;
    }

    public void removeEnergy(int energy) {
        this.energy -= energy;
    }

    public boolean isAlive() {
        return currentHealth <= 0 ? false: true;
    }

    public boolean isInTokyo() {
        return inTokyo;
    }

    public void setInTokyo(boolean inTokyo) {
        this.inTokyo = inTokyo;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public ArrayList<EvolutionCard> getEvoCards() {
        return evoCards;
    }

    public boolean hasEvoCards() {
        return !evoCards.isEmpty();
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    public EvolutionCard drawNewEvolutionCard() {
        return evoCards.remove(0);
    }

    public String cardsToString() {
        String returnString = "";
        if (cards.size() == 0)
            return "[NO CARDS]:";
        for(int i = 0; i < cards.size(); i++) {
            returnString += "\t[" + i + "] " + cards.get(i) + ":";
        }
        return returnString;
    }

}
