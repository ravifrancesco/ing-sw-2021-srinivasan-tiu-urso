package it.polimi.ingsw.client;

import it.polimi.ingsw.GameController;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;

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
        gameController.hideAlert();
    }
}
