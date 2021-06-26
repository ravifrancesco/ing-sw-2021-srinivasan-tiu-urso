package it.polimi.ingsw.client;

import it.polimi.ingsw.GameController;
import it.polimi.ingsw.controller.client.reducedModel.ReducedDashboard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.marbles.Marble;
import javafx.application.Platform;

import java.util.*;

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

    public void moveFaithMarker(String player, int position) {
        if (gameController != null) {
            Platform.runLater(() -> gameController.moveFaithMarker(player, position));
        }
    }

    public void askFaithMarkerPosition(String player) {
        ReducedDashboard reducedDashboard = reducedGame.getReducedPlayer(player).getDashboard();
        Platform.runLater(() -> gameController.moveFaithMarker(player, reducedDashboard.getPosition()));
    }

    public void updateWarehouse(String player, Resource[] deposit, Map<Resource, Integer> locker, Resource[][] extraDeposit, ArrayList<Resource> supply) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.printWarehouse(player, deposit, locker, extraDeposit, supply));
    }

    public void askWarehouseUpdate(String player) {
        ReducedDashboard reducedDashboard = reducedGame.getReducedPlayer(player).getDashboard();
        Resource[] deposit = reducedDashboard.getDeposit();
        Map<Resource, Integer> locker = reducedDashboard.getLocker();
        Resource[][] extraDeposit = reducedDashboard.getExtraDeposits();
        ArrayList<Resource> supply = reducedDashboard.getSupply();
        Platform.runLater(() -> gameController.printWarehouse(player, deposit, locker, extraDeposit, supply));
    }

    public void updateLeaderCards(String player, List<LeaderCard> leaderCardList) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.printPlayedLeaderCards(player, leaderCardList));
    }

    public void askLeaderCardsUpdate(String player) {
        ReducedDashboard reducedDashboard = reducedGame.getReducedPlayer(player).getDashboard();
        List<LeaderCard> leaderCards = reducedDashboard.getPlayedLeaderCards();
        Platform.runLater(() -> gameController.printPlayedLeaderCards(player, leaderCards));
    }

    public void updateMarket(Marble[] marblesGrid, Marble freeMarble) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.updateMarket(marblesGrid, freeMarble));
    }

    public void askMarketUpdate() {
        Marble[] marblesGrid = reducedGameBoard.getMarblesGrid();
        Marble[] marbles = Arrays.copyOfRange(marblesGrid, 0, marblesGrid.length - 1);
        Marble freeMarble = marblesGrid[marblesGrid.length - 1];
        Platform.runLater(() -> gameController.updateMarket(marbles, freeMarble));
    }

    public void updateDevelopmentCardGrid(List<Stack<DevelopmentCard>> grid) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.updateDevCardGrid(grid));
    }

    public void askDevCardGridUpdate() {
        List<Stack<DevelopmentCard>> grid = reducedGameBoard.getGrid();
        Platform.runLater(() -> gameController.updateDevCardGrid(grid));
    }

    public void updatePoints(String player, int points) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.setPointsText(player, "Points: " + points));
    }

    public void askPointsUpdate(String player) {
        ReducedDashboard reducedDashboard = reducedGame.getReducedPlayer(player).getDashboard();
        Platform.runLater(() -> gameController.setPointsText(player, "Points: " + reducedDashboard.getPlayerPoints()));
    }

    public void updateHand(String player, List<LeaderCard> leaderCards) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.updateHand(player, leaderCards));
    }

    public void askHandUpdate(String player) {
        ReducedPlayer reducedPlayer = reducedGame.getPlayers().get(player);
        Platform.runLater(() -> gameController.updateHand(player, reducedPlayer.getHand()));
    }

    public void updateDevelopmentCards(String player, List<Stack<DevelopmentCard>> playedDevelopmentCards) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.printPlayedDevelopmentCards(player, playedDevelopmentCards));
    }

    public void askDevelopmentCardsUpdate(String player) {
        ReducedDashboard reducedDashboard = reducedGame.getReducedPlayer(player).getDashboard();
        List<Stack<DevelopmentCard>> playedDevelopmentCards = reducedDashboard.getPlayedDevelopmentCards();
        Platform.runLater(() -> gameController.printPlayedDevelopmentCards(player, playedDevelopmentCards));
    }

}
