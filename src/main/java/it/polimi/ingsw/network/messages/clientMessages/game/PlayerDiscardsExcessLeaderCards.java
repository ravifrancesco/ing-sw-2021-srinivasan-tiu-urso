package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;


public class PlayerDiscardsExcessLeaderCards extends ClientGameMessage implements Serializable {
    private int cardToDiscard;

    public PlayerDiscardsExcessLeaderCards(int cardToDiscard) {
        this.cardToDiscard = cardToDiscard;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.discardExcessLeaderCards(c.getNickname(), cardToDiscard);
        if (output == 0) {
            c.sendSuccessfulMoveMessage("Card with index " + cardToDiscard + " has been discarded successfully");
            c.sendCLIupdateMessage("next_card_discard");
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.discardExcessLeaderCards(offlineClientVirtualView.getNickname(), cardToDiscard);
        if (output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Card with index " + cardToDiscard + " has been discarded successfully");
            offlineClientVirtualView.getUi().handleMenuCode("next_card_discard");
        }
    }
}
