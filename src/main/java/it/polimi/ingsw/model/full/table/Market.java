package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.full.marbles.*;
import it.polimi.ingsw.model.observerPattern.observables.MarketObservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

/**
 * This class represents the only Market in the game.
 * <p>
 * The class is observable and notifies the observers on a change of the state.
 */

public class Market extends MarketObservable {
    public static final int gridRowLength = 3;

    public static final int gridColLength = 4;

    private Marble[] marblesGrid;

    /**
     * The constructor for the Market class.
     * It allocates space for the grid, using the two final attributes representing the dimension.
     */
    public Market() {
        this.marblesGrid = new Marble[(gridRowLength * gridColLength) + 1];
        this.reset();
    }


    /**
     * Initializes the market, filling the grid and the free marble randomly.
     */
    public void reset() {
        ArrayList<Marble> allMarbles = new ArrayList<>();

        IntStream.range(0, 2).forEach(i -> allMarbles.add(new BlueMarble()));
        IntStream.range(0, 2).forEach(i -> allMarbles.add(new PurpleMarble()));
        IntStream.range(0, 2).forEach(i -> allMarbles.add(new YellowMarble()));
        IntStream.range(0, 2).forEach(i -> allMarbles.add(new GreyMarble()));
        IntStream.range(0, 4).forEach(i -> allMarbles.add(new WhiteMarble()));
        allMarbles.add(new RedMarble());

        Collections.shuffle(allMarbles);

        marblesGrid = allMarbles.toArray(new Marble[0]);

        notify(this);
    }

    /**
     * Executes the "get resources from market" move.
     *
     * @param move integer representing the move. The scheme for the latter are the following:
     *             W Y G P |  0
     *             B P P W |  1
     *             G Y W R |  2
     *             - - - - -
     *             3 4 5 6
     *             For example, if move = 4, the user will receive resources from two yellow marbles and a purple one.
     * @param p    the player doing the move
     * @return an ArrayList of collected Resource from the move.
     */
    public ArrayList<Resource> getResources(int move, Player p) {
        // The check that the 'wmrs' AL contains only activated WhiteMarbleResources is done by the controller
        return isRowMove(move) ? doRowMove(move, p) : doColMove(move - 3, p);
    }

    /**
     * Returns the marble from the grid.
     *
     * @param row of the desired marble
     * @param col of the desired marble
     * @return the marble
     */
    public Marble getMarble(int row, int col) {
        return marblesGrid[(row * gridColLength) + col];
    }

    /**
     * Substitutes the current marble with a new one, inside the grid.
     *
     * @param row    of the marble to be set
     * @param col    of the marble to be set
     * @param newMar the marble that will replace the old one
     */
    private void setMarble(int row, int col, Marble newMar) {
        marblesGrid[(row * gridColLength) + col] = newMar;
    }

    /**
     * Getter for the free marble.
     * @return the current free marble.
     */
    public Marble getFreeMarble() {
        return getMarble(gridRowLength - 1, gridColLength);
    }

    /**
     * Swaps two marbles inside the grid
     *
     * @param rowSrc row of the first marble to be swapped
     * @param colSrc column of the first marble to be swapped
     * @param rowDst row of the second marble to be swapped
     * @param colDst column of the second marble to be swapped
     */
    private void swapMarble(int rowSrc, int colSrc, int rowDst, int colDst) {
        Marble temp = getMarble(rowSrc, colSrc);
        setMarble(rowSrc, colSrc, getMarble(rowDst, colDst));
        setMarble(rowDst, colDst, temp);
    }

    /**
     * Establishes the type of move: from 0 to 2 we have row moves, from 3 to 6 column moves.
     *
     * @param move the move integer
     * @return true if it is a row move, false if not.
     */
    private boolean isRowMove(int move) {
        return move < 3;
    }

    /**
     * Executes a move on a row.
     *
     * @param move the move integer
     * @param p    player doing the move
     * @return the ArrayList of collected resources.
     */
    private ArrayList<Resource> doRowMove(int move, Player p) {
        ArrayList<Resource> collectedResources = new ArrayList<>();

        Resource res;
        for (int i = 0; i < gridColLength; i++) {
            res = getMarble(move, i).getResource(p);
            if (res != null) {
                collectedResources.add(res);
            }
        }

        shiftRow(move);
        swapMarble(move, gridColLength - 1, gridRowLength - 1, gridColLength);
        // the coordinates gridRowLength-1 (2) and gridColLength(4) return 2*4 + 4 = 12, the index of the freeMarble
        notify(this);
        return collectedResources;
    }

    /**
     * Executes a move on a column.
     *
     * @param move the move integer
     * @param p    player doing the move
     * @return the ArrayList of collected resources.
     */
    private ArrayList<Resource> doColMove(int move, Player p) {
        ArrayList<Resource> collectedResources = new ArrayList<>();

        Resource res;
        for (int i = 0; i < gridRowLength; i++) {
            res = getMarble(i, move).getResource(p);
            if (res != null) {
                collectedResources.add(res);
            }
        }

        shiftCol(move);
        swapMarble(gridRowLength - 1, move, gridRowLength - 1, gridColLength);
        // the coordinates gridRowLength-1 (2) and gridColLength(4) return 2*4 + 4 = 12, the index of the freeMarble
        notify(this);
        return collectedResources;
    }

    /**
     * Shifts a row of the grid.
     *
     * @param row the row to be shifted.
     */
    private void shiftRow(int row) {
        for (int i = 0; i < gridColLength - 1; i++) {
            swapMarble(row, i, row, i + 1);
        }
    }

    /**
     * Shifts a column of the grid
     *
     * @param col the grid to be shifted
     */
    private void shiftCol(int col) {
        for (int i = 0; i < gridRowLength - 1; i++) {
            swapMarble(i, col, i + 1, col);

        }
    }

    /**
     * Getter for the marbles grid.
     * @return the marbles grid.
     */
    public Marble[] getMarblesGrid() {
        return marblesGrid;
    }
}