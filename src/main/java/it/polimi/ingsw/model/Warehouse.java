package it.polimi.ingsw.model;
import javax.lang.model.type.ArrayType;
import java.util.*;

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

	/**
	 * The constructor for the class.
	 * ALlocates space for the locker and the deposit (both are Resource-Integer HashMaps), and calls the clear
	 * (initialization) method.
	 */
	public Warehouse() {
		locker = new HashMap<>();
		deposit = new HashMap<>();
		this.clearDeposit();

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
		resToAdd.forEach(r -> locker.put(r, locker.get(r) == null ? 1 : locker.get(r)+1));
	}

	/**
	 * Removes resources from the deposit, with no checks on the legality (there's a separate method for that).
	 * @param cost: HashMap Resource-Integer, where the integer is quantity to be removed. (the cost)
	 */
	public void removeFromDeposit(Map<Resource, Integer> cost) {
		cost.forEach((key, value) -> deposit.put(key, deposit.get(key) - value));
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
		HashMap<Resource, Integer> newDeposit = new HashMap<>();
		resToAdd.forEach(r -> newDeposit.put(r, newDeposit.get(r) == null ? 1 : newDeposit.get(r) + 1));
		deposit.forEach((k, v) -> newDeposit.merge(k, v, Integer::sum));

		return checkShelvesRule(newDeposit);
	}

	/**
	 * Checks if there are enough resources to be removed from the deposit.
	 * @param cost HashMap Resource-Integer, where the integer is quantity to be removed. (the cost)
	 * @return true if there are enough resources to remove, false otherwise.
	 */
	public boolean checkDepositRemoveLegality(Map<Resource, Integer> cost) {
		HashMap<Resource, Integer> newDeposit = new HashMap<>();
		cost.forEach((k, v) -> newDeposit.put(k, deposit.get(k) - v));
		return newDeposit.values().stream().noneMatch(v -> v < 0);
	}

	/**
	 * Checks if there are enough resources to be removed from the locker.
	 * @param cost HashMap Resource-Integer, where the integer is quantity to be removed. (the cost)
	 * @return true if there are enough resources to remove, false otherwise.
	 */
	public boolean checkLockerRemoveLegality(Map<Resource, Integer> cost) {
		HashMap<Resource, Integer> newDeposit = new HashMap<>();
		cost.forEach((k, v) -> newDeposit.put(k, locker.get(k) - v));
		return newDeposit.values().stream().noneMatch(v -> v < 0);
	}

	/**
	 * Checks the 'shelves rules' for the deposit:
	 * <ul>
	 * <li> The maximum amount of stored resources is 6.
	 * <li> First type of resource's stored quantity must be up to 3.
	 * <li> Second type of resource's stored quantity must be up to 2.
	 * <li> Third type of resource's stored quantity must be up to 1.
	 * @param newDeposit the hypothetical deposit after storage
	 * @return true if game rules are respected, false otherwise.
	 */
	private boolean checkShelvesRule(HashMap<Resource, Integer> newDeposit) {
		/*
		To check whether the shelves rule is respected, we take each quantity from the deposit, sort them and
		transform it to an array.
		After that, we iterate on the array and check that each element is below the threshold.
		The threshold starts at 0 for the smallest shelves and goes up to 3.
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
		System.out.println(Arrays.toString(arr));
		int limit = 0;
		for (int qty : arr) {
			if (qty > limit) {
				return false;
			}
			limit++;
		}
		return true;
		// Tried to transform this code from imperative to functional with no success.
	}

}

