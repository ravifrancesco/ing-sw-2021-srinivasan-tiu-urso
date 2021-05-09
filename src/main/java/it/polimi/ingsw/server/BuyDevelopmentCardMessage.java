package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.ResourceContainer;

public class BuyDevelopmentCardMessage implements ClientMessage {

    int row;
    int column;
    ResourceContainer resourceToPayCost;
    int position;

    public BuyDevelopmentCardMessage(int row, int column, ResourceContainer resourceToPayCost, int position) {
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
