package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.utils.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to send FaithTracks updates to the clients.
 */
public class FaithTrackUpdateMessage implements ServerMessage, Serializable {

    private String playerNickname;
    private final int position;
    private final int LorenzoIlMagnificoPosition;
    private Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports; // TODO how should this be?
    private int[] faithTrackVictoryPoints;
    /**
     * Constructor.
     *
     * @param faithTrack faithTrack for the update.
     */
    public FaithTrackUpdateMessage(FaithTrack faithTrack) {
        this.playerNickname = faithTrack.getDashboard().getPlayer().getNickname();
        this.position = faithTrack.getPosition();
        this.LorenzoIlMagnificoPosition = faithTrack.getLorenzoIlMagnificoPosition();
        this.vaticanReports = new HashMap<>();
        faithTrack.getVaticanReports().forEach((key, value) -> vaticanReports.put(new Pair<>(key, value.getVictoryPoints()), new Pair<>(value.getStart(), value.getEnd())));
        this.faithTrackVictoryPoints = faithTrack.getFaithTrackVictoryPoints();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedFaithTrack(playerNickname, position, LorenzoIlMagnificoPosition, vaticanReports, faithTrackVictoryPoints);
    }
}
