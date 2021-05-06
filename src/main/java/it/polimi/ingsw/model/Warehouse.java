package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.IntStream;

public class Warehouse {

	static final int FIRST_SHELF_THRESHOLD = 1;
	static final int SECOND_SHELF_THRESHOLD = 3;
	static final int THIRD_SHELF_THRESHOLD = 6;
	static final int MAX_EXTRA_DEPOSIT_SPACE = 2;
	static final int MAX_EXTRA_DEPOSIT_SLOTS = 2;
	static final int MAX_DEPOSIT_SLOTS = 6;

	private Resource[] deposit;
	private final Map<Resource, Integer> locker;
	private Resource[][] extraDeposits;

	/**
	 * The constructor for the class.
	 * Allocates space for the locker and the deposit (both are Resource-Integer HashMaps), and calls the clear
	 * (initialization) method.
	 */
	public Warehouse() {
		locker = new HashMap<>();
		deposit = new Resource[MAX_DEPOSIT_SLOTS];
		extraDeposits = new Resource[MAX_EXTRA_DEPOSIT_SLOTS][];
		reset();
	}

	public void reset() {
		this.clearDeposit();
		this.clearLocker();
		extraDeposits = new Resource[MAX_EXTRA_DEPOSIT_SLOTS][];
	}

	/**
	 * Initializes the deposit, by setting each resource's stored quantity to 0.
	 */
	public void clearDeposit() {
		deposit = new Resource[MAX_DEPOSIT_SLOTS];
	}

	/**
	 * Initializes the locker, by setting each resource's stored quantity to 0.
	 */
	public void clearLocker() {
		locker.put(Resource.STONE, 0);
		locker.put(Resource.SHIELD, 0);
		locker.put(Resource.GOLD, 0);
		locker.put(Resource.SERVANT, 0);
	}

	/**
	 * Stores a resource from the dashboard's supply inside the deposit.
	 * @param resToAdd 					resource to store
	 * @param pos						deposit position in which the resource is stored
	 * @throws IllegalStateException	if resource is stored in an illegal position
	 */
	public void storeInDeposit(Resource resToAdd, int pos) throws IllegalStateException, IllegalArgumentException {
		Resource[] newDeposit = new Resource[MAX_DEPOSIT_SLOTS];
		IntStream.range(0, MAX_DEPOSIT_SLOTS).forEach(i -> newDeposit[i] = deposit[i]);
		// adding new resource
		newDeposit[pos] = resToAdd;
		// checking deposit legality
		if (!checkShelvesRule(newDeposit)) {
			throw new IllegalStateException("Deposit positioning is illegal");
		}
		if(deposit[pos] != null) {
			throw new IllegalArgumentException();
		}
		// deposit is legal => adding resource to real deposit
		deposit[pos] = resToAdd;
	}

	/**
	 * Clears a deposit position
	 *
	 * @param pos							position to clear
	 */
	public void removeFromDeposit(int pos) {
		deposit[pos] = null;
	}

	/**
	 * Moves two deposit positions.
	 * @param from 	move from.
	 * @param to	move to.
	 */
	public void doDepositMove(int from, int to) {
		Resource temp = deposit[from];
		deposit[from] = deposit[to];
		deposit[to] = temp;
	}

	/**
	 * Returns the amount of stored resources inside the deposit.
	 *
	 * @return								the amount of resources inside the deposit
	 */
	public int getDepositResourceQty() {
		return (int) Arrays.stream(deposit).filter(Objects::nonNull).count();
	}

	/**
	 * Stores resources in the locker, with no checks due to the fact that the locker has no restrictions.
	 *
	 * @param resToAdd 						ArrayList with the resources to add.
	 */
	public void storeInLocker(Resource resToAdd, int qty) {
		locker.put(resToAdd, locker.get(resToAdd) + qty);
	}

	/**
	 * Removes resources from the locker
	 *
	 * @param resToRemove					resource to remove from
	 * @param qty							the amount of resource quantity to remove
	 */
	public void removeFromLocker(Resource resToRemove, int qty) {
		locker.put(resToRemove, locker.get(resToRemove) - qty);
	}

	/**
	 * Activates an extra deposit, allowing to store MAX_EXTRA_DEPOSIT_SLOTS extra units of a single resource type.
	 *
	 * @param extraDepositLeaderCardPos		position of the extra deposit (leader card)
	 */
	public void activateExtraDeposit(int extraDepositLeaderCardPos) {
		extraDeposits[extraDepositLeaderCardPos] = new Resource[MAX_EXTRA_DEPOSIT_SLOTS];
		IntStream.range(0, MAX_EXTRA_DEPOSIT_SLOTS).forEach(i -> extraDeposits[extraDepositLeaderCardPos][i] = null);
	}


	/**
	 * Stores a resource inside an extra deposit.
	 *
	 * @param extraDepositLeaderCardPos		position of the extra deposit (leader card)
	 * @param r								resource to be stored
	 * @param pos							position where to store the resource
	 */
	public void storeInExtraDeposit(int extraDepositLeaderCardPos, Resource r, int pos) throws IllegalArgumentException {
		if(extraDeposits[extraDepositLeaderCardPos][pos] != null) {
			throw new IllegalArgumentException();
		}
		extraDeposits[extraDepositLeaderCardPos][pos] = r;
	}

	/**
	 * Swaps two positions of an extra deposit.
	 *
	 * @param extraDepositLeaderCardPos		position of the extra deposit (leader card)
	 * @param from							first position to swap
	 * @param to							second position to swap
	 */
	public void swapExtraDeposit(int extraDepositLeaderCardPos, int from, int to) {
		Resource fromResource = extraDeposits[extraDepositLeaderCardPos][from];
		extraDeposits[extraDepositLeaderCardPos][from] = extraDeposits[extraDepositLeaderCardPos][to];
		extraDeposits[extraDepositLeaderCardPos][to] = fromResource;
	}

