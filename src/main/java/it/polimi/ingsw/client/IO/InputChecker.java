package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.CreateGameLobby;

public class InputChecker {

    public static ClientMessage createGame(String[] input) {
        try {
            int numberOfPlayers = Integer.parseInt(input[1]);
            return new CreateGameLobby(numberOfPlayers);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

}
