package it.polimi.ingsw.server.messages.updates;

import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.server.messages.ServerMessage;

/**
 * Class used to send FaithTracks updates to the clients.
 */
public class FaithTrackUpdateMessage implements ServerMessage {

    private final FaithTrack faithTrack;

    /**
     * Constructor.
     *
     * @param faithTrack faithTrack for the update.
     */
    public FaithTrackUpdateMessage(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
