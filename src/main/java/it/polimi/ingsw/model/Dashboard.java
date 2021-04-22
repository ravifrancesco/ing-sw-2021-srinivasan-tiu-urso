package it.polimi.ingsw.model;

import it.polimi.ingsw.common.Pair;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * This class represents the player dashboard. It contains references to:
 *
 * <ul>
 * <li> The warehouse.
 * <li> The faith track.
 * <li> The played leader cards.
 * <li> The played development cards.
 * <li> The dashboard production power.
 * <li> The resources supply.
 * </ul>
 * <p>
 *
 * The class also acts as a proxy for the warehouse and  the faith track, that are
 * not directly visible from the outside.
 *
 */
public class Dashboard {

	static final int NUM_DEVELOPMENT_CARD_STACKS = 3;
	static final int NUM_LEADER_CARDS = 2;
	static final int NUM_DEVELOPMENT_CARDS_TO_WIN = 7;

	private final Warehouse warehouse;
	private final FaithTrack faithTrack;
	private final List<LeaderCard> playedLeaderCards;
	private ProductionPower dashBoardProductionPower;
	private ArrayList<Resource> supply;
	private final List<Stack<DevelopmentCard>> playedDevelopmentCards;

	/**
	 * The constructor for a Dashboard object.
	 *
	 * @param gameSettings the settings for the current game.
	 */
	public Dashboard(GameSettings gameSettings) {
		this.warehouse = new Warehouse();
		this.faithTrack = new FaithTrack(gameSettings);

		this.playedLeaderCards = new ArrayList<>();

		this.playedDevelopmentCards = IntStream.range(0, NUM_DEVELOPMENT_CARD_STACKS)
				.mapToObj(e->new Stack<DevelopmentCard>())
				.collect(Collectors.toList());

		// TODO add production power initialization

	}

	/**
	 * Allows to move the Faith Marker on the FaithTrack.
	 * @param pos represents how many positions to go on.
	 */
	public void moveFaithMarker(int pos) {
		faithTrack.moveFaithMarker(pos);
	}

	/**
	 * Gets the current position of the faith marker.
	 *
	 * @return the current position of the faith marker.
	 */
	public int getFaithMarkerPosition() { return faithTrack.getPosition(); }

	/**
	 * Computes the number of victory points of the player.
	 *
	 * @return the current number of victory points of the player.
	 */
	public int computePlayerPoints() {
		int faithTrackVP = faithTrack.getVictoryPoints();

		int leaderCardsVP = playedLeaderCards.stream()
				.map(lc -> lc.getVictoryPoints())
				.reduce(0, Integer::sum);

		int developmentCardsVP = playedDevelopmentCards.stream()
				.map(s -> s.peek().getVictoryPoints())
				.reduce(0, Integer::sum);

		return faithTrackVP + leaderCardsVP + developmentCardsVP;

	}


	/**
	 * Allows to place a Leader Card onto the Dashboard.
	 *
	 * @param c 						the Leader Card to place.
	 * @throws IllegalStateException  	in case the leader card slots are full.
	 * @return the index where the card is placed.
	 */
	public int placeLeaderCard(LeaderCard c) throws IllegalStateException {
		if (playedLeaderCards.size() == NUM_LEADER_CARDS) {
			throw new IllegalStateException();
		} else {
			playedLeaderCards.add(c);
			return playedLeaderCards.indexOf(c);
		}
	}

	/**
	 * Allows to place a Development Card onto the Dashboard
	 *
	 * @param c 						the Development Card to place.
	 * @throws IllegalStateException 	in case the are no slots available
	 * 									for the development card.
	 */
	public void placeDevelopmentCard(DevelopmentCard c) throws IllegalStateException {

		Banner banner = c.getBanner();
		Stack<DevelopmentCard> stack;

		if (playedDevelopmentCards.stream()
				.filter(s -> !s.isEmpty())
				.anyMatch(s -> s.peek().getBanner().isGreater(banner))) {
			stack = playedDevelopmentCards.stream().
					filter(s -> s.peek().getBanner().isGreater(banner))
					.findFirst().orElseThrow(IllegalStateException::new);
			stack.push(c);
		} else if (playedDevelopmentCards.stream().anyMatch((Vector::isEmpty))) {
			stack = playedDevelopmentCards.stream().
					filter(Vector::isEmpty)
					.findFirst().orElseThrow(IllegalStateException::new);
			stack.push(c);
		} else {
			throw new IllegalStateException();
		}

	}

	/**
	 * Allows to store a resource in the Warehouse's locker.
	 *
	 * @param r 		the resource to store.
	 * @param quantity 	the quantity of the resource.
	 */
	public void storeResourceInLocker(Resource r, int quantity) {
		warehouse.storeInLocker(r, quantity);
	}

	/**
	 * Allows to store a resource in the Warehouse's deposit.
	 *
	 * @param r								resource to store.
	 * @param position						position to store the resource.
	 * @throws IllegalArgumentException		if the position is < 0 or > 5.
	 * @throws IllegalStateException		if the resource placement does not respect the game rules.
	 */
	public void storeResourceInDeposit(Resource r, int position) throws IllegalArgumentException, IllegalStateException {
		if (position < 0 || position > 5) {
			throw new IllegalArgumentException();
		}

		try {
			warehouse.storeInDeposit(r, position);
		} catch (IllegalStateException e) {
			throw new IllegalStateException();
		}
	}

