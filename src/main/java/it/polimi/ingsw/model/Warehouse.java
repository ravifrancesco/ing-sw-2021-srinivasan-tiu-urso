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

	private Dashboard dashboard;

	public Warehouse() {
		locker = new HashMap<>();
		deposit = new HashMap<>();
		this.clearDeposit();

	}

	public void clearDeposit() {
		deposit.put(Resource.STONE, 0);
		deposit.put(Resource.SHIELD, 0);
		deposit.put(Resource.GOLD, 0);
		deposit.put(Resource.SERVANT, 0);

	}

	public void clearLocker() {
		locker.put(Resource.STONE, 0);
		locker.put(Resource.SHIELD, 0);
		locker.put(Resource.GOLD, 0);
		locker.put(Resource.SERVANT, 0);

	}

	public void storeInDeposit(ArrayList<Resource> resToAdd) {
		resToAdd.forEach(r -> deposit.put(r, deposit.get(r)+1));
	}

	public void storeInLocker(ArrayList<Resource> resToAdd) {
		resToAdd.forEach(r -> locker.put(r, locker.get(r) == null ? 1 : locker.get(r)+1));
	}

	public void removeFromDeposit(Map<Resource, Integer> cost) {
		cost.forEach((key, value) -> deposit.put(key, deposit.get(key) - value));
	}

	public void removeFromLocker(Map<Resource, Integer> cost) {
		cost.forEach((key, value) -> locker.put(key, locker.get(key) - value));
	}

	public boolean checkAddLegality(ArrayList<Resource> resToAdd) {
		HashMap<Resource, Integer> newDeposit = new HashMap<>();
		resToAdd.forEach(r -> newDeposit.put(r, newDeposit.get(r) == null ? 1 : newDeposit.get(r) + 1));
		deposit.forEach((k, v) -> newDeposit.merge(k, v, Integer::sum));

		return checkResUniqueness(newDeposit) &
				checkShelvesRule(newDeposit);
	}

	public boolean checkRemoveLegality(Map<Resource, Integer> cost) {
		HashMap<Resource, Integer> newDeposit = new HashMap<>();
		cost.forEach((k, v) -> newDeposit.put(k, deposit.get(k) - v));
		return newDeposit.values().stream().noneMatch(v -> v < 0);
	}

	private boolean checkResUniqueness(HashMap<Resource, Integer> newDeposit) {
		return  newDeposit.values().stream().filter(qty -> qty > 0).count() <= 3;
	}

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
