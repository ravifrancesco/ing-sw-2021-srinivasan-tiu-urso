package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.AskGameLobbies;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.CreateGameLobby;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.JoinGameLobby;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.QuitGame;

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
