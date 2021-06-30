package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.IllegalDepositStateException;
import it.polimi.ingsw.controller.exceptions.WrongMoveException;
import it.polimi.ingsw.controller.exceptions.WrongTurnException;
import it.polimi.ingsw.model.full.table.Dashboard;
import it.polimi.ingsw.model.full.table.Game;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;

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

    /**
     * @see Controller#changeDeposit(String, Resource[])
     */
    public void changeResourcesDeposit(String nickname, Resource[] deposit) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(game.getCurrentPlayer());
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.moveDepositResources(deposit);
        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index");
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");
        }
    }

    /**
     * @see Controller#changeDepositExtraDeposit(String, Resource[], Resource[], int)
     */
    public void changeResourcesDepositExtraDeposit(String nickname, Resource[] deposit, Resource[] extraDeposit, int lcIndex) throws WrongTurnException, IllegalDepositStateException, WrongMoveException {
        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(game.getCurrentPlayer());
        Dashboard dashboard = player.getDashboard();

        try {
            dashboard.moveDepositExtraDeposit(deposit, extraDeposit, lcIndex);
        } catch (IllegalStateException e) {
            throw new IllegalDepositStateException("Invalid deposit state");

        } catch (IllegalArgumentException e) {
            throw new WrongMoveException("Invalid index");
        }

    }


  /**
    *  @see Controller#storeFromSupply(String, int, int)
  */
    public void storeFromSupply(String nickname, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(game.getCurrentPlayer());
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
     * @see Controller#storeFromSupplyInExtraDeposit(String, int, int, int)
     */

    public void storeFromSupplyInExtraDeposit(String nickname, int leaderCardPos, int from, int to) throws WrongTurnException, WrongMoveException, IllegalDepositStateException {
        if (!game.getCurrentPlayer().equals(nickname)) {
            throw new WrongTurnException("Not " + nickname + " turn");
        }

        Player player = game.getPlayers().get(game.getCurrentPlayer());
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
