package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.IO.Constants;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.marbles.WhiteMarble;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerGetsFromMarket;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class MarketHandler {
    public static ClientMessage getFromMarket(String[] in, CLI cli) {
        try {
            int move = Integer.parseInt(in[1]);
            ReducedPlayer currentPlayer = cli.getReducedModel().getReducedPlayer();
            ArrayList<Resource> wmrResources = new ArrayList<>();
            int whiteMarbles = (int) cli.getReducedModel().getReducedGameBoard()
                    .getMarblesMove(move).stream().filter(marble -> marble instanceof WhiteMarble).count(); // TODO remove instanceof

            if(currentPlayer.getActivatedWMR().length > 0 && whiteMarbles > 0) {
                // WMRS are present
                if(currentPlayer.getActivatedWMR().length == 1) {
                    // only 1 WMR active, no need to ask the user
                    for(int i = 0; i < whiteMarbles; i++) { // lambda function was giving me some bullshit
                        wmrResources.add(currentPlayer.getActivatedWMR()[0]);
                    }
                } else {
                    // multiple WMR, need to ask user for each white marble
                    wmrResources = handleWMR(whiteMarbles, cli);
                }
            }
            return new PlayerGetsFromMarket(move, wmrResources);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            cli.printErrorMessage("Error parsing move index");
            return null;
        }
    }

    public static ArrayList<Resource> handleWMR(int whiteMarblesCount, CLI cli) {
        ArrayList<Resource> resources = new ArrayList<>();
        Resource res;
        cli.printColoredMessage("" + whiteMarblesCount + " white marbles detected.", Constants.ANSI_BLUE);
        int i = whiteMarblesCount;
        while(i > 0) {
            cli.printColoredMessage("Please insert resource to obtain for white marble number " + (whiteMarblesCount-i), Constants.ANSI_BLUE);
            res = ResourceHandler.parseResource(cli.readLine().split(" ")[0]);
            if(res == null) {
                cli.printErrorMessage("Resource parsing error, try again");
            } else {
                resources.add(res);
                i--;
            }
        }
        return resources;
    }
}
