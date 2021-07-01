package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class PlayLeaderCardGameMessage extends ClientGameMessage implements Serializable {

    /**
     * Message to activate the play leader card move from the controller.
     * See controller doc for more details about arguments.
     */

    int cardToPlay;

    public PlayLeaderCardGameMessage(int cardToPlay) {
        this.cardToPlay = cardToPlay;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output;
        output = controller.playLeaderCard(c.getNickname(), cardToPlay);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Card with index " + cardToPlay + " has been placed correctly!");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output;
        output = controller.playLeaderCard(offlineClientVirtualView.getNickname(), cardToPlay);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Card with index " + cardToPlay + " has been placed correctly!");
        }
    }
}
