package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;

public class ReducedModel {

    private ReducedGame reducedGame;
    private ReducedPlayer reducedPlayer;
    private ReducedGameBoard reducedGameBoard;

    public ReducedModel() {
        reducedGame = new ReducedGame();
        reducedPlayer = new ReducedPlayer();
        reducedGameBoard = new ReducedGameBoard();
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
}
