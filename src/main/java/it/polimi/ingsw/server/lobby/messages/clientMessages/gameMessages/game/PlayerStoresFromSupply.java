package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class PlayerStoresFromSupply extends ClientGameMessage implements Serializable {
    private int from;
    private int to;

    public PlayerStoresFromSupply(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output = serverController.storeFromSupply(c.getNickname(), from, to);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Store from supply successfull, added resource to deposit index " + to);
            c.sendCLIupdateMessage("after_store_supply");
        }
    }
}
