package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class ViewHandler {
    public static ClientMessage show(String[] input, CLI cli) {
        String objectToShow;
        if(input.length >= 2) {
            objectToShow = input[1].toUpperCase();

            String nickname = input.length == 3 ? input[2] : null;

            switch(objectToShow) {
                // global
                case "DVGRID" -> cli.showDVGrid();
                case "MARKET" -> cli.showMarket();
                // player
                case "HAND" -> cli.showHand(nickname);
                case "DASHBOARD" -> cli.showDashboard(nickname);
                case "FAITHTRACK" -> cli.showFaithTrack(nickname);
                case "WAREHOUSE" -> cli.showWarehouse(nickname);
                default -> System.out.println("Invalid command");
            }
        } else {
            cli.showViewMenu();
        }
        return null;
    }
}
