package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ResourceContainer {

    private ArrayList<Integer> selectedDepositIndexes;
    private Map<Resource, Integer> selectedLockerResources;
    private Map<Integer, ArrayList<Integer>> selectedExtraDepositIndexes;

    public ResourceContainer() {
        selectedDepositIndexes = new ArrayList<>();
        selectedLockerResources = new HashMap<>();
        selectedExtraDepositIndexes = new HashMap<>();
        selectedLockerPosInit();
        selectedExtraDepositInit();
    }

    public void selectedLockerPosInit() {
        selectedLockerResources.put(Resource.GOLD, 0);
        selectedLockerResources.put(Resource.STONE, 0);
        selectedLockerResources.put(Resource.SERVANT, 0);
        selectedLockerResources.put(Resource.SHIELD, 0);
    }

    public void selectedExtraDepositInit() {
        IntStream.range(0, Warehouse.MAX_EXTRA_DEPOSIT_SLOTS).forEach(i ->
                selectedExtraDepositIndexes.put(i, new ArrayList<>()));
    }

    // TODO during view coding: add checks to never select an empty slot
    public void addDepositSelectedResource(int pos, Warehouse wh) {
        if (wh.getDeposit()[pos] != null) {
            selectedDepositIndexes.add(pos);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void addExtraDepositSelectedResource(int leaderCardPos, int pos, Warehouse wh) {
        if (wh.getExtraDeposits()[leaderCardPos] != null && wh.getExtraDeposits()[leaderCardPos][pos] != null) {
            selectedExtraDepositIndexes.get(leaderCardPos).add(pos);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void addLockerSelectedResource(Resource r, int qty, Warehouse wh) {
        if (wh.getLocker().get(r) >= qty) {
            selectedLockerResources.put(r, qty);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ArrayList<Integer> getSelectedDepositIndexes() {
        return selectedDepositIndexes;
    }

    public Map<Resource, Integer> getSelectedLockerResources() {
        return selectedLockerResources;
    }

    public Map<Integer, ArrayList<Integer>> getSelectedExtraDepositIndexes() {
        return selectedExtraDepositIndexes;
    }

    public Map<Resource, Integer> getAllResources(Warehouse wh) {
        HashMap<Resource, Integer> res = new HashMap<>(selectedLockerResources);

        selectedDepositIndexes.stream().filter(i -> wh.getDeposit()[i] != null).forEach(i ->
                res.put(wh.getDeposit()[i], res.get(wh.getDeposit()[i]) + 1));

        IntStream.range(0, Warehouse.MAX_EXTRA_DEPOSIT_SLOTS)
                .forEach(i -> selectedExtraDepositIndexes.get(i)
                        .forEach(j -> res.put(wh.getExtraDeposits()[i][j],
                            res.get(wh.getExtraDeposits()[i][j]) + 1)));
        return res;
    }

}
