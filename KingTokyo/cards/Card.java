package KingTokyo.cards;

import KingTokyo.effects.*;
import KingTokyo.game.GameState;

public class Card {
    public String name;
    public int cost;
    public boolean discard;
    private Effect[] effects;
    public String description;
    public boolean inUse;
    protected Round whenToUse;


    public Card(String name, int cost, boolean discard, Effect[] effects, Round whenToUse, String description) {
        this.name = name;
        this.cost = cost;
        this.discard = discard;
        this.effects = effects;
        this.description = description;
        this.whenToUse = whenToUse;
        inUse = false;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    /**
     * Checks whether this card can be used for this round or if it's already in use.
     * @param round The round to check for.
     * @return boolean
     */
    public boolean canBeUsed(Round round) {
        if(round == whenToUse || !inUse) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Uses this card for the current monster in the state.
     * Should check if the card can be used before calling this.
     * @param state The state to use the card on.
     */
    public void useCard(GameState state) {
        if(!inUse) {
            if(discard) {
                inUse = true;
            }
            for(Effect effect : effects) {
                effect.execute(state, this);
            }
        }
    }

    public Effect[] getEffects() {
        return effects;
    }

    public String toString() {
        return name + ", Cost " + cost + ", " + (discard?"DISCARD":"KEEP") + ", Effect: " + description;
    }

}