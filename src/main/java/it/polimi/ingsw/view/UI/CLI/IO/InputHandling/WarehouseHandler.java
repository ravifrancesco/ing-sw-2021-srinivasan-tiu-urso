package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerChangesDeposit;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerStoresFromSupply;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerStoresFromSupplyToExtraDeposit;

import java.util.stream.IntStream;

public class WarehouseHandler {
    public static ClientMessage storeFromSupply(String[] in, CLI cli) {
        try {
            int supplyPos = Integer.parseInt(in[1]);
            int depositPos = Integer.parseInt(in[2]);
            return new PlayerStoresFromSupply(supplyPos, depositPos);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            cli.printErrorMessage("Failed parsing indexes");
            return null;
        }
    }

    public static ClientMessage storeFromSupplyToExtraDeposit(String[] in, CLI cli) {
        try {
            int from = Integer.parseInt(in[1]);
            int lcPos = Integer.parseInt(in[3]);
            int to = Integer.parseInt(in[2]);
            return new PlayerStoresFromSupplyToExtraDeposit(lcPos, from, to);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            cli.printErrorMessage("Failed parsing indexes");
            return null;
        }
    }

    public static ClientMessage changeDeposit(String[] in, CLI cli) {
        try {
            return new PlayerChangesDeposit(swapDeposit(cli));
        } catch (Exception e) {
            cli.printErrorMessage("Failed parsing swap indexes");
            return null;
        }
    }

    private static Resource[] swapDeposit(CLI cli) {
        cli.printMessage("Current deposit: ");
        Resource[] newDeposit = new Resource[6];
        IntStream.range(0, 6).forEach(i -> newDeposit[i] = cli.getReducedModel().getReducedPlayer().getDashboard().getDeposit()[i]);
        cli.showDeposit(newDeposit);
        cli.printMessage("Please insert");
        cli.printColoredMessageNoNL("<index1> <index2> ", Constants.SERVANT_COLOR);
        cli.printMessage("to swap resources");
        cli.printColoredMessageNoNL("DONE", Constants.SERVANT_COLOR);
        cli.printMessage("to finish swapping resources");

        String[] input;
        int firstIndex;
        int secondIndex;
        Resource temp;
        do {
            input = cli.readCommand().toUpperCase().split(" ");
            if(input[0].equals("DONE")) {
                cli.printMessage("Swapping finished");
                break;
            } else {
                System.out.println("Swapping...");
                firstIndex = Integer.parseInt(input[0]);
                secondIndex = Integer.parseInt(input[1]);
                temp = newDeposit[firstIndex];
                newDeposit[firstIndex] = newDeposit[secondIndex];
                newDeposit[secondIndex] = temp;
                cli.printMessage("CURRENT DEPOSIT \n");
                cli.showDeposit(newDeposit);
                cli.printMessage("\nPlease insert");
                cli.printColoredMessageNoNL("<index1> <index2> ", Constants.SERVANT_COLOR);
                cli.printMessage("to swap resources");
                cli.printColoredMessageNoNL("DONE ", Constants.SERVANT_COLOR);
                cli.printMessage("to finish swapping resources\n");
            }
        } while (true);
        return newDeposit;
    }
}
