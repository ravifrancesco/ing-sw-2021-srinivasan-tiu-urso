package it.polimi.ingsw.client.IO.InputHandling;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerGetsFromMarket;

import java.util.ArrayList;

public class MarketHandler {
    public static ClientMessage getFromMarket(String[] in, CLI cli) {
        try {
            int move = Integer.parseInt(in[1]);
            ReducedPlayer currentPlayer = cli.getReducedModel().getReducedPlayer();
            return new PlayerGetsFromMarket(move, new ArrayList<>());
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}
