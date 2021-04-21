package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {

	static final int NUM_PLAYABLE_LEADER_CARDS = 2;

	static final int NUM_WHITE_MARBLE_RESOURCE = 2;

	private Dashboard dashboard;

	private Hand hand;

	private DevelopmentCardDiscount[] activeDiscounts;

	private Resource[] activatedWMR;

	private int numActiveDiscounts;

	private int numActiveWMR;

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

	public void init(GameSettings gameSettings) {
	}

	public void playLeaderCard(int card) {
		// By Rob: if the special ability is a warehouseextraspace, other than activation please use "leaderCardPos"
		// setter to set the position.
		// TODO check auto activation
	}

	public void discardLeaderCard(int card, GameBoard gameBoard) {
		// TODO
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

}