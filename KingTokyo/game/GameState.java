package KingTokyo.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import KingTokyo.server.*;
import KingTokyo.monsters.*;
import KingTokyo.effects.*;
import KingTokyo.cards.*;

/**
 * Game State
 * 
 * Note: most of the functions in the GameState does not return anything and mostly
 * changes the state itself directly.
 */
public class GameState {
    public ArrayList<Monster> monsters;
    public Monster currentMonster;
    Monster tokyoMonster;

    Random ran;
    Deck deck;
    int whenInTokyoStars;
    int starsToWin;
    boolean gotClaws;

    String victor;
    String victoryMsg;

    int dieMaxValue;
    int defaultDice;
    ArrayList<Dice> rolledDice;
    String[] diceToReroll;
    HashMap<Dice, Integer> diceResult;

    // Add effects here
    public int numRerolls;
    public int extraDice;
    public int moreDamage;
    public int starsWhenAttacking;
    public int cardsCostLess;
    
    public GameState(ArrayList<Monster> monsters, Deck deck) {
        this.monsters = monsters;
        this.deck = deck;
        Collections.shuffle(monsters);
        ran = new Random();
        dieMaxValue = 6;
        defaultDice = 6;
        whenInTokyoStars = 1;
        starsToWin = 20;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public String[] getDiceToReroll() {
        return diceToReroll;
    }

    public ArrayList<Dice> getRolledDice() {
        return rolledDice;
    }

    public HashMap<Dice, Integer> getDiceResult() {
        return diceResult;
    }

    public void setRolledDice(ArrayList<Dice> rolledDice) {
        this.rolledDice = rolledDice;
    }

    public Deck getDeck() {
        return deck;
    }

    /**
     * Resets the state effects and properties to their default values.
     * Also updates the current monster to the given monster.
     * @param monster The monster whose round it is.
     */
    public void resetRound(Monster monster) {
        setCurrentMonster(monster);
        numRerolls = 2;
        extraDice = 0;      
        starsWhenAttacking = 0;
        cardsCostLess = 0;  
        moreDamage = 0;
        rolledDice = new ArrayList<Dice>();
        diceResult = new HashMap<Dice, Integer>();
    }

    // Needs to be set every round so that card effects can know whose round it is
    public void setCurrentMonster(Monster monster) {
        currentMonster = monster;
    }

    /**
     * Checks for main.cards in the monster's inventory that can be used for
     * this round. If usable executes the card on this instance of
     * GameState.
     * @param monster Monster on which hand to check for card main.effects.
     * @param round The current round.
     */
    public void checkCardEffects(Monster monster, Round round) {
        for(Card card: monster.getCards()) {
            if(card.canBeUsed(round)) {
                card.useCard(this);
            }
        }
    }

    public void giveStarInTokyo(Monster monster) {
        if(monster.isInTokyo()) {
            monster.addStars(whenInTokyoStars);
        }
    }

    /**
     * Displays information/statistics of all monsters to the current monster.
     * @param monster The monster to display statistics for.
     */
    public void newRoundStatistics(Monster monster) {
        String statusUpdate = "INFO:You are " + monster.getName() + " and it is your turn. Here are the stats";
        for(int count = 0; count < monsters.size(); count++) {
            Monster otherMonster = monsters.get(count);
            statusUpdate += ":"+otherMonster.getName() + (otherMonster.isInTokyo()?" is in Tokyo ":" is not in Tokyo ");
            statusUpdate += "with " + otherMonster.currentHealth + " health, " + otherMonster.stars + " stars, ";
            statusUpdate += otherMonster.energy + " energy, and owns the following main.cards:";
            statusUpdate += otherMonster.cardsToString();
        }
        Server.sendMessage(monster, statusUpdate+"\n");
    }

    /**
     * Rolls all the dice available to the current monster in the state.
     * Rolls the default dice value (6) and any extra dice that any effects
     * might give to the monster.
     */
    public void rollDice() {
        rolledDice = diceRoll(defaultDice + extraDice);
    }

    private ArrayList<Dice> diceRoll(int nrOfDice) {
        ArrayList<Dice> dice = new ArrayList<Dice>();
        for(int i = 0; i < nrOfDice; i++) {
            dice.add(new Dice(ran.nextInt(dieMaxValue)));
        }
        return dice;
    }

    public void askWhichDiceToReroll(Monster monster) {
        String statusUpdate = "ROLLED:You rolled:\t[1]\t[2]\t[3]\t[4]\t[5]\t[6]:";
        for(Dice dice : rolledDice) {
            statusUpdate+="\t[" + dice + "]";
        }
        statusUpdate += ":Choose which dice to reroll, separate with comma and in decending order (e.g. 5,4,1 or 0 to skip)\n";
        
        diceToReroll = Server.sendMessage(monster, statusUpdate).split(",");
    }

    public void rerollDice() {
        if(Integer.parseInt(diceToReroll[0]) != 0) {
            for(String s : diceToReroll) {
                rolledDice.remove(Integer.parseInt(s) - 1);
            }
        }
        rolledDice.addAll(diceRoll(6-rolledDice.size()));
    }

    public void sumUpDice(Monster monster) {
        Collections.sort(rolledDice);
        for(Dice unique : new HashSet<Dice>(rolledDice)) {
            diceResult.put(unique, Collections.frequency(rolledDice, unique));
        }
    }

    public void sendDiceResult(Monster monster) {
        Server.sendMessage(monster, "ROLLED:You rolled " + diceResult + " Press [ENTER]\n");
    }

    /**
     * Checks whether the rolled dice contains triple or more of 1, 2 or 3.
     * @param monster Monster of the current round.
     */
    public void checkThreeOfANumber(Monster monster) {
        for(int num = 1; num < 4; num++) {
            if(diceResult.containsKey(new Dice(num)))
                if(diceResult.get(new Dice(num)).intValue() >= 3) {
                    monster.stars += num + (diceResult.get(new Dice(num)).intValue()-3);
                }        
        }
    }
    
    public void checkEnergy(Monster monster) {
        Dice anEnergy = new Dice(Dice.ENERGY);
        if(diceResult.containsKey(anEnergy)) {
            monster.energy += diceResult.get(anEnergy).intValue();
        }
    }

    /**
     * Checks whether any hearts has been rolled. If 3 or more hearts has been rolled,
     * a new evolution will be drawn.
     * @param monster Monster of the current round.
     */
    public void checkHearts(Monster monster) {
        Dice aHeart = new Dice(Dice.HEART);
        if (diceResult.containsKey(aHeart)) { // +1 currentHealth per heart, up to maxHealth
            if(!monster.isInTokyo()) {
                if (monster.currentHealth + diceResult.get(aHeart).intValue() >= monster.maxHealth) {
                    monster.currentHealth = monster.maxHealth;
                } else {
                    monster.currentHealth += diceResult.get(aHeart).intValue();
                }
            }

            // 6b. 3 hearts = power-up
            if (diceResult.get(aHeart).intValue() >= 3) {
                getPowerUp(monster);
            }
        }
    }

    /**
     * Unlocks a new evolution card for the monster. Tries to use the evolution card if possible.
     * If it is a permanent card it is placed in the monster's hand.
     * @param monster The monster to unlock an evolution card for.
     */
    public void getPowerUp(Monster monster) {
        // Deal a power-up card to the monster
        if (monster.hasEvoCards()) {
            EvolutionCard evoCard = monster.drawNewEvolutionCard();
            if(!evoCard.permanent) {
                evoCard.useCard(this);
            } else {
                monster.addCardToHand(evoCard);
            }
        }
    }

    /**
     * Check whether any claws has been rolled. If there are any and the monster is in Tokyo,
     * attack everyone outside. If outside Tokyo, attack the monster in Tokyo if any.
     * @param monster
     */
    public void checkClaws(Monster monster) {
        Dice aClaw = new Dice(Dice.CLAWS);
        if(diceResult.containsKey(aClaw)) {
            monster.addStars(starsWhenAttacking);

            if(monster.isInTokyo()) {
                attackFromTokyo(monster);
            } else {
                attackTokyo(monster);
            }
        }  
    }

    public void attackFromTokyo(Monster monster) {
        for(Monster otherMonster : monsters) {
            int totalDamage = diceResult.get(new Dice(Dice.CLAWS)).intValue() + moreDamage;
            if(otherMonster != monster && totalDamage > otherMonster.getArmor()) { //Armor Plating
                otherMonster.takeDamage(totalDamage);
            }
        }    
    }

    public void attackTokyo(Monster monster) {
        for(Monster otherMonster : monsters) {
            if(otherMonster.isInTokyo()){
                int totalDamage = diceResult.get(new Dice(Dice.CLAWS)).intValue() + moreDamage;

                if(totalDamage > otherMonster.getArmor()) { //Armor Plating
                    otherMonster.takeDamage(totalDamage);
                }
            }
        }
    }

    public void askAttackedMonsterToLeave() {  
        if(tokyoMonster != null) {
            String msg = "ATTACKED:You have " + tokyoMonster.currentHealth + 
                        " health left. Do you wish to leave Tokyo? [YES/NO]\n";
            String response = Server.sendMessage(tokyoMonster, msg);   
            if(response.equalsIgnoreCase("YES")) {
                tokyoMonster.setInTokyo(false);
                tokyoMonster = null;
            }
        }     
    }

    public void enterTokyo(Monster monster) {
        if(tokyoMonster == null) {
            monster.setInTokyo(true);
            monster.addStars(1);
        }
    }

    public void askWhichCardsToBuy(Monster monster) {
        int buy = -2;

        while(buy == -2) {
            String response = Server.sendMessage(monster, "PURCHASE:Do you want to buy any of the cards from the store? (you have " + monster.getEnergy() + " energy) [-1 to cancel or -2 to reset]:" + deck + "\n");
            buy = Integer.parseInt(response);

            buyPowerCards(monster, buy);
        }
    }

    /**
     * Buys power cards for the monster. If the card is a DISCARD card it will use it immediately. Else
     * it is placed into the monster's hand.
     * @param monster Monster to buy cards for.
     * @param buy The option for which card to buy. 
     */
    public void buyPowerCards(Monster monster, int buy) {
        if(buy >= 0 && (monster.getEnergy() >= (deck.store[buy].cost - cardsCostLess))) { //Alien Metabolism
            if(deck.store[buy].discard) {
                //7a. Play "DISCARD" main.cards immediately
                deck.store[buy].useCard(this);
                deck.discardPile.add(deck.store[buy]); // Add to discard pile
            } else {
                monster.getCards().add(deck.store[buy]);
            }
            //Deduct the cost of the card from energy
            monster.energy -= (deck.store[buy].cost - cardsCostLess); //Alient Metabolism
            //Draw a new card from the deck to replace the card that was bought
            deck.store[buy] = deck.drawCard();
        } else if(buy == -2) {
            deck.resetStore();
        } 
    }

    public String getVictor() {
        return victor;
    }

    public String getVictoryMessage() {
        return victoryMsg;
    }

    /**
     * Checks whether any monster has won, either by having 20 stars or
     * being the only one alive. Sets the victor name and the victory message
     * in the state. If victor has been found {@link #getVictor()} will get the name
     * of the victor and {@link #getVictoryMessage()} will return the victory message.
     * Returns true if a victor has been found, else false.
     * @return Returns boolean.
     */
    public boolean checkVictory() {
        int alive = 0; 
        Monster aliveMonster = null;
        
        for(Monster monster : monsters) {
            if(monster.getStars() >= starsToWin) {
                victor = monster.getName();
                victoryMsg = " has won by stars\n";
                return true;
            }
            
            if(monster.getHealth() > 0) {
                alive++; 
                aliveMonster = monster;
            }
        }
        
        if(alive==1) {
            victor = aliveMonster.getName();
            victoryMsg = " has won by being the only one alive\n";
            return true;
        }

        return false;
    }
}