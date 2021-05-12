package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.model.specialAbilities.WhiteMarbleResource;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.util.ArrayList;

public class PlayerGetsFromMarket extends ClientGameMessage {
    private int move;
    private ArrayList<WhiteMarbleResource> wmrs;

    public PlayerGetsFromMarket(int move, ArrayList<WhiteMarbleResource> wmrs) {
        this.move = move;
        this.wmrs = wmrs;
    }

    @Override
    public void handle(Connection c, ServerController serverController)  {
        try {
            serverController.getFromMarket(c.getNickname(), move, wmrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}