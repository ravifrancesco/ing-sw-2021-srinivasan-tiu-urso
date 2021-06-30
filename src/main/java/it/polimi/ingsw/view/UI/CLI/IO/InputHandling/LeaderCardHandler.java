package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.model.full.specialAbilities.ProductionPower;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.*;

import java.util.HashMap;
import java.util.Map;

public class LeaderCardHandler {
    public static String ANSI_GREEN = "\u001B[32m";
    public static String ANSI_RESET = "\u001B[0m";


    /**
     * Handles the creation for the message to discard an excess card
     * @param in the string to parse with the needed arguments
     * @param cli the CLI
     * @return the client message with the move
     */
    public static ClientMessage discardExcessCard(String[] in, CLI cli) {
        try {
            int cardIndex = Integer.parseInt(in[1]);
            return new PlayerDiscardsExcessLeaderCards(cardIndex);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            cli.printErrorMessage("Error parsing card index");
            return null;
        }
    }

    /**
     * Handles the creation for the message to discard a card
     * @param in the string to parse with the needed arguments
     * @param cli the CLI
     * @return the client message with the move
     */
    public static ClientMessage discardCard(String[] in, CLI cli) {
        try {
            int cardIndex = Integer.parseInt(in[1]);
            return new PlayerDiscardsLeaderCard(cardIndex);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            cli.printErrorMessage("Error parsing card index");
            return null;
        }
    }

    /**
     * Handles the creation for the message to activate a leader card production
     * @param in the string to parse with the needed arguments
     * @param cli the CLI
     * @return the client message with the move
     */
    public static ClientMessage activateLeaderProduction(String[] in, CLI cli) {
        try {
            int card = Integer.parseInt(in[1]);
            cli.printMessage(ANSI_GREEN + "Please choose the payment resources\n" + ANSI_RESET);
            ResourceContainer rc = ResourceHandler.chooseResources(cli);

            cli.printMessage(ANSI_GREEN + "Activating production power..." + ANSI_RESET);

            ProductionPower pp = (ProductionPower) cli.getReducedModel()
                    .getReducedPlayer()
                    .getDashboard()
                    .getPlayedLeaderCards()
                    .get(card)
                    .getSpecialAbility();


            int getNumRequiredAny = pp.getNumRequiredAny();
            int getNumProducedAny = pp.getNumProducedAny();

            if(getNumProducedAny != 0 || getNumRequiredAny != 0) {
                cli.printMessage(ANSI_GREEN + "Production has " + getNumRequiredAny +
                        " required selectable resources and " + getNumProducedAny + " produced selectable resources..." + ANSI_RESET);
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


            return new ActivateLeaderProductionGameMessage(card, rc, requiredResources, producedResources);
        } catch (Exception e) {
            cli.printErrorMessage("Error whilst activating dashboard production");
            cli.printErrorMessage(e.getMessage());
            return null;
        }
    }

    /**
     * Handles the creation for the message to play a leader card
     * @param in the string to parse with the needed arguments
     * @param cli the CLI
     * @return the client message with the move
     */
    public static ClientMessage playLeaderCard(String[] in, CLI cli) {
        try {
            int cardIndex = Integer.parseInt(in[1]);
            return new PlayLeaderCardGameMessage(cardIndex);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            cli.printErrorMessage("Failed parsing leader card hand index");
            return null;
        }
    }
}
