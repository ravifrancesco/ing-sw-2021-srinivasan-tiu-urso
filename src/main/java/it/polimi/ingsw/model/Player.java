package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.specialAbilities.SpecialAbilityType;
import it.polimi.ingsw.model.specialAbilities.WarehouseExtraSpace;
import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class represents the player. It contains references to:
 * <ul>
 * <li> The player's dashboard.
 * <li> The player's hand.
 * <li> The player's active discounts.
 * <li> The player's active WMRs.
 * <li> The player's victory points.
 * </ul>
 * <p>
 * TODO testing
 */
public class Player {

	static final int NUM_PLAYABLE_LEADER_CARDS = 2;

	static final int NUM_WHITE_MARBLE_RESOURCE = 2;

	private Dashboard dashboard;

	private Hand hand;

	private ArrayList<DevelopmentCardDiscount> activeDiscounts;

	private ArrayList<Resource> activatedWMR;

	private int victoryPoints;

	// WMR = white marble resource

	/**
	 * Constructor of the Player class.
	 *
	 * @param gameSettings the settings for the current game.
	 */
	public Player(GameSettings gameSettings) {
		this.dashboard = new Dashboard(gameSettings);
		this.hand = new Hand();
		this.activeDiscounts = new ArrayList<>();
		this.activatedWMR = new ArrayList<>();
	}

	/**
	 * Getter for the player's hand.
	 *
	 * @return the player's hand.
	 */
	public Hand getHand() {
		return hand;
	}

	/**
	 * Resets the class to the starting/initial state.
	 *
	 * @param gameSettings game settings to initialize the class.
	 */
	public void reset(GameSettings gameSettings) {
		this.dashboard.reset();
		this.hand.reset();
		this.activeDiscounts.clear();
		this.activatedWMR.clear();
		this.victoryPoints = 0;
	}

	/**
	 * Method to place a leader card on the dashboard.
	 *
	 * @param card						position of the hand to play the card.
	 * @param position					position on the dashboard to place the card.
	 * @throws IllegalArgumentException	if the card index is out of bounds.
	 * @throws IllegalStateException	if the card cannot be placed.
	 */
	public void playLeaderCard(int card, int position) throws IllegalArgumentException, IllegalStateException {

		if (card < 0 || card >= hand.getHandSize()) {
			throw new IllegalArgumentException();
		}

		LeaderCard leaderCard = hand.getCard(card);

		try {
			dashboard.placeLeaderCard(leaderCard);
		} catch (IllegalStateException e) {
			throw new IllegalStateException();
		}

		hand.removeCard(card);

		SpecialAbilityType saType = leaderCard.getSpecialAbility().getType();
		if(saType == SpecialAbilityType.DEVELOPMENT_CARD_DISCOUNT || saType == SpecialAbilityType.WHITE_MARBLE_RESOURCES) {
			leaderCard.activate(this);
		} else if(saType == SpecialAbilityType.WAREHOUSE_EXTRA_SPACE) {
			leaderCard.activate(this);
			WarehouseExtraSpace wes = (WarehouseExtraSpace) leaderCard.getSpecialAbility();
			wes.setLeaderCardPos(position);
		}
	}

	/**
	 * Allows the player to discard a leader card.
	 * TODO check the controller for exception
	 * TODO handle faith marker points
	 *
	 * @param card		index of the hand to discard the card.
	 * @param gameBoard
	 * @throws IllegalArgumentException
	 */
	public void discardLeaderCard(int card, GameBoard gameBoard) throws IllegalArgumentException {

		if (card < 0 || card >= hand.getHandSize()) {
			throw new IllegalArgumentException();
		}

		gameBoard.discardCard(hand.removeCard(card));
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
	 * @param discount					discount to add to the dashboard.
	 * @throws IllegalStateException	if the dashboard cannot accomodate any more discoounts.
	 */
	public void addActiveDiscount(DevelopmentCardDiscount discount) throws IllegalStateException {
		if (activeDiscounts.size() + activatedWMR.size() > NUM_PLAYABLE_LEADER_CARDS) {
			throw new IllegalStateException();
		}
		activeDiscounts.add(discount);
	}

	/**
	 * Getter for the active player's discount.
	 *
	 * @return	an arraylist of the player's active discounts.
	 */
	public ArrayList<DevelopmentCardDiscount> getActiveDiscounts() {
		return activeDiscounts;
	}

	/**
	 * Checks to see if there are any activated WhiteMarbleResources special abilities.
	 * @return true if there are, false if not.
	 */
	public boolean checkActiveWMR() {
		return !activatedWMR.isEmpty();
	}

	/**
	 * Adds a new WhiteMarbleResource as a player's special ability.
	 * @param wmr the WhiteMarbleResource being added
	 */
	public void addWMR(WhiteMarbleResource wmr) {
		if (activeDiscounts.size() + activatedWMR.size() > NUM_PLAYABLE_LEADER_CARDS) {
			throw new IllegalStateException();
		}
		activatedWMR.add(wmr.getRes());
	}

	/**
	 * Getter for the active WMRs.
	 *
	 * @return	the active WMRs.
	 */
	public Resource[] getActivatedWMR() {
		return activatedWMR.toArray(Resource[]::new);
	}

	/**
	 * Getter for the number of active WMRs.
	 *
	 * @return	number of active WRMs.
	 */
	public int getNumActiveWMR() {
		return activatedWMR.size();
	}

	/**
	 * Getter for a card from the player's hand.
	 *
	 * @param c							index of card to get.
	 * @return							the indexed card.
	 * @throws IllegalArgumentException	if the index is out of bounds.
	 */
	public LeaderCard getFromHand(int c) throws IllegalArgumentException {
		if (c < 0 || c >= hand.getHandSize()) {
			throw new IllegalArgumentException();
		}
		return hand.getCard(c);
	}

	/**
	 * Check if the input WMRs are in the dashboard WMRs.
	 *
	 * @param wmrs	WMRs to check.
	 * @return 		<code>true</code> player has the input WMRs.
	 * 				<code>false</code> otherwise.
	 */
	public boolean checkWMR(ArrayList<WhiteMarbleResource> wmrs) {
		return activatedWMR.containsAll(wmrs.stream()
				.map(WhiteMarbleResource::getRes)
				.collect(Collectors.toList()));
	}

	/**
	 * Allows to update the player's victory points.
	 */
	public void updateVictoryPoints() {
		this.victoryPoints = dashboard.computePlayerPoints();
	}

	/**
	 * Getter for the victory points.
	 *
	 * @return	the current victory points of the player.
	 */
	public int getVictoryPoints() {
		return victoryPoints;
	}
}