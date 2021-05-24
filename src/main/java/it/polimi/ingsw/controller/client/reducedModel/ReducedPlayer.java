package it.polimi.ingsw.controller.client.reducedModel;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;
import java.util.List;

public class ReducedPlayer {

    private final String nickname;

    // Hand
    private List<LeaderCard> hand;

    // Dashboard
    private final ReducedDashboard dashboard;

    public ReducedPlayer(String nickname) {
        this.nickname = nickname;
        this.hand = new ArrayList<>();
        this.dashboard = new ReducedDashboard();
    }

}
