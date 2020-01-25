package test.game;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

import KingTokyo.cards.*;
import KingTokyo.game.*;
import KingTokyo.monsters.*;

public class GameStateTest {
    MonsterFactory monsterFactory;
    ArrayList<Monster> monsters;
    CardFactory cardFactory;
    Deck deck;
    GameState state;

    private void initialize(int numMonsters) {
        monsterFactory = new MonsterFactory();
        monsters = monsterFactory.getMonsters(numMonsters);
        cardFactory = new CardFactory();
        deck = cardFactory.createNormalDeck();
        state = new GameState(monsters, deck);
    }

    // REQ 2: Set victory points to 0
    @Test
    public void testZeroVictoryPoints() {
        MonsterFactory factory = new MonsterFactory();
        ArrayList<Monster> monsters = factory.getMonsters(1);     
        for(Monster monster : monsters) {
            assertEquals(0, monster.getStars());
        }
    }

    // REQ 3: Set life to 10
    @Test
    public void testLifeIsTen() {
        MonsterFactory factory = new MonsterFactory();
        ArrayList<Monster> monsters = factory.getMonsters(1);
        for(Monster monster: monsters) {
            assertEquals(10, monster.getHealth());
        }
    }

    // REQ 4.1: Shuffle store main.cards,
    // REQ 4.2: Start with 3 main.cards face up
    @Test
    public void testFaceUpCards() {
        CardFactory factory = new CardFactory();
        Deck deck = factory.createNormalDeck();
        Card[] store = deck.getStore();
        for(Card card : store) {
            assertNotNull(card);
        }
    }

    // REQ 5: Shuffle evolution main.cards
    // REQ 6: Randomize which monster starts the main.game

    // REQ 7: If monster not in Tokyo - gain no stars
    @Test
    public void testStarIfNotInTokyo() {
        initialize(1);
        state.giveStarInTokyo(monsters.get(0));
        assertEquals(0, monsters.get(0).getStars());
    }

    // REQ 7: If monster in Tokyo - gain 1 star
    @Test
    public void testStarIfInTokyo() {
        initialize(1);

        Monster currentMonster = monsters.get(0);
        currentMonster.setInTokyo(true);

        state.resetRound(currentMonster);
        state.giveStarInTokyo(currentMonster);
        assertEquals(1, currentMonster.getStars());
    }

    // REQ 8: Roll 6 dice
    @Test
    public void testRollSixDice() {
        initialize(1);

        state.rollDice();

        // Check if the six dice rolls are not null
        for(int i = 0; i < 6; i++) {
            assertNotNull(state.getRolledDice().get(i));
        }
    }

    // REQ 9: Select which of your dice to reroll

    // REQ 10: Reroll the selected dice
    public void testRerollDice() {

    }

    // REQ 12:
    // Test 3, 4, 5 and 6 ones.
    @Test
    public void testMultipleDiceNum() {
        initialize(1);     
        Monster currentMonster = monsters.get(0);
        state.resetRound(currentMonster);

        ArrayList<Dice> dice = new ArrayList<Dice>();
        dice.add(new Dice(1));
        dice.add(new Dice(1));
        dice.add(new Dice(1));

        for(int i = 0; i < 3; i++) {

            state.setRolledDice(dice);
            state.sumUpDice(currentMonster);
            state.checkThreeOfANumber(currentMonster);

            assertEquals(1 + i, currentMonster.getStars());
            dice.add(new Dice(1));
            currentMonster.setStars(0);
        }
    }

    @Test
    public void testEnergyFromDice() {
        initialize(1);
        Monster currentMonster = monsters.get(0);
        state.resetRound(currentMonster);

        ArrayList<Dice> dice = new ArrayList<Dice>();
        dice.add(new Dice(4));

        for(int i = 0; i < 5; i++) {
            state.setRolledDice(dice);
            state.sumUpDice(currentMonster);
            state.checkEnergy(currentMonster);

            assertEquals(1 + i, currentMonster.getEnergy());
            dice.add(new Dice(4));
            currentMonster.setEnergy(0);
        }
    }

    @Test
    public void testHeartInTokyo() {
        initialize(1);
        Monster currentMonster = monsters.get(0);
        state.resetRound(currentMonster);

        currentMonster.setInTokyo(true);
        currentMonster.setHealth(1);
        ArrayList<Dice> dice = new ArrayList<Dice>();
        dice.add(new Dice(0));

        state.setRolledDice(dice);
        state.sumUpDice(currentMonster);
        state.checkHearts(currentMonster);

        assertEquals(1, currentMonster.getHealth());
    }

    // Test if hearts received
    @Test
    public void testHeartOutsideTokyo() {
        initialize(1);
        Monster currentMonster = monsters.get(0);
        state.resetRound(currentMonster);

        currentMonster.setHealth(1);

        ArrayList<Dice> dice = new ArrayList<Dice>();
        dice.add(new Dice(Dice.HEART));
        dice.add(new Dice(Dice.HEART));
        state.setRolledDice(dice);
        state.sumUpDice(currentMonster);
        state.checkHearts(currentMonster);

        assertEquals(3, currentMonster.getHealth());
    }

