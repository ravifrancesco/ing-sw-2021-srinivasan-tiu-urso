package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DevelopmentCardGrid {
	static final int gridRowLength = 3;
	static final int gridColLength = 4;
	static final int DEVELOPMENT_CARD_NUM = 48;

	private List<Stack<DevelopmentCard>> grid;

	DevelopmentCardGrid(){
		grid = new ArrayList<>();
		for(int i=0; i<gridRowLength*gridColLength; i++){
			grid.add(new Stack<>());
		}
	}

	public void init(Deck developmentCardDeck) {
		DevelopmentCard c;
		Banner b;
		for(int i=0; i<DEVELOPMENT_CARD_NUM; i++){
			c=(DevelopmentCard) developmentCardDeck.getCard();
			b=c.getBanner();
			grid.get(getPosition(b.getLevel(), getColumn(b.getColor()))).push(c);
		}
	}

	private int getPosition(int row, int column){
		return (row-1)*gridColLength+column;
	}

	private int getColumn(BannerEnum color){
		switch(color){
			case GREEN -> {
				return 0;
			}
			case BLUE -> {
				return 1;
			}
			case YELLOW -> {
				return 2;
			}
			case PURPLE -> {
				return 3;
			}
			default -> {
				return -1;
			}
		}
	}

	public DevelopmentCard buy(int row, int column) {
		int position = getPosition(row, column);
		return grid.get(position).pop();
	}

}
