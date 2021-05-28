package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.DevelopmentCardGrid;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

/**
 * Class used to send DevelopmentCardGrid updates to the clients.
 */
public class DevelopmentCardGridUpdateMessage implements ServerMessage, Serializable {

    private List<Stack<DevelopmentCard>> grid;

    /**
     * Constructor.
     *
     * @param developmentCardGrid faithTrack for the update.
     */
    public DevelopmentCardGridUpdateMessage(DevelopmentCardGrid developmentCardGrid) {
        this.grid = developmentCardGrid.getGrid();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedDVGrid(grid);
    }
}
