package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.specialAbilities.SpecialAbilityType;
import it.polimi.ingsw.model.specialAbilities.WarehouseExtraSpace;
import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Player {

	static final int NUM_PLAYABLE_LEADER_CARDS = 2;

	static final int NUM_WHITE_MARBLE_RESOURCE = 2;

	private Dashboard dashboard;

	private Hand hand;

	private DevelopmentCardDiscount[] activeDiscounts;

	private Resource[] activatedWMR;

	private int numActiveDiscounts;

	private int numActiveWMR;

	private int victoryPoints;

	// WMR = white marble resource

	public Player() {
		this.activeDiscounts = new DevelopmentCardDiscount[NUM_PLAYABLE_LEADER_CARDS];
		this.activatedWMR = new Resource[NUM_WHITE_MARBLE_RESOURCE];
		this.numActiveDiscounts = 0;
		this.numActiveWMR = 0;

	}

	public Hand getHand() {
		return hand;
	}

	public void reset(GameSettings gameSettings) {
		//TODO reset everything
		this.victoryPoints = 0;
	}

	public void playLeaderCard(int card, int position) {
		LeaderCard leaderCard = hand.removeCard(card);
		leaderCard.play(dashboard, position);
		SpecialAbilityType saType = leaderCard.getSpecialAbility().getType();
		if(saType == SpecialAbilityType.DEVELOPMENT_CARD_DISCOUNT || saType == SpecialAbilityType.WHITE_MARBLE_RESOURCES) {
			leaderCard.activate(this);
		}
		else if(saType == SpecialAbilityType.WAREHOUSE_EXTRA_SPACE) {
			leaderCard.activate(this);
			WarehouseExtraSpace wes = (WarehouseExtraSpace) leaderCard.getSpecialAbility();
			wes.setLeaderCardPos(position);
		}
	}

	public void discardLeaderCard(int card, GameBoard gameBoard) {
		gameBoard.discardCard(hand.removeCard(card), dashboard);
		hand.removeCard(card);
	}

	public void discardLeaderCardInExcess(int card, GameBoard gameBoard) {
		gameBoard.discardCardInExcess(hand.removeCard(card));
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(Dashboard dashboard) { this.dashboard = dashboard; }

	public void addActiveDiscount(DevelopmentCardDiscount discount) {
		activeDiscounts[numActiveDiscounts] = discount;
		numActiveDiscounts++;
	}

	public DevelopmentCardDiscount[] getActiveDiscounts() {
		return Arrays.copyOfRange(activeDiscounts, 0, numActiveDiscounts);
	}

	/**
	 * Checks to see if there are any activated WhiteMarbleResources special abilities.
	 * @return true if there are, false if not.
	 */
	public boolean checkActiveWMR() {
		return numActiveWMR > 0;
	}

	/**
	 * Adds a new WhiteMarbleResource as a player's special ability.
	 * @param wmr the WhiteMarbleResource being added
	 */
	public void addWMR(WhiteMarbleResource wmr) {
		activatedWMR[numActiveWMR] = wmr.getRes();
		numActiveWMR = numActiveWMR + 1;
	}

	public Resource[] getActivatedWMR() {
		return Arrays.copyOfRange(activatedWMR, 0, numActiveWMR);
	}

	public int getNumActiveWMR() {
		return numActiveWMR;
	}

	public LeaderCard getFromHand(int c) {
		// TODO
		return null;
	}

	public boolean checkWMR(ArrayList<WhiteMarbleResource> wmrs) {
		// TODO
		return true;
	}

	public void updateVictoryPoints() {
		this.victoryPoints = dashboard.computePlayerPoints();
	}

}