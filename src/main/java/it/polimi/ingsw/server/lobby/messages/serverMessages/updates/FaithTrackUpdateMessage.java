package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;

/**
 * Class used to send FaithTracks updates to the clients.
 */
public class FaithTrackUpdateMessage implements ServerMessage, Serializable {

    private final int position;
    /**
     * Constructor.
     *
     * @param faithTrack faithTrack for the update.
     */
    public FaithTrackUpdateMessage(FaithTrack faithTrack) {
        this.position = faithTrack.getPosition();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        // clientConnection.updateReducedFaithTrack(position);
    }
}
