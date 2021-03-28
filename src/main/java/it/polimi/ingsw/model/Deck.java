package it.polimi.ingsw.model;

import java.util.*;

public class Deck {

	private Stack<DevelopmentCard> developmentCardDeck;
	private Stack<LeaderCard> leaderCardDeck;
	private List<Card> discardDeck;

	Deck(GameSettings gameSettings){
		developmentCardDeck=new Stack<>();
		leaderCardDeck=new Stack<>();
		discardDeck=new ArrayList<>();
		Arrays.stream(gameSettings.getDevelopmentCards()).forEach(c->developmentCardDeck.push(c));
		Arrays.stream(gameSettings.getLeaderCards()).forEach(c->leaderCardDeck.push(c));
	}

	public void shuffle() {
		Collections.shuffle(developmentCardDeck);
		Collections.shuffle(leaderCardDeck);
	}

	public void discard(Card c){
		discardDeck.add(c);
	}

	public LeaderCard getLeaderCard(){
		return !leaderCardDeck.empty() ? leaderCardDeck.pop() : null;
	}

	public DevelopmentCard getDevelopmentCard(){
		return !developmentCardDeck.empty() ? developmentCardDeck.pop() : null;
	}

}