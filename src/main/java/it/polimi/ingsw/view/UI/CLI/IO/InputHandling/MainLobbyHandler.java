package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.AskGameLobbies;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.CreateGameLobby;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.JoinGameLobby;

public class MainLobbyHandler {
    /**
     * Handles the creation for the message to create a game
     * @param input the string to parse with the needed arguments
     * @return the client message with the move
     */
    public static ClientMessage createGame(String[] input) {
        try {
            int numberOfPlayers = Integer.parseInt(input[1]);
            return new CreateGameLobby(numberOfPlayers);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Handles the creation for the message to show available games
     * @return the client message with the move
     */
    public static ClientMessage showGames() {
        return new AskGameLobbies();
    }

    /**
     * Handles the creation for the message to join a game
     * @param input the string to parse with the needed arguments
     * @param cli the CLI
     * @return the client message with the move
     */
    public static ClientMessage joinGame(String[] input, CLI cli) {
        try {
            String id = input[1];
            return new JoinGameLobby(id);
        } catch (Exception e) {
            cli.printErrorMessage("Something went wrong while joining a game");
            return null;
        }
    }
}
