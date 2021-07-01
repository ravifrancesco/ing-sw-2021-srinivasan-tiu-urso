package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.lobby.GameLobby;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class EndTurnGameMessage extends ClientGameMessage implements Serializable {

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        String menuCode;
        int numberOfPlayers = controller.getNumberOfPlayers();
        int output = numberOfPlayers == 1 ? controller.endTurnSinglePlayer(c.getNickname()) : controller.endTurn(c.getNickname());
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Turn ended correctly");
            menuCode = numberOfPlayers == 1 ? "after_end_turn_single" : "after_end_turn";
            ((GameLobby) c.getCurrentLobby()).getConnectedPlayers().forEach((nickname, connection) -> connection.sendCLIupdateMessage(menuCode));
        }
        if (output == 1) {
            GameLobby gl = (GameLobby) c.getCurrentLobby();
            gl.getConnectedPlayers().forEach((nick, player) -> {
                player.sendCLIupdateMessage("force_disconnection");
            });
            c.getMainLobby().removeGameLobby(gl);
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.endTurnSinglePlayer(offlineClientVirtualView.getNickname());
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Turn ended correctly");
            offlineClientVirtualView.getUi().handleMenuCode("after_end_turn_single");
        }
        if (output == 1) {
            offlineClientVirtualView.getUi().handleMenuCode("force_disconnection");
            offlineClientVirtualView.handleGameEnded();
        }
    }
}
