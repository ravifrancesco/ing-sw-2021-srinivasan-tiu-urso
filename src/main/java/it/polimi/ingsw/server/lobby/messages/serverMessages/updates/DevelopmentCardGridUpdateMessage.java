package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.model.DevelopmentCardGrid;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

/**
 * Class used to send DevelopmentCardGrid updates to the clients.
 */
public class DevelopmentCardGridUpdateMessage implements ServerMessage {

    private final DevelopmentCardGrid developmentCardGrid;

    /**
     * Constructor.
     *
     * @param developmentCardGrid faithTrack for the update.
     */
    public DevelopmentCardGridUpdateMessage(DevelopmentCardGrid developmentCardGrid) {
        this.developmentCardGrid = developmentCardGrid;
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}