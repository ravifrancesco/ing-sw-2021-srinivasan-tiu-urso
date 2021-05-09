package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.ServerController;

public class StartGameMessage implements ClientMessage {

    @Override
    public void handle(Connection c, ServerController serverController) {
        try {
            serverController.startGame();
        } catch (Exception e) {
            // TODO
        }
    }
}
