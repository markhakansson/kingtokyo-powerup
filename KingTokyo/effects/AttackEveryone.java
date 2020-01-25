package KingTokyo.effects;

import KingTokyo.game.*;
import KingTokyo.cards.*;
import KingTokyo.monsters.*;

public class AttackEveryone extends Effect {
    public AttackEveryone(int damage) {
        super(damage);
    }

    /**
     * Deals damage to all monsters except the one whose round it currently is.
     * @param state GameState to affect.
     * @param card Card this effect belongs to.
     */
    @Override
    public void execute(GameState state, Card card) {
        for(Monster otherMonster : state.monsters) {
            if(otherMonster != state.currentMonster) {
                otherMonster.takeDamage(getEffectValue());;
            }
        }
    }
}