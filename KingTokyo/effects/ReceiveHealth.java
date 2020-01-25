package KingTokyo.effects;

import KingTokyo.cards.*;
import KingTokyo.game.*;

public class ReceiveHealth extends Effect {
    public ReceiveHealth(int health) {
        super(health);
    }

    /**
     * Adds health to the current monster.
     * @param state The state to affect.
     * @param card The card this effect belongs to.
     */
    @Override
    public void execute(GameState state, Card card) {
        state.currentMonster.addHealth(getEffectValue());
    }
}