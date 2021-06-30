package it.polimi.ingsw.network.server.lobby;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.game.ClientGameMessage;

import javax.naming.InvalidNameException;
import java.io.Serializable;
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

    public Map<String, ServerVirtualView> getConnectedPlayers() {
        return connectedPlayers;
    }

    private final Map<String, ServerVirtualView> connectedPlayers;


    private final Controller controller;
    private final GameSettings gameSettings;

    public GameLobby(String id, int maxPlayers) throws IllegalArgumentException {
        this.id = id;

        this.gameSettings = GameSettings.loadDefaultGameSettings();

        this.maxPlayers = maxPlayers;
        this.connectedPlayers = new HashMap<>();
        this.controller = new Controller(id, maxPlayers);
        this.endGamePhase = false;
    }

    @Override
    public synchronized void handleMessage(ClientMessage clientMessage, ServerVirtualView c) {
        ((ClientGameMessage) clientMessage).handle(c, controller);
    }

    public String getCreator() {
        return creator;
    }

    public synchronized void enterLobby(ServerVirtualView c) throws InvalidNameException, IllegalStateException {
        if(connectedPlayers.size() == 0) { // if it was the first player
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
