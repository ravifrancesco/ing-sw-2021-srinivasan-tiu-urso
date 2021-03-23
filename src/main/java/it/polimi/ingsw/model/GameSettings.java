package it.polimi.ingsw.model;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

public class GameSettings {

    static final int DEVELOPMENT_CARD_NUM = 40;
    static final int LEADER_CARD_NUM = 16;
    static final int VATICAN_REPORTS_NUM = 3;
    static final int FAITH_TRACK_LENGTH = 25;

    private DevelopmentCard[] developmentCards;

    private LeaderCard[] leaderCards;

    private ProductionPower dashBoardProductionPower;

    private List<VaticanReport> vaticanReports;

    private int[] faithTrackVictoryPoints;

    public void saveSettings(Path p) {

        try {

            OutputStream output = new FileOutputStream(p.toString());
            Properties prop = new Properties();

            // set the properties value
            IntStream.range(0, DEVELOPMENT_CARD_NUM)
                    .forEach(i -> prop.setProperty("DevelopmentCard."+ i , developmentCards[i].toString()));
            IntStream.range(0, LEADER_CARD_NUM)
                    .forEach(i -> prop.setProperty("LeaderCard."+ i , leaderCards[i].toString()));
            prop.setProperty("DashBoardProductionPower", dashBoardProductionPower.toString());
            vaticanReports.
                    forEach(vr -> prop.setProperty("VaticanReport."+ vaticanReports.indexOf(vr) ,vr.toString()));
            IntStream.range(0, FAITH_TRACK_LENGTH)
                    .forEach(i -> prop.setProperty("FaithTrackVictoryPoint."+ i , String.valueOf(faithTrackVictoryPoints[i])));

            // save properties to path
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    public void loadSettings(Path p) {
        // TODO after implementing constructors for cards and production power
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

    public List<VaticanReport> getVaticanReports() {
        return vaticanReports;
    }

    public int[] getFaithTrackVictoryPoints() {
        return faithTrackVictoryPoints;
    }

}
