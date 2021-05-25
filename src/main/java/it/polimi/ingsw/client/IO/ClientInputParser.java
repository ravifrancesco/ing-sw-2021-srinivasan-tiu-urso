package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class ClientInputParser {

    public static ClientMessage parseInput(String input) {

        String[] in = input.split(" ");
        String command = in[0].toUpperCase();

        return switch (command) {
            case "CREATEGAME" -> InputChecker.createGame(in);
            case "SHOWGAMES"-> InputChecker.showGames(in);
            case "JOINGAME" -> InputChecker.joinGame(in);
            case "LEAVELOBBY" -> InputChecker.leaveLobby(in);
            case "QUIT" -> InputChecker.quit(in);
            default -> null;
        };
    }

}
