package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.*;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultSettingsBuilder {

    private static final int leaderCardNum = 16;
    private final GameSettings gameSettings;

    public DefaultSettingsBuilder() {
        gameSettings = new GameSettings(developmentCardsBuilder(), leaderCardNum, leaderCardsBuilder(), dashboardProductionPowerBuilder(),
                vaticanReportsListBuilder(), faithTrackVictoryPointsBuilder());
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    private DevelopmentCard[] developmentCardsBuilder() {
        DevelopmentCard[] developmentCards = new DevelopmentCard[48];

        developmentCards[0] = buildDevelopmentCard("ID=1;VP=1;BA=GREEN:1;RC=SHIELD:2;SA=PP;RR=GOLD:1;RP=;FP=1;");
        developmentCards[1] = buildDevelopmentCard("ID=2;VP=1;BA=PURPLE:1;RC=SERVANT:2;SA=PP;RR=STONE:1;RP=;FP=1;");
        developmentCards[2] = buildDevelopmentCard("ID=3;VP=1;BA=BLUE:1;RC=GOLD:2;SA=PP;RR=SHIELD:1;RP=;FP=1;");
        developmentCards[3] = buildDevelopmentCard("ID=4;VP=1;BA=YELLOW:1;RC=STONE:2;SA=PP;RR=SERVANT:1;RP=;FP=1;");
        developmentCards[4] = buildDevelopmentCard("ID=5;VP=2;BA=GREEN:1;RC=SHIELD:1,SERVANT:1,STONE:1;SA=PP;RR=STONE:1;RP=SERVANT:1;FP=0;");
        developmentCards[5] = buildDevelopmentCard("ID=6;VP=2;BA=PURPLE:1;RC=SHIELD:1,SERVANT:1,GOLD:1;SA=PP;RR=GOLD:1;RP=SHIELD:1;FP=0;");
        developmentCards[6] = buildDevelopmentCard("ID=7;VP=2;BA=BLUE:1;RC=GOLD:1,SERVANT:1,STONE:1;SA=PP;RR=SERVANT:1;RP=STONE:1;FP=0;");
        developmentCards[7] = buildDevelopmentCard("ID=8;VP=2;BA=YELLOW:1;RC=SHIELD:1,STONE:1,GOLD:1;SA=PP;RR=SHIELD:1;RP=GOLD:1;FP=0;");
        developmentCards[8] = buildDevelopmentCard("ID=9;VP=3;BA=GREEN:1;RC=SHIELD:3;SA=PP;RR=SERVANT:2;RP=GOLD:1,SHIELD:1,STONE:1;FP=0;");
        developmentCards[9] = buildDevelopmentCard("ID=10;VP=3;BA=PURPLE:1;RC=SERVANT:3;SA=PP;RR=GOLD:2;RP=SERVANT:1,SHIELD:1,STONE:1;FP=0;");
        developmentCards[10] = buildDevelopmentCard("ID=11;VP=3;BA=BLUE:1;RC=GOLD:3;SA=PP;RR=STONE:2;RP=GOLD:1,SERVANT:1,SHIELD:1;FP=0;");
        developmentCards[11] = buildDevelopmentCard("ID=12;VP=3;BA=YELLOW:1;RC=STONE:3;SA=PP;RR=SHIELD:2;RP=GOLD:1,SERVANT:1,STONE:1;FP=0;");
        developmentCards[12] = buildDevelopmentCard("ID=13;VP=4;BA=GREEN:1;RC=SHIELD:2,GOLD:2;SA=PP;RR=STONE:1,SERVANT:1;RP=GOLD:2;FP=1;");
        developmentCards[13] = buildDevelopmentCard("ID=14;VP=4;BA=PURPLE:1;RC=SERVANT:2,STONE:2;SA=PP;RR=GOLD:1,SHIELD:1;RP=STONE:2;FP=1;");
        developmentCards[14] = buildDevelopmentCard("ID=15;VP=4;BA=BLUE:1;RC=GOLD:2,SERVANT:2;SA=PP;RR=SHIELD:1,STONE:1;RP=SERVANT:2;FP=1;");
        developmentCards[15] = buildDevelopmentCard("ID=16;VP=4;BA=YELLOW:1;RC=STONE:2,SHIELD:2;SA=PP;RR=GOLD:1,SERVANT:1;RP=SHIELD:2;FP=1;");
        developmentCards[16] = buildDevelopmentCard("ID=17;VP=5;BA=GREEN:2;RC=SHIELD:4;SA=PP;RR=STONE:1;RP=;FP=2;");
        developmentCards[17] = buildDevelopmentCard("ID=18;VP=5;BA=PURPLE:2;RC=SERVANT:4;SA=PP;RR=GOLD:1;RP=;FP=2;");
        developmentCards[18] = buildDevelopmentCard("ID=19;VP=5;BA=BLUE:2;RC=GOLD:4;SA=PP;RR=SERVANT:1;RP=;FP=2;");
        developmentCards[19] = buildDevelopmentCard("ID=20;VP=5;BA=YELLOW:2;RC=STONE:4;SA=PP;RR=SHIELD:1;RP=;FP=2;");
        developmentCards[20] = buildDevelopmentCard("ID=21;VP=6;BA=GREEN:2;RC=SHIELD:3,SERVANT:2;SA=PP;RR=SHIELD:1,SERVANT:1;RP=STONE:3;FP=0;");
        developmentCards[21] = buildDevelopmentCard("ID=22;VP=6;BA=PURPLE:2;RC=SERVANT:3,GOLD:2;SA=PP;RR=GOLD:1,SERVANT:1;RP=SHIELD:3;FP=0;");
        developmentCards[22] = buildDevelopmentCard("ID=23;VP=6;BA=BLUE:2;RC=GOLD:3,STONE:2;SA=PP;RR=GOLD:1,STONE:1;RP=SERVANT:3;FP=0;");
        developmentCards[23] = buildDevelopmentCard("ID=24;VP=6;BA=YELLOW:2;RC=STONE:3,SHIELD:2;SA=PP;RR=STONE:1,SHIELD:1;RP=GOLD:3;FP=0;");
        developmentCards[24] = buildDevelopmentCard("ID=25;VP=7;BA=GREEN:2;RC=SHIELD:5;SA=PP;RR=GOLD:2;RP=STONE:2;FP=2;");
        developmentCards[25] = buildDevelopmentCard("ID=26;VP=7;BA=PURPLE:2;RC=SERVANT:5;SA=PP;RR=STONE:2;RP=GOLD:2;FP=2;");
        developmentCards[26] = buildDevelopmentCard("ID=27;VP=7;BA=BLUE:2;RC=GOLD:5;SA=PP;RR=SERVANT:2;RP=SHIELD:2;FP=2;");
        developmentCards[27] = buildDevelopmentCard("ID=28;VP=7;BA=YELLOW:2;RC=STONE:5;SA=PP;RR=SHIELD:2;RP=SERVANT:2;FP=2;");
        developmentCards[28] = buildDevelopmentCard("ID=29;VP=8;BA=GREEN:2;RC=SHIELD:3,GOLD:3;SA=PP;RR=GOLD:1;RP=SHIELD:2;FP=1;");
        developmentCards[29] = buildDevelopmentCard("ID=30;VP=8;BA=PURPLE:2;RC=SERVANT:3,SHIELD:3;SA=PP;RR=STONE:1;RP=SERVANT:2;FP=1;");
        developmentCards[30] = buildDevelopmentCard("ID=31;VP=8;BA=BLUE:2;RC=GOLD:3,STONE:3;SA=PP;RR=SERVANT:1;RP=STONE:2;FP=1;");
        developmentCards[31] = buildDevelopmentCard("ID=32;VP=8;BA=YELLOW:2;RC=STONE:3,SERVANT:3;SA=PP;RR=SHIELD:1;RP=GOLD:2;FP=1;");
        developmentCards[32] = buildDevelopmentCard("ID=33;VP=9;BA=GREEN:3;RC=SHIELD:6;SA=PP;RR=GOLD:2;RP=STONE:3;FP=2;");
        developmentCards[33] = buildDevelopmentCard("ID=34;VP=9;BA=PURPLE:3;RC=SERVANT:6;SA=PP;RR=STONE:2;RP=GOLD:3;FP=2;");
        developmentCards[34] = buildDevelopmentCard("ID=35;VP=9;BA=BLUE:3;RC=GOLD:6;SA=PP;RR=SERVANT:2;RP=SHIELD:3;FP=2;");
        developmentCards[35] = buildDevelopmentCard("ID=36;VP=9;BA=YELLOW:3;RC=STONE:6;SA=PP;RR=SHIELD:2;RP=SERVANT:3;FP=2;");
        developmentCards[36] = buildDevelopmentCard("ID=37;VP=10;BA=GREEN:3;RC=SHIELD:5,SERVANT:2;SA=PP;RR=GOLD:1,SERVANT:1;RP=SHIELD:2,STONE:2;FP=1;");
        developmentCards[37] = buildDevelopmentCard("ID=38;VP=10;BA=PURPLE:3;RC=SERVANT:5,GOLD:2;SA=PP;RR=STONE:1,SHIELD:1;RP=GOLD:2,SERVANT:2;FP=1;");
        developmentCards[38] = buildDevelopmentCard("ID=39;VP=10;BA=BLUE:3;RC=GOLD:5,STONE:2;SA=PP;RR=GOLD:1,SHIELD:1;RP=SERVANT:2,STONE:2;FP=1;");
        developmentCards[39] = buildDevelopmentCard("ID=40;VP=10;BA=YELLOW:3;RC=STONE:5,SERVANT:2;SA=PP;RR=STONE:1,SERVANT:1;RP=GOLD:2,SHIELD:2;FP=1;");
        developmentCards[40] = buildDevelopmentCard("ID=41;VP=11;BA=GREEN:3;RC=SHIELD:7;SA=PP;RR=SERVANT:1;RP=GOLD:1;FP=3;");
        developmentCards[41] = buildDevelopmentCard("ID=42;VP=11;BA=PURPLE:3;RC=SERVANT:7;SA=PP;RR=GOLD:1;RP=STONE:1;FP=3;");
        developmentCards[42] = buildDevelopmentCard("ID=43;VP=11;BA=BLUE:3;RC=GOLD:7;SA=PP;RR=STONE:1;RP=SHIELD:1;FP=3;");
        developmentCards[43] = buildDevelopmentCard("ID=44;VP=11;BA=YELLOW:3;RC=STONE:7;SA=PP;RR=SHIELD:1;RP=SERVANT:1;FP=3;");
        developmentCards[44] = buildDevelopmentCard("ID=45;VP=12;BA=GREEN:3;RC=SHIELD:4,GOLD:4;SA=PP;RR=STONE:1;RP=GOLD:3,SHIELD:1;FP=0;");
        developmentCards[45] = buildDevelopmentCard("ID=46;VP=12;BA=PURPLE:3;RC=SERVANT:4,SHIELD:4;SA=PP;RR=GOLD:1;RP=STONE:3,SERVANT:1;FP=0;");
        developmentCards[46] = buildDevelopmentCard("ID=47;VP=12;BA=BLUE:3;RC=GOLD:4,STONE:4;SA=PP;RR=SERVANT:1;RP=GOLD:1,SHIELD:3;FP=0;");
        developmentCards[47] = buildDevelopmentCard("ID=48;VP=12;BA=YELLOW:3;RC=STONE:4,SERVANT:4;SA=PP;RR=SHIELD:1;RP=STONE:1,SERVANT:3;FP=0;");

        return developmentCards;
    }

    private DevelopmentCard buildDevelopmentCard(String card) {

        Map<String, String> propertyMap = Arrays.stream(card.split(";"))
                .map(s -> s.split("="))
                .filter(s -> s.length > 1)
                .collect(Collectors.toMap(s-> s[0], s->s[1]));

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

    private LeaderCard[] leaderCardsBuilder() {
        LeaderCard[] leaderCards = new LeaderCard[leaderCardNum];

        leaderCards[0] = buildLeaderCard("ID=1;VP=2;BC=YELLOW:1:1,GREEN:1:1;RC=;SA=DCD;R=SERVANT;Q=1;");
        leaderCards[1] = buildLeaderCard("ID=2;VP=2;BC=BLUE:1:1,PURPLE:1:1;RC=;SA=DCD;R=SHIELD;Q=1;");
        leaderCards[2] = buildLeaderCard("ID=3;VP=2;BC=GREEN:1:1,BLUE:1:1;RC=;SA=DCD;R=STONE;Q=1;");
        leaderCards[3] = buildLeaderCard("ID=4;VP=2;BC=YELLOW:1:1,PURPLE:1:1;RC=;SA=DCD;R=GOLD;Q=1;");
        leaderCards[4] = buildLeaderCard("ID=5;VP=3;BC=;RC=GOLD:5;SA=WES;R=STONE;");
        leaderCards[5] = buildLeaderCard("ID=6;VP=3;BC=;RC=STONE:5;SA=WES;R=SERVANT;");
        leaderCards[6] = buildLeaderCard("ID=7;VP=3;BC=;RC=SERVANT:5;SA=WES;R=SHIELD;");
        leaderCards[7] = buildLeaderCard("ID=8;VP=3;BC=;RC=SHIELD:5;SA=WES;R=GOLD;");
        leaderCards[8] = buildLeaderCard("ID=9;VP=5;BC=YELLOW:1:2,BLUE:1:1;RC=;SA=WMR;R=SERVANT;");
        leaderCards[9] = buildLeaderCard("ID=10;VP=5;BC=GREEN:1:2,PURPLE:1:1;RC=;SA=WMR;R=SHIELD;");
        leaderCards[10] = buildLeaderCard("ID=11;VP=5;BC=BLUE:1:2,YELLOW:1:1;RC=;SA=WMR;R=STONE;");
        leaderCards[11] = buildLeaderCard("ID=12;VP=5;BC=PURPLE:1:2,GREEN:1:1;RC=;SA=WMR;R=GOLD;");
        leaderCards[12] = buildLeaderCard("ID=13;VP=4;BC=YELLOW:2:1;RC=;SA=PP;RR=SHIELD:1;RP=ANY:1;FP=1;");
        leaderCards[13] = buildLeaderCard("ID=14;VP=4;BC=BLUE:2:1;RC=;SA=PP;RR=SERVANT:1;RP=ANY:1;FP=1;");
        leaderCards[14] = buildLeaderCard("ID=15;VP=4;BC=PURPLE:2:1;RC=;SA=PP;RR=STONE:1;RP=ANY:1;FP=1;");
        leaderCards[15] = buildLeaderCard("ID=16;VP=4;BC=GREEN:2:1;RC=;SA=PP;RR=GOLD:1;RP=ANY:1;FP=1;");

        return leaderCards;
    }

    private LeaderCard buildLeaderCard(String card) {

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

        return switch (resourceName.toUpperCase()) {
            case "SHIELD" -> Resource.SHIELD;
            case "SERVANT" -> Resource.SERVANT;
            case "STONE" -> Resource.STONE;
            case "GOLD" -> Resource.GOLD;
            case "ANY" -> Resource.ANY;
            default -> null;
        };

    }

    private Banner parseBanner(String bannerColor, String level) {

        return switch (bannerColor.toUpperCase()) {
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

        Map<Resource, Integer> resourceProduced = new HashMap<>();

        Map<Resource, Integer> resourceRequired = Arrays.stream(propertyMap.get("RR").split(","))
                .map(e -> e.split(":"))
                .collect(Collectors.toMap(e -> parseResource(e[0]), e -> Integer.parseInt(e[1])));

        if  (propertyMap.get("RP") != null) {
            resourceProduced = Arrays.stream(propertyMap.get("RP").split(","))
                    .map(e -> e.split(":"))
                    .collect(Collectors.toMap(e -> parseResource(e[0]), e -> Integer.parseInt(e[1])));
        }

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