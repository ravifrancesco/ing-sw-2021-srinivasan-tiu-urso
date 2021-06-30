package it.polimi.ingsw.network.messages.clientMessages.lobbyMessage;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.messages.clientMessages.game.ClientGameMessage;
import it.polimi.ingsw.network.server.lobby.GameLobby;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class LeaveLobby extends ClientGameMessage implements Serializable {
    @Override
    public void handle(ServerVirtualView serverVirtualView, Controller controller) {
        GameLobby currentGameLobby = (GameLobby) serverVirtualView.getCurrentLobby();
        // leave current game lobby
        currentGameLobby.leaveLobby(serverVirtualView);

        // if game lobby is now empty, remove it
        if (currentGameLobby.getConnectedPlayers().size() == 0) {
            serverVirtualView.getMainLobby().removeGameLobby(currentGameLobby);
        }

        // go back to main lobby
        serverVirtualView.enterLobby(serverVirtualView.getMainLobby());
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        // TODO
    }
}
