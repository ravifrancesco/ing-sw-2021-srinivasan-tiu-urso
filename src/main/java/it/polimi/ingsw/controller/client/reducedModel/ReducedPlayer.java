package it.polimi.ingsw.controller.client.reducedModel;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;
import java.util.List;

public class ReducedPlayer {

    private String nickname;

    // Hand
    private List<LeaderCard> hand;
    private int handSize;

    // Dashboard
    private final ReducedDashboard dashboard;

    public ReducedPlayer() {
        this.nickname = "";
        this.hand = new ArrayList<>();
        this.dashboard = new ReducedDashboard();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<LeaderCard> getHand() {
        return hand;
    }

    public void setHand(List<LeaderCard> hand) {
        this.hand = hand;
    }

    public int getHandSize() {
        return handSize;
    }

    public void setHandSize(int handSize) {
        this.handSize = handSize;
    }

    public ReducedDashboard getDashboard() {
        return dashboard;
    }
}
