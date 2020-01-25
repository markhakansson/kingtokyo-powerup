package KingTokyo.effects;

import KingTokyo.game.*;
import KingTokyo.cards.*;

public class ReceiveEnergy extends Effect {
    public ReceiveEnergy(int energy) {
        super(energy);
    }

    /**
     * Adds energy to the current monster.
     * @param state The state to affect.
     * @param card The card this effect belongs to.
     */
    @Override
    public void execute(GameState state, Card card) {
        state.currentMonster.addEnergy(getEffectValue());
    }
}