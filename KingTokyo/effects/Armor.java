package KingTokyo.effects;

import KingTokyo.game.*;
import KingTokyo.cards.*;
import KingTokyo.server.Server;

public class Armor extends Effect {
    public Armor(int armor) {
        super(armor);
    }

    /**
     * Adds armor to the current monster in the ´state´.
     * Marks the card as in use, so to not stack the effect.
     * @param state The state to affect.
     *  @param card The card the effect belongs to.
     */
    @Override
    public void execute(GameState state, Card card) {
        state.currentMonster.addArmor(getEffectValue());
        card.inUse = true;
    }
}