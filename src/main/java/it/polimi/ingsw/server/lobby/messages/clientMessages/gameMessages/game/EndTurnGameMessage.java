package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.client.SinglePlayerView;
import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class EndTurnGameMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(Connection c, ServerController serverController) {
        int numberOfPlayers = serverController.getNumberOfPlayers();
        int output = numberOfPlayers == 1 ? serverController.endTurnSinglePlayer(c.getNickname()) : serverController.endTurn(c.getNickname());
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Turn ended correctly");
            ((GameLobby) c.getCurrentLobby()).getConnectedPlayers().forEach((nickname, connection) -> connection.sendCLIupdateMessage("after_end_turn"));
        }
        if (output == 1) {
            GameLobby gl = (GameLobby) c.getCurrentLobby();
            gl.getConnectedPlayers().forEach((nick, player) -> {
                player.enterLobby(player.getMainLobby());
                player.sendCLIupdateMessage("back_in_lobby");
            });
            c.getMainLobby().removeGameLobby(gl);

        }
    }

    @Override
    public void handleLocally(SinglePlayerView singlePlayerView, ServerController serverController) {
        int output = serverController.endTurnSinglePlayer(singlePlayerView.getNickname());
        if (output == 0) {
            singlePlayerView.printSuccessfulMove("Turn ended correctly");
            singlePlayerView.getUi().handleMenuCode("after_end_turn");
        }
        if (output == 1) {
            singlePlayerView.getUi().handleMenuCode("back_in_lobby");
            singlePlayerView.handleGameEnded();
        }
    }
}
