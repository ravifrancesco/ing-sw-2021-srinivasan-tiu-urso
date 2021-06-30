package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.table.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderCardControllerTest {

    static Controller controller;

    @Test
    public void discardExcessLeaderCardsTest() {
        controller = ControllerTestUtility.initWithoutDiscarding();
        String robert = "rbta-svg";
        String giuseppe = "giuseppeurso";
        List<LeaderCard> handRobert = new ArrayList<>(controller.getGameStatus().getPlayers().get(robert).getHand());
        List<LeaderCard> handGiuseppe = new ArrayList<>(controller.getGameStatus().getPlayers().get(giuseppe).getHand());
        controller.discardExcessLeaderCards(giuseppe, 0);
        Assert.assertEquals(handGiuseppe, controller.getGameStatus().getPlayers().get(giuseppe).getHand());
        controller.discardExcessLeaderCards(robert, 0);
        handRobert.remove(0);
        Assert.assertEquals(handRobert, controller.getGameStatus().getPlayers().get(robert).getHand());
        controller.discardExcessLeaderCards(robert, 4);
        Assert.assertEquals(handRobert, controller.getGameStatus().getPlayers().get(robert).getHand());
        controller.discardExcessLeaderCards(robert, 0);
        handRobert.remove(0);
        controller.discardExcessLeaderCards(robert, 0);
        Assert.assertEquals(handRobert, controller.getGameStatus().getPlayers().get(robert).getHand());
    }

    @Test
    public void playLeaderCardTest() {
        controller = ControllerTestUtility.init();
        String player = "giuseppeurso";
        List<LeaderCard> hand = new ArrayList<>(controller.getGameStatus().getPlayers().get(player).getHand());
        controller.playLeaderCard(player, 0);
        Assert.assertEquals(hand, controller.getGameStatus().getPlayers().get(player).getHand());
    }

    @Test
    public void activateDevelopmentCardProductionPowerTest() {
        controller = ControllerTestUtility.init();
        String robert = "rbta-svg";
        String giuseppe = "giuseppeurso";
        Map<Resource, Integer> handRobert = new HashMap<>(controller.getGameStatus().getPlayers().get(robert).getDashboard().getWarehouse().getLocker());
        Map<Resource, Integer> handGiuseppe = new HashMap<>(controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getWarehouse().getLocker());
        controller.discardLeaderCard(giuseppe, 0);
        Assert.assertEquals(handGiuseppe, controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getWarehouse().getLocker());
        controller.discardLeaderCard(robert, -1);
        Assert.assertEquals(handRobert, controller.getGameStatus().getPlayers().get(robert).getDashboard().getWarehouse().getLocker());
        handRobert.remove(0);
        controller.discardLeaderCard(robert, 0);
        Assert.assertEquals(handRobert, controller.getGameStatus().getPlayers().get(robert).getDashboard().getWarehouse().getLocker());
    }

}
