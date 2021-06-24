package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.*;

public class MainLobbyHandler {
    public static ClientMessage createGame(String[] input) {
        try {
            int numberOfPlayers = Integer.parseInt(input[1]);
            return new CreateGameLobby(numberOfPlayers);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static ClientMessage showGames() {
        return new AskGameLobbies();
    }

    public static ClientMessage joinGame(String[] input, CLI cli) {
        try {
            String id = input[1];
            return new JoinGameLobby(id);
        } catch (Exception e) {
            cli.printErrorMessage("Something went wrong while joining a game");
            return null;
        }
    }
    public static ClientMessage quit(String[] in) {
        return new QuitGame();
    }
}
