package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.model.full.table.FaithTrack;
import it.polimi.ingsw.model.full.table.VaticanReport;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to send FaithTracks updates to the clients.
 */
public class FaithTrackUpdateMessage implements ServerMessage, Serializable {

    private final String playerNickname;
    private final int position;
    private final int LorenzoIlMagnificoPosition;
    private final Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports;

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
        faithTrack.getVaticanReports().forEach((key, value) -> vaticanReports.put(new Pair<>(value.getStart(), value.getEnd()), new Pair<>(value.getVictoryPoints(), vaticanReportState(value))));
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.updateReducedFaithTrack(playerNickname, position, LorenzoIlMagnificoPosition, vaticanReports);
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
