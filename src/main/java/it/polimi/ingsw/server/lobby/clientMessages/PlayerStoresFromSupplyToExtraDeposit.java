package it.polimi.ingsw.server.lobby.clientMessages;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;

public class PlayerStoresFromSupplyToExtraDeposit implements ClientMessage {
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
