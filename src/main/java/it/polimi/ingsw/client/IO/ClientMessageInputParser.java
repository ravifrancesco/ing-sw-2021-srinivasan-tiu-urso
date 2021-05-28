package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class ClientMessageInputParser {

    public static ClientMessage parseInput(String input, CLI cli) {

        String[] in = input.split(" ");
        String command = in[0].toUpperCase();

        return switch (command) {
            case "CREATEGAME" -> InputChecker.createGame(in);
            case "SHOWGAMES"-> InputChecker.showGames(in);
            case "JOINGAME" -> InputChecker.joinGame(in);
            case "LEAVELOBBY" -> InputChecker.leaveLobby(in);
            case "QUIT" -> InputChecker.quit(in);
            case "SHOW" -> parseShowCommand(in, cli);
            default -> {
                cli.printErrorMessage("Invalid Command");
                yield null;
            }

        };

    }

    private static ClientMessage parseShowCommand(String[] input, CLI cli) {

        String objectToShow = input[1].toUpperCase();
        String nickname = input.length == 3 ? input[2] : null;

        switch(objectToShow) {
            // global
            case "DVGRID" -> cli.showDVGrid();
            case "GAMEBOARD" -> cli.showGameBoard();
            case "MARKET" -> cli.showMarket();
            // player
            case "HAND" -> cli.showHand(nickname);
            case "DASHBOARD" -> cli.showDashboard(nickname);
            case "FAITHTRACK" -> cli.showFaithTrack(nickname);
            case "WAREHOUSE" -> cli.showWarehouse(nickname);
            default -> System.out.println("Invalid command");
        }

        return null;

    }

}
