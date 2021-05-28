package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.*;

public class InputChecker {

    public static ClientMessage createGame(String[] input) {
        try {
            int numberOfPlayers = Integer.parseInt(input[1]);
            return new CreateGameLobby(numberOfPlayers);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage showGames(String[] input) {
        return new AskGameLobbies();
    }

    public static ClientMessage joinGame(String[] input) {
        try {
            String id = input[1];
            System.out.println("Joining lobby " + id + "...");
            return new JoinGameLobby(id);
        } catch (Exception e) {
            System.out.println("Something went wrong while joining a game");
            return null;
        }
    }

    public static ClientMessage leaveLobby(String[] in) {
        return new LeaveLobby();
    }

    public static ClientMessage quit(String[] in) {
        return new QuitGame();
    }
}
