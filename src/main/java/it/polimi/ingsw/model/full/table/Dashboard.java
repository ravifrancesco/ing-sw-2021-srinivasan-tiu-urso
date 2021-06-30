package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.model.full.cards.Banner;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.observerPattern.observables.DashboardObservable;
import it.polimi.ingsw.model.full.specialAbilities.*;

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
 * The class is observable and notifies the observers on a change of the state.
 *
 * TODO testing
 * TODO changing the way production powers are activated, and use it to notify()
 */
public class Dashboard extends DashboardObservable {

	static final int NUM_DEVELOPMENT_CARD_STACKS = 3;
	static final int NUM_LEADER_CARDS = 2;
	static final int NUM_DEVELOPMENT_CARDS_TO_WIN = 7;

	private final Warehouse warehouse;
	private final FaithTrack faithTrack;
	private final List<LeaderCard> playedLeaderCards;
	private final ProductionPower dashBoardProductionPower;
	private final ArrayList<Resource> supply;
	private final List<Stack<DevelopmentCard>> playedDevelopmentCards;

	private int playerPoints;

	private final Player player;

	/**
	 * The constructor for a Dashboard object.
	 *
	 * @param gameSettings the settings for the current game.
	 */
	public Dashboard(GameSettings gameSettings, Player player) {

		this.warehouse = new Warehouse(this);

		this.faithTrack = new FaithTrack(gameSettings, this);

		this.playedLeaderCards = new ArrayList<>();

		this.playedDevelopmentCards = IntStream.range(0, NUM_DEVELOPMENT_CARD_STACKS)
				.mapToObj(e->new Stack<DevelopmentCard>())
				.collect(Collectors.toList());

		this.dashBoardProductionPower = gameSettings.getDashBoardProductionPower();
		this.supply = new ArrayList<>();

		this.player = player;
	}


	/**
	 * Resets the dashboard to the initial state.
	 */
	public void reset() {
		warehouse.reset();
		faithTrack.reset();
		playedLeaderCards.clear();
		dashBoardProductionPower.reset();
		supply.clear();
		playedDevelopmentCards.forEach(Vector::clear);
		playerPoints = 0;
		notify(this);
	}

	/**
	 * Allows to move the Faith Marker on the FaithTrack.
	 * @param pos represents how many positions to go on.
	 */
	public void moveFaithMarker(int pos) {
		faithTrack.moveFaithMarker(pos);
		updatePlayerPoints();
		notify(this);
	}

	/**
	 * Gets the current position of the faith marker.
	 *
	 * @return the current position of the faith marker.
	 */
	public int getFaithMarkerPosition() { return faithTrack.getPosition(); }

	public int getLorenzoIlMagnificoPosition() { return faithTrack.getLorenzoIlMagnificoPosition(); }

	/**
	 * Getter for the number of victory points of the player.
	 *
	 * @return the current number of victory points of the player.
	 */
	public int getPlayerPoints() {

		return playerPoints;

	}

	/**
	 * Updates the player points.
	 */
	private void updatePlayerPoints() {

		int faithTrackVP = faithTrack.getVictoryPoints();

		int leaderCardsVP = playedLeaderCards.stream()
				.map(LeaderCard::getVictoryPoints)
				.reduce(0, Integer::sum);

		int developmentCardsVP = playedDevelopmentCards.stream()
				.map(s -> s.stream()
						.map(DevelopmentCard::getVictoryPoints)
						.reduce(0, Integer::sum))
				.reduce(0, Integer::sum);

		int resourcePoints = getAllPlayerResources()
				.values().stream()
				.reduce(0, Integer::sum) / 5;

		this.playerPoints = faithTrackVP + leaderCardsVP + developmentCardsVP + resourcePoints;

		notify(this);

	}


