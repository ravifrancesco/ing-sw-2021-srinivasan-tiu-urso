package it.polimi.ingsw.model.reduced;

import it.polimi.ingsw.model.full.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReducedPlayer {

    private String nickname;

    // Hand
    private List<LeaderCard> hand;
    private int handSize;
    private Resource[] activatedWMR;
    private ArrayList<DevelopmentCardDiscount> discounts;

    public HashMap<Resource, Integer> getDiscounts() {
        HashMap<Resource, Integer> discountsMap = new HashMap<>();
        discounts.forEach(dvd -> {
            discountsMap.merge(dvd.getResource(), dvd.getQuantity(), Integer::sum);
        });
        return discountsMap;
    }

    public void setDiscounts(ArrayList<DevelopmentCardDiscount> discounts) {
        this.discounts = discounts;
    }

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
