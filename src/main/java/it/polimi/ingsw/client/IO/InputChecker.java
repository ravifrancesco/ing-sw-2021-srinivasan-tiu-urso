package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.AskGameLobbies;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.CreateGameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.JoinGameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.LeaveLobby;

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
        try {
            return new AskGameLobbies();
        } catch (Exception e) {
            System.out.println("AskGameLobbies creation failed");
            System.out.println(e.getMessage());
            return null;
        }
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
}
