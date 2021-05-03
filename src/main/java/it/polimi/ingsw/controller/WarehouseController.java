package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.Pair;
import it.polimi.ingsw.controller.exceptions.IllegalDepositStateException;
import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.marbles.Marble;

import java.util.ArrayList;

public class WarehouseController {
    private final Game game;

    private String currentPlayer;

    /**
     * Constructor for a Warehouse Controller object.
     * @param game represents the game which the controller belongs to.
     */

    public WarehouseController(Game game) {
        this.game = game;
    }

    /**
     * Setter for the current player.
     * @param currentPlayer the current player of the game.
     */

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void moveResourcesDepositDeposit(String nickname, Pair<Integer, Integer> move) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.moveDepositResources(move);
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index");
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");
        }
    }

    /**
     * @see ServerController#moveResourceDepositExtraDeposit
     */
    public void moveResourcesDepositExtraDeposit(String nickname, Pair<Integer, Integer> move, int lcPos, int lcCardIndex) throws WrongTurnException, IllegalDepositStateException, WrongMoveException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.moveDepositExtraDepositResources(move, lcPos, lcCardIndex);
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");

        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index");
        }

    }


  /**
    *  @see ServerController#storeFromSupply(String, int, int)
  */
    public void storeFromSupply(String nickname, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.storeFromSupply(from, to);
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index");
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");
        }
    }

    /**
     * @see ServerController#storeFromSupplyInExtraDeposit(String, int, int, int)
     */

    public void storeFromSupplyInExtraDeposit(String nickname, int leaderCardPos, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!currentPlayer.equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(currentPlayer);
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.storeFromSupplyInExtraDeposit(leaderCardPos, from, to);
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index(es)");
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");
        }
    }
}
