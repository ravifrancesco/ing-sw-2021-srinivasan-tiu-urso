package it.polimi.ingsw.model.utils;

import it.polimi.ingsw.model.full.cards.Banner;
import it.polimi.ingsw.model.full.cards.BannerEnum;
import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.*;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.VaticanReport;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
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
public class GameSettings implements Serializable {

    public static final int DEVELOPMENT_CARD_NUM = 48;
    public static final int FAITH_TRACK_LENGTH = 25;

    private DevelopmentCard[] developmentCards;

    private int leaderCardNum;
    private LeaderCard[] leaderCards;

    private ProductionPower dashBoardProductionPower;

    private int vaticanReportsNum;
    private List<VaticanReport> vaticanReports;

    private int[] faithTrackVictoryPoints;

    /**
     * The constructor for a GameSettings object.
     *
     * @param developmentCards         array of DEVELOPMENT_CARD_NUM developmentCards.
     * @param leaderCards              array of LEADER_CARD_NUM leaderCards.
     * @param dashBoardProductionPower productionPower of the dashboard.
     * @param vaticanReports           List of vaticanReports.
     * @param faithTrackVictoryPoints  array of FAITH_TRACK_LENGTH int representing faithTrackVictoryPoints.
     */
    public GameSettings(DevelopmentCard[] developmentCards, int leaderCardNum, LeaderCard[] leaderCards,
                        ProductionPower dashBoardProductionPower,
                        List<VaticanReport> vaticanReports, int[] faithTrackVictoryPoints) {

        this.developmentCards = developmentCards;
        this.leaderCardNum = leaderCardNum;
        this.leaderCards = leaderCards;
        this.dashBoardProductionPower = dashBoardProductionPower;
        this.vaticanReportsNum = vaticanReports.size();
        this.vaticanReports = vaticanReports;
        this.faithTrackVictoryPoints = faithTrackVictoryPoints;

    }

    /**
     * Constructor to use to create GameSettings from .properties file.
     *
     * @param path path of the .properties file.
     */
    public GameSettings(String path) {
        loadSettings(path);
    }

    public static GameSettings loadDefaultGameSettings() {
        DefaultSettingsBuilder defaultSettingsBuilder = new DefaultSettingsBuilder();
        return defaultSettingsBuilder.getGameSettings();
    }

