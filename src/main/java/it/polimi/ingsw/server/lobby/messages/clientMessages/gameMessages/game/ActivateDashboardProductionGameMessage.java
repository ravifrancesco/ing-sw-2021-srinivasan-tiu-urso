package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;
import java.util.Map;

public class ActivateDashboardProductionGameMessage extends ClientGameMessage implements Serializable {

    ResourceContainer resourceToPayCost;
    Map<Resource, Integer> resourceRequiredOptional;
    Map<Resource, Integer> resourceProducedOptional;

    public ActivateDashboardProductionGameMessage(ResourceContainer resourceToPayCost, Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {
        this.resourceToPayCost = resourceToPayCost;
        this.resourceRequiredOptional = resourceRequiredOptional;
        this.resourceProducedOptional = resourceProducedOptional;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output;
        output = serverController.activateDashboardProductionPower(c.getNickname(), resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if(output == 0) {
            c.sendSuccessfulMoveMessage("Dashboard production activated successfully!");
        }
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        int output;
        output = serverController.activateDashboardProductionPower(singlePlayerView.getNickname(), resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if(output == 0) {
            singlePlayerView.printSuccessfulMove("Dashboard production activated successfully!");
        }
    }
}
