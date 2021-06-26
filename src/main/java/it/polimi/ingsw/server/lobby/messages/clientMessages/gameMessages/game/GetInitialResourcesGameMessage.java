package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class GetInitialResourcesGameMessage extends ClientGameMessage implements Serializable {

    Resource resource;
    int position;

    public GetInitialResourcesGameMessage(Resource resource, int position) {
        this.resource = resource;
        this.position = position;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output = serverController.getInitialResources(c.getNickname(), resource, position);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Initial resources obtained successfully, adding "
                    + resource + " to your deposition on position " + position);
            c.sendCLIupdateMessage("after_initial_resources");
        }
    }
}
