package KingTokyo.effects;

import java.util.ArrayList;

import KingTokyo.game.*;
import KingTokyo.cards.*;

public class MoreDamage extends Effect {
    public MoreDamage(int dmg) {
        super(dmg);
    }

    /**
     *  Increments the ´moreDamage´ variable in the game state.
     *  @param state The state to affect.
     *  @param card The card this effect belongs to.
     */
    @Override
    public void execute(GameState state, Card card) {
        state.moreDamage += getEffectValue();
    }

}