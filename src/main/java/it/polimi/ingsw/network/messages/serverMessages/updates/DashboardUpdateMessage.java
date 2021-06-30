package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.full.table.Dashboard;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Class used to send Dashboard updates to the clients.
 */
public class DashboardUpdateMessage implements ServerMessage, Serializable {

    private final String playerNickname;
    private final int playerPoints;
    private final List<LeaderCard> playedLeaderCards;
    private final List<Stack<DevelopmentCard>> playedDevelopmentCards;
    private final ArrayList<Resource> supply;
    private final ProductionPower productionPower;

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
        this.productionPower = dashboard.getDashBoardProductionPower();
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.updateReducedDashboard(playerNickname, playerPoints, playedLeaderCards, playedDevelopmentCards, supply, productionPower);
    }
}
