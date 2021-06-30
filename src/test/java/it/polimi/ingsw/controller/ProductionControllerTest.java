package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ProductionControllerTest {

    static Controller controller;

    @BeforeClass
    public static void init() {
        controller = ControllerTestUtility.init();
    }

    @Test
    public void activateLeaderCardProductionPowerTest() {
        String player = "giuseppeurso";
        Map<Resource, Integer> locker = new HashMap<>(controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getLocker());
        controller.activateLeaderCardProductionPower(player, 0, new ResourceContainer(), new HashMap<>(), new HashMap<>());
        Assert.assertEquals(locker, controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getLocker());
    }

    @Test
    public void activateDashboardProductionPowerTest() {
        String player = "giuseppeurso";
        Map<Resource, Integer> locker = new HashMap<>(controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getLocker());
        controller.activateDashboardProductionPower(player, new ResourceContainer(), new HashMap<>(), new HashMap<>());
        Assert.assertEquals(locker, controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getLocker());
    }

    @Test
    public void activateDevelopmentCardProductionPowerTest() {
        String player = "giuseppeurso";
        Map<Resource, Integer> locker = new HashMap<>(controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getLocker());
        controller.activateDevelopmentCardProductionPower(player, 0, new ResourceContainer(), new HashMap<>(), new HashMap<>());
        Assert.assertEquals(locker, controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getLocker());
    }

}
