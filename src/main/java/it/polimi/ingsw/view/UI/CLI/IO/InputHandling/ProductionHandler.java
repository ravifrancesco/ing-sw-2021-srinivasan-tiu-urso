package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.model.full.specialAbilities.ProductionPower;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.ActivateDashboardProductionGameMessage;
import it.polimi.ingsw.view.UI.CLI.CLI;

import java.util.HashMap;
import java.util.Map;

public class ProductionHandler {
    public static String ANSI_GREEN = "\u001B[32m";
    public static String ANSI_RESET = "\u001B[0m";

    /**
     * Creates a message to activate a dashboard card production
     *
     * @param cli the cli
     * @return the client message for the server
     */
    public static ClientMessage activateDashboardProduction(CLI cli) {
        try {
            ProductionPower pp = cli.getReducedModel().getReducedPlayer().getDashboard().getProductionPower();
            Map<Resource, Integer> reqRes = pp.getResourceRequired();
            int getNumRequiredAny = pp.getNumRequiredAny();
            int getNumProducedAny = pp.getNumProducedAny();

            cli.printPaymentCost(reqRes.get(Resource.GOLD) != null ? reqRes.get(Resource.GOLD) : 0,
                    reqRes.get(Resource.SERVANT) != null ? reqRes.get(Resource.SERVANT) : 0,
                    reqRes.get(Resource.SHIELD) != null ? reqRes.get(Resource.SHIELD) : 0,
                    reqRes.get(Resource.STONE) != null ? reqRes.get(Resource.STONE) : 0);

            if (getNumRequiredAny != 0) {
                cli.printMessage(getNumRequiredAny + " resources are selectable by you.");
            }

            cli.printMessage(ANSI_GREEN + "Please choose the payment resources" + ANSI_RESET);
            ResourceContainer rc = ResourceHandler.chooseResources(cli);
            cli.printMessage("");

            Map<Resource, Integer> selectedResources = rc.getAllResources(cli.getReducedModel().getReducedPlayer().getDashboard());
            reqRes.forEach((k, v) -> {
                if (k != Resource.ANY) selectedResources.merge(k, v, (v1, v2) -> v1 - v2);
            });

            Map<Resource, Integer> producedResources;


            if (getNumProducedAny != 0) {
                cli.printMessage(ANSI_GREEN + "Produced Resources " + ANSI_RESET);
                producedResources = ResourceHandler.chooseAnyResources(cli, getNumProducedAny);
                cli.printMessage("\nYou have selected to produce: ");
                ResourceHandler.showCurrSelected(cli, producedResources);
            } else {
                producedResources = new HashMap<>();
            }

            cli.printMessage("");
            return new ActivateDashboardProductionGameMessage(rc, selectedResources, producedResources);
        } catch (Exception e) {
            cli.printErrorMessage("Error whilst activating dashboard production");
            cli.printErrorMessage(e.getMessage());
            return null;
        }
    }
}
