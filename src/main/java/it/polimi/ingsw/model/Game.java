package it.polimi.ingsw.model;

import java.util.Collection;

public class Game {

	private String gameId;

	private Player currentPlayer;

	private Collection<Player> players;

	private GameBoard gameBoard;

	public void init() {

	}

	public boolean checkEnd() {
		return false;
	}

	public Player checkWinner() {
		return null;
	}

	public boolean checkDiscardedResources() {
		return false;
	}

}
