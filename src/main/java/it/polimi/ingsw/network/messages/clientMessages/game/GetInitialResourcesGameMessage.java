package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class GetInitialResourcesGameMessage extends ClientGameMessage implements Serializable {

    /**
     * Message to activate the get initial resources move from the controller.
     * See controller doc for more details about the arguments.
     */

    Resource resource;
    int position;

    public GetInitialResourcesGameMessage(Resource resource, int position) {
        this.resource = resource;
        this.position = position;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.getInitialResources(c.getNickname(), resource, position);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Initial resources obtained successfully, adding "
                    + resource + " to your deposition on position " + position);
            c.sendCLIupdateMessage("after_initial_resources");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.getInitialResources(offlineClientVirtualView.getUi().getNickname(), resource, position);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Initial resources obtained successfully, adding "
                    + resource + " to your deposition on position " + position);
            offlineClientVirtualView.getUi().handleMenuCode("after_initial_resources");
        }
    }


}
