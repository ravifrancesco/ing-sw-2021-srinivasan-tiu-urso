package it.polimi.ingsw.model.reduced;

import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

public class ReducedDashboard {

    // Cards
    public static final int NUM_DEVELOPMENT_CARD_STACKS = 3;
    public static final int NUM_LEADER_CARDS = 2;
    // Warehouse
    static final int MAX_DEPOSIT_SLOTS = 6;
    private final ReducedPlayer reducedPlayer;
    private final ReducedModel reducedModel;
    private int playerPoints;
    private ProductionPower productionPower;
    private List<LeaderCard> playedLeaderCards;
    private List<Stack<DevelopmentCard>> playedDevelopmentCards;
    private ArrayList<Resource> supply;
    private Resource[] deposit;
    private Map<Resource, Integer> locker;
    private Resource[][] extraDeposits;
    // FaithTrack
    private int position;
    private int LorenzoIlMagnificoPosition;
    private Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports;

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
        updatePlayerPoints();
    }

    public List<LeaderCard> getPlayedLeaderCards() {
        return playedLeaderCards;
    }

    public void setPlayedLeaderCards(List<LeaderCard> playedLeaderCards) {
        this.playedLeaderCards = playedLeaderCards;
        updateLeaderCards();
    }

    public List<Stack<DevelopmentCard>> getPlayedDevelopmentCards() {
        return playedDevelopmentCards;
    }

    public void setPlayedDevelopmentCards(List<Stack<DevelopmentCard>> playedDevelopmentCards) {
        this.playedDevelopmentCards = playedDevelopmentCards;
        updateDevelopmentCards();
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
        this.reducedModel.moveFaithMarker(reducedPlayer.getNickname(), position);
    }

    public Map<Pair<Integer, Integer>, Pair<Integer, Integer>> getVaticanReports() {
        return vaticanReports;
    }

    public void setVaticanReports(Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports) {
        this.vaticanReports = vaticanReports;
        updateVaticanReports();
    }

    public void setFaithTrackVictoryPoints() {
    }

    public int getLorenzoIlMagnificoPosition() {
        return LorenzoIlMagnificoPosition;
    }

    public void setLorenzoIlMagnificoPosition(int LorenzoIlMagnificoPosition) {
        this.LorenzoIlMagnificoPosition = LorenzoIlMagnificoPosition;
        updateLorenzoFaithMarker(LorenzoIlMagnificoPosition);
    }

    public ProductionPower getProductionPower() {
        return productionPower;
    }

    public void setProductionPower(ProductionPower productionPower) {
        this.productionPower = productionPower;
    }

    private void updateWarehouseView() {
        this.reducedModel.updateWarehouse(reducedPlayer.getNickname(), deposit, locker, extraDeposits, supply);
    }

    private void updateLeaderCards() {
        this.reducedModel.updateLeaderCards(reducedPlayer.getNickname(), playedLeaderCards);
    }

    private void updatePlayerPoints() {
        this.reducedModel.updatePoints(reducedPlayer.getNickname(), playerPoints);
    }

    private void updateDevelopmentCards() {
        this.reducedModel.updateDevelopmentCards(reducedPlayer.getNickname(), playedDevelopmentCards);
    }

    private void updateVaticanReports() {
        this.reducedModel.updateVaticanReports(reducedPlayer.getNickname(), vaticanReports);
    }

    private void updateLorenzoFaithMarker(int LorenzoIlMagnificoPosition) {
        this.reducedModel.updateLorenzoFaithMarker(LorenzoIlMagnificoPosition);
    }
}
