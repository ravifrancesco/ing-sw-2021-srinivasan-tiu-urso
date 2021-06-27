package it.polimi.ingsw.server.lobby;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.GameSettings;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.ResourceContainer;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.ClientGameMessage;
import it.polimi.ingsw.utils.Pair;

import javax.naming.InvalidNameException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameLobby implements Lobby, Serializable {

    private final String id;

    private final int maxPlayers;

    public boolean isEndGamePhase() {
        return endGamePhase;
    }

    public void startEndGamePhase() {
        this.endGamePhase = true;
        getConnectedPlayers().forEach((nickname, player) -> {
            player.sendCLIupdateMessage("end_game_has_started");
        });
    }

    private boolean endGamePhase = false;

    private boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

    public void gameOverMultiPlayer() {
        this.gameOver = true;
        getConnectedPlayers().forEach((nickname, player) -> {
            player.sendCLIupdateMessage("game_has_ended");
        });
    }

    private String creator;

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Map<String, Connection> getConnectedPlayers() {
        return connectedPlayers;
    }

    private final Map<String, Connection> connectedPlayers;


    private final ServerController serverController;
    private GameSettings gameSettings;

    public GameLobby(String id, int maxPlayers) throws IllegalArgumentException {
        this.id = id;

        this.gameSettings = GameSettings.loadDefaultGameSettings();

        this.maxPlayers = maxPlayers;
        this.connectedPlayers = new HashMap<>();
        this.serverController = new ServerController(id, maxPlayers);
        this.endGamePhase = false;
    }

    @Override
    public synchronized void handleMessage(ClientMessage clientMessage, Connection c) {
        ((ClientGameMessage) clientMessage).handle(c, serverController);
    }

    public String getCreator() {
        return creator;
    }

    public synchronized void enterLobby(Connection c) throws InvalidNameException, IllegalStateException {
        if(connectedPlayers.size() == 0) { // if it was the first player
            serverController.loadGameSettings(gameSettings);
            creator = c.getNickname();
        }
        try {
            serverController.joinGame(c.getNickname());
        } catch (GameFullException e) {
            throw new IllegalStateException();
        }
        connectedPlayers.put(c.getNickname(), c);

        c.enterLobby(this);

        connectedPlayers.entrySet().stream().filter(e -> !e.getKey().equals(c.getNickname())).forEach(e -> {
            e.getValue().playerJoinedLobby(c.getNickname());
        });

        serverController.addObservers(c, connectedPlayers);
        c.sendGameLobbyEntered(id, c.getNickname().equals(creator));
    }

    public synchronized void leaveLobby(Connection c) {
        if (connectedPlayers.containsValue(c)) {
            try {
                serverController.removeObservers(c, connectedPlayers);
                connectedPlayers.remove(c.getNickname());
                serverController.leaveGame(c.getNickname());
            } catch (InvalidNameException e) {
                throw new IllegalStateException();
            }

        }
    }

    public String getId() {
        return id;
    }

    public LobbyType getType() {
        return LobbyType.GAME_LOBBY;
    }

    public void gameOverSinglePlayer() {
        this.gameOver = true;
        getConnectedPlayers().forEach((nickname, player) -> {
            player.sendCLIupdateMessage("game_has_ended_single");
        });
    }
}
