package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class BuyDevelopmentCardGameMessage extends ClientGameMessage implements Serializable {

    private int row;
    private int column;
    private ResourceContainer resourceToPayCost;
    private int position;

    public BuyDevelopmentCardGameMessage(int row, int column, ResourceContainer resourceToPayCost, int position) {
        this.row = row;
        this.column = column;
        this.resourceToPayCost = resourceToPayCost;
        this.position = position;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output = serverController.buyDevelopmentCard(c.getNickname(), row, column, resourceToPayCost, position);
        if(output == 0) {
            c.sendSuccessfulMoveMessage("Development card (" + row + "," + column + ")" +
                    "has been fsuccesfully bought and placed in position " + position);
        }
    }
}
