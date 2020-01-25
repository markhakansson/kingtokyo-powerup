package KingTokyo.effects;

import KingTokyo.game.*;
import KingTokyo.cards.*;

public abstract class Effect {
    protected int value;

    /**
     * Executes the effect on the game. The card the effect belongs to
     * is needed as a parameter to get the card information. It is up to the implementation
     * of the effect whether to use it or not. 
     * 
     * @param state The instance of the game to affect.
     * @param card The card this effect belongs to.
     */
    public abstract void execute(GameState state, Card card);
    
    public int getEffectValue() {
        return value;
    }

    public Effect(int value) {
        this.value = value;
    }
} 