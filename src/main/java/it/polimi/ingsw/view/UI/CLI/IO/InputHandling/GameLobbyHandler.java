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
    public static ClientMessage leaveLobby(String[] in, CLI cli) {
        cli.printMessage("Leaving lobby...");
        return new LeaveLobby();
    }

    public static ClientMessage startGame(String[] in, CLI cli) {
        return new StartGameGameMessage();
    }

    public static ClientMessage endTurn(String[] in) {
        return new EndTurnGameMessage();
    }

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

    public static ClientMessage loadGameSettings(String[] in) {
        String path = in[1];
        // is this the correct usage?
        GameSettings gameSettings = new GameSettings(path);
        gameSettings.loadSettings(path);
        return new LoadGameSettingsGameMessage(gameSettings);
    }
}
