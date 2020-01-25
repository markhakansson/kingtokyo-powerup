package KingTokyo.cards;

import java.util.ArrayList;
import java.util.Collections;

import KingTokyo.effects.*;
import KingTokyo.game.*;
/**
 * Creates cards and generate new decks.
 * A card can contain one or more effects.
 */
public class CardFactory {

    /**
     * Creates a card with the specified name.
     * Returns null if the card could not be found.
     * @param cardName The name of the card.
     * @return 
     */
    public Card getCard(String cardName) {
        if(cardName.equalsIgnoreCase("Acid Attack")) {
            Effect[] effects = {new MoreDamage(1)};
            return new Card("Acid Attack", 6, false, effects, Round.RESOLVE_DICE, "Deal 1 extra damage each turn");

        } else if(cardName.equalsIgnoreCase("Alien Metabolism")) {
            Effect[] effects = {new CardsCostLess(1)};
            return new Card("Alien Metabolism", 3, false, effects, Round.BUY_CARDS, "Buying cards costs you 1 less");

        } else if(cardName.equalsIgnoreCase("Alpha Monster")) {
            Effect[] effects = {new StarsWhenAttacking(1)};
            return new Card("Alpha Monster", 5, false, effects, Round.RESOLVE_DICE, "Gain 1 star when you attack");

        } else if(cardName.equalsIgnoreCase("Apartment Building")) {
            Effect[] effects = {new ReceiveStars(3)};
            return new Card("Apartment Building", 5, true, effects, Round.IMMEDIATELY, "+3 stars");

        } else if(cardName.equalsIgnoreCase("Armor Plating")) {
            Effect[] effects = {new Armor(1)};
            return new Card("Armor Plating", 4, false, effects, Round.RESOLVE_DICE, "Ignore damage of 1");

        } else if(cardName.equalsIgnoreCase("Commuter Train")) {
            Effect[] effects = {new ReceiveStars(2)};
            return new Card("Commuter Train", 4, true, effects, Round.IMMEDIATELY, "+2 stars");

        } else if(cardName.equalsIgnoreCase("Corner Stone")) {
            Effect[] effects = {new ReceiveStars(1)};
            return new Card("Corner Stone", 3, true, effects, Round.IMMEDIATELY, "+1 stars");
        }

        // Add more cards here

        return null;
    }

    /**
     * Creates a pre-determined shuffled deck ready for normal play.
     * @return The normal deck.
     */
    public Deck createNormalDeck() {
        Deck deck = new Deck();

        ArrayList<Card> cards = createNormalCards();
        deck.add(cards);
        deck.shuffle();
        deck.setupStore();

        return deck;
    }

    /**
     * Creates a list of shuffled normal cards.
     * @return The normal cards.
     */
    public ArrayList<Card> createNormalCards() {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(getCard("Acid Attack"));
        cards.add(getCard("Alien Metabolism"));
        cards.add(getCard("Alpha Monster"));
        cards.add(getCard("Apartment Building"));
        cards.add(getCard("Armor Plating"));
        cards.add(getCard("Commuter Train"));
        cards.add(getCard("Corner Stone"));

        // Add more cards here

        Collections.shuffle(cards);
        return cards;
    }
}