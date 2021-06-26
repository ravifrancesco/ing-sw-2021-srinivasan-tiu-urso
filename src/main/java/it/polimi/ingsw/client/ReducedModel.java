package it.polimi.ingsw.client;

import it.polimi.ingsw.GameController;
import it.polimi.ingsw.controller.client.reducedModel.ReducedDashboard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.marbles.Marble;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReducedModel {

    private ReducedGame reducedGame;
    private ReducedPlayer reducedPlayer;
    private ReducedGameBoard reducedGameBoard;

    GameController gameController;

    public ReducedModel() {
        reducedGame = new ReducedGame(this);
        reducedPlayer = new ReducedPlayer(this);
        reducedGameBoard = new ReducedGameBoard(this);
    }

    public ReducedGame getReducedGame() {
        return reducedGame;
    }

    public ReducedPlayer getReducedPlayer() {
        return reducedPlayer;
    }

    public void setReducedPlayer(ReducedPlayer reducedPlayer) {
        this.reducedPlayer = reducedPlayer;
    }

    public ReducedGameBoard getReducedGameBoard() {
        return reducedGameBoard;
    }

    public void setNickname(String nickname) {
        reducedPlayer.setNickname(nickname);
        reducedGame.setClientPlayer(nickname);
        reducedGame.createPlayer(reducedPlayer); // TODO is there a better way to do this?
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void hideStartGameAlert() {
        if (gameController != null) {
            gameController.hideAlert();
        }
    }

    public void moveFaithMarker(int position) {
        if (gameController != null) {
            Platform.runLater(() -> gameController.moveFaithMarker(position));
        }
    }

    public void updateWarehouse(String player, Resource[] deposit, Map<Resource, Integer> locker, Resource[][] extraDeposit, ArrayList<Resource> supply) {
        Platform.runLater(() -> gameController.printWarehouse(player, deposit, locker, extraDeposit, supply));
    }

    public void updateWarehouse(String player) {
        ReducedDashboard reducedDashboard = reducedGame.getReducedPlayer(player).getDashboard();
        Resource[] deposit = reducedDashboard.getDeposit();
        Map<Resource, Integer> locker = reducedDashboard.getLocker();
        Resource[][] extraDeposit = reducedDashboard.getExtraDeposits();
        ArrayList<Resource> supply = reducedDashboard.getSupply();
        Platform.runLater(() -> gameController.printWarehouse(player, deposit, locker, extraDeposit, supply));
    }

    public void updateLeaderCards(String player, List<LeaderCard> leaderCardList) {
        Platform.runLater(() -> gameController.printPlayedLeaderCards(player, leaderCardList));
    }

    public void updateLeaderCards(String player) {
        ReducedDashboard reducedDashboard = reducedGame.getReducedPlayer(player).getDashboard();
        List<LeaderCard> leaderCards = reducedDashboard.getPlayedLeaderCards();
        Platform.runLater(() -> gameController.printPlayedLeaderCards(player, leaderCards));
    }

    public void updateMarket(Marble[] marblesGrid, Marble freeMarble) {
        Platform.runLater(() -> gameController.updateMarket(marblesGrid, freeMarble));
    }

    public void askMarketUpdate() {
        Marble[] marblesGrid = reducedGameBoard.getMarblesGrid();
        Marble[] marbles = Arrays.copyOfRange(marblesGrid, 0, marblesGrid.length - 1);
        Marble freeMarble = marblesGrid[marblesGrid.length - 1];
        Platform.runLater(() -> gameController.updateMarket(marbles, freeMarble));
    }

}
