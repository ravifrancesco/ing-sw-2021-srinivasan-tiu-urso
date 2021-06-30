package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.full.table.Resource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class WarehouseControllerTest {
    static Controller controller;

    @BeforeClass
    public static void init() {
        controller = ControllerTestUtility.init();
    }

    @Test
    public void changeDepositTest() {
        String robert = "rbta-svg";
        String giuseppe = "giuseppeurso";
        Resource[] depositRobert = controller.getGameStatus().getPlayers().get(robert).getDashboard().getWarehouse().getDeposit();
        Resource[] depositGiuseppe = controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getWarehouse().getDeposit();
        controller.changeDeposit(giuseppe, new Resource[]{Resource.GOLD, null, null, null, null, null});
        Assert.assertEquals(depositGiuseppe, controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getWarehouse().getDeposit());
        controller.changeDeposit(robert, new Resource[]{Resource.GOLD, null, null, null, null, null});
        Assert.assertEquals(depositRobert, controller.getGameStatus().getPlayers().get(robert).getDashboard().getWarehouse().getDeposit());
    }

    @Test
    public void changeResourcesDepositExtraDepositTest() {
        String player = "giuseppeurso";
        Resource[] deposit = controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getDeposit();
        Resource[][] extraDeposits = controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getExtraDeposits();
        controller.changeDepositExtraDeposit(player, new Resource[]{Resource.GOLD, null, null, null, null, null}, new Resource[]{null, null}, 0);
        Assert.assertEquals(extraDeposits, controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getExtraDeposits());
        Assert.assertEquals(deposit, controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getDeposit());
    }

    @Test
    public void storeFromSupplyTest() {
        String robert = "rbta-svg";
        String giuseppe = "giuseppeurso";
        ArrayList<Resource> supplyGiuseppe = new ArrayList<>(controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getSupply());
        Resource[] depositGiuseppe = controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getWarehouse().getDeposit();
        controller.storeFromSupply(giuseppe, 0, 0);
        Assert.assertEquals(supplyGiuseppe, controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getSupply());
        Assert.assertEquals(depositGiuseppe, controller.getGameStatus().getPlayers().get(giuseppe).getDashboard().getWarehouse().getDeposit());
        controller.getFromMarket(robert, 0, new ArrayList<>());
        ArrayList<Resource> supplyRobert = controller.getGameStatus().getPlayers().get(robert).getDashboard().getSupply();
        Resource[] depositRobert = controller.getGameStatus().getPlayers().get(robert).getDashboard().getWarehouse().getDeposit();
        int supplySize = supplyRobert.size();
        Resource resource = supplyRobert.get(0);
        controller.storeFromSupply(robert, 0, 0);
        Assert.assertEquals(resource, depositRobert[0]);
        Assert.assertEquals(supplySize - 1, supplyRobert.size());
    }


    @Test
    public void storeFromSupplyInExtraDepositTest() {
        String player = "giuseppeurso";
        Resource[] deposit = controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getDeposit();
        ArrayList<Resource> supply = new ArrayList<>(controller.getGameStatus().getPlayers().get(player).getDashboard().getSupply());
        controller.storeFromSupplyInExtraDeposit(player, 0, 0, 0);
        Assert.assertEquals(supply, controller.getGameStatus().getPlayers().get(player).getDashboard().getSupply());
        Assert.assertEquals(deposit, controller.getGameStatus().getPlayers().get(player).getDashboard().getWarehouse().getDeposit());
    }


}


