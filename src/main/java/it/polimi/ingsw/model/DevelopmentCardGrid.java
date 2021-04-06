package it.polimi.ingsw.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The class represents the grid of Development Card onto the Dashboard
 */

public class DevelopmentCardGrid {
	static final int gridRowLength = 3;
	static final int gridColLength = 4;
	static final int DEVELOPMENT_CARD_NUM = 48;

	private final List<Stack<DevelopmentCard>> grid;

	/**
	 * The constructor for a DevelopmentCardGrid object
	 * It creates a grid of empty stacks
	 */

	DevelopmentCardGrid(){
		grid = IntStream.range(0, gridRowLength*gridColLength)
				.mapToObj(e->new Stack<DevelopmentCard>())
				.collect(Collectors.toList());
	}

	/**
	 * Init method for the class
	 * It fills the grid with all Development Cards of the game
	 * @param developmentCardDeck the deck of Development Cards
	 */

	public void init(Deck developmentCardDeck) {
		IntStream.range(0, DEVELOPMENT_CARD_NUM)
				.mapToObj(i -> (DevelopmentCard) developmentCardDeck.getCard())
				.forEach(c -> grid.get(getPosition(c.getBanner().getLevel(), getColumn(c.getBanner().getColor()))).push(c));
	}

	/**
	 * Private method to process the position in the list
	 * @param row index of the interest row
	 * @param column index of the interest column
	 * @return the position in the list of the element
	 */

	private int getPosition(int row, int column){
		return (row-1)*gridColLength+column;
	}

	/**
	 * Private method to get the column based on banner color
	 * @param color the color of the banner
	 * @return the column index
	 */

	private int getColumn(BannerEnum color){
		return switch(color){
			case GREEN -> 0;
			case BLUE -> 1;
			case YELLOW -> 2;
			case PURPLE -> 3;
		};
	}

	/**
	 * Allows to buy a card
	 * @param row index of the row where the card is placed
	 * @param column index of the column where the card is placed
	 * @return the bought card
	 */

	public DevelopmentCard buy(int row, int column) {
		int position = getPosition(row, column);
		return grid.get(position).pop();
	}

	/**
	 * Allows to know if the development card selected is buyable
	 * @param row index of the row where the card is placed
	 * @param column index of the column where the card is placed
	 * @param playerResources all the resources of the player
	 * @return if the development card is buyable or not with the given resources
	 */

	public boolean isBuyable(int row, int column, Map<Resource, Integer> playerResources) {
		long contResources;
		int position = getPosition(row, column);
		DevelopmentCard developmentCard = grid.get(position).peek();
		Map<Resource, Integer> resourceCost = developmentCard.getResourceCost();

		contResources=resourceCost.entrySet().stream()
				.filter(entry -> playerResources.get(entry.getKey())!=null && playerResources.get(entry.getKey())>=entry.getValue())
				.count();

		return contResources>=resourceCost.size();
	}

}
