package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.full.cards.BannerEnum;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.observerPattern.observables.DevelopmentCardGridObservable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The class represents the grid of Development Card onto the GameBoard.
 */

public class DevelopmentCardGrid extends DevelopmentCardGridObservable {
    public static final int GRID_ROW_LENGTH = 3;
    public static final int GRID_COL_LENGTH = 4;
    public static final int DEVELOPMENT_CARD_NUM = 48;

    private List<Stack<DevelopmentCard>> grid;

    /**
     * Constructor for a DevelopmentCardGrid object.
     */
    public DevelopmentCardGrid() {
        reset();
    }

    /**
     * Reset method for the class.
     * It fills the grid with empty stacks of cards.
     */
    public void reset() {

        this.grid = IntStream.range(0, GRID_ROW_LENGTH * GRID_COL_LENGTH)
                .mapToObj(e -> new Stack<DevelopmentCard>())
                .collect(Collectors.toList());

        notify(this);

    }

    /**
     * Fill method for the class.
     * It fills the grid with all Development Cards of the game.
     *
     * @param developmentCardDeck the deck of Development Cards.
     */
    public void fillCardGrid(Deck developmentCardDeck) {
        IntStream.range(0, DEVELOPMENT_CARD_NUM)
                .mapToObj(i -> (DevelopmentCard) developmentCardDeck.getCard())
                .forEach(c -> grid.get(getPosition(c.getBanner().getLevel(), getColumn(c.getBanner().getColor()))).push(c));

        notify(this);
    }

    /**
     * Private method to process the position in the list.
     *
     * @param row    index of the interest row.
     * @param column index of the interest column.
     * @return the position in the list of the element.
     */
    private int getPosition(int row, int column) {
        return (row - 1) * GRID_COL_LENGTH + (column - 1);
    }

    /**
     * Private method to get the column based on banner color.
     *
     * @param color the color of the banner.
     * @return the column index.
     */
    private int getColumn(BannerEnum color) {
        return switch (color) {
            case GREEN -> 1;
            case BLUE -> 2;
            case YELLOW -> 3;
            case PURPLE -> 4;
        };
    }

    /**
     * Allows to buy a card.
     *
     * @param row    index of the row where the card is placed (starting from 1).
     * @param column index of the column where the card is placed (starting from 1).
     * @return the bought card.
     */
    public DevelopmentCard buy(int row, int column) {
        int position = getPosition(row, column);
        DevelopmentCard card = grid.get(position).pop();
        notify(this);
        return card;
    }

    /**
     * Allows to peek a card.
     *
     * @param row    index of the row where the card is placed (starting from 1).
     * @param column index of the column where the card is placed (starting from 1).
     * @return the "peek" card.
     */
    public DevelopmentCard peek(int row, int column) {
        int position = getPosition(row, column);
        return grid.get(position).peek();
    }

    /**
     * Allows to know if the development card selected is buyable.
     *
     * @param row             index of the row where the card is placed (starting from 1).
     * @param column          index of the column where the card is placed (starting from 1).
     * @param playerResources all the resources of the player.
     * @param activeDiscounts the activated discounts of the player.
     * @throws IllegalArgumentException if the row or the column is out of bound or if the stack of the grid is empty.
     * @return if the development card is buyable or not with the given resources.
     */
    public boolean isBuyable(int row, int column, Map<Resource, Integer> playerResources, ArrayList<DevelopmentCardDiscount> activeDiscounts)
            throws IllegalArgumentException {
        long contResources;
        int position = getPosition(row, column);
        DevelopmentCard developmentCard;

        if (row < 1 || row > 3 || column < 1 || column > 4) {
            throw new IllegalArgumentException();
        }

        try {
            developmentCard = grid.get(position).peek();
        } catch (EmptyStackException e) {
            throw new IllegalArgumentException();
        }

        Map<Resource, Integer> resourceCost = developmentCard.getResourceCost();

        resourceCost.entrySet().forEach(e -> activeDiscounts.stream()
                .filter(e2 -> e.getKey() == e2.getResource())
                .forEach(e2 -> e.setValue(Math.max(e.getValue() - e2.getQuantity(), 0))));

        resourceCost.values().removeIf(v -> v == 0);

        contResources = resourceCost.entrySet().stream()
                .filter(entry -> playerResources.get(entry.getKey()) != null && playerResources.get(entry.getKey()) >= entry.getValue())
                .count();

        return contResources >= resourceCost.size();
    }

    /**
     * Getter for the grid.
     * @return the grid of the development cards.
     */
    public List<Stack<DevelopmentCard>> getGrid() {
        return grid;
    }

    /**
     * Discards a card from the grid (used by Lorenzo in the singleplayer)
     *
     * @param COLOR_INDEX the index for the color to discard.
     * @return if the position after discarding is empty, false otherwise.
     */
    public boolean discardCard(int COLOR_INDEX) {
        if (!grid.get(COLOR_INDEX).isEmpty()) {
            grid.get(COLOR_INDEX).pop();
        } else if (!grid.get(COLOR_INDEX + 4).isEmpty()) {
            grid.get(COLOR_INDEX + 4).pop();
        } else if (!grid.get(COLOR_INDEX + 2 * 4).isEmpty()) {
            grid.get(COLOR_INDEX + 2 * 4).pop();
        }

        if (grid.get(COLOR_INDEX + 2 * 4).isEmpty()) {
            notify(this);
            return true;
        } else {
            notify(this);
            return false;
        }

    }
}