	// TODO method to get resources as object

	public Map<Banner, Integer> getBanners() {
		// TODO
	}

	/**
	 * Returns a Leader Card on the dashboard given the index.
	 *
	 * @param c	index to retrieve Leader Card
	 * @return	played Leader Card at index c
	 */
	public LeaderCard getLeaderCard(int c) {
		return playedLeaderCards.get(c);
	}

	/**
	 * Returns the Dashboard Production power.
	 *
	 * @return	Dashboard Production power.
	 */
	public ProductionPower getDashBoardProductionPower() {
		return dashBoardProductionPower;
	}

	/**
	 * Returns the top Development Card on the stack at index i.
	 *
	 * @param i index to retrieve the Development Card.
	 * @return	Development Card on top of top of the stack and index i.
	 */
	public DevelopmentCard getDevelopmentCard(int i) {
		return playedDevelopmentCards.get(1).peek();
	}

	/**
	 * Allows to add resources to the Dashboard Supply.
	 *
	 * @param res	resources to add.
	 */
	public void addResourcesToSupply(ArrayList<Resource> res) {
		supply.addAll(res);
	}


	/**
	 * Discards the resources in the dashboard Supply.
	 *
	 * @return	number of faith points to add to other players.
	 */
	public int discardResources() {
		int faithPoints = supply.size();
		supply.clear();
		return faithPoints;
	}

	/**
	 * Resets the flag "activated" in all the production
	 * powers to false.
	 *
	 */
	public void resetProductionPowers() {

		playedLeaderCards.stream()
				.filter(Objects::nonNull)
				.filter(lc -> lc.getSpecialAbility().getType().equals(SpecialAbilityType.PRODUCTION_POWER))
				.forEach(lc -> ((ProductionPower) lc.getSpecialAbility()).reset());

		playedDevelopmentCards.stream()
				.map(Stack::peek)
				.forEach(dc -> dc.getProductionPower().reset());

		dashBoardProductionPower.reset();

	}

	/**
	 * Returns the quantity of resources stored in the Warehouse Deposit.
	 *
	 * @return	the quantity of resources stored in the Warehouse Deposit.
	 */
	public int getDepositResourceQty() {
		warehouse.getDepositResourceQty();
	}

	/**
	 * Allows to move resources in the Deposit.
	 *
	 * @param moves						list of moves.
	 * @throws IllegalArgumentException	if the moves indexes are out of bound.
	 * @throws IllegalStateException	if the moves do not compile to the game rules.
	 */
	public void moveDepositResources(ArrayList<Pair<Integer, Integer>> moves) throws IllegalArgumentException, IllegalStateException {
		if (moves.stream().anyMatch(m -> ((m.first < 0 || m.first > 5) || m.second < 0 || m.second > 5))) {
			throw new IllegalArgumentException();
		}
		// TODO continue
	}

	/**
	 * Allows to move resouces from the supply to the Warehouse.
	 *
	 * @param from						index of the supply box.
	 * @param to						index of the deposit.
	 * @throws IllegalArgumentException	if the move indexes are out of bounds.
	 * @throws IllegalStateException	if the move does not compile to the game rules.
	 */
	public void storeFromSupply(int from, int to) throws IllegalArgumentException, IllegalStateException {
		// TODO
		if (from < 0 || from > 3 || to < 0 || to > 5) {
			throw new IllegalArgumentException();
		}
		// TODO checks that the moves generate a legal deposit -> else IllegalState
	}

	/**
	 * Allows to move resouces from the supply to the Extraspace.
	 *
	 * @param leaderCardPosition		index of the Leader Card to be used.
	 * @param from						index of the supply box.
	 * @param to						index of the extra space.
	 * @throws IllegalArgumentException	if the move indexes are out of bounds or the LeaderCard index is out of bound.
	 * @throws IllegalStateException	if the move does not compile to the game rules.
	 */
	public void storeFromSupplyInExtraDeposit(int leaderCardPosition, int from, int to) throws IllegalArgumentException, IllegalStateException {
		if (leaderCardPosition < 0 || leaderCardPosition > 1 || from < 0 || from > 3 || to < 0 || to > 1) {
			throw new IllegalArgumentException();
		}
		SpecialAbility specialAbility = playedLeaderCards.get(leaderCardPosition).getSpecialAbility();

		if (!specialAbility.getType().equals(SpecialAbilityType.WAREHOUSE_EXTRA_SPACE)) {
			throw new IllegalArgumentException();
		}
		// TODO Continue
	}

	/**
	 * Checks if the game has reached the final turns.
	 *
	 * @return	<code>true</code> if the player has reached the end of the faith track
	 * 			or if the user has bought NUM_DEVELOPMENT_CARDS_TO_WIN cards;
	 *         	<code>false</code> otherwise.
	 */
	public boolean checkGameEnd() {
		return 	(getFaithMarkerPosition() == GameSettings.FAITH_TRACK_LENGTH - 1) ||
				playedDevelopmentCards.stream().
						mapToInt(Vector::size).
						reduce(0, Integer::sum) == NUM_DEVELOPMENT_CARDS_TO_WIN;
	}

}
