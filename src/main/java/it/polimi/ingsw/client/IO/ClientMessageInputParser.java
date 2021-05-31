package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.IO.InputHandling.InputHandler;
import it.polimi.ingsw.client.IO.InputHandling.ViewHandler;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class ClientMessageInputParser {

    public static ClientMessage parseInput(String input, CLI cli) {

        String[] in = input.split(" ");
        String command = in[0].toUpperCase();

        return switch (command) {
            case "CREATEGAME" -> InputHandler.createGame(in);
            case "SHOWGAMES"-> InputHandler.showGames();
            case "JOINGAME" -> InputHandler.joinGame(in, cli);
            case "LEAVELOBBY" -> InputHandler.leaveLobby(in, cli);
            case "QUIT" -> InputHandler.quit(in); // TODO fix
            case "SHOW" -> ViewHandler.show(in, cli);
            case "DISCARDEXCESSCARD" -> InputHandler.discardExcessCard(in);
            case "BUYDEVELOPMENTCARD" -> InputHandler.buyDevelopmentCard(in, cli);
            case "ENDTURN" -> InputHandler.endTurn(in);
            case "DISCARDCARD" -> InputHandler.discardCard(in);
            case "GETFROMMARKET" -> InputHandler.getFromMarket(in, cli);
            case "STOREFROMSUPPLY" -> InputHandler.storeFromSupply(in);
            case "STOREFROMSUPPLYTOEXTRADEPOSIT" -> InputHandler.storeFromSupplyToExtraDeposit(in);
            case "GETINITIALRESOURCES" -> InputHandler.getInitialResources(in);
            case "LOADGAMESETTINGS" -> InputHandler.loadGameSettings(in);
            case "CHANGEDEPOSIT" -> InputHandler.changeDeposit(in, cli);
            case "PLAYLEADERCARD" -> InputHandler.playLeaderCard(in);
            case "ACTIVATEDASHBOARDPRODUCTION" -> InputHandler.activateDashboardProduction(in, cli);
            case "ACTIVATEDEVELOPMENTPRODUCTION" -> InputHandler.activateDevelopmentProduction(in, cli);
            case "ACTIVATELEADERPRODUCTION" -> InputHandler.activateLeaderProduction(in, cli);
            case "STARTGAME" -> InputHandler.startGame(in, cli);
            default -> {
                cli.printErrorMessage("Invalid Command");
                yield null;
            }
        };

    }
}
