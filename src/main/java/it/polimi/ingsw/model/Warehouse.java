package it.polimi.ingsw.model;
import it.polimi.ingsw.common.Pair;

import java.util.*;
import java.util.stream.IntStream;

public class Warehouse {

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
		IntStream.range(0, MAX_DEPOSIT_SLOTS).forEach(i -> deposit[i] = Resource.EMPTY);
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
	 * Clears everything related to extra deposits. (for game reset purposes)
	 */
	public void clearExtraDeposits() {
		extraDeposits[0] = new Resource[MAX_EXTRA_DEPOSIT_SLOTS];
		extraDeposits[1] = new Resource[MAX_EXTRA_DEPOSIT_SLOTS];
	}

	/**
	 * Inits a map with all possible resources. (helper method)
	 * @param resCtr the map to be init'd.
	 */
	private void resCtrInit(HashMap<Resource, Integer> resCtr) {
		resCtr.put(Resource.STONE, 0);
		resCtr.put(Resource.GOLD, 0);
		resCtr.put(Resource.SHIELD, 0);
		resCtr.put(Resource.SERVANT, 0);
	}

	/**
	 * Stores a resource from the dashboard's supply inside the deposit.
	 * @param resToAdd 					resource to store
	 * @param pos						deposit position in which the resource is stored
	 * @throws IllegalStateException	if resource is stored in an illegal position
	 */
	public void storeInDeposit(Resource resToAdd, int pos) throws IllegalStateException {
		Resource[] newDeposit = new Resource[MAX_DEPOSIT_SLOTS];
		IntStream.range(0, MAX_DEPOSIT_SLOTS).forEach(i -> newDeposit[i] = deposit[i]);
		// adding new resource
		newDeposit[pos] = resToAdd;
		// checking deposit legality
		if(!checkShelvesRule(newDeposit)) {
			throw new IllegalStateException("Deposit positioning is illegal");
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
		deposit[pos] = Resource.EMPTY;
	}

	/**
	 * Moves two deposit positions.
	 * @param move 									a Pair datastructure, containing the two indexes to move.
	 */
	public void doDepositMove(Pair<Integer, Integer> move) {
		Resource temp = deposit[move.first];
		deposit[move.first] = deposit[move.second];
		deposit[move.second] = temp;
	}

	/**
	 * Returns the amount of stored resources inside the deposit.
	 *
	 * @return										the amount of resources inside the deposit
	 */
	public int getDepositResourceQty() {
		return (int) Arrays.stream(deposit).filter(r -> r != Resource.EMPTY).count();
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
	 * @param extraDepositLeaderCardPos				position of the extra deposit (leader card)
	 */
	public void activateExtraDeposit(int extraDepositLeaderCardPos) {
		extraDeposits[extraDepositLeaderCardPos] = new Resource[MAX_EXTRA_DEPOSIT_SLOTS];
		IntStream.range(0, MAX_EXTRA_DEPOSIT_SLOTS).forEach(i -> extraDeposits[extraDepositLeaderCardPos][i] = Resource.EMPTY);
	}


	/**
	 * Stores a resource inside an extra deposit.
	 *
	 * @param extraDepositLeaderCardPos				position of the extra deposit (leader card)
	 * @param r										resource to be stored
	 * @param pos									position where to store the resource
	 */
	public void storeInExtraDeposit(int extraDepositLeaderCardPos, Resource r, int pos) {
		extraDeposits[extraDepositLeaderCardPos][pos] = r;
	}

	/**
	 * Swaps two positions of an extra deposit.
	 *
	 * @param extraDepositLeaderCardPos				position of the extra deposit (leader card)
	 * @param from									first position to swap
	 * @param to									second position to swap
	 */
	public void swapExtraDeposit(int extraDepositLeaderCardPos, int from, int to) {
		Resource fromResource = extraDeposits[extraDepositLeaderCardPos][from];
		extraDeposits[extraDepositLeaderCardPos][from] = extraDeposits[extraDepositLeaderCardPos][to];
		extraDeposits[extraDepositLeaderCardPos][to] = fromResource;
	}

	/**
	 * Removes the resource from an extra deposit slot.
	 * @param extraDepositLeaderCardPos				position of the extra deposit (leader card)
	 * @param pos									position of the resource to remove
	 */
	public void removeFromExtraDeposit(int extraDepositLeaderCardPos, int pos) {
		extraDeposits[extraDepositLeaderCardPos][pos] = null;
	}

	/**
	 * Removes resources from the LOCKER, with no checks on the legality (there's a separate method for that).
	 * @param cost: HashMap Resource-Integer, where the integer is quantity to be removed. (the cost)
	 */
	public void removeFromLocker(Map<Resource, Integer> cost) {
		cost.forEach((key, value) -> locker.put(key, locker.get(key) - value));
	}

	/**
	 * Checks the 'shelves rules' for the deposit:
	 * <ul>
	 * <li> The maximum amount of stored resources is 6.
	 * <li> Only 3 different types of resources can be stored.
	 * <li> The maximum amount of each different kind of resource must be, in order, 3 2 and 1. (see game rules)
	 * @param newDeposit the hypothetical deposit after storage
	 * @return true if game rules are respected, false otherwise.
	 */
	public boolean checkShelvesRule(Resource[] newDeposit) {
		ArrayList<Resource> allResources = new ArrayList<>();
		allResources.add(Resource.SERVANT);
		allResources.add(Resource.STONE);
		allResources.add(Resource.SHIELD);
		allResources.add(Resource.GOLD);




		// You can place only one type of Resource in a single depot.
		return Arrays.stream(newDeposit, 0, 1).filter(res -> res != Resource.EMPTY).distinct().count() <= 1 &&
				Arrays.stream(newDeposit, 1, 3).filter(res -> res != Resource.EMPTY).distinct().count() <= 1 &&
				Arrays.stream(newDeposit, 3, 6).filter(res -> res != Resource.EMPTY).distinct().count() <= 1 &&
				// You can’t place the same type of Resource in two different depots.
		allResources.stream().noneMatch(resType ->
				((Arrays.stream(newDeposit, 0, 1).filter(res -> res == resType).count() *
								Arrays.stream(newDeposit, 1, 3).filter(res -> res == resType).count()) != 0)
										||
						((Arrays.stream(newDeposit, 1, 3).filter(res -> res == resType).count() *
												Arrays.stream(newDeposit, 3, 6).filter(res -> res == resType).count()) != 0)
						|| ((Arrays.stream(newDeposit, 0, 1).filter(res -> res == resType).count() *
										Arrays.stream(newDeposit, 3, 6).filter(res -> res == resType).count()) != 0));

	}

	/**
	 * Returns all the resources contained inside the warehouse. (deposit + locker + extraDeposits if present)
	 * @return										a Resource Integer map containing all of the warehouse's resources.
	 */
	public HashMap<Resource, Integer> getAllResources() {
		HashMap<Resource, Integer> resourceCounter = new HashMap<>(locker);

		Arrays.stream(deposit)
				.filter(Objects::nonNull)
				.forEach(r -> resourceCounter.put(r, resourceCounter.get(r)+1));

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

	public void printDepositActually() {
		IntStream.range(0, 1).forEach(i -> System.out.print(deposit[i] + " "));
		System.out.println();
		IntStream.range(1, 3).forEach(i -> System.out.print(deposit[i] + " "));
		System.out.println();
		IntStream.range(3, 6).forEach(i -> System.out.print(deposit[i] + " "));
	}
}


