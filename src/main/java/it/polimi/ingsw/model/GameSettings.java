package it.polimi.ingsw.model;

import java.nio.file.Path;
import java.util.Map;

public class GameSettings {

    static final int DEVELOPMENT_CARD_NUM = 40;
    static final int LEADER_CARD_NUM = 16;
    static final int VATICAN_REPORTS_NUM = 3;

    private DevelopmentCard[] developmentCards;

    private LeaderCard[] leaderCards;

    private ProductionPower dashBoardProductionPower;

    private VaticanReport[] vaticanReports;

    private Map<Integer, Integer> faithTrackVictoryPoints;

    public void loadSettings(Path p) {

    }

    public DevelopmentCard[] getDevelopmentCards() {
        return developmentCards;
    }

    public LeaderCard[] getLeaderCards() {
        return leaderCards;
    }

    public ProductionPower getDashBoardProductionPower() {
        return dashBoardProductionPower;
    }

    public VaticanReport[] getVaticanReports() {
        return vaticanReports;
    }

    public Map<Integer, Integer> getFaithTrackVictoryPoints() {
        return faithTrackVictoryPoints;
    }

}
