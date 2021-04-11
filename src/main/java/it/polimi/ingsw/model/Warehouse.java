package it.polimi.ingsw.model;
import java.util.*;
import java.util.stream.IntStream;

public class Warehouse {

	private Map<Resource, Integer> deposit;

	public Map<Resource, Integer> getDeposit() {
		return deposit;
	}

	private Map<Resource, Integer> locker;

	public Map<Resource, Integer> getLocker() {
		return locker;
	}

	private Dashboard dashboard; // not used yet?

	static final int MAX_EXTRA_DEPOSIT_SLOTS = 2;

	private Map<Resource, Integer> extraDeposit;

	private ArrayList<Resource> extraDepositResources;

	private boolean activatedExtraDeposit;

	/**
	 * The constructor for the class.
	 * Allocates space for the locker and the deposit (both are Resource-Integer HashMaps), and calls the clear
	 * (initialization) method.
	 */
	public Warehouse() {
		locker = new HashMap<>();
		deposit = new HashMap<>();
		this.clearDeposit();
		this.clearLocker();

	}

	/**
	 * Initializes the deposit, by setting each resource's stored quantity to 0.
	 */
	public void clearDeposit() {
		deposit.put(Resource.STONE, 0);
		deposit.put(Resource.SHIELD, 0);
		deposit.put(Resource.GOLD, 0);
		deposit.put(Resource.SERVANT, 0);

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
		activatedExtraDeposit = false;
		extraDeposit.clear();
		extraDepositResources.clear();
		extraDeposit.put(Resource.GOLD, 0);
		extraDeposit.put(Resource.STONE, 0);
		extraDeposit.put(Resource.SERVANT, 0);
		extraDeposit.put(Resource.SHIELD, 0);
	}

	/**
	 * Stores resources in the deposit, with no checks on the legality (there's a separate method for that).
	 * @param resToAdd: ArrayList with the resources to add. The ArrayList collection is chosen due to the Market's
	 * getResources() method, which returns an ArrayList of collected resources.
	 */
	public void storeInDeposit(ArrayList<Resource> resToAdd) {
		resToAdd.forEach(r -> deposit.put(r, deposit.get(r)+1));
	}

	/**
	 * Stores resources in the locker, with no checks due to the fact that the locker has no restrictions.
	 * @param resToAdd ArrayList with the resources to add.
	 */
	public void storeInLocker(ArrayList<Resource> resToAdd) {
		resToAdd.forEach(r -> locker.put(r, locker.get(r)+1));
	}

	/**
	 * Stores resources in the extra deposit for that resource. Checks on its legality are done with a separate method.
	 * @param resToAdd ArrayList with the resources to add.
	 */
	public void storeInExtraDeposit(ArrayList<Resource> resToAdd) { resToAdd.forEach(r -> extraDeposit.put(r, extraDeposit.get(r) + 1));}

	/**
	 * Removes the resources from the warehouse: the resources will at first be taken from the deposit,
	 * followed by the locker.
	 * @param cost HashMap Resource-Integer, where the integer is quantity to be removed. (the cost)
	 */
	public void removeFromWarehouse(Map<Resource, Integer> cost) {
		// cost is updated throughout; if deposit has enough then it will just be a map of zeros
		// 1. remove from deposit
		cost.forEach((key, value) -> { // for each resource to be removed
			if(deposit.get(key) - value >= 0) {
				// deposit has enough of the resource to be removed
				deposit.put(key, deposit.get(key) - value);
				cost.put(key, 0);
			} else {
				// deposit has less than the required amount of resource to removed:
				// set the deposit qty to 0 and take the remaining from the locker
				cost.put(key, value - deposit.get(key));
				deposit.put(key, 0);
			}
		});
		// 2. remove from extra deposit (leader card abilities) if possible
		if(hasExtraDeposit()) { attemptRemovalFromExtraDeposit(cost); }
		// 3. remove from locker
		removeFromLocker(cost);
	}

	/**
	 * Used for warehouse removal operations: checks to see if any of the resources to be removed
	 * are part of an extraDeposit. If so, they will be removed from the extra deposit and the cost HashMap
	 * will be updated with the remaining (if any) needed quantity.
	 * @param cost a HashMap Resource-Integer representing the resources to be removed from the warehouse.
	 */
	public void attemptRemovalFromExtraDeposit(Map<Resource, Integer> cost) {
		// checks too see what can be removed from the extra warehouses from the cost
		cost.forEach((key, value) -> {
			if (extraDeposit.get(key) - value >= 0) {
				cost.put(key, 0);
				extraDeposit.put(key, extraDeposit.get(key) - value);
			} else {
				cost.put(key, value - extraDeposit.get(key));
				extraDeposit.put(key, 0);
			}
		});
	}

	/**
	 * Removes resources from the LOCKER, with no checks on the legality (there's a separate method for that).
	 * @param cost: HashMap Resource-Integer, where the integer is quantity to be removed. (the cost)
	 */
	public void removeFromLocker(Map<Resource, Integer> cost) {
		cost.forEach((key, value) -> locker.put(key, locker.get(key) - value));
	}

