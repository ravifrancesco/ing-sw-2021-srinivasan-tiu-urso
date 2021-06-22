package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class EndTurnGameMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output = serverController.endTurn(c.getNickname());
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Turn ended correctly");
            ((GameLobby) c.getCurrentLobby()).getConnectedPlayers().forEach((nickname, connection) -> {
                connection.sendCLIupdateMessage("after_end_turn");
            });
        }
    }
}
