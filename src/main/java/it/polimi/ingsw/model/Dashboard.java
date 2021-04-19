package it.polimi.ingsw.model;

import it.polimi.ingsw.common.Pair;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The class represent a personal Dashboard of the game.
 * It has a Warehouse, a FaithTrack, the Played Leader Cards and

 * the reference of the player.
 */

public class Dashboard {

	static final int NUM_DEVELOPMENT_CARD_STACKS = 3;

	private Warehouse warehouse;
	private final FaithTrack faithTrack;
	private final LeaderCard[] playedLeaderCards;
	private Player player;
	private ProductionPower dashBoardProductionPower;
	private ArrayList<Resource> supply;
	private List<Stack<DevelopmentCard>> playedDevelopmentCards;



	//TODO add three stacks of development card

	/**
	 * The constructor for a Dashboard object.
	 * It creates an empty Warehouse, a FaithTrack and initializes the playedLeaderCards and the player.
	 * @param gameSettings the settings for the current game.
	 * @param player the player to whom the dashboard belongs.
	 */
	public Dashboard(GameSettings gameSettings, Player player) {
		this.warehouse = new Warehouse();
		this.faithTrack = new FaithTrack(gameSettings);
		this.playedLeaderCards = new LeaderCard[2];

		this.playedDevelopmentCards = IntStream.range(0, NUM_DEVELOPMENT_CARD_STACKS)
				.mapToObj(e->new Stack<DevelopmentCard>())
				.collect(Collectors.toList());

		this.player = player;
	}

	/**
	 * Allows to move the Faith Marker on the FaithTrack.
	 * @param pos represents how many positions to go on
	 */
	public void moveFaithMarker(int pos) {
		faithTrack.moveFaithMarker(pos);
	}

	public int getFaithMarkerPosition() { return faithTrack.getPosition(); }

	public int computePlayerPoints() {
		// TODO

		return 0;
	}

	/**
	 * Allows to insert a Leader Card onto the Dashboard
	 * @param c the Leader Card to place
	 * @param position the position (1 or 2) where to place the card
	 */
	public void insertLeaderCard(LeaderCard c, int position){
		// TODO
	}

	/**
	 * Allows to insert a DevelopmentCard onto the Dashboard
	 * @param c the Development Card to place
	 */
	public void insertDevelopmentCard(DevelopmentCard c){
		// TODO
	}

	/**
	 * Allows to store a resource in the Warehouse's locker
	 * @param r the resource to store
	 * @param quantity the quantity of the resource
	 */
	public void storeResourceInLocker(Resource r, int quantity) {
		// TODO
	}

	public void storeResourceInDeposit(Resource r, int position) throws IllegalArgumentException, IllegalStateException {
		// TODO
	}

	/**
	 * Getter for the Player
	 * @return the player to whom the dashboard belongs
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Setter for the Player
	 * @param player the player to whom the dashboard belongs
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse wh) {
		warehouse = wh;
	}

	public Map<Resource, Integer> getResources() {
		// TODO
		return null;
	}
  
	public Map<Banner, Integer> getBanners() {
		// TODO
		return null;
	}

	public LeaderCard getLeaderCard(int c) {
		return playedLeaderCards[c];
	}

	public ProductionPower getDashBoardProductionPower() {
		return dashBoardProductionPower;
	}

	public DevelopmentCard getDevelopmentCard(int c) {
		// TODO
		return null;
	}

	public void addResourcesToSupply(ArrayList<Resource> res) {
		supply.addAll(res);
	}



	public int discardResources() {
		int faithPoints = supply.size();
		supply.clear();
		return faithPoints;
	}

	public void resetProductionPowers() {
		// TODO
	}

	public int getStoredResourceQty() {
		// TODO
		return 0;
	}

	public void moveDepositResources(ArrayList<Pair<Integer, Integer>> moves) throws IllegalArgumentException, IllegalStateException {
		// TODO
		// checks that the two integer of the pair makes sense -> else IllegalArg
		// checks that the moves generate a legal deposit -> else IllegalState
	}

	public void storeFromSupply(int from, int to) throws IllegalArgumentException, IllegalStateException{
		// TODO
		// checks that the two integer of the pair makes sense -> else IllegalArg
		// checks that the moves generate a legal deposit -> else IllegalState
	}

	public void storeInExtraDeposit(int leaderCardPosition, int from, int to) throws IllegalArgumentException, IllegalStateException {
		// TODO
	}
}
