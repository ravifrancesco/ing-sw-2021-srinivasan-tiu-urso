package it.polimi.ingsw.model;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

/**
 * This class represents the settings for the current game.
 * The object memorizes the modifiable settings of the game.
 * The state includes this custom settings:
 * <ul>
 * <li> The development cards.
 * <li> the leader card.
 * <li> The production power of the dashboard.
 * <li> The vatican reports.
 * <li> The faith track victory points.
 * </ul>
 * <p>
 */
public class GameSettings {

    static final int DEVELOPMENT_CARD_NUM = 40;
    static final int FAITH_TRACK_LENGTH = 25;

    private DevelopmentCard[] developmentCards;

    private int leaderCardNum;
    private LeaderCard[] leaderCards;

    private ProductionPower dashBoardProductionPower;

    private List<VaticanReport> vaticanReports;

    private int[] faithTrackVictoryPoints;

    /**
     * The constructor for a GameSettings object.
     *
     * @param developmentCards          array of DEVELOPMENT_CARD_NUM developmentCards.
     * @param leaderCards               array of LEADER_CARD_NUM leaderCards.
     * @param dashBoardProductionPower  productionPower of the dashboard.
     * @param vaticanReports            List of vaticanReports.
     * @param faithTrackVictoryPoints   array of FAITH_TRACK_LENGTH int representing faithTrackVictoryPoints.
     */
    public GameSettings(DevelopmentCard[] developmentCards, int leaderCardNum, LeaderCard[] leaderCards,
                        ProductionPower dashBoardProductionPower, List<VaticanReport> vaticanReports, int[] faithTrackVictoryPoints) {
        this.developmentCards = developmentCards;
        this.leaderCardNum = leaderCardNum;
        this.leaderCards = leaderCards;
        this.dashBoardProductionPower = dashBoardProductionPower;
        this.vaticanReports = vaticanReports;
        this.faithTrackVictoryPoints = faithTrackVictoryPoints;
    }

    /**
     * Construtor to use to create GameSettings from .properties file.
     *
     * @param p path of the .properties file.
     */
    public GameSettings(Path p) {
        loadSettings(p);
    }


    /**
     * Saves the current settings to a .properties file described by a path.
     *
     * @param p path to locate where to save the .properties file.
     */
    public void saveSettings(Path p) {

        try {

            OutputStream output = new FileOutputStream(p.toString());
            Properties prop = new Properties();

            // set the properties value
            IntStream.range(0, DEVELOPMENT_CARD_NUM)
                    .forEach(i -> prop.setProperty("DevelopmentCard."+ i , developmentCards[i].toString()));
            prop.setProperty("LeaderCardNum", String.valueOf(leaderCardNum));
            IntStream.range(0, leaderCardNum)
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

    /**
     * Getter for developmentCards.
     *
     * @return an array of DEVELOPMENT_CARD_NUM development cards.
     */
    public DevelopmentCard[] getDevelopmentCards() {
        return developmentCards;
    }

    /**
     * Getter for leaderCards.
     *
     * @return an array of LEADER_CARD_NUM leader cards.
     */
    public LeaderCard[] getLeaderCards() {
        return leaderCards;
    }

    /**
     * Getter for dashBoardProductionPower.
     *
     * @return the dashBoardProductionPower.
     */
    public ProductionPower getDashBoardProductionPower() {
        return dashBoardProductionPower;
    }

    /**
     * Getter for developmentCards.
     *
     * @return a List of vaticanReports.
     */
    public List<VaticanReport> getVaticanReports() {
        return vaticanReports;
    }

    /**
     * Getter for faithTrackVictoryPoints.
     *
     * @return array of FAITH_TRACK_LENGTH int representing faithTrackVictoryPoints.
     */
    public int[] getFaithTrackVictoryPoints() {
        return faithTrackVictoryPoints;
    }

}
