package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.*;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultSettingsBuilder {

    private static final int leaderCardNum = 1;
    private String path;
    private final GameSettings gameSettings;

    public DefaultSettingsBuilder(String path) {
        gameSettings = new GameSettings(developmentCardsBuilder(), leaderCardNum, leaderCardsBuilder(), dashboardProductionPowerBuilder(),
                vaticanReportsListBuilder(), faithTrackVictoryPointsBuilder());
        gameSettings.saveSettings(path);
    }

    private DevelopmentCard[] developmentCardsBuilder() {
        // TODO
        DevelopmentCard[] developmentCards = new DevelopmentCard[48];
        return developmentCards;
    }

    private LeaderCard[] leaderCardsBuilder() {
        // TODO
        LeaderCard[] leaderCards = new LeaderCard[leaderCardNum];

        leaderCards[0] = leaderCardBuilder("ID=1;VP=2;BC=YELLOW:1:1,GREEN:1:1;RC=;SA=DCD;R=SERVANT;Q=1;");

        return leaderCards;
    }

    private LeaderCard leaderCardBuilder(String card) {

        Map<String, String> propertyMap = Arrays.stream(card.split(";"))
                .map(s -> s.split("="))
                .filter(s -> s.length > 1)
                .collect(Collectors.toMap(s-> s[0], s->s[1]));

        Map<Resource, Integer> resourceCost = new HashMap<>();
        Map<Banner, Integer> bannerCost = new HashMap<>();

        int id = Integer.parseInt(propertyMap.get("ID"));
        int victoryPoints = Integer.parseInt(propertyMap.get("VP"));

        if  (propertyMap.get("RC") != null) {
            resourceCost = Arrays.stream(propertyMap.get("RC").split(","))
                    .map(e -> e.split(":"))
                    .collect(Collectors.toMap(e -> parseResource(e[0]), e -> Integer.parseInt(e[1])));
        }

        if  (propertyMap.get("BC") != null) {
            bannerCost = Arrays.stream(propertyMap.get("BC").split(","))
                    .map(e -> e.split(":"))
                    .collect(Collectors.toMap(e -> parseBanner(e[0], e[1]), e -> Integer.parseInt(e[2])));
        }

        SpecialAbility specialAbility = parseSpecialAbility(propertyMap);

        return new LeaderCard(id, victoryPoints, bannerCost, resourceCost, specialAbility);

    }

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

    private Banner parseBanner(String bannerColor, String level) {

        return switch (bannerColor) {
            case "GREEN" -> new Banner(BannerEnum.GREEN, Integer.parseInt(level));
            case "YELLOW" -> new Banner(BannerEnum.YELLOW, Integer.parseInt(level));
            case "BLUE" -> new Banner(BannerEnum.BLUE, Integer.parseInt(level));
            case "PURPLE" -> new Banner(BannerEnum.PURPLE, Integer.parseInt(level));
            default -> null;
        };


    }

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

    private DevelopmentCardDiscount parseDevelopmentCardDiscount(Map<String, String> propertyMap) {

        Resource resource = parseResource(propertyMap.get("R"));

        int quantity = Integer.parseInt(propertyMap.get("Q"));

        return new DevelopmentCardDiscount(resource, quantity);

    }

    private WhiteMarbleResource parseWhiteMarbleResource(Map<String, String> propertyMap) {

        Resource resource = parseResource(propertyMap.get("R"));

        return new WhiteMarbleResource(resource);

    }

    private WarehouseExtraSpace parseWarehouseExtraSpace(Map<String, String> propertyMap) {

        Resource resource = parseResource(propertyMap.get("R"));

        return new WarehouseExtraSpace(resource);

    }

    private ProductionPower dashboardProductionPowerBuilder() {
        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.ANY, 2);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.ANY, 1);

        return new ProductionPower(resourceRequired, resourceProduced, 0);

    }

    private List<VaticanReport> vaticanReportsListBuilder() {
        List<VaticanReport> vaticanReportsList = new ArrayList<>();
        vaticanReportsList.add(new VaticanReport(5, 8, 2));
        vaticanReportsList.add(new VaticanReport(12, 16, 3));
        vaticanReportsList.add(new VaticanReport(19, 24, 4));

        return vaticanReportsList;
    }

    private int[] faithTrackVictoryPointsBuilder() {
        return new int[]{0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,20};
    }
}