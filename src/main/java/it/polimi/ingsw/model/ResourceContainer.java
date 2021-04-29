package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ResourceContainer {

    private ArrayList<Integer> containedDepositResources;
    private Map<Resource, Integer> containedLockerResources;
    private Map<Integer, ArrayList<Integer>> containedExtraDepositResources;

    public ResourceContainer() {
        containedDepositResources = new ArrayList<>();
        containedLockerResources = new HashMap<>();
        containedExtraDepositResources = new HashMap<>();
        selectedLockerPosInit();
        selectedExtraDepositInit();
    }

    public void selectedLockerPosInit() {
        containedLockerResources.put(Resource.GOLD, 0);
        containedLockerResources.put(Resource.STONE, 0);
        containedLockerResources.put(Resource.SERVANT, 0);
        containedLockerResources.put(Resource.SHIELD, 0);
    }

    public void selectedExtraDepositInit() {
        IntStream.range(0, Warehouse.MAX_EXTRA_DEPOSIT_SLOTS).forEach(i ->
                containedExtraDepositResources.put(i, new ArrayList<>()));
    }

    // TODO during view coding: add checks to never select an empty slot
    public void addDepositSelectedResource(int pos, Warehouse wh) {
        containedDepositResources.add(pos);
    }

    public void addExtraDepositSelectedResource(int leaderCardPos, int pos, Warehouse wh) {
        containedExtraDepositResources.get(leaderCardPos).add(pos);
    }

    public void addLockerSelectedResource(Resource r, int qty, Warehouse wh) {
        containedLockerResources.put(r, qty);
    }

    public ArrayList<Integer> getContainedDepositResources() {
        return containedDepositResources;
    }

    public Map<Resource, Integer> getContainedLockerResources() {
        return containedLockerResources;
    }

    public Map<Integer, ArrayList<Integer>> getContainedExtraDepositResources() {
        return containedExtraDepositResources;
    }

    public Map<Resource, Integer> getAllResources(Warehouse wh) {
        HashMap<Resource, Integer> res = new HashMap<>(containedLockerResources);

        containedDepositResources.forEach(i ->
                res.put(wh.getDeposit()[i], res.get(wh.getDeposit()[i]) + 1));

        IntStream.range(0, Warehouse.MAX_EXTRA_DEPOSIT_SLOTS)
                .forEach(i -> containedExtraDepositResources.get(i)
                        .forEach(j -> res.put(wh.getExtraDeposits()[i][j],
                            res.get(wh.getExtraDeposits()[i][j]) + 1)));
        return res;
    }

}