    /**
     * Saves the current settings to a .properties file described by a path.
     *
     * @param path path to locate where to save the .properties file.
     */
    public void saveSettings(String path) {

        try {

            OutputStream output = new FileOutputStream(path);
            Properties prop = new Properties();

            // set the properties value
            IntStream.range(0, DEVELOPMENT_CARD_NUM)
                    .forEach(i -> prop.setProperty("DevelopmentCard." + i, developmentCards[i].toString()));
            prop.setProperty("LeaderCardNum", String.valueOf(leaderCardNum));
            IntStream.range(0, leaderCardNum)
                    .forEach(i -> prop.setProperty("LeaderCard." + i, leaderCards[i].toString()));
            prop.setProperty("DashBoardProductionPower", dashBoardProductionPower.toString());
            prop.setProperty("VaticanReportsNum", String.valueOf(vaticanReportsNum));
            vaticanReports.
                    forEach(vr -> prop.setProperty("VaticanReport." + vaticanReports.indexOf(vr), vr.toString()));
            IntStream.range(0, FAITH_TRACK_LENGTH)
                    .forEach(i -> prop.setProperty("FaithTrackVictoryPoint." + i, String.valueOf(faithTrackVictoryPoints[i])));

            // save properties to path
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    /**
     * Loads the settings from a .properties file
     *
     * @param path path of the .properties file
     */
    public void loadSettings(String path) {

        try (InputStream input = new FileInputStream(path)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // load the properties
            loadDevelopmentCards(prop);
            this.leaderCardNum = Integer.parseInt(prop.getProperty("LeaderCardNum"));
            loadLeaderCards(prop);
            this.dashBoardProductionPower = parseProductionPower(
                    propertyParser(prop.getProperty("DashBoardProductionPower")));
            loadVaticanReports(prop);
            loadFaithTrackVictoryPoints(prop);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Given a property string it will return a map<property, value>
     *
     * @param property string extracted from the property file
     * @return map with keys being the property, values being the property value
     */
    private Map<String, String> propertyParser(String property) {

        return Arrays.stream(property.split(";"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));

    }

    /**
     * Converts a map representing the properties of development card into a DevelopmentCard
     *
     * @param propertyMap map with keys being the property, values being the property value
     * @return a Development card built using the propertyMap properties
     */
    private DevelopmentCard buildDevelopmentCard(Map<String, String> propertyMap) {

        int id = Integer.parseInt(propertyMap.get("ID"));
        int victoryPoints = Integer.parseInt(propertyMap.get("VP"));

        String[] bannerString = propertyMap.get("BA").split(":");
        Banner banner = parseBanner(bannerString[0], bannerString[1]);

        Map<Resource, Integer> resourceCost = Arrays.stream(propertyMap.get("RC").split(","))
                .map(e -> e.split(":"))
                .collect(Collectors.toMap(e -> parseResource(e[0]), e -> Integer.parseInt(e[1])));

        ProductionPower productionPower = parseProductionPower(propertyMap);

        return new DevelopmentCard(id, victoryPoints, resourceCost, productionPower, banner);

    }

    /**
     * Converts a map representing the properties of leader card into a LeaderCard
     *
     * @param propertyMap map with keys being the property, values being the property value
     * @return a Leader card built using the propertyMap properties
     */
    private LeaderCard buildLeaderCard(Map<String, String> propertyMap) {

        int id = Integer.parseInt(propertyMap.get("ID"));
        int victoryPoints = Integer.parseInt(propertyMap.get("VP"));

        Map<Resource, Integer> resourceCost = Arrays.stream(propertyMap.get("RC").split(","))
                .map(e -> e.split(":"))
                .collect(Collectors.toMap(e -> parseResource(e[0]), e -> Integer.parseInt(e[1])));

        Map<Banner, Integer> bannerCost = Arrays.stream(propertyMap.get("BC").split(","))
                .map(e -> e.split(":"))
                .collect(Collectors.toMap(e -> parseBanner(e[0], e[1]), e -> Integer.parseInt(e[2])));

        SpecialAbility specialAbility = parseSpecialAbility(propertyMap);

        return new LeaderCard(id, victoryPoints, bannerCost, resourceCost, specialAbility);

    }

    /**
     * Converts a map representing the properties of vatican report into a VaticanReport
     *
     * @param propertyMap map with keys being the property, values being the property value
     * @return a Vatican Report built using the propertyMap properties
     */
    private VaticanReport buildVaticanReport(Map<String, String> propertyMap) {

        int start = Integer.parseInt(propertyMap.get("ST"));
        int end = Integer.parseInt(propertyMap.get("END"));
        int victoryPoints = Integer.parseInt(propertyMap.get("VP"));

        return new VaticanReport(start, end, victoryPoints);

    }

    /**
     * Loads the DevelopmentCard array
     *
     * @param prop game settings properties
     */
    private void loadDevelopmentCards(Properties prop) {

        this.developmentCards = IntStream.range(0, DEVELOPMENT_CARD_NUM)
                .boxed()
                .map(i -> propertyParser(prop.getProperty("DevelopmentCard." + i)))
                .map(this::buildDevelopmentCard)
                .toArray(DevelopmentCard[]::new);

    }

    /**
     * Loads the LeaderCards array
     *
     * @param prop game settings properties
     */
    private void loadLeaderCards(Properties prop) {

        this.leaderCards = IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> propertyParser(prop.getProperty("LeaderCard." + i)))
                .map(this::buildLeaderCard)
                .toArray(LeaderCard[]::new);

    }

    /**
     * Loads the VaticanReports list
     *
     * @param prop game settings properties
     */
    private void loadVaticanReports(Properties prop) {

        this.vaticanReportsNum = Integer.parseInt(prop.getProperty("VaticanReportsNum"));

        this.vaticanReports = IntStream.range(0, vaticanReportsNum)
                .boxed()
                .map(i -> propertyParser(prop.getProperty("VaticanReport." + i)))
                .map(this::buildVaticanReport)
                .collect(Collectors.toList());

    }

    /**
     * Loads the faithTrackVictoryPoints array
     *
     * @param prop game settings properties
     */
    private void loadFaithTrackVictoryPoints(Properties prop) {

        this.faithTrackVictoryPoints = IntStream.range(0, FAITH_TRACK_LENGTH)
                .boxed()
                .map(i -> Integer.parseInt(prop.getProperty("FaithTrackVictoryPoint." + i)))
                .mapToInt(Integer::intValue)
                .toArray();

    }

    /**
     * Method to convert a property string into a resource
     *
     * @param resourceName string representing the name of the resource
     * @return a Resource matching the resourceName
     */
    private Resource parseResource(String resourceName) {

        return switch (resourceName) {
            case "SHIELD" -> Resource.SHIELD;
            case "SERVANT" -> Resource.SERVANT;
            case "STONE" -> Resource.STONE;
            case "GOLD" -> Resource.GOLD;
            case "ANY" -> Resource.ANY;
            default -> null;
        };

    }

    /**
     * Method to convert a property string into a banner
     *
     * @param bannerColor string representing the color of the banner
     * @param level       string representing the level of the banner
     * @return a banner matching the given color and level
     */
    private Banner parseBanner(String bannerColor, String level) {

        return switch (bannerColor) {
            case "GREEN" -> new Banner(BannerEnum.GREEN, Integer.parseInt(level));
            case "YELLOW" -> new Banner(BannerEnum.YELLOW, Integer.parseInt(level));
            case "BLUE" -> new Banner(BannerEnum.BLUE, Integer.parseInt(level));
            case "PURPLE" -> new Banner(BannerEnum.PURPLE, Integer.parseInt(level));
            default -> null;
        };


    }

    /**
     * Converts a map representing the properties of a special ability into a special ability.
     *
     * @param propertyMap map with keys being the property, values being the property value.
     * @return a ProductionPower built using the propertyMap properties.
     */
    private SpecialAbility parseSpecialAbility(Map<String, String> propertyMap) {

        String specialAbilityType = propertyMap.get("SA");

        return switch (specialAbilityType) {
            case "DCD" -> parseDevelopmentCardDiscount(propertyMap);
            case "PP" -> parseProductionPower(propertyMap);
            case "WES" -> parseWarehouseExtraSpace(propertyMap);
            case "WMR" -> parseWhiteMarbleResource(propertyMap);
            default -> null;
        };

    }

    /**
     * Converts a map representing the properties of a production power into a production power
     *
     * @param propertyMap map with keys being the property, values being the property value
     * @return a ProductionPower built using the propertyMap properties
     */
    private ProductionPower parseProductionPower(Map<String, String> propertyMap) {

        Map<Resource, Integer> resourceRequired = Arrays.stream(propertyMap.get("RR").split(","))
                .map(e -> e.split(":"))
                .collect(Collectors.toMap(e -> parseResource(e[0]), e -> Integer.parseInt(e[1])));

        Map<Resource, Integer> resourceProduced = Arrays.stream(propertyMap.get("RP").split(","))
                .map(e -> e.split(":"))
                .collect(Collectors.toMap(e -> parseResource(e[0]), e -> Integer.parseInt(e[1])));

        int numberFaithPoints = Integer.parseInt(propertyMap.get("FP"));

        return new ProductionPower(resourceRequired, resourceProduced, numberFaithPoints);

    }

    /**
     * Converts a map representing the properties of a production power into a development card discount.*
     * @param propertyMap map with keys being the property, values being the property value
     * @return a DevelopmentCardDiscount built using the propertyMap properties
     */
    private DevelopmentCardDiscount parseDevelopmentCardDiscount(Map<String, String> propertyMap) {

        Resource resource = parseResource(propertyMap.get("R"));

        int quantity = Integer.parseInt(propertyMap.get("Q"));

        return new DevelopmentCardDiscount(resource, quantity);

    }

    /**
     * Converts a map representing the properties of a production power into a white marble resource.
     *
     * @param propertyMap map with keys being the property, values being the property value
     * @return a WhiteMarbleResource built using the propertyMap properties
     */
    private WhiteMarbleResource parseWhiteMarbleResource(Map<String, String> propertyMap) {

        Resource resource = parseResource(propertyMap.get("R"));

        return new WhiteMarbleResource(resource);

    }

    /**
     * Converts a map representing the properties of a production power into a warehouse extra space.
     *
     * @param propertyMap map with keys being the property, values being the property value
     * @return a WarehouseExtraSpace built using the propertyMap properties
     */
    private WarehouseExtraSpace parseWarehouseExtraSpace(Map<String, String> propertyMap) {

        Resource resource = parseResource(propertyMap.get("R"));

        return new WarehouseExtraSpace(resource);

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
     * Getter for vaticanReports.
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

    /**
     * Getter for leaderCardNum.
     *
     * @return the leaderCardNum.
     */

    public int getLeaderCardNum() {
        return leaderCardNum;
    }
}
