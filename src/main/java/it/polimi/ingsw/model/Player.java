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

	private ArrayList<DevelopmentCardDiscount> activeDiscounts;

	private ArrayList<Resource> activatedWMR;

	private int victoryPoints;

	// WMR = white marble resource

	public Player() {
		this.activeDiscounts = new ArrayList<>();
		this.activatedWMR = new ArrayList<>();
	}

	public Hand getHand() {
		return hand;
	}

	public void reset(GameSettings gameSettings) {
		this.dashboard.reset();
		this.hand.reset();
		this.activeDiscounts.clear();
		this.activatedWMR.clear();
		this.victoryPoints = 0;
	}

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

	public void addActiveDiscount(DevelopmentCardDiscount discount) throws IllegalStateException {
		if (activeDiscounts.size() + activatedWMR.size() > NUM_PLAYABLE_LEADER_CARDS) {
			throw new IllegalStateException();
		}
		activeDiscounts.add(discount);
	}

	public DevelopmentCardDiscount[] getActiveDiscounts() {
		return Arrays.copyOfRange(activeDiscounts, 0, numActiveDiscounts);
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

	public Resource[] getActivatedWMR() {
		return activatedWMR.toArray(Resource[]::new);
	}

	public int getNumActiveWMR() {
		return activatedWMR.size();
	}

	public LeaderCard getFromHand(int c) throws IllegalArgumentException {
		if (c < 0 || c >= hand.getHandSize()) {
			throw new IllegalArgumentException();
		}
		return hand.getCard(c);
	}

	public boolean checkWMR(ArrayList<WhiteMarbleResource> wmrs) {
		return activatedWMR.containsAll(wmrs.stream()
				.map(WhiteMarbleResource::getRes)
				.collect(Collectors.toList()));
	}

	public void updateVictoryPoints() {
		this.victoryPoints = dashboard.computePlayerPoints();
	}

}