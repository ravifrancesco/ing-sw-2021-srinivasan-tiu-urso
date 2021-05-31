package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class InputHandler {
    public static ClientMessage createGame(String[] in) {
        return MainLobbyHandler.createGame(in);
    }

    public static ClientMessage showGames() {
        return MainLobbyHandler.showGames();
    }

    public static ClientMessage joinGame(String[] in, CLI cli) {
        return MainLobbyHandler.joinGame(in, cli);
    }

    public static ClientMessage leaveLobby(String[] in, CLI cli) {
        return GameLobbyHandler.leaveLobby(in, cli);
    }

    public static ClientMessage quit(String[] in) {
        return MainLobbyHandler.quit(in);
    }

    public static ClientMessage discardExcessCard(String[] in) {
        return LeaderCardHandler.discardExcessCard(in);
    }

    public static ClientMessage endTurn(String[] in) {
        return GameLobbyHandler.endTurn(in);
    }

    public static ClientMessage discardCard(String[] in) {
        return LeaderCardHandler.discardCard(in);
    }

    public static ClientMessage getFromMarket(String[] in, CLI cli) {
        return MarketHandler.getFromMarket(in, cli);
    }

    public static ClientMessage storeFromSupply(String[] in) {
        return WarehouseHandler.storeFromSupply(in);
    }

    public static ClientMessage storeFromSupplyToExtraDeposit(String[] in) {
        return WarehouseHandler.storeFromSupplyToExtraDeposit(in);
    }

    public static ClientMessage getInitialResources(String[] in) {
        return GameLobbyHandler.getInitialResources(in);
    }

    public static ClientMessage loadGameSettings(String[] in) {
        return GameLobbyHandler.loadGameSettings(in);
    }

    public static ClientMessage changeDeposit(String[] in, CLI cli) {
        return WarehouseHandler.changeDeposit(in, cli);
    }

    public static ClientMessage playLeaderCard(String[] in) {
        return LeaderCardHandler.playLeaderCard(in);
    }

    public static ClientMessage activateDashboardProduction(String[] in, CLI cli) {
        return ProductionHandler.activateDashboardProduction(in, cli);
    }

    public static ClientMessage activateDevelopmentProduction(String[] in, CLI cli) {
        return DevelopmentCardHandler.activateDevelopmentProduction(in, cli);
    }

    public static ClientMessage activateLeaderProduction(String[] in, CLI cli) {
        return LeaderCardHandler.activateLeaderProduction(in, cli);
    }

    public static ClientMessage startGame(String[] in, CLI cli) {
        return GameLobbyHandler.startGame(in, cli);
    }

    public static ClientMessage buyDevelopmentCard(String[] in, CLI cli) {
        return DevelopmentCardHandler.buyDevelopmentCard(in, cli);
    }

}
