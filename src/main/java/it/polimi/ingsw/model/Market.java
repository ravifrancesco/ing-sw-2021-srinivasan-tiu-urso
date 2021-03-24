package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class Market {

	private Marble[][] marblesGrid;

	private Marble freeMarble;

	/**
	 * @param move is the integer representing the row and column where the free marble is moved.
	 *             The scheme of moves is the following:
	 *             W Y G P |  1
	 *             B P P W |  2
	 *             G Y W R |  3
	 *             - - - - -
	 *             7 6 5 4
	 *             4 3 2 1
	 *
	 *             Move 7 would get the player resources from a White, Blue and Grey marble.
	 *
	 * @return a collection of the resources corresponding to the marbles moved.
	 */

	public Collection<Resource> getResources(int move, Player p) {
		ArrayList<Resource> collectedResources = new ArrayList<Resource>();
		if (move < 4) {
			move--;
			// move is now the corresponding index of the row to be shifted


			for (int i = 0; i < 4; i++) {
				collectedResources.add(marblesGrid[i][move].getResource(p));
			}

			// shiftAfterCollection();
		} else {
			move = move - 4;
			// move is now the corresponding index of the column to be shifted

			for (int i = 0; i < 3; i++) {
				Resource res = marblesGrid[i][move].getResource(p);
				if (res != null) {
					collectedResources.add(res);
				}
			}
		}

		return collectedResources;
	}


	private void initMarket() {
		marblesGrid = new Marble[3][4];
		ArrayList<Marble> availableMarbles = new ArrayList<Marble>();

		for(int i = 0; i < 4; i++) {
			availableMarbles.add(new WhiteMarble());
		}

		for(int i = 0; i < 2; i++) {
			availableMarbles.add(new BlueMarble());
		}

		for(int i = 0; i < 2; i++) {
			availableMarbles.add(new GreyMarble());
		}

		for(int i = 0; i < 2; i++) {
			availableMarbles.add(new YellowMarble());
		}

		for(int i = 0; i < 2; i++) {
			availableMarbles.add(new PurpleMarble());
		}

		availableMarbles.add(new RedMarble());

		freeMarble = availableMarbles.get(ThreadLocalRandom.current().nextInt(0, availableMarbles.size()));

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 4; j++) {
				marblesGrid[i][j] = availableMarbles.get(ThreadLocalRandom.current().nextInt(0, availableMarbles.size()));
			}
		}

	}

	private void showMarket() {
		System.out.println("Free marble is " + freeMarble.getNum());

		System.out.println("The market is \n");

		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 4; j++) {
				System.out.print(marblesGrid[i][j].getNum() + " ");
			}
			System.out.println();
		}
	}


	public static void main(String[] args) {
		Player p = new Player();
		Market a = new Market();
		a.initMarket();
		a.showMarket();
		Collection<Resource> temp = a.getResources(5, p);
		for(Resource prova : temp) {
			System.out.println(prova);
		}
	}

	private void shiftRowAfterCollection(int move) {
		Marble temp;
		Marble oldFreeMarble = freeMarble;
		freeMarble = marblesGrid[move][0];
		for(int i = 0; i < 3; i++) {
			for(int j = 1; j < 4; j++) {
				temp = marblesGrid[move][i];
				marblesGrid[move][i] = marblesGrid[move][j];


			}
		}
	}

	private void shiftColumnAfterCollection(int move) {
		Marble oldFreeMarble = freeMarble;
		freeMarble = marblesGrid[0][move];
	}
}
