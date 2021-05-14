package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

public class BuyDevelopmentCardGameMessage extends ClientGameMessage {

    int row;
    int column;
    ResourceContainer resourceToPayCost;
    int position;

    public BuyDevelopmentCardGameMessage(int row, int column, ResourceContainer resourceToPayCost, int position) {
        this.row = row;
        this.column = column;
        this.resourceToPayCost = resourceToPayCost;
        this.position = position;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.buyDevelopmentCard(c.getNickname(), row, column, resourceToPayCost, position);
        } catch (Exception e) {
            // TODO
        }
    }
}
