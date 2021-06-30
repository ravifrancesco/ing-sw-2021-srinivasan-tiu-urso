package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.controller.exceptions.WrongTurnPhaseException;
import it.polimi.ingsw.model.full.table.*;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class MarketController {
    private final Game game;

    /**
     * Constructor for a Market Controller object.
     *
     * @param game represents the game which the controller belongs to.
     */
    public MarketController(Game game) {
        this.game = game;
    }

    /**
     * @see Controller#getFromMarket(String, int, ArrayList)
     */
    public void getFromMarket(String nickname, int move, ArrayList<Resource> wmrs) throws WrongTurnException, WrongMoveException {
        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        } else if (!game.getTurnPhase().equals(TurnPhase.COMMON)) {
            throw new WrongTurnPhaseException("Turn phase is " + game.getTurnPhase().name());
        }

        Player player = game.getPlayers().get(nickname);

        if (!player.checkWMR(wmrs)) {
            // check that each marble in WMRS is actually an activated special ability
            throw new WrongMoveException(game.getCurrentPlayer() + " doesn't have these white marble resources");
        }

        GameBoard gameboard = game.getGameBoard();
        Market market = gameboard.getMarket();
        Dashboard dashboard = player.getDashboard();

        if (move < 0 || move > 6) {
            throw new WrongMoveException("Desired move is out of index");
        }


        ArrayList<Resource> marketRes = market.getResources(move, player);
        // marketRes will return an arrayList of resources, where the white marbles will be converted to Resource.ANY
        // if two whiteMarbleResource special abilities are activated (see WhiteMarble.getRes()) -- this doesn't happen
        // when only WMR is activated.


        // check to see that the WMRS size corresponds to the amount of white marbles (against hacked clients)
        if (marketRes.stream().filter(r -> r == Resource.ANY).count() != wmrs.size()) {
            throw new IllegalArgumentException(game.getCurrentPlayer() + " asked for too many white marble transformed resources.");
        }

        // removes all ANYs
        IntStream.range(0, (int) marketRes.stream().filter(res -> res == Resource.ANY).count()).forEach(i -> marketRes.remove(Resource.ANY));
        // substitutes with the user's choice of returned resource
        marketRes.addAll(wmrs);

        dashboard.addResourcesToSupply(marketRes);
        game.startUniquePhase(TurnPhase.MARKET);

    }
}
