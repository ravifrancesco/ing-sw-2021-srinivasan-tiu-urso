package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

public class GetInitialResourcesGameMessage extends ClientGameMessage {

    Resource resource;
    int position;

    public GetInitialResourcesGameMessage(Resource resource, int position) {
        this.resource = resource;
        this.position = position;
    }

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.getInitialResources(c.getNickname(), resource, position);
        } catch (Exception e) {
            // TODO
        }
    }
}
