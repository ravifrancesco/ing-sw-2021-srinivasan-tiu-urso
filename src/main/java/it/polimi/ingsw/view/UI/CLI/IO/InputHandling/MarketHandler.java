package it.polimi.ingsw.view.UI.CLI.IO.InputHandling;

import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.model.reduced.ReducedPlayer;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.marbles.WhiteMarble;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerGetsFromMarket;

import java.util.ArrayList;

public class MarketHandler {
    /**
     * Creates a message to activate a get from market move
     * @param in the input string to be parsed
     * @param cli the cli
     * @return the client message for the server
     */
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

    /**
     * Creates the arraylist of WMRs to be sent to the server
     * @param whiteMarblesCount amount of white marbles
     * @param cli the cli
     * @return the client message for the server
     */
    public static ArrayList<Resource> handleWMR(int whiteMarblesCount, CLI cli) {
        ArrayList<Resource> resources = new ArrayList<>();
        Resource res;
        cli.printColoredMessage("" + whiteMarblesCount + " white marbles detected.", Constants.SHIELD_COLOR);
        int i = whiteMarblesCount;
        while(i > 0) {
            cli.printColoredMessage("Please insert resource to obtain for white marble number " + (whiteMarblesCount-i), Constants.SHIELD_COLOR);
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
