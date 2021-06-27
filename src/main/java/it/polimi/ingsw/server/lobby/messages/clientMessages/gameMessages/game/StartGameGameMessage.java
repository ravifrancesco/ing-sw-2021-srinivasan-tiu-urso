package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class StartGameGameMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(Connection c, ServerController serverController) {
        int output = serverController.startGame(c.getNickname());
        System.out.println("output is " + output);
        if(output == 0) {
            ((GameLobby) c.getCurrentLobby()).getConnectedPlayers().forEach((name, connection) -> connection.sendCLIupdateMessage("after_game_start"));
        }
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        int output = serverController.startGame(singlePlayerView.getNickname());
        System.out.println("output is " + output);
        if(output == 0) {
            singlePlayerView.getUi().handleMenuCode("after_game_start");
        }
    }
}
