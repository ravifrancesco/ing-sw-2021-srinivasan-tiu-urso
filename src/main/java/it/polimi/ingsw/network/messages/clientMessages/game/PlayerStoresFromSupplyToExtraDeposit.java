package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class PlayerStoresFromSupplyToExtraDeposit extends ClientGameMessage implements Serializable {
    private int leaderCardPos;
    private int from;
    private int to;

    public PlayerStoresFromSupplyToExtraDeposit(int leaderCardPos, int from, int to) {
        this.leaderCardPos = leaderCardPos;
        this.from = from;
        this.to = to;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.storeFromSupplyInExtraDeposit(c.getNickname(), leaderCardPos, from, to);
        if(output == 0) {
            c.sendSuccessfulMoveMessage("Successfull storage to extra deposit, adding resource to leader card " +
                    leaderCardPos + " on position " + to);
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.storeFromSupplyInExtraDeposit(offlineClientVirtualView.getNickname(), leaderCardPos, from, to);
        if(output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Successfull storage to extra deposit, adding resource to leader card " +
                    leaderCardPos + " on position " + to);
        }
    }
}