	/**
	 * Verifies if the added resources respect the game rules.
	 * @param resToAdd ArrayList of resources trying to be added
	 * @return true if the move is legal, false otherwise.
	 */
	public boolean checkAddLegality(ArrayList<Resource> resToAdd) {
		/*
		 newDeposit is how the deposit would look like after the addition of resToAdd.
		 */
		HashMap<Resource, Integer> newDeposit = new HashMap<>(deposit);
		resToAdd.forEach(r -> newDeposit.put(r, newDeposit.get(r) == null ? 1 : newDeposit.get(r) + 1));

		return checkShelvesRule(newDeposit);
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
	private boolean checkShelvesRule(HashMap<Resource, Integer> newDeposit) {
		/*
		To check whether the shelves rule is respected, we take each quantity from the deposit, sort them in ascending
		 order and transform it into an array.
		After that, we iterate on the array and check that each element is below the threshold.
		The threshold starts at 0 and increases up to 3.
		The first element will always be 0 due to the fact that there can't be 4 different resources simultaneously.
		For example, if we have:
		- 4 stones
		- 2 shield
		- 1 gold
		The returned array will be [0, 1, 2, 4]
		The checks will then be:
		0 > 0
		1 > 1
		2 > 2
		4 > 3
		If any of those checks is false, then false is returned since the operation is illegal.
		In this case we return false due to 4 > 3, since we can't store 4 of the same resource.
		 */
		Integer[] arr = newDeposit.values().stream().sorted().toArray(Integer[]::new);
		return IntStream.range(0, 4).filter(i -> arr[i] <= i).count() == 4;
	}

	/**
	 * Check to see if an add operation on an extraDeposit is legal. The check rules are:
	 * 1. Resources are stored only if an extra deposit of that kind has been activated.
	 * 2. The stored quantity for each extra warehouse is not over the limit (MAX_EXTRA_DEPOSIT_SLOTS)
	 * @param resToAdd the resources to be added.
	 * @return true if the add is legal, false if it is not.
	 */
	public boolean checkExtraDepositAddLegality(ArrayList<Resource> resToAdd) {
		// copying current extra deposit
		HashMap<Resource, Integer> changedExtraD = new HashMap<>(extraDeposit);
		// adding the desired resources
		resToAdd.forEach(r -> changedExtraD.put(r, changedExtraD.get(r) + 1));

		return changedExtraD.values().stream().filter(v -> v > 0 && v <= MAX_EXTRA_DEPOSIT_SLOTS).count() == extraDepositResources.size()
				& changedExtraD.entrySet().stream().filter(v -> v.getValue() == 0 || extraDepositResources.contains(v.getKey())).count() == changedExtraD.size();
	}

	/**
	 * Checks if there are enough resources to withdraw from the warehouse.
	 * @param cost a HashMap Resource-Integer, where the integer is the quantity to be removed. (the cost)
	 * @return true if the warehouse has enough resources, false otherwise.
	 */
	public boolean checkRemoveLegality(Map<Resource, Integer> cost) {
		/*
		 The check is done through the creation of a HashMap 'remainingResources'
		 which at first is the union of resources inside the locker and the deposit.
		 After the merger, each resource cost is subtracted and the map is checked for negative
		 values: if there are none, it means that the warehouse had enough resources inside and the
		 operation was legal.
		 */
		HashMap<Resource, Integer> remainingResources = new HashMap<>(locker);
		deposit.forEach((k, v) -> remainingResources.merge(k, v, Integer::sum));
		if(hasExtraDeposit()) { extraDeposit.forEach((k, v) -> remainingResources.merge(k, v, Integer::sum)); }
		cost.forEach((k, v) -> remainingResources.merge(k, v, (a, b) -> a-b));

		return remainingResources.values().stream().noneMatch(v -> v < 0);
	}

	/**
	 * Activates an extra deposit, allowing to store MAX_EXTRA_DEPOSIT_SLOTS extra units of a single resource type.
	 * @param r the resource type of which to store MAX_EXTRA_DEPOSIT_SLOTS extra units.
	 */
	public void activateExtraDeposit(Resource r) {
		if(!hasExtraDeposit()) {
			extraDeposit = new HashMap<>();
			extraDepositResources = new ArrayList<>();
			activatedExtraDeposit = true;
		}
		extraDeposit.put(Resource.GOLD, 0);
		extraDeposit.put(Resource.STONE, 0);
		extraDeposit.put(Resource.SERVANT, 0);
		extraDeposit.put(Resource.SHIELD, 0);
		extraDepositResources.add(r);
	}

	public boolean hasExtraDeposit() {
		return activatedExtraDeposit;
	}

	// required for tests

	public Map<Resource, Integer> getExtraDeposit() {
		return extraDeposit;
	}

	public ArrayList<Resource> getExtraDepositResources() {
		return extraDepositResources;
	}

	public boolean isActivatedExtraDeposit() {
		return activatedExtraDeposit;
	}
}

