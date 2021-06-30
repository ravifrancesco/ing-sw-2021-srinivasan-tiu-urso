package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.full.cards.LeaderCard;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the player's hand.
 * <p>
 * The class is observable and notifies the observers on a change of the state.
 */
public class Hand {

    public static final int MAX_HAND_SIZE = 4;
    private final ArrayList<LeaderCard> cards;

    /**
     * Constructor for the class.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     * Resets the hand to the initial state.
     */
    public void reset() {
        this.cards.clear();
    }

    /**
     * Adds card to the hand.
     *
     * @param c leaderCard to add.
     */
    public void addCard(LeaderCard c) {
        cards.add(c);
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param c index of the card to remove.
     * @return the card that was removed.
     */
    public LeaderCard removeCard(int c) {
        return cards.remove(c);
    }

    /**
     * Gets a card from the player's hand.
     *
     * @param c index of the card to get.
     * @return the card that was indexed.
     */
    public LeaderCard getCard(int c) {
        return cards.get(c);
    }

    /**
     * Gets the size of the player's hand.
     *
     * @return the hand's size.
     */
    public int getHandSize() {
        return cards.size();
    }

    public List<LeaderCard> getAllCards() {
        return cards;
    }
}