	/**
	 * Removes the resource from an extra deposit slot.
	 * @param extraDepositLeaderCardPos		position of the extra deposit (leader card)
	 * @param pos							position of the resource to remove
	 */
	public void removeFromExtraDeposit(int extraDepositLeaderCardPos, int pos) {
		extraDeposits[extraDepositLeaderCardPos][pos] = null;
	}

	/**
	 * Swaps resources from/to deposit to/from a extraDeposit
	 * @param from 							move from.
	 * @param to							move to.
	 * @param lcPos 						is 0 if move.first is the extraDeposit index,
	 *                                         1 if move.second is the extraDeposit index
	 * @param lcIndex 						the leader card index
	 */
	public void doExtraDepositMove(int from, int to, int lcPos, int lcIndex) {
		Resource temp;
		if(lcPos == 0) { // move.first is the extra deposit index
			temp = extraDeposits[lcIndex][from];
			extraDeposits[lcIndex][from] = deposit[to];
			deposit[to] = temp;
		} else {
			temp = extraDeposits[lcIndex][to];
			extraDeposits[lcIndex][to] = deposit[from];
			deposit[from] = temp;
		}
	}

	/**
	 * Checks the 'shelves rules' for the deposit
	 * <ul>
	 * <li> The maximum amount of stored resources is 6.
	 * <li> Only 3 different types of resources can be stored.
	 * <li> The maximum amount of each different kind of resource must be, in order, 3 2 and 1. (see game rules)
	 * @param newDeposit 						the hypothetical deposit after storage
	 * @return 									true if game rules are respected, false otherwise.
	 */
	public boolean checkShelvesRule(Resource[] newDeposit) {
		ArrayList<Resource> allResources = new ArrayList<>();
		allResources.add(Resource.SERVANT);
		allResources.add(Resource.STONE);
		allResources.add(Resource.SHIELD);
		allResources.add(Resource.GOLD);

		// You can place only one type of Resource in a single depot.
		return checkNoDistinctResource(newDeposit, 0, FIRST_SHELF_THRESHOLD) &&
				checkNoDistinctResource(newDeposit, FIRST_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD) &&
				checkNoDistinctResource(newDeposit, SECOND_SHELF_THRESHOLD, THIRD_SHELF_THRESHOLD) &&
				// You can’t place the same type of Resource in two different depots.
		allResources.stream().noneMatch(resType ->
				checkNoSameResourceTwoShelves(newDeposit, 0, FIRST_SHELF_THRESHOLD, FIRST_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD, resType) ||
						checkNoSameResourceTwoShelves(newDeposit, FIRST_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD,  THIRD_SHELF_THRESHOLD, resType) ||
						checkNoSameResourceTwoShelves(newDeposit, 0, FIRST_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD, THIRD_SHELF_THRESHOLD, resType));
	}

	/**
	 * Checks to see that a certain part of a Resource array doesn't have more than one type of resource.
	 * @param arr 								the array to check
	 * @param startIndex 						the first position to check
	 * @param endIndex							the last position to check
	 * @return 									true if there is only one type, false if not.
	 */
	private boolean checkNoDistinctResource(Resource[] arr, int startIndex, int endIndex) {
		return Arrays.stream(arr, startIndex, endIndex).filter(Objects::nonNull).distinct().count() <= 1;

	}

	/**
	 * Counts occurences of a certain resource in two different parts of the deposit and checks to see if their
	 * product is 0. If it is, then only one shelve contained the resource and true is returned. If not, false.
	 * @param arr the array of resources to check
	 * @param firstPartStartIndex 				first index of first shelve to check
	 * @param firstPartEndIndex   				second index of first shelve to check
	 * @param secondPartStartIndex 				first index of second shelve to check
	 * @param secondPartEndIndex 				end index of second shelve to check
	 * @param toCount 							resource to count
	 * @return 									true if resource appears only on one shelve
	 */
	private boolean checkNoSameResourceTwoShelves(Resource[] arr, int firstPartStartIndex, int firstPartEndIndex,
												int secondPartStartIndex, int secondPartEndIndex, Resource toCount) {
		return (Arrays.stream(arr, firstPartStartIndex, firstPartEndIndex).filter(res -> res == toCount).count() *
				Arrays.stream(arr, secondPartStartIndex, secondPartEndIndex).filter(res -> res == toCount).count()) != 0;
	}

	/**
	 * Returns all the resources contained inside the warehouse. (deposit + locker + extraDeposits if present)
	 * @return									a Resource Integer map containing all of the warehouse's resources.
	 */
	public HashMap<Resource, Integer> getAllResources() {
		HashMap<Resource, Integer> resourceCounter = new HashMap<>(locker);

		Arrays.stream(deposit)
				.filter(Objects::nonNull)
				.forEach(r -> resourceCounter.put(r, resourceCounter.get(r) + 1));

		Arrays.stream(extraDeposits) // for each extra deposit
				.filter(Objects::nonNull) // if not null => extraDeposit was activated

				.forEach(extraDepo ->
						Arrays.stream(extraDepo)
								.filter(Objects::nonNull)
								.forEach(r -> resourceCounter.put(r, resourceCounter.get(r)+1)));

		return resourceCounter;
	}
	public Resource[] getDeposit() {
		return deposit;
	}

	public Map<Resource, Integer> getLocker() {
		return locker;
	}

	public Resource[][] getExtraDeposits() {
		return extraDeposits;
	}
}



