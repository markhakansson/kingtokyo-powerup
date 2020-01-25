package KingTokyo.game;

import java.util.ArrayList;

import KingTokyo.monsters.*;
import KingTokyo.effects.*;
import KingTokyo.server.Server;

public class Game {
    public GameState state;

    public Game(ArrayList<Monster> monsters, Deck deck) {
        state = new GameState(monsters, deck);
    }

    public void run() {
        while(true) {   
            for(Monster monster : state.monsters) {
                if(monster.isAlive()) {
                    beginRound(monster);
                    rollDiceRound(monster);
                    resolveDiceRound(monster);
                    buyCardsRound(monster);
                    endRound(monster);
                }
            }   
        }
    }

    private void beginRound(Monster monster) {
        state.resetRound(monster);
        state.checkCardEffects(monster, Round.START);
        state.giveStarInTokyo(monster);
        state.newRoundStatistics(monster);
    }

    private void rollDiceRound(Monster monster) {
        state.checkCardEffects(monster, Round.ROLL_DICE);
        state.rollDice();
        for(int i = 0; i < state.numRerolls; i++) {
            state.askWhichDiceToReroll(monster);
            state.rerollDice();
        }
        state.sumUpDice(monster);
        state.sendDiceResult(monster);
    }

    private void resolveDiceRound(Monster monster) {
        state.checkCardEffects(monster, Round.RESOLVE_DICE);
        state.checkThreeOfANumber(monster);
        state.checkHearts(monster);
        state.checkEnergy(monster);
        state.checkClaws(monster);
        state.askAttackedMonsterToLeave();
        state.enterTokyo(monster);
    }

    private void buyCardsRound(Monster monster) {
        state.checkCardEffects(monster, Round.BUY_CARDS);
        state.askWhichCardsToBuy(monster);
    }

    private void endRound(Monster monster) {
        state.checkCardEffects(monster, Round.END);
        if(state.checkVictory()) {
            for(Monster someMonster : state.getMonsters()) {
                Server.sendMessage(someMonster, "Victory: " + state.getVictor() + " " + state.getVictoryMessage());
            }
            System.exit(0);
        }
    }


}