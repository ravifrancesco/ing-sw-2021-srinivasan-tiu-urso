package it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;

import java.io.Serializable;

public class EndTurnGameMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(Connection c, ServerController serverController) {
        String menuCode;
        int numberOfPlayers = serverController.getNumberOfPlayers();
        int output = numberOfPlayers == 1 ? serverController.endTurnSinglePlayer(c.getNickname()) : serverController.endTurn(c.getNickname());
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Turn ended correctly");
            menuCode = numberOfPlayers == 1 ? "after_end_turn_single" : "after_end_turn";
            ((GameLobby) c.getCurrentLobby()).getConnectedPlayers().forEach((nickname, connection) -> connection.sendCLIupdateMessage(menuCode));
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
}
