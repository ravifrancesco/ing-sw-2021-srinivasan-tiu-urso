package it.polimi.ingsw.view.UI.CLI.IO;

import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.InputHandling.InputHandler;
import it.polimi.ingsw.view.UI.CLI.IO.InputHandling.ViewHandler;

public class ClientMessageInputParser {

    /**
     * Handles every possible (and correct) input for a command in the CLI
     *
     * @param input the input
     * @param cli   the cli where the command has to be handled
     * @return a client message
     */
    public static ClientMessage parseInput(String input, CLI cli) {

        String[] in = input.split(" ");
        String command = in[0].toUpperCase();

        return switch (command) {
            case "CREATEGAME" -> InputHandler.createGame(in); // tested
            case "SHOWGAMES" -> InputHandler.showGames(); // tested
            case "JOINGAME" -> InputHandler.joinGame(in, cli); // tested
            case "LEAVELOBBY" -> InputHandler.leaveLobby(cli); // tested
            case "STARTGAME" -> InputHandler.startGame();
            case "QUIT" -> InputHandler.quit(); // TODO fix

            case "SHOW" -> ViewHandler.show(in, cli); // tested

            case "DISCARDEXCESSCARD" -> InputHandler.discardExcessCard(in, cli); // tested ---
            case "GETINITIALRESOURCES" -> InputHandler.getInitialResources(in, cli); // tested

            case "GETFROMMARKET" -> InputHandler.getFromMarket(in, cli); // TODO fix WMRS ----
            case "BUYDEVELOPMENTCARD" -> InputHandler.buyDevelopmentCard(in, cli); // tested ---
            case "ACTIVATEDASHBOARDPRODUCTION" -> InputHandler.activateDashboardProduction(cli); // tested
            case "ACTIVATEDEVELOPMENTPRODUCTION" -> InputHandler.activateDevelopmentProduction(in, cli); // tested
            case "ACTIVATELEADERPRODUCTION" -> InputHandler.activateLeaderProduction(in, cli); // tested


            case "DISCARDCARD" -> InputHandler.discardCard(in, cli); // -----
            case "PLAYLEADERCARD" -> InputHandler.playLeaderCard(in, cli); // tested

            case "STOREFROMSUPPLY" -> InputHandler.storeFromSupply(in, cli); // tested
            case "STOREFROMSUPPLYED" -> InputHandler.storeFromSupplyToExtraDeposit(in, cli); // TODO controllare se ho sistemato gli indici
            case "CHANGEDEPOSIT" -> InputHandler.changeDeposit(cli); // tested

            case "LOADGAMESETTINGS" -> InputHandler.loadGameSettings(in); // TODO test

            case "ENDTURN" -> InputHandler.endTurn(); // tested -// ---
            case "HELP" -> InputHandler.sendHelp(cli);

            case "HACK" -> InputHandler.hack(); // used to give pretty much unlimited resources, testing purposes
            case "GIMME" -> InputHandler.gimme(in); // gives a developmentcard without paying, testing purposes
            case "PLAY" -> InputHandler.play(in); // plays a developmentcard without paying, testing purposes
            case "END" -> InputHandler.end();
            default -> {
                cli.printErrorMessage("Invalid Command");
                yield null;
            }
        };

    }
}
