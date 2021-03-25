package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class Market {
	private int gridRowLenght;

	private int gridColLength;

	private Marble[][] marblesGrid;

	private Marble freeMarble;


	/**
	 * Initializes the market, filling it at random.
	 */
	public void init() {
		gridRowLenght = 3;
		gridColLength = 4;
		marblesGrid = new Marble[gridRowLenght][gridColLength];
		ArrayList<Marble> availableMarbles = new ArrayList<Marble>();
		fillAvailableMarbles(availableMarbles);
		int randInt;

		randInt = ThreadLocalRandom.current().nextInt(0, availableMarbles.size());
		freeMarble = availableMarbles.get(randInt);
		availableMarbles.remove(randInt);

		for(int i = 0; i < gridRowLenght; i++) {
			for(int j = 0; j < gridColLength; j++) {
				randInt = ThreadLocalRandom.current().nextInt(0, availableMarbles.size());
				marblesGrid[i][j] = availableMarbles.get(randInt);
				availableMarbles.remove(randInt);
			}
		}
	}

	/**
	 * @param move is the integer representing the row and column where the free marble is moved.
	 *             The scheme of moves is the following:
	 *             W Y G P |  1
	 *             B P P W |  2
	 *             G Y W R |  3
	 *             - - - - -
	 *             4 5 6 7
	 *
	 *             Move 4 for example would get the player resources from a White, Blue and Grey marble.
	 *
	 * @param p is the player associated executing the move.
	 * @return a collection of the resources corresponding to the marbles moved.
	 */
	public Collection<Resource> getResources(int move, Player p) {
		ArrayList<Resource> collectedResources = new ArrayList<Resource>();
		if (move < 4) {
			move--;
			// move is now the corresponding index of the row to be shifted


			for (int i = 0; i < 4; i++) {
				Resource res = marblesGrid[move][i].getResource(p);
				if(res != null) {
					collectedResources.add(marblesGrid[move][i].getResource(p));
				}
			}
			shiftRowAfterCollection(move);

		} else {
			move = move - 4;
			// move is now the corresponding index of the column to be shifted

			for (int i = 0; i < 3; i++) {
				Resource res = marblesGrid[i][move].getResource(p);
				if (res != null) {
					collectedResources.add(res);
				}
			}
			shiftColumnAfterCollection(move);
		}

		for(Resource r : collectedResources) {
			System.out.println(r);
		}
		showMarket();

		return collectedResources;
	}

	/**
	 * Fills a collection with every possible marble in the market.
	 * @param allMarbles the collection that will be filled.
	 */
	private void fillAvailableMarbles(Collection<Marble> allMarbles) {

		for(int i = 0; i < 4; i++) {
			allMarbles.add(new WhiteMarble());
		}

		for(int i = 0; i < 2; i++) {
			allMarbles.add(new BlueMarble());
		}

		for(int i = 0; i < 2; i++) {
			allMarbles.add(new GreyMarble());
		}

		for(int i = 0; i < 2; i++) {
			allMarbles.add(new YellowMarble());
		}

		for(int i = 0; i < 2; i++) {
			allMarbles.add(new PurpleMarble());
		}

		allMarbles.add(new RedMarble());

	}

	/**
	 * Shifts the corresponding row, inserting the freeMarble according to game rules.
	 * @param move is the integer associated with the row to be shifted.
	 */
	private void shiftRowAfterCollection(int move) {
		int i, j;
		Marble temp;
		Marble oldFreeMarble = freeMarble;

		// System.out.println("La freeMarble era " + freeMarble.getNum());
		freeMarble = marblesGrid[move][0];
		// System.out.println("La freeMarble è " + freeMarble.getNum());

		for(i = 0; i < gridColLength-1; i++) {
			j = i + 1;
			temp = marblesGrid[move][i];
			marblesGrid[move][i] = marblesGrid[move][j];
			marblesGrid[move][j] = temp;
			if(j == 3) {
				marblesGrid[move][j] = oldFreeMarble;
			}
		}
	}

	/**
	 * Shifts the corresponding column, inserting the freeMarble according to game rules.
	 * @param move is the integer associated with the column to be shifted.
	 */
	private void shiftColumnAfterCollection(int move) {
		int i, j;
		Marble temp;
		Marble oldFreeMarble = freeMarble;
		freeMarble = marblesGrid[0][move];

		for(i = 0; i < gridRowLenght-1; i++) {
			j = i + 1;
			temp = marblesGrid[i][move];
			marblesGrid[i][move] = marblesGrid[j][move];
			marblesGrid[j][move] = temp;
			if(j == 2) {
				marblesGrid[j][move] = oldFreeMarble;
			}
		}
	}

	/**
	 * Prints the market, mostly used for initial debugging.
	 */
	private void showMarket() {
		System.out.println("Free marble is " + freeMarble.getNum());

		System.out.println("The market is \n");

		for(int i = 0; i < gridRowLenght; i++) {
			for(int j = 0; j < gridColLength; j++) {
				System.out.print(marblesGrid[i][j].getNum() + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Main used for debugging.
	 * @param args
	 */
	public static void main(String[] args) {
		Player p = new Player();
		Market a = new Market();
		a.init();
		a.showMarket();

		System.out.println(a.getMarblesGrid()[0][0] instanceof WhiteMarble);
	}

	public int getGridRowLenght() {
		return gridRowLenght;
	}

	public int getGridColLength() {
		return gridColLength;
	}

	public Marble[][] getMarblesGrid() {
		return marblesGrid;
	}

	public Marble getFreeMarble() {
		return freeMarble;
	}


}


