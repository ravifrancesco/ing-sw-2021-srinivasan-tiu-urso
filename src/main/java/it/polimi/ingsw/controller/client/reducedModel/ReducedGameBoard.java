package it.polimi.ingsw.controller.client.reducedModel;

import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.marbles.Marble;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ReducedGameBoard {

    // Market
    static final int gridRowLength = 3;
    static final int gridColLength = 4;
    private Marble[] marblesGrid;

    // Development card grid
    public static final int GRID_ROW_LENGTH = 3;
    public static final int GRID_COL_LENGTH = 4;
    private List<Stack<DevelopmentCard>> grid;

    // Decks
    private List<LeaderCard> leaderCardDeck;
    private List<DevelopmentCard> developmentCardDeck;
    private List<Card> discardDeck;

    private ReducedModel reducedModel;

    public ReducedGameBoard(ReducedModel reducedModel) {
        this.reducedModel = reducedModel;
        this.marblesGrid = new Marble[(gridRowLength * gridColLength) + 1];
        grid = new ArrayList<>();
        leaderCardDeck = new ArrayList<>();
        developmentCardDeck = new ArrayList<>();
        discardDeck = new ArrayList<>();
    }

    public Marble[] getMarblesGrid() {
        return marblesGrid;
    }

    public void setMarblesGrid(Marble[] marblesGrid) {
        this.marblesGrid = marblesGrid;
        updateMarket();
    }

    public List<Stack<DevelopmentCard>> getGrid() {
        return grid;
    }

    public void setGrid(List<Stack<DevelopmentCard>> grid) {
        this.grid = grid;
        updateDecCardGrid();
    }

    public List<LeaderCard> getLeaderCardDeck() {
        return leaderCardDeck;
    }

    public void setLeaderCardDeck(List<LeaderCard> leaderCardDeck) {
        this.leaderCardDeck = leaderCardDeck;
    }

    public List<DevelopmentCard> getDevelopmentCardDeck() {
        return developmentCardDeck;
    }

    public void setDevelopmentCardDeck(List<DevelopmentCard> developmentCardDeck) {
        this.developmentCardDeck = developmentCardDeck;
    }

    public List<Card> getDiscardDeck() {
        return discardDeck;
    }

    public void setDiscardDeck(List<Card> discardDeck) {
        this.discardDeck = discardDeck;
    }

    public ArrayList<Marble> getMarblesMove(int move) {
        return move < 3 ? getMarblesRowMove(move) : getMarblesColMove(move-3);
    }

    public Marble getMarble(int row, int col) {
        return marblesGrid[(row*gridColLength) + col];
    }

    private ArrayList<Marble> getMarblesRowMove(int move) {
        ArrayList<Marble> marbles = new ArrayList<>();

        for(int i = 0; i < gridColLength; i++) {
            marbles.add(getMarble(move, i));
        }
        return marbles;
    }

    private ArrayList<Marble> getMarblesColMove(int move) {
        ArrayList<Marble> marbles = new ArrayList<>();

        for (int i = 0; i < gridRowLength; i++) {
            marbles.add(getMarble(i, move));
        }
        return marbles;
    }

    private void updateMarket() {
        Marble[] marbles = Arrays.copyOfRange(marblesGrid, 0, marblesGrid.length - 1);
        Marble freeMarble = marblesGrid[marblesGrid.length - 1];
        this.reducedModel.updateMarket(marbles, freeMarble);
    }

    private void updateDecCardGrid() {
        reducedModel.updateDevelopmentCardGrid(this.grid);
    }

}
