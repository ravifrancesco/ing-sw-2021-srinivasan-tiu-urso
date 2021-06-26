package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.singlePlayer.tokens.Token;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Class used to send Game updates to the clients.
 */
public class GameUpdateMessage implements ServerMessage, Serializable {
    private final String firstPlayer;
    private final String currentPlayer;
    private final ArrayList<String> players;
    private final TurnPhase turnPhase;
    private final int firstTurns;
    private final boolean gameStarted;

    private final Stack<Token> tokens;
    private final Token lastToken;

    public GameUpdateMessage(Game game) {
        this.gameStarted = game.isGameStarted();
        this.firstPlayer = game.getFirstPlayer();
        this.currentPlayer = game.getCurrentPlayer();
        this.players = new ArrayList<>(game.getPlayers().keySet());
        this.turnPhase = game.getTurnPhase();
        this.firstTurns = game.getFirstTurns();
        this.tokens = game.getTokens();
        this.lastToken = game.getLastToken();
        System.out.println("Ci arrivo? ");
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedGame(firstPlayer, currentPlayer, players, turnPhase, firstTurns, gameStarted, tokens, lastToken);
    }
}
