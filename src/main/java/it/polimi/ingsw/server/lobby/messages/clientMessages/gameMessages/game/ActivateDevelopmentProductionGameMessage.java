package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;
import java.util.Map;

public class ActivateDevelopmentProductionGameMessage extends ClientGameMessage implements Serializable {

    int cardToActivate;
    ResourceContainer resourceToPayCost;
    Map<Resource, Integer> resourceRequiredOptional;
    Map<Resource, Integer> resourceProducedOptional;

    public ActivateDevelopmentProductionGameMessage(int cardToActivate, ResourceContainer resourceToPayCost, Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {
        this.cardToActivate = cardToActivate;
        this.resourceToPayCost = resourceToPayCost;
        this.resourceRequiredOptional = resourceRequiredOptional;
        this.resourceProducedOptional = resourceProducedOptional;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output;
        output = serverController.activateDevelopmentCardProductionPower(c.getNickname(), cardToActivate, resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Development card production activated successfully");
        }
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        int output;
        output = serverController.activateDevelopmentCardProductionPower(singlePlayerView.getNickname(), cardToActivate, resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if (output == 0) {
            singlePlayerView.printSuccessfulMove("Development card production activated successfully");
        }
    }
}
