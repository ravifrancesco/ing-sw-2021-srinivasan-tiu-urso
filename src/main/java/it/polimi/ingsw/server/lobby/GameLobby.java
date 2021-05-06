package it.polimi.ingsw.server.lobby;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.controller.exceptions.GameFullException;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.utils.Pair;

import javax.naming.InvalidNameException;
import java.util.HashMap;
import java.util.Map;

public class GameLobby implements Lobby {

    private final String id;

    private final int maxPlayers;
    private final Map<String, Connection> connectedPlayers;

    private final ServerController serverController;

    public GameLobby(String id, int maxPlayers) throws IllegalArgumentException {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.connectedPlayers = new HashMap<>();
        this.serverController = new ServerController(id, maxPlayers);
    }

    public void handleMessage(String msg, Connection c) {
        // TODO
    }

    public void enterLobby(Connection c) throws InvalidNameException, IllegalStateException {
        try {
            serverController.joinGame(c.getNickname());
        } catch (GameFullException e) {
            throw new IllegalStateException();
        }
        connectedPlayers.put(c.getNickname(), c);
    }

    public String getId() {
        return id;
    }

    public Pair<Integer, Integer> getLobbyStatus() {
        return new Pair<>(connectedPlayers.size(), maxPlayers);
    }

}
