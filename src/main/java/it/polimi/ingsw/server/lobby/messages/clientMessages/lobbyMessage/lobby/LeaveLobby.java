package it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class LeaveLobby extends ClientGameMessage implements Serializable {
    @Override
    public void handle(Connection connection, ServerController serverController) {
        GameLobby currentGameLobby = (GameLobby) connection.getCurrentLobby();
        // leave current game lobby
        currentGameLobby.leaveLobby(connection);

        // if game lobby is now empty, remove it
        if(currentGameLobby.getConnectedPlayers().size() == 0) {
            connection.getMainLobby().removeGameLobby(currentGameLobby);
        }

        // go back to main lobby
        connection.enterLobby(connection.getMainLobby());
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        // TODO
    }
}
