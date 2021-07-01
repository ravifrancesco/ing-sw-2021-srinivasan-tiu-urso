package it.polimi.ingsw.model.full.table;

import it.polimi.ingsw.model.observerPattern.observables.WarehouseObservable;

import java.util.*;
import java.util.stream.IntStream;

/**
 * The class represents a warehouse. Its state includes:
 * The locker.
 * The deposit.
 * The extra deposits.
 * The dashboard.
 * The class is observable and notifies the observers on a change of the state.
 */
public class Warehouse extends WarehouseObservable {

    public static final int FIRST_SHELF_THRESHOLD = 1;
    public static final int SECOND_SHELF_THRESHOLD = 3;
    public static final int THIRD_SHELF_THRESHOLD = 6;
    public static final int MAX_EXTRA_DEPOSIT_SPACE = 2;
    public static final int MAX_EXTRA_DEPOSIT_SLOTS = 2;
    public static final int MAX_DEPOSIT_SLOTS = 6;
    private final Map<Resource, Integer> locker;
    private final Dashboard dashboard;
    private Resource[] deposit;
    private Resource[][] extraDeposits;

    /**
     * The constructor for the class.
     * Allocates space for the locker and the deposit (both are Resource-Integer HashMaps), and calls the clear
     * (initialization) method.
     * @param dashboard the dashboard which warehouse belongs to.
     */
    public Warehouse(Dashboard dashboard) {
        locker = new HashMap<>();
        deposit = new Resource[MAX_DEPOSIT_SLOTS];
        extraDeposits = new Resource[MAX_EXTRA_DEPOSIT_SLOTS][];
        this.dashboard = dashboard;
        reset();
    }

    /**
     * Reset method for the class. It clears the deposit, the locker and creates the extra deposits.
     */
    public void reset() {
        this.clearDeposit();
        this.clearLocker();
        extraDeposits = new Resource[MAX_EXTRA_DEPOSIT_SLOTS][];
        notify(this);
    }

    /**
     * Initializes the deposit, by setting each resource's stored quantity to 0.
     */
    private void clearDeposit() {
        deposit = new Resource[MAX_DEPOSIT_SLOTS];
    }

    /**
     * Initializes the locker, by setting each resource's stored quantity to 0.
     */
    private void clearLocker() {
        locker.put(Resource.STONE, 0);
        locker.put(Resource.SHIELD, 0);
        locker.put(Resource.GOLD, 0);
        locker.put(Resource.SERVANT, 0);
    }

    /**
     * Stores a resource from the dashboard's supply inside the deposit.
     *
     * @param resToAdd resource to store
     * @param pos      deposit position in which the resource is stored
     * @throws IllegalStateException    if resource is stored in an illegal position
     * @throws IllegalArgumentException if the given position is already full
     */
    public void storeInDeposit(Resource resToAdd, int pos) throws IllegalStateException, IllegalArgumentException {
        Resource[] newDeposit = new Resource[MAX_DEPOSIT_SLOTS];
        IntStream.range(0, MAX_DEPOSIT_SLOTS).forEach(i -> newDeposit[i] = deposit[i]);
        // adding new resource
        newDeposit[pos] = resToAdd;
        // checking deposit legality
        if (!checkShelvesRule(newDeposit)) {
            notify(this);
            throw new IllegalStateException("Deposit positioning is illegal");
        }
        if (deposit[pos] != null) {
            notify(this);
            throw new IllegalArgumentException();
        }
        // deposit is legal => adding resource to real deposit
        deposit[pos] = resToAdd;
        notify(this);
    }

    /**
     * Clears a deposit position
     *
     * @param pos position to clear
     */
    public void removeFromDeposit(int pos) {
        deposit[pos] = null;
        notify(this);
    }

    /**
     * Moves two deposit positions.
     *
     * @param from move from.
     * @param to   move to.
     */
    public void doDepositMove(int from, int to) {
        Resource temp = deposit[from];
        deposit[from] = deposit[to];
        deposit[to] = temp;
        notify(this);
    }

    /**
     * Returns the amount of stored resources inside the deposit.
     *
     * @return the amount of resources inside the deposit
     */
    public int getDepositResourceQty() {
        return (int) Arrays.stream(deposit).filter(Objects::nonNull).count();
    }

    /**
     * Stores resources in the locker, with no checks due to the fact that the locker has no restrictions.
     *
     * @param resToAdd ArrayList with the resources to add.
     * @param qty the amount of resources to add.
     */
    public void storeInLocker(Resource resToAdd, int qty) {
        locker.put(resToAdd, locker.get(resToAdd) + qty);
        notify(this);
    }

