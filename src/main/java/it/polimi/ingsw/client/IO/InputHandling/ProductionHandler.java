package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.model.specialAbilities.ProductionPower;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.ActivateDashboardProductionGameMessage;

import java.util.HashMap;
import java.util.Map;

public class ProductionHandler {
    public static String ANSI_GREEN = "\u001B[32m";
    public static String ANSI_RESET = "\u001B[0m";
    public static String ANSI_BLUE = "\u001B[34m";

    public static ClientMessage activateDashboardProduction(String[] in, CLI cli) {
        try {
            cli.printMessage(ANSI_GREEN + "Please choose the payment resources" + ANSI_RESET);
            ResourceContainer rc = ResourceHandler.chooseResources(cli);
            cli.printMessage("DEPOSIT INDEXES: " + rc.getSelectedDepositIndexes());
            cli.printMessage("LOCKER RESOURCES: " + rc.getSelectedLockerResources());
            cli.printMessage("EXTRADEPOSIT INDEXES: " + rc.getSelectedExtraDepositIndexes());
            cli.printMessage("");

            cli.printMessage(ANSI_GREEN + "Activating production power..." + ANSI_RESET);
            ProductionPower pp = cli.getReducedModel().getReducedPlayer().getDashboard().getProductionPower();
            cli.printMessage(pp.toString());
            cli.printMessage("");

            int getNumRequiredAny = pp.getNumRequiredAny();
            int getNumProducedAny = pp.getNumProducedAny();

            cli.printMessage(ANSI_GREEN + "Production has " + getNumRequiredAny +
                    "required selectable resources and " + getNumProducedAny + " produced selectable resources..." + ANSI_RESET);

            Map<Resource, Integer> requiredResources;
            Map<Resource, Integer> producedResources;


            if (getNumRequiredAny != 0) {
                cli.printMessage(ANSI_GREEN + "Please choose the required selectables resources" + ANSI_RESET);
                requiredResources = ResourceHandler.chooseAnyResources(cli, getNumRequiredAny);
                cli.printMessage(ANSI_GREEN + "You have selected: " + requiredResources + ANSI_RESET);
            } else {
                requiredResources = new HashMap<>();
            }
            cli.printMessage("");

            if (getNumProducedAny != 0) {
                cli.printMessage(ANSI_GREEN + "Please choose the produced selectable resources" + ANSI_RESET);
                producedResources = ResourceHandler.chooseAnyResources(cli, getNumProducedAny);
                cli.printMessage(ANSI_GREEN + "You have selected: " + producedResources + ANSI_RESET);
            } else {
                producedResources = new HashMap<>();
            }
            cli.printMessage("");

            cli.printMessage(ANSI_BLUE + "# DONE! Calling ActivateDashboardProductionGameMessage with:" + ANSI_RESET);
            cli.printMessage(ANSI_BLUE + "# DEPOSIT INDEXES: " + rc.getSelectedDepositIndexes() + ANSI_RESET);
            cli.printMessage(ANSI_BLUE + requiredResources + ANSI_RESET);
            cli.printMessage(ANSI_BLUE + producedResources + ANSI_RESET);
            cli.printMessage("");
            return new ActivateDashboardProductionGameMessage(rc, requiredResources, producedResources);
        } catch (Exception e) {
            cli.printErrorMessage("Error whilst activating dashboard production");
            cli.printErrorMessage(e.getMessage());
            return null;
        }
    }
}
