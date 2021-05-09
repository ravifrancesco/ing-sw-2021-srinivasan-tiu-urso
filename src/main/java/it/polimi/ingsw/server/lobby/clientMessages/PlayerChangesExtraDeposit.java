package it.polimi.ingsw.server.lobby.clientMessages;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.Connection;

public class PlayerChangesExtraDeposit implements ClientMessage {
    private Resource[] deposit;
    private Resource[] extraDeposit;
    private int lcIndex;

    public PlayerChangesExtraDeposit(Resource[] deposit, Resource[] extraDeposit, int lcIndex) {
        this.deposit = deposit;
        this.extraDeposit = extraDeposit;
        this.lcIndex = lcIndex;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.changeDepositExtraDeposit(c.getNickname(), deposit, extraDeposit, lcIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