    /**
     * Removes resources from the locker
     *
     * @param resToRemove resource to remove from
     * @param qty         the amount of resource quantity to remove
     */
    public void removeFromLocker(Resource resToRemove, int qty) {
        locker.put(resToRemove, locker.get(resToRemove) - qty);
        notify(this);
    }

    /**
     * Activates an extra deposit, allowing to store MAX_EXTRA_DEPOSIT_SLOTS extra units of a single resource type.
     *
     * @param extraDepositLeaderCardPos position of the extra deposit (leader card)
     */
    public void activateExtraDeposit(int extraDepositLeaderCardPos) {
        extraDeposits[extraDepositLeaderCardPos] = new Resource[MAX_EXTRA_DEPOSIT_SLOTS];
        IntStream.range(0, MAX_EXTRA_DEPOSIT_SLOTS).forEach(i -> extraDeposits[extraDepositLeaderCardPos][i] = null);
    }


    /**
     * Stores a resource inside an extra deposit.
     *
     * @param extraDepositLeaderCardPos position of the extra deposit (leader card)
     * @param r                         resource to be stored
     * @param pos                       position where to store the resource
     * @throws IllegalArgumentException if the given position is already full
     */
    public void storeInExtraDeposit(int extraDepositLeaderCardPos, Resource r, int pos) throws IllegalArgumentException {
        if (extraDeposits[extraDepositLeaderCardPos][pos] != null) {
            notify(this);
            throw new IllegalArgumentException();
        }
        extraDeposits[extraDepositLeaderCardPos][pos] = r;
        notify(this);
    }

    /**
     * Swaps two positions of an extra deposit.
     *
     * @param extraDepositLeaderCardPos position of the extra deposit (leader card)
     * @param from                      first position to swap
     * @param to                        second position to swap
     */
    public void swapExtraDeposit(int extraDepositLeaderCardPos, int from, int to) {
        Resource fromResource = extraDeposits[extraDepositLeaderCardPos][from];
        extraDeposits[extraDepositLeaderCardPos][from] = extraDeposits[extraDepositLeaderCardPos][to];
        extraDeposits[extraDepositLeaderCardPos][to] = fromResource;
        notify(this);
    }

    /**
     * Removes the resource from an extra deposit slot.
     *
     * @param extraDepositLeaderCardPos position of the extra deposit (leader card)
     * @param pos                       position of the resource to remove
     */
    public void removeFromExtraDeposit(int extraDepositLeaderCardPos, int pos) {
        extraDeposits[extraDepositLeaderCardPos][pos] = null;
        notify(this);
    }

    /**
     * Swaps resources from/to deposit to/from a extraDeposit
     *
     * @param from    move from.
     * @param to      move to.
     * @param lcPos   is 0 if move.first is the extraDeposit index,
     *                1 if move.second is the extraDeposit index
     * @param lcIndex the leader card index
     */
    public void doExtraDepositMove(int from, int to, int lcPos, int lcIndex) {
        Resource temp;
        if (lcPos == 0) { // move.first is the extra deposit index
            temp = extraDeposits[lcIndex][from];
            extraDeposits[lcIndex][from] = deposit[to];
            deposit[to] = temp;
        } else {
            temp = extraDeposits[lcIndex][to];
            extraDeposits[lcIndex][to] = deposit[from];
            deposit[from] = temp;
        }
        notify(this);
    }

    /**
     * Checks the 'shelves rules' for the deposit
     * <ul>
     * <li> The maximum amount of stored resources is 6.
     * <li> Only 3 different types of resources can be stored.
     * <li> The maximum amount of each different kind of resource must be, in order, 3 2 and 1. (see game rules)
     *
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
        return checkNoDistinctResource(newDeposit, 0, FIRST_SHELF_THRESHOLD) &&
                checkNoDistinctResource(newDeposit, FIRST_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD) &&
                checkNoDistinctResource(newDeposit, SECOND_SHELF_THRESHOLD, THIRD_SHELF_THRESHOLD) &&
                // You can’t place the same type of Resource in two different depots.
                allResources.stream().noneMatch(resType ->
                        checkNoSameResourceTwoShelves(newDeposit, 0, FIRST_SHELF_THRESHOLD, FIRST_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD, resType) ||
                                checkNoSameResourceTwoShelves(newDeposit, FIRST_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD, THIRD_SHELF_THRESHOLD, resType) ||
                                checkNoSameResourceTwoShelves(newDeposit, 0, FIRST_SHELF_THRESHOLD, SECOND_SHELF_THRESHOLD, THIRD_SHELF_THRESHOLD, resType));
    }

    /**
     * Checks to see that a certain part of a Resource array doesn't have more than one type of resource.
     *
     * @param arr        the array to check
     * @param startIndex the first position to check
     * @param endIndex   the last position to check
     * @return true if there is only one type, false if not.
     */
    private boolean checkNoDistinctResource(Resource[] arr, int startIndex, int endIndex) {
        return Arrays.stream(arr, startIndex, endIndex).filter(Objects::nonNull).distinct().count() <= 1;

    }

