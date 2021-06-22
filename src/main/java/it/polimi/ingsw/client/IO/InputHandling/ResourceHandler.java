package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;

import java.util.HashMap;
import java.util.Map;

public class ResourceHandler {

    public static Resource parseResource(String in) {
        return switch (in.toUpperCase()) {
            case "STONE" -> Resource.STONE;
            case "SERVANT" -> Resource.SERVANT;
            case "SHIELD" -> Resource.SHIELD;
            case "GOLD" -> Resource.GOLD;
            default -> null;
        };
    }

    public static Map<Resource, Integer> chooseAnyResources(CLI cli, int qty) {
        HashMap<Resource, Integer> output = new HashMap<>();
        Resource r;
        System.out.println("Please choose the resources you wish to use");
        try {
            while (qty != 0) {
                String input = cli.readCommand();
                if(input.equals("DONE")) {
                    throw new Exception();
                }
                r = parseResource(input);
                if(r != null) {
                    output.put(r, output.get(r) != null ? output.get(r) + 1 : 1);
                    qty -= 1;
                } else {
                    System.out.println("Resourced not parsed correctly.");
                }
            }
            System.out.println("Read all the necessary resources.");
            return output;
        } catch (Exception e) {
            System.out.println("Error whilst reading produced resources. ");
            return output;
        }
    }


    public static ResourceContainer chooseResources(CLI cli) {
        ResourceContainer rc = new ResourceContainer();
        System.out.println("Please choose with which resources you wish to pay the price: ");
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
            System.out.println("Resource Container at the moment has:");
            System.out.println("DEPOSIT INDEXES: " + rc.getSelectedDepositIndexes());
            System.out.println("LOCKER RESOURCES: " + rc.getSelectedLockerResources());
            System.out.println("EXTRADEPOSIT INDEXES: " + rc.getSelectedExtraDepositIndexes());
        } while (!command.equals("DONE"));
        return rc;
    }

    public static void handleDepositSelection(String[] input, ReducedPlayer player, ResourceContainer rc) {
        try {
            int index = Integer.parseInt(input[1]);
            rc.addDepositSelectedResource(index, player.getDashboard().getDeposit());
        } catch (Exception e) {
            System.out.println("Error while adding from deposit to resource container");
        }
    }

    public static void handleExtraDepositSelection(String[] input, ReducedPlayer player, ResourceContainer rc) {
        try {
            int lcPos = Integer.parseInt(input[1]);
            int lcIndex = Integer.parseInt(input[2]);
            rc.addExtraDepositSelectedResource(lcPos, lcIndex, player.getDashboard().getExtraDeposits());
        } catch (Exception e) {
            System.out.println("Error while adding from extra deposit to resource container");
        }
    }

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
