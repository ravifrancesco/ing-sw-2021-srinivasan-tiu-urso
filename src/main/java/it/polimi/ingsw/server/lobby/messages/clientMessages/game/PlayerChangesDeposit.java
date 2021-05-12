package it.polimi.ingsw.server.lobby.messages.clientMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class PlayerChangesDeposit implements ClientMessage {
    Resource[] deposit;

    public PlayerChangesDeposit(Resource[] deposit) {
        this.deposit = deposit;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.changeDeposit(c.getNickname(), deposit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
