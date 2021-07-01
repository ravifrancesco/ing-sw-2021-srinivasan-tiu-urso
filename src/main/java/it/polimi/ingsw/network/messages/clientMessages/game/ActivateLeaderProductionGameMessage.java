package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;
import java.util.Map;

public class ActivateLeaderProductionGameMessage extends ClientGameMessage implements Serializable {

    /**
     * Message to activate a leader card production move in the controller.
     * See controller doc for more details about arguments.
     */
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
    public void handle(ServerVirtualView c, Controller controller) {
        int output;
        output = controller.activateLeaderCardProductionPower(c.getNickname(), cardToActivate, resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Leader card production activated successfully!");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output;
        output = controller.activateLeaderCardProductionPower(offlineClientVirtualView.getNickname(), cardToActivate, resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Leader card production activated successfully!");
        }
    }
}
