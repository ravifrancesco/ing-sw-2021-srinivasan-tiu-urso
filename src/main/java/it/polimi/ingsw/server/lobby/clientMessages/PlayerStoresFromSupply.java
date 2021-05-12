package it.polimi.ingsw.server.lobby.clientMessages;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;

public class PlayerStoresFromSupply implements ClientMessage {
    private int from;
    private int to;

    public PlayerStoresFromSupply(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.storeFromSupply(c.getNickname(), from, to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