    /**
     * Counts occurences of a certain resource in two different parts of the deposit and checks to see if their
     * product is 0. If it is, then only one shelve contained the resource and true is returned. If not, false.
     *
     * @param arr                  the array of resources to check
     * @param firstPartStartIndex  first index of first shelve to check
     * @param firstPartEndIndex    second index of first shelve to check
     * @param secondPartStartIndex first index of second shelve to check
     * @param secondPartEndIndex   end index of second shelve to check
     * @param toCount              resource to count
     * @return true if resource appears only on one shelve
     */
    private boolean checkNoSameResourceTwoShelves(Resource[] arr, int firstPartStartIndex, int firstPartEndIndex,
                                                  int secondPartStartIndex, int secondPartEndIndex, Resource toCount) {
        return (Arrays.stream(arr, firstPartStartIndex, firstPartEndIndex).filter(res -> res == toCount).count() *
                Arrays.stream(arr, secondPartStartIndex, secondPartEndIndex).filter(res -> res == toCount).count()) != 0;
    }

    /**
     * Returns all the resources contained inside the warehouse. (deposit + locker + extraDeposits if present)
     *
     * @return a Resource Integer map containing all of the warehouse's resources.
     */
    public HashMap<Resource, Integer> getAllResources() {
        return sumAllResources(deposit, locker, extraDeposits[0], extraDeposits[1]);
    }

    /**
     * Sums all the resources contained in a deposit, locker and two extra deposits.
     *
     * @param pDeposit         a deposit with resources to sum
     * @param pLocker          a locker with resources to sum
     * @param pExtraDepositOne the first extra deposit with resources to sum
     * @param pExtraDepositTwo the second extra deposit with resources to sum
     * @return a map containing all the added up resources.
     */
    public HashMap<Resource, Integer> sumAllResources(Resource[] pDeposit, Map<Resource, Integer> pLocker, Resource[] pExtraDepositOne, Resource[] pExtraDepositTwo) {
        HashMap<Resource, Integer> resourceCounter = new HashMap<>(pLocker);

        Arrays.stream(pDeposit).filter(Objects::nonNull).forEach(r -> resourceCounter.put(r, resourceCounter.get(r) + 1));

        if (pExtraDepositOne != null) {
            Arrays.stream(pExtraDepositOne).filter(Objects::nonNull).forEach(r -> resourceCounter.put(r, resourceCounter.get(r) + 1));
        }

        if (pExtraDepositTwo != null) {
            Arrays.stream(pExtraDepositTwo).filter(Objects::nonNull).forEach(r -> resourceCounter.put(r, resourceCounter.get(r) + 1));
        }

        return resourceCounter;
    }


    /**
     * Replaces current deposit with a new, updated one.
     *
     * @param newDeposit the new deposit.
     */
    public void changeDeposit(Resource[] newDeposit) {
        IntStream.range(0, MAX_DEPOSIT_SLOTS).forEach(i -> deposit[i] = newDeposit[i]);
        notify(this);
    }

    /**
     * Replaces a current extraDeposit with a new one
     *
     * @param newExtraDeposit the new extra deposit
     * @param lcIndex         the index of the leader card where the extra deposit is
     */
    public void changeExtraDeposit(Resource[] newExtraDeposit, int lcIndex) {
        IntStream.range(0, MAX_EXTRA_DEPOSIT_SPACE).forEach(i -> extraDeposits[lcIndex][i] = newExtraDeposit[i]);
        notify(this);
    }

    /**
     * Getter for the deposit.
     * @return the deposit.
     */
    public Resource[] getDeposit() {
        return deposit;
    }

    /**
     * Getter for the locker.
     * @return the locker.
     */
    public Map<Resource, Integer> getLocker() {
        return locker;
    }

    /**
     * Getter for the extra deposits.
     * @return the extra deposits.
     */
    public Resource[][] getExtraDeposits() {
        return extraDeposits;
    }

    /**
     * Getter for the dashboard.
     * @return the dashboard.
     */
    public Dashboard getDashboard() {
        return dashboard;
    }
}



