package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Class used to send Game updates to the clients.
 */
public class GameUpdateMessage implements ServerMessage, Serializable {
    private final String firstPlayer;
    private final String currentPlayer;
    private final ArrayList<String> players;
    private final TurnPhase turnPhase;
    private final int firstTurns;

    public GameUpdateMessage(Game game) {
        this.firstPlayer = game.getFirstPlayer();
        this.currentPlayer = game.getCurrentPlayer();
        this.players = new ArrayList<>(game.getPlayers().keySet());
        this.turnPhase = game.getTurnPhase();
        this.firstTurns = game.getFirstTurns();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedGame(firstPlayer, currentPlayer, players, turnPhase, firstTurns);
    }
}
