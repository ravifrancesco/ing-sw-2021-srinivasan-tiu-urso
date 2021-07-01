package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class PlayerDiscardsLeaderCard extends ClientGameMessage implements Serializable {

    /**
     * Message to activate the discard leader card move from the controller.
     * See controller doc for more details about arguments.
     */


    private final int cardToDiscard;

    public PlayerDiscardsLeaderCard(int cardToDiscard) {
        this.cardToDiscard = cardToDiscard;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.discardLeaderCard(c.getNickname(), cardToDiscard);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Card with index " + cardToDiscard + " has been discarded successfully");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.discardLeaderCard(offlineClientVirtualView.getNickname(), cardToDiscard);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Card with index " + cardToDiscard + " has been discarded successfully");
        }
    }
}
