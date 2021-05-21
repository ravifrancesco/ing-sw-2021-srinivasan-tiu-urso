package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class ClientInputParser {

    public static ClientMessage parseInput(String input) {

        String[] in = input.split(" ");
        String command = in[0].toUpperCase();

        return switch (command) {
            case "CREATE_GAME" -> InputChecker.createGame(in);
            default -> null;
        };
    }

}
