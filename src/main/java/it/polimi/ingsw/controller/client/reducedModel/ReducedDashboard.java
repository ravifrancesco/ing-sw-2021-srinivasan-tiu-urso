package it.polimi.ingsw.controller.client.reducedModel;

import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.server.lobby.LobbyType;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

public class ReducedDashboard {

    private ReducedPlayer reducedPlayer;

    private int playerPoints;

    private ProductionPower productionPower;

    // Cards
    static final int NUM_DEVELOPMENT_CARD_STACKS = 3;
    static final int NUM_LEADER_CARDS = 2;
    private List<LeaderCard> playedLeaderCards;
    private List<Stack<DevelopmentCard>> playedDevelopmentCards;

    // Warehouse
    static final int MAX_DEPOSIT_SLOTS = 6;
    private ArrayList<Resource> supply;
    private Resource[] deposit;
    private Map<Resource, Integer> locker;
    private Resource[][] extraDeposits;

    // FaithTrack
    private int position;
    private int LorenzoIlMagnificoPosition;
    private Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports; //TODO
    private int[] faithTrackVictoryPoints;

    private ReducedModel reducedModel;

    public ReducedDashboard(ReducedPlayer reducedPlayer, ReducedModel reducedModel) {
        this.reducedPlayer = reducedPlayer;
        this.reducedModel = reducedModel;
        this.playedLeaderCards = new ArrayList<>();
        this.playedDevelopmentCards = new ArrayList<>();
        this.supply = new ArrayList<>();
        this.deposit = new Resource[MAX_DEPOSIT_SLOTS];
        this.locker = new HashMap<>();
        this.extraDeposits = new Resource[2][];
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    public List<LeaderCard> getPlayedLeaderCards() {
        return playedLeaderCards;
    }

    public void setPlayedLeaderCards(List<LeaderCard> playedLeaderCards) {
        this.playedLeaderCards = playedLeaderCards;
    }

    public List<Stack<DevelopmentCard>> getPlayedDevelopmentCards() {
        return playedDevelopmentCards;
    }

    public void setPlayedDevelopmentCards(List<Stack<DevelopmentCard>> playedDevelopmentCards) {
        this.playedDevelopmentCards = playedDevelopmentCards;
    }

    public ArrayList<Resource> getSupply() {
        return supply;
    }

    public void setSupply(ArrayList<Resource> supply) {
        this.supply = supply;
        updateWarehouseView();
    }

    public Resource[] getDeposit() {
        return deposit;
    }

    public void setDeposit(Resource[] deposit) {
        this.deposit = deposit;
        updateWarehouseView();
    }

    public Map<Resource, Integer> getLocker() {
        return locker;
    }

    public void setLocker(Map<Resource, Integer> locker) {
        this.locker = locker;
        updateWarehouseView();
    }

    public Resource[][] getExtraDeposits() {
        return extraDeposits;
    }

    public void setExtraDeposits(Resource[][] extraDeposits) {
        this.extraDeposits = extraDeposits;
        updateWarehouseView();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        this.reducedModel.moveFaithMarker(position);
    }

    public Map<Pair<Integer, Integer>, Pair<Integer, Integer>> getVaticanReports() {
        return vaticanReports;
    }

    public void setVaticanReports(Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports) {
        this.vaticanReports = vaticanReports;
    }

    public int[] getFaithTrackVictoryPoints() {
        return faithTrackVictoryPoints;
    }

    public void setFaithTrackVictoryPoints(int[] faithTrackVictoryPoints) {
        this.faithTrackVictoryPoints = faithTrackVictoryPoints;
    }

    public void setLorenzoIlMagnificoPosition(int LorenzoIlMagnificoPosition) {
        this.LorenzoIlMagnificoPosition = LorenzoIlMagnificoPosition;
    }

    public void setProductionPower(ProductionPower productionPower) {
        this.productionPower = productionPower;
    }

    public ProductionPower getProductionPower() {
        return productionPower;
    }

    private void updateWarehouseView() {
        this.reducedModel.updateWarehouse(reducedPlayer.getNickname(), deposit, locker, extraDeposits, supply);
    }
}
