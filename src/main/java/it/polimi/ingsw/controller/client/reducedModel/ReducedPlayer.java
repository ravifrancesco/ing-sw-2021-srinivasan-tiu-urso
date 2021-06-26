package it.polimi.ingsw.controller.client.reducedModel;

import it.polimi.ingsw.controller.ReducedDashboard;
import it.polimi.ingsw.controller.ReducedModel;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.util.ArrayList;
import java.util.List;

public class ReducedPlayer {

    private String nickname;

    // Hand
    private List<LeaderCard> hand;
    private int handSize;
    private Resource[] activatedWMR;

    // Dashboard
    private final ReducedDashboard dashboard;

    private ReducedModel reducedModel;

    public ReducedPlayer(ReducedModel reducedModel) {
        this.reducedModel = reducedModel;
        this.nickname = "";
        this.hand = new ArrayList<>();
        this.dashboard = new ReducedDashboard(this, reducedModel);
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
        updateHand();
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

    public Resource[] getActivatedWMR() {
        return activatedWMR;
    }

    public void setActivatedWMR(Resource[] activatedWMR) {
        this.activatedWMR = activatedWMR;
    }

    private void updateHand() {
        reducedModel.updateHand(nickname, hand);
    }

}
