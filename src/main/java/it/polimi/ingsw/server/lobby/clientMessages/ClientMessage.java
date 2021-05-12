package it.polimi.ingsw.server.lobby.clientMessages;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;

public interface ClientMessage {

    void handle(Connection connection, ServerController serverController);

}
