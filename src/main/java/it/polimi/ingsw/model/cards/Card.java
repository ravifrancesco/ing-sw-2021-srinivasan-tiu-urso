package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Player;

public interface Card {

	void play(Dashboard d, int position);

	void activate(Player p);

}