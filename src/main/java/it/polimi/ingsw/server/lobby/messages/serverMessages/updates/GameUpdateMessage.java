package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
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

    public GameUpdateMessage(Game game) {
        this.firstPlayer = game.getFirstPlayer();
        this.currentPlayer = game.getCurrentPlayer();
        this.players = new ArrayList<>(game.getPlayers().keySet());
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.cli.printMessage("New player joined, game lobby now contains the following players: " + players);
    }
}
