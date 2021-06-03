package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class PlayerChangesDeposit extends ClientGameMessage implements Serializable {
    Resource[] deposit;

    public PlayerChangesDeposit(Resource[] deposit) {
        this.deposit = deposit;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output = serverController.changeDeposit(c.getNickname(), deposit);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Deposit swaps successful");
        }
    }
}
