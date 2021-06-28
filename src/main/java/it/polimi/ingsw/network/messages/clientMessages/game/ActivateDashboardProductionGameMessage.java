package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

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
    public void handle(ServerVirtualView c, Controller controller) {
        int output;
        output = controller.activateDashboardProductionPower(c.getNickname(), resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if(output == 0) {
            c.sendSuccessfulMoveMessage("Dashboard production activated successfully!");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output;
        output = controller.activateDashboardProductionPower(offlineClientVirtualView.getNickname(), resourceToPayCost, resourceRequiredOptional, resourceProducedOptional);
        if(output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Dashboard production activated successfully!");
        }
    }
}
