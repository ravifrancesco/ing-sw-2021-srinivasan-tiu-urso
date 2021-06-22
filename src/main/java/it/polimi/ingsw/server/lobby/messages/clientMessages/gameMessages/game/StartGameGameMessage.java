package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class StartGameGameMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output = serverController.startGame(c.getNickname());
        if(output == 0) {
            // ((GameLobby) c.getCurrentLobby()).getConnectedPlayers().forEach((name, connection) -> connection.sendSuccessfulMoveMessage("Game is starting"));
            ((GameLobby) c.getCurrentLobby()).getConnectedPlayers().forEach((name, connection) -> connection.sendCLIupdateMessage("after_game_start"));
        }
    }
}
