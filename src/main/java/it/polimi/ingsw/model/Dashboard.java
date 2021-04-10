package it.polimi.ingsw.model;

/**
 * The class represent a personal Dashboard of the game.
 * It has a Warehouse, a FaithTrack, the Played Leader Cards and

 * the reference of the player.
 */

public class Dashboard {

	private Warehouse warehouse;
	private final FaithTrack faithTrack;
	private final LeaderCard[] playedLeaderCards;
	private Player player;

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
	 * @param position the position (1, 2 or 3) where to place the card
	 */
	public void insertDevelopmentCard(DevelopmentCard c, int position){
		// TODO
	}

	/**
	 * Allows to store a resource in the Warehouse's locker
	 * @param r the resource to store
	 * @param quantity the quantity of the resource
	 */
	public void storeResourceInLocker(Resource r, int quantity){
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


}
