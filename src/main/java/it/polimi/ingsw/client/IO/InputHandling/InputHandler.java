package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.EndMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.GimmeMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.UnlimitedResourcesMessage;

public class InputHandler {

    public static ClientMessage createGame(String[] in) { return MainLobbyHandler.createGame(in); }

    public static ClientMessage showGames() { return MainLobbyHandler.showGames(); }

    public static ClientMessage joinGame(String[] in, CLI cli) {
        return MainLobbyHandler.joinGame(in, cli);
    }

    public static ClientMessage leaveLobby(String[] in, CLI cli) {
        return GameLobbyHandler.leaveLobby(in, cli);
    }

    public static ClientMessage quit(String[] in) {
        return MainLobbyHandler.quit(in);
    }

    public static ClientMessage discardExcessCard(String[] in, CLI cli) {
        return LeaderCardHandler.discardExcessCard(in, cli);
    }

    public static ClientMessage hack() {
        return new UnlimitedResourcesMessage();
    }

    public static ClientMessage endTurn(String[] in) {
        return GameLobbyHandler.endTurn(in);
    }

    public static ClientMessage discardCard(String[] in, CLI cli) {
        return LeaderCardHandler.discardCard(in, cli);
    }

    public static ClientMessage getFromMarket(String[] in, CLI cli) {
        return MarketHandler.getFromMarket(in, cli);
    }

    public static ClientMessage storeFromSupply(String[] in, CLI cli) {
        return WarehouseHandler.storeFromSupply(in, cli);
    }

    public static ClientMessage storeFromSupplyToExtraDeposit(String[] in, CLI cli) { return WarehouseHandler.storeFromSupplyToExtraDeposit(in, cli); }

    public static ClientMessage getInitialResources(String[] in, CLI cli) {
        return GameLobbyHandler.getInitialResources(in, cli);
    }

    public static ClientMessage loadGameSettings(String[] in) {
        return GameLobbyHandler.loadGameSettings(in);
    }

    public static ClientMessage changeDeposit(String[] in, CLI cli) {
        return WarehouseHandler.changeDeposit(in, cli);
    }

    public static ClientMessage playLeaderCard(String[] in, CLI cli) {
        return LeaderCardHandler.playLeaderCard(in, cli);
    }

    public static ClientMessage activateDashboardProduction(String[] in, CLI cli) { return ProductionHandler.activateDashboardProduction(in, cli); }

    public static ClientMessage activateDevelopmentProduction(String[] in, CLI cli) { return DevelopmentCardHandler.activateDevelopmentProduction(in, cli); }

    public static ClientMessage activateLeaderProduction(String[] in, CLI cli) { return LeaderCardHandler.activateLeaderProduction(in, cli); }

    public static ClientMessage startGame(String[] in, CLI cli) { return GameLobbyHandler.startGame(in, cli); }

    public static ClientMessage buyDevelopmentCard(String[] in, CLI cli) { return DevelopmentCardHandler.buyDevelopmentCard(in, cli); }

    public static ClientMessage gimme(String[] in) {
        try {
            int row = Integer.parseInt(in[1]);
            int column = Integer.parseInt(in[2]);
            int index = Integer.parseInt(in[3]);
            return new GimmeMessage(row, column, index);

        } catch (Exception e) {
            return null;
        }
    }

    public static ClientMessage play(String[] in) {
        try {
            int index = Integer.parseInt(in[1]);
            return new PlayMessage(index);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static ClientMessage sendHelp(CLI cli) {
        cli.showGeneralMenu();
        return null;
    }

    public static ClientMessage end(String[] in) {
        return new EndMessage();
    }
}
