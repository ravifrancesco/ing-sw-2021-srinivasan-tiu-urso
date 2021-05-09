package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ServerController;

public interface ClientMessage {

    void handle(Connection connection, ServerController serverController);
}
