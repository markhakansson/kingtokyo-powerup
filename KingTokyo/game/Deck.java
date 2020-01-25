package KingTokyo.game;

import java.util.ArrayList;
import java.util.Collections;
import KingTokyo.cards.*;

public class Deck {
    private int noFaceUpCards;
    public ArrayList<Card> deck;
    public Card[] store;
    public ArrayList<Card> discardPile;

    public Deck() {
        noFaceUpCards = 3;
        deck = new ArrayList<Card>();
        discardPile = new ArrayList<Card>();
        store = new Card[noFaceUpCards];
    }

    /**
     * Start the game with 3 cards face up in the store
     */ 
    public void setupStore() {
        for(int i = 0; i < noFaceUpCards; i++) {
            store[i] = drawCard();
        }
    }

    public void add(ArrayList<Card> cards) {
        deck = cards;
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card[] getStore() {
        return store;
    }

    /**
     * Draws a card from the deck if there is any left.
     * If the deck is empty it takes the discard pile, shuffles it and 
     * uses those card as the new deck.
     * 
     * If neither the deck or discard piles have any cards left, returns null.
     */
    public Card drawCard() {
        if(deck.isEmpty() && !discardPile.isEmpty()) {
            for(int i = 0; i < discardPile.size(); i++) {
                deck.add(discardPile.remove(i));
            }
            Collections.shuffle(deck);
        } else if(deck.isEmpty()) {
            return null;
        }
        
        return deck.remove(0);
    }

    public void resetStore() {  
        for(Card card : store) {
            discardPile.add(card);
        }
        setupStore();
    }
    
    // Print the store
    public String toString() {
        String returnString = "";
        for(int i = 0; i < noFaceUpCards; i++) {
            returnString += "\t[" + i + "] " + store[i] + ":";
        }
        return returnString;
    }
}