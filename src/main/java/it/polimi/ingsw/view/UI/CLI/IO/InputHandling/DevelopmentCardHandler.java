package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.model.full.specialAbilities.ProductionPower;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.ActivateDevelopmentProductionGameMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.BuyDevelopmentCardGameMessage;

import java.util.HashMap;
import java.util.Map;

public class DevelopmentCardHandler {
    public static String ANSI_GREEN = "\u001B[32m";
    public static String ANSI_RESET = "\u001B[0m";
    public static String ANSI_BLUE = "\u001B[34m";
    public static ClientMessage activateDevelopmentProduction(String[] in, CLI cli) {
        try {
            int card = Integer.parseInt(in[1]);
            cli.printMessage(ANSI_GREEN + "Please choose the payment resources" + ANSI_RESET);
            ResourceContainer rc = ResourceHandler.chooseResources(cli);

            cli.printMessage(ANSI_GREEN + "Activating production power..." + ANSI_RESET);

            ProductionPower pp = cli.getReducedModel()
                    .getReducedPlayer()
                    .getDashboard()
                    .getPlayedDevelopmentCards()
                    .get(card)
                    .peek()
                    .getProductionPower();

            int getNumRequiredAny = pp.getNumRequiredAny();
            int getNumProducedAny = pp.getNumProducedAny();

            if(getNumProducedAny != 0 || getNumRequiredAny != 0) {
                cli.printMessage(ANSI_GREEN + "Production has " + getNumRequiredAny +
                        "required selectable resources and " + getNumProducedAny + " produced selectable resources..." + ANSI_RESET);
            }

                Map<Resource, Integer> requiredResources;
            Map<Resource, Integer> producedResources;

            if (getNumRequiredAny != 0) {
                cli.printMessage(ANSI_GREEN + "Please choose the required selectables resources" + ANSI_RESET);
                requiredResources = ResourceHandler.chooseAnyResources(cli, getNumRequiredAny);
                ResourceHandler.showCurrSelected(cli, requiredResources);
            } else {
                requiredResources = new HashMap<>();
            }
            cli.printMessage("");

            if (getNumProducedAny != 0) {
                cli.printMessage(ANSI_GREEN + "Please choose the produced selectable resources" + ANSI_RESET);
                producedResources = ResourceHandler.chooseAnyResources(cli, getNumProducedAny);
                ResourceHandler.showCurrSelected(cli, producedResources);
            } else {
                producedResources = new HashMap<>();
            }
            cli.printMessage("");

            return new ActivateDevelopmentProductionGameMessage(card, rc, requiredResources, producedResources);
        } catch (Exception e) {
            cli.printErrorMessage("Error whilst activating dashboard production");
            cli.printErrorMessage(e.getMessage());
            return null;
        }
    }

    public static ClientMessage buyDevelopmentCard(String[] in, CLI cli) {
        try {
            int row = Integer.parseInt(in[1]);
            int column = Integer.parseInt(in[2]);
            int position = Integer.parseInt(in[3]);
            ResourceContainer resourceToPay = ResourceHandler.chooseResources(cli);
            return new BuyDevelopmentCardGameMessage(row, column, resourceToPay, position);
        } catch (Exception e) {
            cli.printErrorMessage("Failed parsing indexes");
            return null;
        }
    }
}
