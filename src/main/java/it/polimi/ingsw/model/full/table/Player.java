package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.full.specialAbilities.SpecialAbilityType;
import it.polimi.ingsw.model.full.specialAbilities.WarehouseExtraSpace;
import it.polimi.ingsw.model.full.specialAbilities.WhiteMarbleResource;
import it.polimi.ingsw.model.observerPattern.observables.PlayerObservable;
import it.polimi.ingsw.model.utils.GameSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the player. It contains references to:
 * <ul>
 * <li> The player's dashboard.
 * <li> The player's hand.
 * <li> The player's active discounts.
 * <li> The player's active WMRs.
 * <li> The player's nickname.
 * <li> The player's victory points.
 * </ul>
 * <p>
 * <p>
 * The class is observable and notifies the observers on a change of the state.
 */
public class Player extends PlayerObservable {

    static final int NUM_PLAYABLE_LEADER_CARDS = 2;

    private final Dashboard dashboard;

    private final Hand hand;

    private final ArrayList<DevelopmentCardDiscount> activeDiscounts;

    private final ArrayList<Resource> activatedWMR;

    private String nickname;

    private int victoryPoints;

    // WMR = white marble resource

    /**
     * Constructor of the Player class.
     *
     * @param gameSettings  the settings for the current game.
     * @param nickname      the player's nickname.
     */
    public Player(GameSettings gameSettings, String nickname) {
        this.dashboard = new Dashboard(gameSettings, this);
        this.hand = new Hand();
        this.activeDiscounts = new ArrayList<>();
        this.activatedWMR = new ArrayList<>();
        this.nickname = nickname;
    }


    /**
     * Resets the class to the starting/initial state.
     */
    public void reset() {
        this.dashboard.reset();
        this.hand.reset();
        this.activeDiscounts.clear();
        this.activatedWMR.clear();
        this.victoryPoints = 0;
        notify(this);
    }

    /**
     * Method to place a leader card on the dashboard.
     *
     * @param card position of the hand to play the card.
     * @throws IllegalArgumentException if the card index is out of bounds.
     * @throws IllegalStateException    if the card cannot be placed.
     */
    public void playLeaderCard(int card) throws IllegalArgumentException, IllegalStateException {

        if (card < 0 || card >= hand.getHandSize()) {
            notify(this);
            throw new IllegalArgumentException();
        }

        LeaderCard leaderCard = hand.getCard(card);
        int position;

        try {
            position = dashboard.placeLeaderCard(leaderCard);
        } catch (IllegalStateException e) {
            notify(this);
            throw new IllegalStateException();
        }

        hand.removeCard(card);

        SpecialAbilityType saType = leaderCard.getSpecialAbility().getType();
        if (saType == SpecialAbilityType.DEVELOPMENT_CARD_DISCOUNT || saType == SpecialAbilityType.WHITE_MARBLE_RESOURCES) {
            leaderCard.activate(this);
        } else if (saType == SpecialAbilityType.WAREHOUSE_EXTRA_SPACE) {
            WarehouseExtraSpace wes = (WarehouseExtraSpace) leaderCard.getSpecialAbility();
            wes.setLeaderCardPos(position);
            leaderCard.activate(this);
        }
        notify(this);
    }

    /**
     * Allows the player to discard a leader card.
     *
     * @param card      index of the hand to discard the card.
     * @param gameBoard gameBoard that will "receive" the discarded card.
     * @throws IllegalArgumentException if if the card argument is out of index
     */
    public void discardLeaderCard(int card, GameBoard gameBoard) throws IllegalArgumentException {

        if (card < 0 || card >= hand.getHandSize()) {
            throw new IllegalArgumentException();
        }

        gameBoard.discardCard(hand.removeCard(card));
        notify(this);
    }

    /**
     * Getter for the player dashboard.
     *
     * @return the player's dashboard.
     */
    public Dashboard getDashboard() {
        return dashboard;
    }

    /**
     * Allow to add a discount to the player's dashboard.
     *
     * @param discount discount to add to the dashboard.
     * @throws IllegalStateException if the dashboard cannot accomodate any more discoounts.
     */
    public void addActiveDiscount(DevelopmentCardDiscount discount) throws IllegalStateException {
        if (activeDiscounts.size() + activatedWMR.size() >= NUM_PLAYABLE_LEADER_CARDS) {
            throw new IllegalStateException();
        }
        activeDiscounts.add(discount);
        notify(this);
    }

    /**
     * Getter for the active player's discount.
     *
     * @return an arraylist of the player's active discounts.
     */
    public ArrayList<DevelopmentCardDiscount> getActiveDiscounts() {
        return activeDiscounts;
    }

    /**
     * Checks to see if there are any activated WhiteMarbleResources special abilities.
     *
     * @return true if there are, false if not.
     */
    public boolean checkActiveWMR() {
        return !activatedWMR.isEmpty();
    }

    /**
     * Adds a new WhiteMarbleResource as a player's special ability.
     *
     * @param wmr the WhiteMarbleResource being added
     * @throws IllegalStateException if too many special abilities are activated.
     */
    public void addWMR(WhiteMarbleResource wmr) {
        if (activeDiscounts.size() + activatedWMR.size() >= NUM_PLAYABLE_LEADER_CARDS) {
            throw new IllegalStateException();
        }
        activatedWMR.add(wmr.getRes());
    }

    /**
     * Getter for the active WMRs.
     *
     * @return the active WMRs.
     */
    public Resource[] getActivatedWMR() {
        return activatedWMR.toArray(Resource[]::new);
    }

    /**
     * Getter for the number of active WMRs.
     *
     * @return number of active WRMs.
     */
    public int getNumActiveWMR() {
        return activatedWMR.size();
    }

    /**
     * Check if the input WMRs are in the dashboard WMRs.
     *
     * @param wmrs WMRs to check.
     * @return <code>true</code> player has the input WMRs.
     * <code>false</code> otherwise.
     */
    public boolean checkWMR(ArrayList<Resource> wmrs) {
        ArrayList<Resource> copyWmr = new ArrayList<>(wmrs);
        copyWmr.removeAll(activatedWMR);
        return copyWmr.size() == 0;
    }

    /**
     * Allows to update the player's victory points.
     */
    public void updateVictoryPoints() {
        this.victoryPoints = dashboard.getPlayerPoints();
    }

    /**
     * Getter for the victory points.
     *
     * @return the current victory points of the player.
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Getter for the hand size.
     * @return the hands size.
     */
    public int getHandSize() {
        return this.hand.getHandSize();
    }

    /**
     * Getter for a card from the hand.
     * @param c the index of the card in the hand.
     * @return the leader card from the hand in c position.
     */
    public LeaderCard getCard(int c) {
        return this.hand.getCard(c);
    }

    /**
     * Adds a card to the hand
     *
     * @param lc the leader card to add.
     */
    public void addCard(LeaderCard lc) {
        this.hand.addCard(lc);
        notify(this);
    }

    /**
     * Fills the hand with leader cards
     *
     * @param leaderCards list of leader cards
     */
    public void fillHand(List<LeaderCard> leaderCards) {
        leaderCards.forEach(card -> hand.addCard(card));
        notify(this);
    }

    /**
     * Getter for the hand.
     * @return the player's hand.
     */
    public List<LeaderCard> getHand() {
        return hand.getAllCards();
    }

    /**
     * Getter for the nickname.
     * @return the player's nickname.
     */
    public String getNickname() {
        return nickname;
    }
}