package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public abstract class ClientGameMessage extends ClientMessage  {

    public abstract void handle(Connection connection, ServerController serverController);

    public abstract void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController);

}