    @Test
    public void testReceiveEvolutionCard() {
        initialize(1);
        Monster currentMonster = monsters.get(0);
        state.resetRound(currentMonster);
        int numOfEvoCards = currentMonster.getEvoCards().size();
        ArrayList<Dice> dice = new ArrayList<Dice>();
        dice.add(new Dice(0));
        dice.add(new Dice(0));
        dice.add(new Dice(0));
        
        state.setRolledDice(dice);
        state.sumUpDice(currentMonster);
        state.checkHearts(currentMonster);

        assertEquals(numOfEvoCards - 1, currentMonster.getEvoCards().size());

    }

    // Test if damage is dealt to monsters outside of Tokyo
    @Test
    public void testClawsInsideTokyo() {
        initialize(3);
        Monster currentMonster = monsters.get(0);
        currentMonster.setInTokyo(true);
        state.resetRound(currentMonster);

        ArrayList<Dice> dice = new ArrayList<Dice>();
        dice.add(new Dice(Dice.CLAWS));

        state.setRolledDice(dice);
        state.sumUpDice(currentMonster);
        state.checkClaws(currentMonster);

        for(Monster monster : monsters) {
            if(monster != currentMonster) {
                assertEquals(9, monster.getHealth());
            }
        }
    }

    @Test
    public void testClawsOutsideTokyo() {
        initialize(3);
        Monster currentMonster = monsters.get(0);
        Monster monsterInTokyo = monsters.get(1);
        Monster monsterNotInTokyo = monsters.get(2);

        monsterInTokyo.setInTokyo(true);
        state.resetRound(currentMonster);
        ArrayList<Dice> dice = new ArrayList<Dice>();
        dice.add(new Dice(Dice.CLAWS));

        state.setRolledDice(dice);
        state.sumUpDice(currentMonster);
        state.checkClaws(currentMonster);

        assertEquals(10, currentMonster.getHealth());
        assertEquals(10, monsterNotInTokyo.getHealth());
        assertEquals(9, monsterInTokyo.getHealth());
    }

    // REQ 13: Purchase a card
    @Test
    public void testPurchaseCard() {
        initialize(1);
        int numOfCards = deck.deck.size();

        Monster currentMonster = monsters.get(0);
        currentMonster.setEnergy(20);
        state.resetRound(currentMonster);

        state.buyPowerCards(currentMonster, 1);

        assertEquals(numOfCards - 1, state.getDeck().deck.size());
    }

    @Test
    public void testResetStore() {
        initialize(1);
        Monster currentMonster = monsters.get(0);
        currentMonster.setEnergy(20);
        state.resetRound(currentMonster);

        state.buyPowerCards(currentMonster, -2);

        assertEquals(3, state.getDeck().discardPile.size());
    }

    @Test
    public void testDiscardCards() {
        MonsterFactory monsterFactory = new MonsterFactory();
        ArrayList<Monster> monsters = monsterFactory.getMonsters(1);
        Monster currentMonster = monsters.get(0);
        currentMonster.setEnergy(20);
        CardFactory cardFactory = new CardFactory();
        Deck deck = new Deck();
        ArrayList<Card> cards = new ArrayList<Card>();

        cards.add(cardFactory.getCard("Apartment Building"));
        cards.add(cardFactory.getCard("Commuter Train"));
        cards.add(cardFactory.getCard("Corner Stone"));
        deck.add(cards);
        deck.setupStore();

        GameState state = new GameState(monsters, deck);
        state.resetRound(currentMonster);
        state.buyPowerCards(currentMonster, 1);

        assertEquals(true, currentMonster.getCards().isEmpty());    
    }

    @Test
    public void testKeepCards() {
        MonsterFactory monsterFactory = new MonsterFactory();
        ArrayList<Monster> monsters = monsterFactory.getMonsters(1);
        Monster currentMonster = monsters.get(0);
        currentMonster.setEnergy(20);
        CardFactory cardFactory = new CardFactory();
        Deck deck = new Deck();
        ArrayList<Card> cards = new ArrayList<Card>();
        
        cards.add(cardFactory.getCard("Acid Attack"));
        cards.add(cardFactory.getCard("Alien Metabolism"));
        cards.add(cardFactory.getCard("Alpha Monster"));
        deck.add(cards);
        deck.setupStore();

        GameState state = new GameState(monsters, deck);
        state.resetRound(currentMonster);
        state.buyPowerCards(currentMonster, 1);

        assertEquals(false, currentMonster.getCards().isEmpty());
    }

    @Test
    public void testVictoryByStars() {
        initialize(3);
        Monster currentMonster = monsters.get(0);
        currentMonster.setStars(20);

        state.resetRound(currentMonster);
        
        assertEquals(true, state.checkVictory());
    }

    // Test sole survivor
    @Test
    public void testVictoryBattleRoyale() {
        initialize(3);
        Monster currentMonster = monsters.get(0);
        Monster otherMonster1 = monsters.get(1);
        Monster otherMonster2 = monsters.get(2);

        otherMonster1.setHealth(0);
        otherMonster2.setHealth(0);

        state.resetRound(currentMonster);

        assertEquals(true, state.checkVictory());
    }

    @Test
    public void testZeroHealthDead() {
        initialize(3);
        Monster monster1 = monsters.get(0);
        Monster monster2 = monsters.get(1);
        Monster monster3 = monsters.get(2);

        monster1.setHealth(0);
        monster2.setHealth(-2);

        assertEquals(false, monster1.isAlive());
        assertEquals(false, monster2.isAlive());
        assertEquals(true, monster3.isAlive());
    }
    
}