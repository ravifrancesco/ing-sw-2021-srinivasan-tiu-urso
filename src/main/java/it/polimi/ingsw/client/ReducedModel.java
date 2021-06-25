package it.polimi.ingsw.client;

import it.polimi.ingsw.GameController;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.Resource;
import javafx.application.Platform;

import java.util.ArrayList;
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
        // TODO change ravi
        Platform.runLater(() -> gameController.printWarehouse(player, deposit, locker, extraDeposit, supply));
    }
}
