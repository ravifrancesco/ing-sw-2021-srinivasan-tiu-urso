package it.polimi.ingsw.model;

import java.util.Map;

public class Dashboard {

	private Warehouse warehouse;

	private FaithTrack faithTrack;

	private LeaderCard[] playedLeaderCards;

	private Player player;

	public Dashboard(Warehouse warehouse, FaithTrack faithTrack, LeaderCard[] playedLeaderCards, Player player) {
		this.warehouse = warehouse;
		this.faithTrack = faithTrack;
		this.playedLeaderCards = playedLeaderCards;
		this.player = player;
	}

	public void moveFaithMarker(int pos) {
		faithTrack.moveFaithMarker(pos);
	}

	public int computePlayerPoints() {
		return 0;
	}

	public void insertLeaderCard(LeaderCard c, int position){

	}

	public void insertDevelopmentCard(DevelopmentCard c, int position){

	}

	public void storeResourceInLocker(Resource r, int quantity){
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}