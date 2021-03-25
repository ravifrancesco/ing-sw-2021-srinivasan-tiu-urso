package it.polimi.ingsw.model;

public class Player {

	private String nickname;

	private String id;

	private Dashboard dashboard;

	public Player(String nickname, String id, Dashboard dashboard) {
		this.nickname = nickname;
		this.id = id;
		this.dashboard = dashboard;
	}

	public void init() {

	}

	public Dashboard getDashboard() {
		return dashboard;
	}
}