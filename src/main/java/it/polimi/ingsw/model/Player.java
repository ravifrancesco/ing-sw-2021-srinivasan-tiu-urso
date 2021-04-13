package it.polimi.ingsw.model;

import java.util.Arrays;

public class Player {

	static final int NUM_PLAYABLE_LEADER_CARDS = 2;

	static final int NUM_WHITE_MARBLE_RESOURCE = 2;

	private final String nickname;

	private final String id;

	private Dashboard dashboard;

	private DevelopmentCardDiscount[] activeDiscounts;

	private WhiteMarbleResource[] activatedWMR;

	private int numActiveDiscounts;

	private int numActiveWMR;

	// WMR = white marble resource

	public Player(String nickname, String id) {
		this.nickname = nickname;
		this.id = id;
		this.activeDiscounts = new DevelopmentCardDiscount[NUM_PLAYABLE_LEADER_CARDS];
		this.activatedWMR = new WhiteMarbleResource[NUM_WHITE_MARBLE_RESOURCE];
		this.numActiveDiscounts = 0;
		this.numActiveWMR = 0;

	}

	public void init() {
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
		activatedWMR[numActiveWMR] = wmr;
		numActiveWMR = numActiveWMR + 1;
	}

	public WhiteMarbleResource[] getActivatedWMR() {
		return Arrays.copyOfRange(activatedWMR, 0, numActiveWMR);
	}

	public int getNumActiveWMR() {
		return numActiveWMR;
	}
}