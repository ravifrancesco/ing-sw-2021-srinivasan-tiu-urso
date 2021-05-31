package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class PlayerStoresFromSupplyToExtraDeposit extends ClientGameMessage implements Serializable {
    private int leaderCardPos;
    private int from;
    private int to;

    public PlayerStoresFromSupplyToExtraDeposit(int leaderCardPos, int from, int to) {
        this.leaderCardPos = leaderCardPos;
        this.from = from;
        this.to = from;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.storeFromSupplyInExtraDeposit(c.getNickname(), leaderCardPos, from, to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