	/**
	 * Allows to place a Leader Card onto the Dashboard.
	 *
	 * @param c 						the Leader Card to place.
	 * @return 							the position of the leader card on the dashboard.
	 * @throws NullPointerException  	if the Leader Card to place is null.
	 * @throws IllegalArgumentException if no more Leader Card slots are available.
	 */
	public int placeLeaderCard(LeaderCard c) throws IllegalStateException {
		if (playedLeaderCards.size() == NUM_LEADER_CARDS) {
			notify(this);
			throw new IllegalStateException("Leader Card grid is full");
		} else {
			playedLeaderCards.add(c);
		}
		updatePlayerPoints();
		notify(this);
		return playedLeaderCards.indexOf(c);
	}

	/**
	 * Allows to place a Development Card onto the Dashboard
	 *
	 * @param c 						the Development Card to place.
	 * @param position					position to place the development card.
	 * @throws IllegalStateException 	in case the state of the stacs does not
	 * 									follow game rules.
	 * @throws IllegalStateException	in case the index is out of bounds TODO check if call is catched later
	 *
	 */
	public void placeDevelopmentCard(DevelopmentCard c, int position) throws IllegalStateException, IllegalArgumentException {

		Banner banner = c.getBanner();

		if (position < 0 || position > 2) {
			notify(this);
			throw new IllegalArgumentException("Not a valid index");
		}

		if (!playedDevelopmentCards.get(position).isEmpty() &&
				!playedDevelopmentCards.get(position).peek().getBanner().isOneLess(banner)) {
			notify(this);
			throw new IllegalStateException();
		} else if (playedDevelopmentCards.get(position).isEmpty() &&
				c.getBanner().getLevel() > 1) {
			notify(this);
			throw new IllegalStateException();
		} else {
			playedDevelopmentCards.get(position).push(c);
			notify(this);
			updatePlayerPoints();
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
		updatePlayerPoints();
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
			notify(this);
			throw new IllegalArgumentException();
		}

		try {
			warehouse.storeInDeposit(r, position);
			updatePlayerPoints();
		} catch (IllegalStateException e) {
			throw new IllegalStateException();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * Gets the player's banners on the dashboard.
	 *
	 * @return a map with keys being the banners and values being the quantities.
	 * TODO fix this implementation
	 */
	public Map<Banner, Integer> getBanners() {

		HashMap<Banner, Integer> playerBanners = new HashMap<>();

		List<Banner> bannersList = playedDevelopmentCards.stream()
				.flatMap(List::stream)
				.map(DevelopmentCard::getBanner)
				.collect(Collectors.toList());

		for (Banner banner : bannersList) {
			if (playerBanners.entrySet().stream().anyMatch(e -> e.getKey().toString().equals(banner.toString()))) {
				playerBanners.entrySet().stream().filter(e -> e.getKey().toString().equals(banner.toString()))
						.forEach(e -> e.setValue(e.getValue()+1));
			}
			else {
				playerBanners.put(banner, 1);
			}
		}

		return playerBanners;

	}



	/**
	 * Returns a Leader Card on the dashboard given the index.
	 *
	 * @param c							index to retrieve Leader Card.
	 * @return							played Leader Card at index c.
	 * @throws IllegalArgumentException if the index is out of bounds.
	 * @throws NullPointerException 	if the selected slot is empty.
	 */
	public LeaderCard getLeaderCard(int c) throws IllegalArgumentException {
		if (c < 0 || c >= playedLeaderCards.size()) {
			throw new IllegalArgumentException("Invalid index");
		}
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
		return playedDevelopmentCards.get(i).isEmpty() ? null : playedDevelopmentCards.get(i).peek();
	}

	/**
	 * Allows to add resources to the Dashboard Supply.
	 *
	 * @param res	resources to add.
	 */
	public void addResourcesToSupply(ArrayList<Resource> res) {
		supply.addAll(res);
		notify(this);
	}


	/**
	 * Discards the resources in the dashboard Supply.
	 *
	 * @return	number of faith points to add to other players.
	 */
	public int discardResources() {
		int faithPoints = supply.size();
		supply.clear();
		notify(this);
		return faithPoints;
	}

	/**
	 * Resets the flag "activated" in all the production
	 * powers to false.
	 */
	public void resetProductionPowers() {

		playedLeaderCards.stream()
				.filter(Objects::nonNull)
				.filter(lc -> lc.getSpecialAbility().getType().equals(SpecialAbilityType.PRODUCTION_POWER))
				.forEach(lc -> ((ProductionPower) lc.getSpecialAbility()).reset());

		playedDevelopmentCards.stream()
				.filter(s -> !s.isEmpty())
				.map(Stack::peek)
				.forEach(dc -> dc.getProductionPower().reset());

		dashBoardProductionPower.reset();

		notify(this);

	}

	/**
	 * Returns the quantity of resources stored in the Warehouse Deposit.
	 * @return	the quantity of resources stored in the Warehouse Deposit.
	 */
	public int getDepositResourceQty() {
		return warehouse.getDepositResourceQty();
	}

	/**
	 * Allows to move resources in the Deposit.
	 *
	 * @param newDeposit					the new deposit with the moved resources
	 * @throws IllegalArgumentException	if the passed deposit has the wrong capacity
	 * @throws IllegalStateException	if the moves do not compile to the game rules
	 */
	public void moveDepositResources(Resource[] newDeposit) throws IllegalArgumentException, IllegalStateException {
		if (newDeposit.length != 6) {
			notify(this);
			throw new IllegalArgumentException();
		}
		if (!warehouse.checkShelvesRule(newDeposit) || !checkSameResourcesExtraDeposits(newDeposit, warehouse.getExtraDeposits()[0], 0)) {
			notify(this);
			throw new IllegalStateException("Moves create an illegal deposit");
		}
		warehouse.changeDeposit(newDeposit);
	}

	/**
	 * Allows to move resources between the deposit and extra deposit.
	 * @param newDeposit 				the new deposit with the moved resources
	 * @param newExtraDeposit			the new extra deposit with the moved resources
	 * @param lcIndex					the index of the leader card where the extra deposit is
	 * @throws IllegalArgumentException	if the indexes are wrong
	 * @throws IllegalStateException	if the new deposit has illegal placements
	 */
	public void moveDepositExtraDeposit(Resource[] newDeposit, Resource[] newExtraDeposit, int lcIndex) throws IllegalArgumentException, IllegalStateException {
		if(newDeposit.length != 6 || newExtraDeposit.length != 2 || lcIndex < 0 || lcIndex > 1) {
			notify(this);
			throw new IllegalArgumentException();
		} else if (!playedLeaderCards.get(lcIndex).getSpecialAbility().getType().equals(SpecialAbilityType.WAREHOUSE_EXTRA_SPACE)) {
			notify(this);
			throw new IllegalArgumentException();
		}

		if(!warehouse.checkShelvesRule(newDeposit) || !checkSameResourcesExtraDeposits(newDeposit, newExtraDeposit, lcIndex)) {
			notify(this);
			throw new IllegalStateException("Move creates an illegal deposit");
		}

		warehouse.changeDeposit(newDeposit);
		warehouse.changeExtraDeposit(newExtraDeposit, lcIndex);
	}

	/**
	 * Checks to see that the resources quantity aren't changed a new deposit and extra deposit (only moving allowed)
	 * @param newDeposit 				the new deposit
	 * @param newExtraDeposit			the new extraDeposit
	 * @param lcIndex					the index of the placed leader card with the extra deposit
	 * @return							true if the resources qty are still the same, false if not
	 */
	private boolean checkSameResourcesExtraDeposits(Resource[] newDeposit, Resource[] newExtraDeposit, int lcIndex) {
		HashMap<Resource, Integer> allResourceBefore = warehouse.getAllResources();
		HashMap<Resource, Integer> allResourcesAfter;
		if(lcIndex == 0) {
			allResourcesAfter = warehouse.sumAllResources(newDeposit, warehouse.getLocker(), newExtraDeposit, warehouse.getExtraDeposits()[1]);
		} else {
			allResourcesAfter = warehouse.sumAllResources(newDeposit, warehouse.getLocker(), warehouse.getExtraDeposits()[0], newExtraDeposit);
		}
		allResourceBefore.forEach((k, v) -> allResourcesAfter.merge(k, v, (v1, v2) -> v1-v2));
		return allResourcesAfter.values().stream().noneMatch(v -> v != 0);
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

		if (from < 0 || from > 3 || to < 0 || to > 5) {
			// invalid indexes
			notify(this);
			throw new IllegalArgumentException();
		}

		try {
			warehouse.storeInDeposit(supply.get(from), to);
		} catch(IllegalStateException e) {
			throw new IllegalStateException();
		} catch(Exception e) {
			throw new IllegalArgumentException();
		}

		supply.remove(from);
		notify(this);

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
			notify(this);
			throw new IllegalArgumentException();
		}
		SpecialAbility specialAbility = playedLeaderCards.get(leaderCardPosition).getSpecialAbility();

		if (!specialAbility.getType().equals(SpecialAbilityType.WAREHOUSE_EXTRA_SPACE)) {
			notify(this);
			throw new IllegalArgumentException();
		}

		WarehouseExtraSpace wes = (WarehouseExtraSpace) specialAbility;
		Resource addedResource;

		try {
			addedResource = supply.get(from);
		} catch (Exception e) {
			notify(this);
			throw new IllegalArgumentException();
		}

		if(addedResource != wes.getStoredResource()) {
			notify(this);
			throw new IllegalStateException();
		}

		try {
			warehouse.storeInExtraDeposit(leaderCardPosition, supply.get(from), to);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException();
		}

		supply.remove(from);
		notify(this);

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
						reduce(0, Integer::sum) == NUM_DEVELOPMENT_CARDS_TO_WIN ||
				(getLorenzoIlMagnificoPosition() == GameSettings.FAITH_TRACK_LENGTH - 1);

	}

	/**
	 * Activates new extra deposit inside the warehouse
	 */
	public void activateExtraDeposit(int leaderCardPos) {
		warehouse.activateExtraDeposit(leaderCardPos);
	}


	/**
	 * Returns all of the player's resources.
	 *
	 * @return	a map with a count of each total qty of each resource in the warehouse.
	 */
	public Map<Resource, Integer> getAllPlayerResources() {
		return warehouse.getAllResources();
	}


	/**
	 * Removes the resources from the warehouse
	 * @param resToPayWith    a SelectedResource data structure containing the player's choices of where to take the resources from
	 */
	public void payPrice(ResourceContainer resToPayWith) {
		resToPayWith.getSelectedDepositIndexes().forEach(warehouse::removeFromDeposit);
		resToPayWith.getSelectedLockerResources().forEach(warehouse::removeFromLocker);
		IntStream.range(0, 2).forEach(i ->
				resToPayWith.getSelectedExtraDepositIndexes().get(i).forEach(pos ->
						warehouse.removeFromExtraDeposit(i, pos)));
		notify(this);
	}

	// TODO doc
	public void simulatePayment(ResourceContainer resToPayWith, Map<Resource, Integer> cost) throws IllegalArgumentException {
		HashMap<Resource, Integer> rcAllRes = (HashMap<Resource, Integer>) resToPayWith.getAllResources(warehouse);
		cost.forEach((k, v) -> rcAllRes.merge(k, v, (v1, v2) -> v1-v2));
		if (rcAllRes.values().stream().anyMatch(v -> v != 0)) {
			notify(this);
			throw new IllegalArgumentException("Resources do not match the cost");
		}
	}

	// TODO doc

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public FaithTrack getFaithTrack() {
		return faithTrack;
	}

	public ArrayList<Resource> getSupply() {
		return supply;
	}

	public List<LeaderCard> getPlayedLeaderCards() {
		return playedLeaderCards;
	}

	public List<Stack<DevelopmentCard>> getPlayedDevelopmentCards() {
		return playedDevelopmentCards;
	}

	public Player getPlayer() {
		return player;
	}
}
