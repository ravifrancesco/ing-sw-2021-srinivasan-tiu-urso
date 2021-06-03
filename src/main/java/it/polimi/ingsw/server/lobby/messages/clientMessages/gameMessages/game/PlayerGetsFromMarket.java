package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerGetsFromMarket extends ClientGameMessage implements Serializable {
    private int move;
    private ArrayList<Resource> wmrs;

    public PlayerGetsFromMarket(int move, ArrayList<Resource> wmrs) {
        this.move = move;
        this.wmrs = wmrs;
    }

    @Override
    public void handle(Connection c, ServerController serverController)  {
        int output = serverController.getFromMarket(c.getNickname(), move, wmrs);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Market move completed, adding resources to your supply...");
        }
    }
}