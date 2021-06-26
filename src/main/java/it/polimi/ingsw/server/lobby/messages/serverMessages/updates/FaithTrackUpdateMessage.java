package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.VaticanReport;
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
    private Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports;
    private int[] faithTrackVictoryPoints;
    /**
     * Constructor.
     *
     * @param faithTrack faithTrack for the update.
     */
    public FaithTrackUpdateMessage(FaithTrack faithTrack) {
        this.playerNickname = faithTrack.getDashboard().getPlayer().getNickname();
        this.position = faithTrack.getPosition();
        this.vaticanReports = new HashMap<>();
        faithTrack.getVaticanReports().forEach((key, value) -> vaticanReports.put(new Pair<>(value.getStart(), value.getEnd()), new Pair<>(value.getVictoryPoints(), vaticanReportState(value))));
        this.faithTrackVictoryPoints = faithTrack.getFaithTrackVictoryPoints();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedFaithTrack(playerNickname, position, vaticanReports, faithTrackVictoryPoints);
    }

    private int vaticanReportState(VaticanReport v) {
        if (!v.isMissed() && !v.isAchieved()) {
            return 0;
        } else if (v.isMissed()) {
            return 1;
        } else {
            return 2;
        }
    }

}
