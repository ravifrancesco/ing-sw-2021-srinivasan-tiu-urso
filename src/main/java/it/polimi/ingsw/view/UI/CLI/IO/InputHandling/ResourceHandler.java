package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.reduced.ReducedPlayer;
import it.polimi.ingsw.model.utils.ResourceContainer;
import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;

import java.util.HashMap;
import java.util.Map;

public class ResourceHandler {


    /**
     * Parses a resource
     *
     * @param in string to parse
     * @return the resource
     */
    public static Resource parseResource(String in) {
        return switch (in.toUpperCase()) {
            case "STONE" -> Resource.STONE;
            case "SERVANT" -> Resource.SERVANT;
            case "SHIELD" -> Resource.SHIELD;
            case "GOLD" -> Resource.GOLD;
            default -> null;
        };
    }

    /**
     * Handles the selection of resources
     *
     * @param cli the CLI
     * @param qty amount of resources to parse
     * @return the selected resources
     */
    public static Map<Resource, Integer> chooseAnyResources(CLI cli, int qty) {
        HashMap<Resource, Integer> output = new HashMap<>();
        Resource r;
        System.out.println("Please choose the resource(s)");
        System.out.println("Options: STONE, GOLD, SHIELD, SERVANT");

        try {
            while (qty != 0) {
                System.out.println("Remaining to select: " + qty);
                String input = cli.readCommand();
                if (input.equals("DONE")) {
                    throw new Exception();
                }
                r = parseResource(input);
                if (r != null) {
                    output.put(r, output.get(r) != null ? output.get(r) + 1 : 1);
                    qty -= 1;
                } else {
                    System.out.println("Resourced not parsed correctly.");
                }
            }
            cli.printColoredMessage("Selection complete", Constants.ANSI_GREEN);
            return output;
        } catch (Exception e) {
            System.out.println("Error whilst reading produced resources. ");
            return output;
        }
    }


    /**
     * Handles the choosing of resources for a payment
     *
     * @param cli the CLI
     * @return the resource container
     */
    public static ResourceContainer chooseResources(CLI cli) {
        ResourceContainer rc = new ResourceContainer();
        cli.showWarehouse(cli.getReducedModel().getReducedPlayer().getNickname());
        System.out.println("Insert payment resources by typing:");
        cli.printColoredMessageNoNL("DEPOSIT <index> ", Constants.SERVANT_COLOR);
        cli.printMessage(" to select a resource index from the deposit");
        cli.printColoredMessageNoNL("LOCKER <resource> <qty>", Constants.SERVANT_COLOR);
        cli.printMessage(" to select resources from the locker");
        cli.printColoredMessageNoNL("EXTRADEPOSIT <lcPos> <lcIndex> ", Constants.SERVANT_COLOR);
        cli.printMessage(" to select an index from an extradeposit");
        cli.printColoredMessageNoNL("DONE", Constants.SERVANT_COLOR);
        cli.printMessage(" to conclude the selection");

        System.out.println();

        System.out.println();
        System.out.println("Resources selected until now: ");
        Map<Resource, Integer> currentRes = rc.getAllResources(cli.getReducedModel().getReducedPlayer().getDashboard());
        showCurrSelected(cli, currentRes);
        cli.printMessage("\nInsert DONE when finished selecting.");


        String[] input;
        String command;
        do {
            input = cli.readCommand().toUpperCase().split(" ");
            command = input[0];
            switch (command) {
                case "DEPOSIT" -> handleDepositSelection(input, cli.getReducedModel().getReducedPlayer(), rc);
                case "LOCKER" -> handleLockerSelection(input, cli.getReducedModel().getReducedPlayer(), rc);
                case "EXTRADEPOSIT" -> handleExtraDepositSelection(input, cli.getReducedModel().getReducedPlayer(), rc);
            }
            currentRes = rc.getAllResources(cli.getReducedModel().getReducedPlayer().getDashboard());
            showCurrSelected(cli, currentRes);
        } while (!command.equals("DONE"));
        return rc;
    }

    /**
     * Shows the currently selected resources for the chooseResources method
     *
     * @param cli        the cli
     * @param currentRes the resource map
     */
    public static void showCurrSelected(CLI cli, Map<Resource, Integer> currentRes) {

        if (currentRes.get(Resource.STONE) != null && currentRes.get(Resource.STONE) != 0) {
            cli.printColoredMessageNoNL("STONE: ", Constants.STONE_COLOR);
            cli.printMessage(currentRes.get(Resource.STONE) + "");
        }

        if (currentRes.get(Resource.GOLD) != null && currentRes.get(Resource.GOLD) != 0) {
            cli.printColoredMessageNoNL("GOLD: ", Constants.GOLD_COLOR);
            cli.printMessage(currentRes.get(Resource.GOLD) + "");
        }

        if (currentRes.get(Resource.SERVANT) != null && currentRes.get(Resource.SERVANT) != 0) {
            cli.printColoredMessageNoNL("SERVANT: ", Constants.SERVANT_COLOR);
            cli.printMessage(currentRes.get(Resource.SERVANT) + "");
        }

        if (currentRes.get(Resource.SHIELD) != null && currentRes.get(Resource.SHIELD) != 0) {
            cli.printColoredMessageNoNL("SHIELD: ", Constants.SHIELD_COLOR);
            cli.printMessage(currentRes.get(Resource.SHIELD) + "");
        }
    }

    /**
     * Parses and handles a deposit selection
     *
     * @param input  the input string
     * @param player the reduced player
     * @param rc     the resource container to add resources to
     */
    public static void handleDepositSelection(String[] input, ReducedPlayer player, ResourceContainer rc) {
        try {
            int index = Integer.parseInt(input[1]);
            rc.addDepositSelectedResource(index, player.getDashboard().getDeposit());
        } catch (Exception e) {
            System.out.println("Error while adding from deposit to resource container");
        }
    }

    /**
     * Parses and handles an extra deposit selection
     *
     * @param input  the input string
     * @param player the reduced player
     * @param rc     the resource container
     */
    public static void handleExtraDepositSelection(String[] input, ReducedPlayer player, ResourceContainer rc) {
        try {
            int lcPos = Integer.parseInt(input[1]);
            int lcIndex = Integer.parseInt(input[2]);
            rc.addExtraDepositSelectedResource(lcPos, lcIndex, player.getDashboard().getExtraDeposits());
        } catch (Exception e) {
            System.out.println("Error while adding from extra deposit to resource container");
        }
    }

    /**
     * Parses and handles locker selection
     *
     * @param input  the string to parse
     * @param player the reduces player
     * @param rc     the resource container
     */
    public static void handleLockerSelection(String[] input, ReducedPlayer player, ResourceContainer rc) {
        try {
            Resource res = ResourceHandler.parseResource(input[1]);
            int qty = Integer.parseInt(input[2]);
            rc.addLockerSelectedResource(res, qty, player.getDashboard().getLocker());
        } catch (Exception e) {
            System.out.println("Error while adding from locker to resource container");
        }
    }

}
