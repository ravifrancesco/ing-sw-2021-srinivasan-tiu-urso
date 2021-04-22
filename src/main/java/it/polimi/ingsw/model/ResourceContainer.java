package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResourceContainer {

    private ArrayList<Integer> containedDepositResources;
    private Map<Resource, Integer> containedLockerResources;
    private ArrayList[] containedExtraDepositResources;

    public ResourceContainer() {
        containedDepositResources = new ArrayList<>();
        containedExtraDepositResources = new ArrayList[2];
        containedExtraDepositResources[0] = new ArrayList<Integer>();
        containedExtraDepositResources[1] = new ArrayList<Integer>();
        containedLockerResources = new HashMap<>();
        selectedLockerPosInit();
    }

    public void selectedLockerPosInit() {
        containedLockerResources.put(Resource.GOLD, 0);
        containedLockerResources.put(Resource.STONE, 0);
        containedLockerResources.put(Resource.SERVANT, 0);
        containedLockerResources.put(Resource.SHIELD, 0);
    }

    public void addDepositSelectedResource(int pos) {
        containedDepositResources.add(pos);
    }

    public void addExtraDepositSelectedResource(int leaderCardPos, int pos) {
        containedExtraDepositResources[leaderCardPos].add(pos);
    }

    public void addLockerSelectedResource(Resource r, int qty) {
        containedLockerResources.put(r, qty);
    }

    public ArrayList<Integer> getContainedDepositResources() {
        return containedDepositResources;
    }

    public Map<Resource, Integer> getContainedLockerResources() {
        return containedLockerResources;
    }

    public ArrayList<Integer>[] getContainedExtraDepositResources() {
        return containedExtraDepositResources;
    }
}
