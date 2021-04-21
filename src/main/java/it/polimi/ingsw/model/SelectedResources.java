package it.polimi.ingsw.model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SelectedResources {

    private ArrayList<Integer> selectedDepositPositions;
    private Map<Resource, Integer> selectedLockerPositions;
    private ArrayList[] selectedExtraDepositsPosition;

    public SelectedResources() {
        selectedDepositPositions = new ArrayList<>();
        selectedExtraDepositsPosition = new ArrayList[2];
        selectedExtraDepositsPosition[0] = new ArrayList<Integer>();
        selectedExtraDepositsPosition[1] = new ArrayList<Integer>();
        selectedLockerPositions = new HashMap<>();
        selectedLockerPosInit();
    }

    public void selectedLockerPosInit() {
        selectedLockerPositions.put(Resource.GOLD, 0);
        selectedLockerPositions.put(Resource.STONE, 0);
        selectedLockerPositions.put(Resource.SERVANT, 0);
        selectedLockerPositions.put(Resource.SHIELD, 0);
    }

    public void addDepositSelectedResource(int pos) {
        selectedDepositPositions.add(pos);
    }

    public void addExtraDepositSelectedResource(int leaderCardPos, int pos) {
        selectedExtraDepositsPosition[leaderCardPos].add(pos);
    }

    public void addLockerSelectedResource(Resource r, int qty) {
        selectedLockerPositions.put(r, qty);
    }

    public ArrayList<Integer> getSelectedDepositPositions() {
        return selectedDepositPositions;
    }

    public Map<Resource, Integer> getSelectedLockerPositions() {
        return selectedLockerPositions;
    }

    public ArrayList<Integer>[] getSelectedExtraDepositsPosition() {
        return selectedExtraDepositsPosition;
    }
}
