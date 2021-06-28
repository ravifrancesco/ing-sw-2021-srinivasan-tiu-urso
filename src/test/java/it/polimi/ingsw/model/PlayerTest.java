package it.polimi.ingsw.model;

import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.full.specialAbilities.WarehouseExtraSpace;
import it.polimi.ingsw.model.full.specialAbilities.WhiteMarbleResource;
import it.polimi.ingsw.model.full.table.GameBoard;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.GameSettings;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.fail;

public class PlayerTest {

    private static Player player;

    @BeforeClass
    static public void createPlayer() {

        GameSettings gameSettings = new ReducedGameSettingsTest().buildGameSettings();
        player = new Player(gameSettings, "test");

        Assert.assertNotNull(player);

    }

    @Test
    public void playLeaderCardTest() {

        player.reset();

        player.addCard(new LeaderCard(0, 2, new HashMap<>(), new HashMap<>(), new DevelopmentCardDiscount(Resource.GOLD, 3)));
        player.addCard(new LeaderCard(0, 2, new HashMap<>(), new HashMap<>(), new WarehouseExtraSpace(Resource.GOLD)));
        player.addCard(new LeaderCard(0, 2, new HashMap<>(), new HashMap<>(), new WhiteMarbleResource(Resource.GOLD)));

        try {
            player.playLeaderCard(4);
            fail();
        } catch (IllegalStateException e) {
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        player.playLeaderCard(0);
        player.playLeaderCard(0);

        try {
            player.playLeaderCard(0);
            fail();
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void discardLeaderCardTest() {

        player.reset();

        GameBoard gameBoard = new GameBoard();


        player.addCard(new LeaderCard(0, 2, new HashMap<>(), new HashMap<>(), new DevelopmentCardDiscount(Resource.GOLD, 3)));
        player.addCard(new LeaderCard(0, 2, new HashMap<>(), new HashMap<>(), new WarehouseExtraSpace(Resource.GOLD)));
        player.addCard(new LeaderCard(0, 2, new HashMap<>(), new HashMap<>(), new WhiteMarbleResource(Resource.GOLD)));

        try {
            player.discardLeaderCard(4, gameBoard);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        Assert.assertEquals(3, player.getHandSize());

        player.discardLeaderCard(0, gameBoard);

        Assert.assertEquals(2, player.getHandSize());

        player.discardLeaderCard(0, gameBoard);

        Assert.assertEquals(1, player.getHandSize());

        player.discardLeaderCard(0, gameBoard);

        Assert.assertEquals(0, player.getHandSize());

        try {
            player.discardLeaderCard(0, gameBoard);
            fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }

        Assert.assertEquals(0, player.getHandSize());

    }

    @Test
    public void activeDiscountAndWMRTest() {

        player.reset();

        DevelopmentCardDiscount developmentCardDiscount1 = new DevelopmentCardDiscount(Resource.GOLD, 3);
        DevelopmentCardDiscount developmentCardDiscount2 = new DevelopmentCardDiscount(Resource.SHIELD, 2);

        WhiteMarbleResource whiteMarbleResource1 = new WhiteMarbleResource(Resource.SERVANT);
        WhiteMarbleResource whiteMarbleResource2 = new WhiteMarbleResource(Resource.SHIELD);

        Assert.assertTrue(player.getActiveDiscounts().isEmpty());
        Assert.assertFalse(player.checkActiveWMR());
        Assert.assertEquals(0, player.getNumActiveWMR());

        player.addActiveDiscount(developmentCardDiscount1);
        player.addWMR(whiteMarbleResource1);

        try {
            player.addActiveDiscount(developmentCardDiscount2);
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

        try {
            player.addWMR(whiteMarbleResource2);
            fail();
        } catch (IllegalStateException e) {
            Assert.assertTrue(true);
        }

        Assert.assertFalse(player.getActiveDiscounts().isEmpty());
        Assert.assertTrue(player.checkActiveWMR());
        Assert.assertEquals(1, player.getNumActiveWMR());

        Assert.assertTrue(player.getActiveDiscounts().contains(developmentCardDiscount1));
        Assert.assertEquals(Resource.SERVANT, player.getActivatedWMR()[0]);

    }

    @Test
    public void checkWMRTest() {

        player.reset();

        WhiteMarbleResource whiteMarbleResource1 = new WhiteMarbleResource(Resource.SERVANT);
        WhiteMarbleResource whiteMarbleResource2 = new WhiteMarbleResource(Resource.SHIELD);

        ArrayList<WhiteMarbleResource> wmrs = new ArrayList<>();
        wmrs.add(whiteMarbleResource1);
        wmrs.add(whiteMarbleResource2);

        player.addWMR(new WhiteMarbleResource(Resource.SERVANT));
        player.addWMR(new WhiteMarbleResource(Resource.SHIELD));

        //Assert.assertTrue(player.checkWMR(wmrs)); // todo fix

        wmrs.add(new WhiteMarbleResource(Resource.GOLD));

        // Assert.assertFalse(player.checkWMR(wmrs)); // todo fix

    }

}
