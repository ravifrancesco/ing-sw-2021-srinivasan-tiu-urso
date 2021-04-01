package it.polimi.ingsw.model;

public class Player {

	private String nickname;

	private String id;

	private Dashboard dashboard;

	public Player(String nickname, String id) {
		this.nickname = nickname;
		this.id = id;
	}

	public void init() {

	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(Dashboard dashboard) { this.dashboard = dashboard; }
}