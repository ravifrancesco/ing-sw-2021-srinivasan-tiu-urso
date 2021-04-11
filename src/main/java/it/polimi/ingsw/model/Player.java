package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Player {

	static final int NUM_PLAYABLE_LEADER_CARDS = 2;

	private final String nickname;

	private final String id;

	private Dashboard dashboard;

	private DevelopmentCardDiscount[] activeDiscounts;

	private int numActiveDiscounts;

	public Player(String nickname, String id) {
		this.nickname = nickname;
		this.id = id;
		this.activeDiscounts = new DevelopmentCardDiscount[NUM_PLAYABLE_LEADER_CARDS];
		this.numActiveDiscounts = 0;
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
}