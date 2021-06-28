package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

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
    public void handle(ServerVirtualView c, Controller controller) {
        int output;
        output = controller.activateDevelopmentCardProductionPower(c.getNickname(), cardToActivate, resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Development card production activated successfully");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output;
        output = controller.activateDevelopmentCardProductionPower(offlineClientVirtualView.getNickname(), cardToActivate, resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Development card production activated successfully");
        }
    }
}
