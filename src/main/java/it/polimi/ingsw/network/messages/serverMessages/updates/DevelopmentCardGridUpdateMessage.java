package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.table.DevelopmentCardGrid;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

/**
 * Class used to send DevelopmentCardGrid updates to the clients.
 */
public class DevelopmentCardGridUpdateMessage implements ServerMessage, Serializable {

    private final List<Stack<DevelopmentCard>> grid;

    /**
     * Constructor.
     *
     * @param developmentCardGrid faithTrack for the update.
     */
    public DevelopmentCardGridUpdateMessage(DevelopmentCardGrid developmentCardGrid) {
        this.grid = developmentCardGrid.getGrid();
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.updateReducedDVGrid(grid);
    }
}
