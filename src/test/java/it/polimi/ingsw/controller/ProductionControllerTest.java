package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.PowerNotActivatableException;
import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.controller.exceptions.WrongTurnPhaseException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ProductionControllerTest {

    @Test
    public void activateLeaderProductionTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.COMMON);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 5);
        warehouse.storeInLocker(Resource.SHIELD, 5);
        warehouse.storeInLocker(Resource.STONE, 5);
        warehouse.storeInLocker(Resource.SERVANT, 5);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 1, warehouse);
        resourceContainer.addLockerSelectedResource(Resource.SHIELD, 1, warehouse);

        Map<Resource, Integer> resourceRequiredOptional = new HashMap<>();

        resourceRequiredOptional.put(Resource.SHIELD, 1);

        productionController.setCurrentPlayer("player1");
        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            thrownExceptions++;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 4);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 5);
        Assert.assertEquals(thrownExceptions, 0);
    }

    @Test
    public void wrongTurnExceptionTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.COMMON);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 5);
        warehouse.storeInLocker(Resource.SHIELD, 5);
        warehouse.storeInLocker(Resource.STONE, 5);
        warehouse.storeInLocker(Resource.SERVANT, 5);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 1, warehouse);
        resourceContainer.addLockerSelectedResource(Resource.SHIELD, 1, warehouse);

        Map<Resource, Integer> resourceRequiredOptional = new HashMap<>();

        resourceRequiredOptional.put(Resource.SHIELD, 1);

        productionController.setCurrentPlayer("player2");
        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (WrongTurnException e) {
            thrownExceptions++;
        } catch (PowerNotActivatableException | WrongMoveException e) {
            thrownExceptions = 0;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 5);
        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void wrongTurnPhaseExceptionTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.MARKET);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 5);
        warehouse.storeInLocker(Resource.SHIELD, 5);
        warehouse.storeInLocker(Resource.STONE, 5);
        warehouse.storeInLocker(Resource.SERVANT, 5);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 1, warehouse);
        resourceContainer.addLockerSelectedResource(Resource.SHIELD, 1, warehouse);

        Map<Resource, Integer> resourceRequiredOptional = new HashMap<>();

        resourceRequiredOptional.put(Resource.SHIELD, 1);

        productionController.setCurrentPlayer("player1");
        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (WrongTurnPhaseException e) {
            thrownExceptions++;
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            thrownExceptions = 0;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 5);
        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void invalidIndexTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.COMMON);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 5);
        warehouse.storeInLocker(Resource.SHIELD, 5);
        warehouse.storeInLocker(Resource.STONE, 5);
        warehouse.storeInLocker(Resource.SERVANT, 5);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 1, warehouse);
        resourceContainer.addLockerSelectedResource(Resource.SHIELD, 1, warehouse);

        Map<Resource, Integer> resourceRequiredOptional = new HashMap<>();

        resourceRequiredOptional.put(Resource.SHIELD, 1);

        productionController.setCurrentPlayer("player1");
        try {
            productionController.activateLeaderCardProduction("player1", -1, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (PowerNotActivatableException e) {
            thrownExceptions++;
        } catch (WrongTurnException | WrongMoveException e) {
            thrownExceptions = 0;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 5);
        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void invalidAbilityTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.COMMON);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() == SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 5);
        warehouse.storeInLocker(Resource.SHIELD, 5);
        warehouse.storeInLocker(Resource.STONE, 5);
        warehouse.storeInLocker(Resource.SERVANT, 5);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 1, warehouse);
        resourceContainer.addLockerSelectedResource(Resource.SHIELD, 1, warehouse);

        Map<Resource, Integer> resourceRequiredOptional = new HashMap<>();

        resourceRequiredOptional.put(Resource.SHIELD, 1);

        productionController.setCurrentPlayer("player1");
        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (PowerNotActivatableException e) {
            thrownExceptions++;
        } catch (WrongTurnException | WrongMoveException e) {
            thrownExceptions = 0;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 5);
        Assert.assertEquals(thrownExceptions, 1);
    }

    @Test
    public void invalidNumberOfResourcesTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.COMMON);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 5);
        warehouse.storeInLocker(Resource.SHIELD, 5);
        warehouse.storeInLocker(Resource.STONE, 5);
        warehouse.storeInLocker(Resource.SERVANT, 5);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 1, warehouse);
        resourceContainer.addLockerSelectedResource(Resource.SHIELD, 1, warehouse);

        productionController.setCurrentPlayer("player1");
        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, new HashMap<>(), new HashMap<>());
        } catch (PowerNotActivatableException e) {
            thrownExceptions++;
        } catch (WrongTurnException | WrongMoveException e) {
            thrownExceptions = 0;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 5);
        Assert.assertEquals(thrownExceptions, 1);
    }

    // TODO after fixing container
    /*
    @Test
    public void notEnoughResourcesTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.COMMON);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 1);
        warehouse.storeInLocker(Resource.SHIELD, 1);
        warehouse.storeInLocker(Resource.STONE, 1);
        warehouse.storeInLocker(Resource.SERVANT, 1);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 2, warehouse);

        Map<Resource, Integer> resourceRequiredOptional = new HashMap<>();

        resourceRequiredOptional.put(Resource.SHIELD, 1);

        productionController.setCurrentPlayer("player1");
        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (PowerNotActivatableException e) {
            thrownExceptions++;
        } catch (WrongTurnException | WrongMoveException e) {
            thrownExceptions = 0;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();

        System.out.println(playerResources);

        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 1);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 1);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 1);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 1);
        Assert.assertEquals(thrownExceptions, 1);
    }*/

    @Test
    public void alreadyActivatedTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.COMMON);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 5);
        warehouse.storeInLocker(Resource.SHIELD, 5);
        warehouse.storeInLocker(Resource.STONE, 5);
        warehouse.storeInLocker(Resource.SERVANT, 5);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 1, warehouse);
        resourceContainer.addLockerSelectedResource(Resource.SHIELD, 1, warehouse);

        Map<Resource, Integer> resourceRequiredOptional = new HashMap<>();

        resourceRequiredOptional.put(Resource.SHIELD, 1);

        productionController.setCurrentPlayer("player1");
        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (WrongTurnException | PowerNotActivatableException | WrongMoveException e) {
            thrownExceptions = 0;
        }

        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (PowerNotActivatableException e) {
            thrownExceptions++;
        } catch (WrongTurnException | WrongMoveException e) {
            thrownExceptions = 0;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 4);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 5);
        Assert.assertEquals(thrownExceptions, 1);
    }

    // TODO check methods of resourceContainer
    /*
    @Test
    public void mismatchCostTest() {
        String gameId = "123456789";
        int numberOfPlayers = 2;
        int thrownExceptions = 0;

        Player p1 = new Player(buildGameSettings());
        Player p2 = new Player(buildGameSettings());

        Game game = new Game(gameId, numberOfPlayers);
        game.loadGameSettings(buildGameSettings());
        game.reset();
        game.addPlayer("player1", p1);
        game.addPlayer("player2", p2);

        ProductionController productionController = new ProductionController(game);
        game.startUniquePhase(TurnPhase.COMMON);

        GameBoard gameBoard = game.getGameBoard();
        LeaderCard lc;
        do{
            lc = gameBoard.getLeaderCard();
        } while (lc.getSpecialAbility().getType() != SpecialAbilityType.PRODUCTION_POWER);

        p1.addCard(lc);

        Warehouse warehouse = p1.getDashboard().getWarehouse();
        warehouse.storeInLocker(Resource.GOLD, 5);
        warehouse.storeInLocker(Resource.SHIELD, 5);
        warehouse.storeInLocker(Resource.STONE, 5);
        warehouse.storeInLocker(Resource.SERVANT, 5);

        p1.playLeaderCard(0);

        ResourceContainer resourceContainer = new ResourceContainer();

        resourceContainer.addLockerSelectedResource(Resource.GOLD, 1, warehouse);
        resourceContainer.addLockerSelectedResource(Resource.SERVANT, 1, warehouse);

        Map<Resource, Integer> resourceRequiredOptional = new HashMap<>();

        resourceRequiredOptional.put(Resource.SHIELD, 1);

        productionController.setCurrentPlayer("player1");
        try {
            productionController.activateLeaderCardProduction("player1", 0, resourceContainer, resourceRequiredOptional, new HashMap<>());
        } catch (WrongMoveException e) {
            thrownExceptions++;
        } catch (WrongTurnException | PowerNotActivatableException e) {
            thrownExceptions = 0;
        }

        Map<Resource, Integer> playerResources = p1.getDashboard().getWarehouse().getAllResources();
        Assert.assertEquals((int) playerResources.get(Resource.GOLD), 4);
        Assert.assertEquals((int) playerResources.get(Resource.SHIELD), 5);
        Assert.assertEquals((int) playerResources.get(Resource.STONE), 5);
        Assert.assertEquals((int) playerResources.get(Resource.SERVANT), 5);
        Assert.assertEquals(thrownExceptions, 1);
    }*/

    private GameSettings buildGameSettings() {


        DevelopmentCard[] developmentCards = developmentCardDeckBuilder();

        int leaderCardNum = 16;
        LeaderCard[] leaderCards = leaderCardDeckBuilder(leaderCardNum);

        ProductionPower dashboardProductionPower = productionPowerBuilder();

        int vaticanReportsNum = 3;
        List<VaticanReport> vaticanReports = vaticanReportsListBuilder();

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        return new GameSettings(developmentCards, leaderCardNum, leaderCards,
                dashboardProductionPower, vaticanReports, faithTrackVictoryPoints);

    }

    private DevelopmentCard[] developmentCardDeckBuilder() {

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 2);
        resourceCost.put(Resource.GOLD, 1);
        resourceCost.put(Resource.SHIELD, 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        return IntStream.range(0, GameSettings.DEVELOPMENT_CARD_NUM)
                .boxed()
                .map(i -> new DevelopmentCard(i, 4, resourceCost, p, chooseBanner(i)))
                .toArray(DevelopmentCard[]::new);

    }

    private LeaderCard[] leaderCardDeckBuilder(int leaderCardNum) {

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SERVANT, 3);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        SpecialAbility[] SAs = new SpecialAbility[4];
        SAs[0] = new ProductionPower(resourceRequired, resourceProduced, 1);
        SAs[1] = new DevelopmentCardDiscount(Resource.GOLD, 3);
        SAs[2] = new WarehouseExtraSpace(Resource.SERVANT);
        SAs[3] = new WhiteMarbleResource(Resource.SHIELD);

        return  IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> new LeaderCard(i, 2, bannerCost, resourceCost, SAs[i%4]))
                .toArray(LeaderCard[]::new);

    }

    private ProductionPower productionPowerBuilder() {

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        return new ProductionPower(resourceRequired, resourceProduced,2);

    }

    private List<VaticanReport> vaticanReportsListBuilder() {

        List<VaticanReport> vaticanReportsList = new ArrayList<>();
        vaticanReportsList.add(new VaticanReport(5, 8, 2));
        vaticanReportsList.add(new VaticanReport(12, 16, 3));
        vaticanReportsList.add(new VaticanReport(19, 24, 4));

        return vaticanReportsList;


    }

    private Banner chooseBanner(int val) {
        return switch (val % 12) {
            case 0 -> new Banner(BannerEnum.GREEN, 1);
            case 1 -> new Banner(BannerEnum.GREEN, 2);
            case 2 -> new Banner(BannerEnum.GREEN, 3);
            case 3 -> new Banner(BannerEnum.YELLOW, 1);
            case 4 -> new Banner(BannerEnum.YELLOW, 2);
            case 5 -> new Banner(BannerEnum.YELLOW, 3);
            case 6 -> new Banner(BannerEnum.BLUE, 1);
            case 7 -> new Banner(BannerEnum.BLUE, 2);
            case 8 -> new Banner(BannerEnum.BLUE, 3);
            case 9 -> new Banner(BannerEnum.PURPLE, 1);
            case 10 -> new Banner(BannerEnum.PURPLE, 2);
            case 11 -> new Banner(BannerEnum.PURPLE, 3);
            default -> null;
        };
    }
}
