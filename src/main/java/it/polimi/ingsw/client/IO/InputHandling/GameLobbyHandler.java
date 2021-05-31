package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.EndTurnGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.GetInitialResourcesGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.LoadGameSettingsGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.StartGameGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.LeaveLobby;

public class GameLobbyHandler {
    public static ClientMessage leaveLobby(String[] in, CLI cli) {
        cli.printMessage("Leaving lobby...");
        return new LeaveLobby();
    }

    public static ClientMessage startGame(String[] in, CLI cli) {
        cli.printMessage("Game is starting...");
        return new StartGameGameMessage();
    }

    public static ClientMessage endTurn(String[] in) {
        return new EndTurnGameMessage();
    }

    public static ClientMessage getInitialResources(String[] in) {
        try {
            Resource initialResource = ResourceParser.parse(in[1]);
            if(initialResource == null) { throw new IllegalArgumentException(); }
            int pos = Integer.parseInt(in[2]);
            return new GetInitialResourcesGameMessage(initialResource, pos);
        } catch (Exception e) {
            return null;
        }
    }

    public static ClientMessage loadGameSettings(String[] in) {
        String path = in[1];
        // is this the correct usage?
        GameSettings gameSettings = new GameSettings(path);
        gameSettings.loadSettings(path);
        return new LoadGameSettingsGameMessage(gameSettings);
    }
}
