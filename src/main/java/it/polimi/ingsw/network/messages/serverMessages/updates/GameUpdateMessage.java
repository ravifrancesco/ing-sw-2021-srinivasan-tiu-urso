package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.model.full.table.Game;
import it.polimi.ingsw.model.full.table.TurnPhase;
import it.polimi.ingsw.model.full.tokens.Token;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

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
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.updateReducedGame(firstPlayer, currentPlayer, players, turnPhase, firstTurns, gameStarted, tokens, lastToken);
    }
}
