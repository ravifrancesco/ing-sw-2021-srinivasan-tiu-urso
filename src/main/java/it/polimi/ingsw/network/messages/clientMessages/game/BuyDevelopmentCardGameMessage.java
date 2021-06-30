package it.polimi.ingsw.network.messages.clientMessages.game;

import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.Serializable;

public class BuyDevelopmentCardGameMessage extends ClientGameMessage implements Serializable {

    private final int row;
    private final int column;
    private final ResourceContainer resourceToPayCost;
    private final int position;

    public BuyDevelopmentCardGameMessage(int row, int column, ResourceContainer resourceToPayCost, int position) {
        this.row = row;
        this.column = column;
        this.resourceToPayCost = resourceToPayCost;
        this.position = position;
    }

    @Override
    public void handle(ServerVirtualView c, Controller controller) {
        int output = controller.buyDevelopmentCard(c.getNickname(), row, column, resourceToPayCost, position);
        if(output == 0) {
            c.sendSuccessfulMoveMessage("Development card (" + row + "," + column + ")" +
                    " has been succesfully bought and placed in position " + position);
        }
    }

    @Override
    public void handleLocally(OfflineClientVirtualView offlineClientVirtualView, Controller controller) {
        int output = controller.buyDevelopmentCard(offlineClientVirtualView.getNickname(), row, column, resourceToPayCost, position);
        if(output == 0) {
            offlineClientVirtualView.printSuccessfulMove("Development card (" + row + "," + column + ")" +
                    " has been succesfully bought and placed in position " + position);
        }
    }
}
