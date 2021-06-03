package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class GimmeMessage extends ClientGameMessage implements Serializable {
    private int row;
    private int column;
    private int index;

    public GimmeMessage(int row, int column, int index) {
        this.row = row;
        this.column = column;
        this.index = index;
    }




    @Override
    public void handle(Connection connection, ServerController serverController) {
        serverController.gimme(connection.getNickname(), row, column, index);
    }
}
