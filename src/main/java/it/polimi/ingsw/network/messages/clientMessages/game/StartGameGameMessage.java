package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.server.lobby.GameLobby;

import java.io.Serializable;

public class StartGameGameMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.startGame(c.getNickname());
        if(output == 0) {
            ((GameLobby) c.getCurrentLobby()).getConnectedPlayers().forEach((name, connection) -> connection.sendCLIupdateMessage("after_game_start"));
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.startGame(offlineClientVirtualView.getNickname());
        if(output == 0) {
            offlineClientVirtualView.getUi().handleMenuCode("after_game_start");
        }
    }
}
