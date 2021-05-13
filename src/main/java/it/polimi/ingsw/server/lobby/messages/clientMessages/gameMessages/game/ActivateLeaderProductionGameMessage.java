package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.util.Map;

public class ActivateLeaderProductionGameMessage extends ClientGameMessage {

    int cardToActivate;
    ResourceContainer resourceToPayCost;
    Map<Resource, Integer> resourceRequiredOptional;
    Map<Resource, Integer> resourceProducedOptional;

    public ActivateLeaderProductionGameMessage(int cardToActivate, ResourceContainer resourceToPayCost, Map<Resource, Integer> resourceRequiredOptional, Map<Resource, Integer> resourceProducedOptional) {
        this.cardToActivate = cardToActivate;
        this.resourceToPayCost = resourceToPayCost;
        this.resourceRequiredOptional = resourceRequiredOptional;
        this.resourceProducedOptional = resourceProducedOptional;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.activateLeaderCardProductionPower(c.getNickname(), cardToActivate, resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        } catch (Exception e) {
            // TODO
        }
    }
}