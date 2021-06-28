package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;


public class PlayerJoinsGame extends ClientGameMessage implements Serializable {
    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        try {
            controller.joinGame(c.getNickname());
        } catch (Exception e) {
            c.sendFailedMoveMessage("Failed connectiong to the lobby: " + e.getMessage());
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        try {
            controller.joinGame(offlineClientVirtualView.getNickname());
        } catch (Exception e) {
            offlineClientVirtualView.printSuccessfulMove("Failed connectiong to the lobby: " + e.getMessage());
        }
    }
}
