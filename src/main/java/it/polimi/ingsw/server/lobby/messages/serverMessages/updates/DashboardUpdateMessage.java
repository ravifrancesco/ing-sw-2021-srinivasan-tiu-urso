package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

/**
 * Class used to send Dashboard updates to the clients.
 */
public class DashboardUpdateMessage implements ServerMessage {

    private final Dashboard dashboard;

    /**
     * Constructor.
     *
     * @param dashboard dashboard for the update.
     */
    public DashboardUpdateMessage(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
