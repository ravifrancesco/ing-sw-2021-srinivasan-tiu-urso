package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerChangesDeposit;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerStoresFromSupply;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerStoresFromSupplyToExtraDeposit;

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
            int supplyPos = Integer.parseInt(in[1]);
            int extraDepositIndex = Integer.parseInt(in[2]);
            int extraDepositPos = Integer.parseInt(in[3]);
            return new PlayerStoresFromSupplyToExtraDeposit(supplyPos, extraDepositIndex, extraDepositPos);
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
        cli.printMessage("Please insert the pair of indexes to swap, input DONE when finished.");
        Resource[] newDeposit = new Resource[6];
        IntStream.range(0, 6).forEach(i -> newDeposit[i] = cli.getReducedModel().getReducedPlayer().getDashboard().getDeposit()[i]);
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
            }
        } while (true);
        return newDeposit;
    }
}
