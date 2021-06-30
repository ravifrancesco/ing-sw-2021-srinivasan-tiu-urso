package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.EndTurnGameMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.GetInitialResourcesGameMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.LoadGameSettingsGameMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.StartGameGameMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.LeaveLobby;

public class GameLobbyHandler {
    /**
     * Generates the message to leave the lobby
     * @param cli the cli
     * @return the client message to leave the lobby
     */
    public static ClientMessage leaveLobby(CLI cli) {
        cli.printMessage("Leaving lobby...");
        return new LeaveLobby();
    }

    /**
     * Generates the message to start the game
     * @return the client message to start the game
     */
    public static ClientMessage startGame() {
        return new StartGameGameMessage();
    }

    /**
     * Generates the message to end the turn
     * @return the client message to end the turn
     */
    public static ClientMessage endTurn() {
        return new EndTurnGameMessage();
    }

    /**
     * Generates the message to get initial resources
     * @param in the string to parse with the index
     * @param cli the cli
     * @return the client message to get initial resources
     */
    public static ClientMessage getInitialResources(String[] in, CLI cli) {
        try {
            Resource initialResource = ResourceParser.parse(in[1]);
            if(initialResource == null) { throw new IllegalArgumentException(); }
            int pos = Integer.parseInt(in[2]);
            return new GetInitialResourcesGameMessage(initialResource, pos);
        } catch (Exception e) {
            cli.printErrorMessage("Failed parsing resource or position");
            return null;
        }
    }

    /**
     * Generates the message to load game settings
     * @param in the string to parse with the path for the game settings
     * @return the client message
     */
    public static ClientMessage loadGameSettings(String[] in) {
        String path = in[1];
        GameSettings gameSettings = new GameSettings(path);
        gameSettings.loadSettings(path);
        return new LoadGameSettingsGameMessage(gameSettings);
    }
}
