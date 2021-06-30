package it.polimi.ingsw.network.server.lobby;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.ClientGameMessage;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import javax.naming.InvalidNameException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the game lobby. It's identified by a unique
 * id and handles the main game lobby functions.
 *
 * It also contains the reference to all the ServerVirtualViews connencted
 * to the clients.
 */
public class GameLobby implements Lobby, Serializable {

    private final String id;

    private final int maxPlayers;
    private final Map<String, ServerVirtualView> connectedPlayers;
    private final Controller controller;
    private final GameSettings gameSettings;
    private boolean endGamePhase = false;
    private boolean gameOver = false;
    private String creator;

    /**
     * Defaul constructor.
     *
     * @param id                            id of the lobby
     * @param maxPlayers                    number of players of the game
     * @throws IllegalArgumentException     if the number of players does not match the expected values
     */
    public GameLobby(String id, int maxPlayers) throws IllegalArgumentException {
        this.id = id;

        this.gameSettings = GameSettings.loadDefaultGameSettings();

        this.maxPlayers = maxPlayers;
        this.connectedPlayers = new HashMap<>();
        this.controller = new Controller(id, maxPlayers);
        this.endGamePhase = false;
    }

    /**
     * Getter for end phase
     * @return  boolean representing the game status
     */
    public boolean isEndGamePhase() {
        return endGamePhase;
    }

    /**
     * Initializes end game phase of the game
     */
    public void startEndGamePhase() {
        this.endGamePhase = true;
        getConnectedPlayers().forEach((nickname, player) -> {
            player.sendCLIupdateMessage("end_game_has_started");
        });
    }

    /**
     * Getter for game over
     *
     * @return  true if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game to game over state for multiplayer games
     */
    public void gameOverMultiPlayer() {
        this.gameOver = true;
        getConnectedPlayers().forEach((nickname, player) -> {
            player.sendCLIupdateMessage("game_has_ended");
        });
    }

    /**
     * Getter for the max number of players
     *
     * @return  max number of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Getter for  the connected players
     *
     * @return  the connected players
     */
    public Map<String, ServerVirtualView> getConnectedPlayers() {
        return connectedPlayers;
    }

    /**
     * @see Lobby#handleMessage(ClientMessage clientMessage, ServerVirtualView c)
     */
    @Override
    public synchronized void handleMessage(ClientMessage clientMessage, ServerVirtualView c) {
        ((ClientGameMessage) clientMessage).handle(c, controller);
    }

    /**
     * Getter for creator of the lobby
     *
     * @return  nickname of the creator of the lobby
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @see Lobby#enterLobby(ServerVirtualView c) throws InvalidNameException, IllegalStateException
     */
    @Override
    public synchronized void enterLobby(ServerVirtualView c) throws InvalidNameException, IllegalStateException {
        if (connectedPlayers.size() == 0) { // if it was the first player
            controller.loadGameSettings(gameSettings);
            creator = c.getNickname();
        }
        try {
            controller.joinGame(c.getNickname());
        } catch (GameFullException e) {
            throw new IllegalStateException();
        }
        connectedPlayers.put(c.getNickname(), c);

        c.enterLobby(this);

        connectedPlayers.entrySet().stream().filter(e -> !e.getKey().equals(c.getNickname())).forEach(e -> {
            e.getValue().playerJoinedLobby(c.getNickname());
        });

        controller.addObservers(c, connectedPlayers);
        c.sendGameLobbyEntered(c.getNickname().equals(creator));
    }

    /**
     * To leave the lobby
     *
     * @param c ServerVirtualView that is leaving the lobby
     */
    public synchronized void leaveLobby(ServerVirtualView c) {
        if (connectedPlayers.containsValue(c)) {
            try {
                controller.removeObservers(c, connectedPlayers);
                connectedPlayers.remove(c.getNickname());
                controller.leaveGame(c.getNickname());
            } catch (InvalidNameException e) {
                throw new IllegalStateException();
            }

        }
    }

    /**
     * Getter for lobby id
     *
     * @return  id of the lobby
     */
    public String getId() {
        return id;
    }

    /**
     * @see Lobby#getType()
     */
    @Override
    public LobbyType getType() {
        return LobbyType.GAME_LOBBY;
    }

    /**
     * Sets the game over in case of singlePlayer
     */
    public void gameOverSinglePlayer() {
        this.gameOver = true;
        getConnectedPlayers().forEach((nickname, player) -> {
            player.sendCLIupdateMessage("game_has_ended_single");
        });
    }
}
