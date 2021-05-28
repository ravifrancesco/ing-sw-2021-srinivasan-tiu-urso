package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class used to send Dashboard updates to the clients.
 */
public class DashboardUpdateMessage implements ServerMessage, Serializable {

    private String playerNickname;
    private int playerPoints;
    private final List<LeaderCard> playedLeaderCards;
    private final List<Stack<DevelopmentCard>> playedDevelopmentCards;
    private ArrayList<Resource> supply;

    /**
     * Constructor.
     *
     * @param dashboard dashboard for the update.
     */
    public DashboardUpdateMessage(Dashboard dashboard) {
        this.playerNickname = dashboard.getPlayer().getNickname();
        this.playerPoints = dashboard.getPlayerPoints();
        this.playedLeaderCards = dashboard.getPlayedLeaderCards();
        this.playedDevelopmentCards = dashboard.getPlayedDevelopmentCards();
        this.supply = dashboard.getSupply();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedDashboard(playerNickname, playerPoints, playedLeaderCards, playedDevelopmentCards, supply);
    }
}
