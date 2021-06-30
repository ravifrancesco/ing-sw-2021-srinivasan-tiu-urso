package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.EndMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.GimmeMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.UnlimitedResourcesMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.QuitGame;
import it.polimi.ingsw.view.UI.CLI.CLI;

public class InputHandler {

    /**
     * Every method in this class redirects a call to generated a client message from a string to the correct handler class
     */

    public static ClientMessage createGame(String[] in) {
        return MainLobbyHandler.createGame(in);
    }

    public static ClientMessage showGames() {
        return MainLobbyHandler.showGames();
    }

    public static ClientMessage joinGame(String[] in, CLI cli) {
        return MainLobbyHandler.joinGame(in, cli);
    }

    public static ClientMessage leaveLobby(CLI cli) {
        return GameLobbyHandler.leaveLobby(cli);
    }

    public static ClientMessage quit() {
        System.exit(0);
        return new QuitGame();
    }

    public static ClientMessage discardExcessCard(String[] in, CLI cli) {
        return LeaderCardHandler.discardExcessCard(in, cli);
    }

    public static ClientMessage hack() {
        return new UnlimitedResourcesMessage();
    }

    public static ClientMessage endTurn() {
        return GameLobbyHandler.endTurn();
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

    public static ClientMessage storeFromSupplyToExtraDeposit(String[] in, CLI cli) {
        return WarehouseHandler.storeFromSupplyToExtraDeposit(in, cli);
    }

    public static ClientMessage getInitialResources(String[] in, CLI cli) {
        return GameLobbyHandler.getInitialResources(in, cli);
    }

    public static ClientMessage loadGameSettings(String[] in) {
        return GameLobbyHandler.loadGameSettings(in);
    }

    public static ClientMessage changeDeposit(CLI cli) {
        return WarehouseHandler.changeDeposit(cli);
    }

    public static ClientMessage playLeaderCard(String[] in, CLI cli) {
        return LeaderCardHandler.playLeaderCard(in, cli);
    }

    public static ClientMessage activateDashboardProduction(CLI cli) {
        return ProductionHandler.activateDashboardProduction(cli);
    }

    public static ClientMessage activateDevelopmentProduction(String[] in, CLI cli) {
        return DevelopmentCardHandler.activateDevelopmentProduction(in, cli);
    }

    public static ClientMessage activateLeaderProduction(String[] in, CLI cli) {
        return LeaderCardHandler.activateLeaderProduction(in, cli);
    }

    public static ClientMessage startGame() {
        return GameLobbyHandler.startGame();
    }

    public static ClientMessage buyDevelopmentCard(String[] in, CLI cli) {
        return DevelopmentCardHandler.buyDevelopmentCard(in, cli);
    }

    /**
     * Shows the menu with all the possible commands
     *
     * @param cli
     * @return
     */
    public static ClientMessage sendHelp(CLI cli) {
        cli.showGeneralMenu();
        return null;
    }

    /**
     * Hacked commands to simplify testing
     *
     * @param in
     * @return
     */
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
        } catch (Exception e) {
            return null;
        }
    }

    public static ClientMessage end() {
        return new EndMessage();
    }


}
