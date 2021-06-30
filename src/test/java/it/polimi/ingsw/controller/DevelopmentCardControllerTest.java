package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.marbles.Marble;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DevelopmentCardControllerTest {

    static Controller controller;

    @BeforeClass
    public static void init() {
        controller = ControllerTestUtility.init();
    }

    @Test
    public void testGetFromMarket() {
        String robert = "rbta-svg";
        String giuseppe = "giuseppeurso";
        List<Stack<DevelopmentCard>> playedDevelopmentCardsGiuseppe = new ArrayList<>(controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getPlayedDevelopmentCards());
        List<Stack<DevelopmentCard>> playedDevelopmentCardsRobert = new ArrayList<>(controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getPlayedDevelopmentCards());
        controller.buyDevelopmentCard(giuseppe, 0, 0, new ResourceContainer(), 0);
        Assert.assertEquals(playedDevelopmentCardsGiuseppe, controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getPlayedDevelopmentCards());
        controller.buyDevelopmentCard(robert, -1, -1, new ResourceContainer(), 0);
        Assert.assertEquals(playedDevelopmentCardsRobert, controller.getGameStatus().getPlayers().get(robert).getDashboard().getPlayedDevelopmentCards());
        controller.buyDevelopmentCard(robert, 0, 0, new ResourceContainer(), 1);
        Assert.assertEquals(playedDevelopmentCardsRobert, controller.getGameStatus().getPlayers().get(robert).getDashboard().getPlayedDevelopmentCards());
    }

}
