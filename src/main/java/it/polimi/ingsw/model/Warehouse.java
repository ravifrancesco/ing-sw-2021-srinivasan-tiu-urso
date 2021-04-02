package it.polimi.ingsw.model;
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
		HashMap<Resource, Integer> newDeposit = new HashMap<>();
		resToAdd.forEach(r -> newDeposit.put(r, newDeposit.get(r) == null ? 1 : newDeposit.get(r) + 1));
		deposit.forEach((k, v) -> newDeposit.merge(k, v, Integer::sum));

		return checkResUniqueness(newDeposit) &
				checkShelvesRule(newDeposit);
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
	 * Checks how many different resources are in the deposit: due to game rules, it can't be more than 3.
	 * @param depo the hypothetical deposit after storage
	 * @return true if game rules are respected, false otherwise.
	 */
	private boolean checkResUniqueness(HashMap<Resource, Integer> depo) {
		return  depo.values().stream().filter(qty -> qty > 0).count() <= 3;
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
		return newDeposit.values().stream().reduce(0, Integer::sum) <= 6 && // sum of resources must be less than 6
				newDeposit.values().stream().max(Integer::compare).get() <= 3 &&  // max amount of single resource must be 3 or below
				(newDeposit.values().stream().max(Integer::compare).get() == 3 ?
				newDeposit.values().stream().filter(a -> a < 3).filter(b -> b == 2).count() <= 1 &&
						newDeposit.values().stream().filter(a -> a == 3).count() == 1 :
						newDeposit.values().stream().anyMatch(a -> a < 2)) &&
				newDeposit.values().stream().filter(a -> a == 2).count() <= 2;
	}


}
