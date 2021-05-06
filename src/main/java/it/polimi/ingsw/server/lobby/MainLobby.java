package it.polimi.ingsw.server.lobby;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.lobby.messageHandlers.MainLobbyMessageHandler;

import javax.naming.InvalidNameException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainLobby implements Lobby {

    private final List<Connection> connections;
    private final Map<String, Connection> waitingConnection;
    private final Map<String, Connection> playingConnection;

    private final MainLobbyMessageHandler mainLobbyMessageHandler;

    private final List<GameLobby> activeGameLobbies;

    private final ExecutorService executor = Executors.newFixedThreadPool(Server.THREAD_NUMBER);

    public MainLobby() {
        this.connections = new ArrayList<>();
        this.waitingConnection = new HashMap<>();
        this.playingConnection = new HashMap<>();
        this.mainLobbyMessageHandler = new MainLobbyMessageHandler(this);
        this.activeGameLobbies = new ArrayList<>();
    }

    public void enterLobby(Connection c) throws InvalidNameException {
        String nickname = c.getNickname();
        if (waitingConnection.containsKey(nickname)) throw new InvalidNameException();
        else {
            waitingConnection.put(nickname, c);
            c.changeLobby(this);
        }
    }

    public void registerConnection(Connection c) {
        connections.add(c);
        executor.submit(c);
    }

    public void deregisterConnection(Connection c) {
        connections.remove(c);
    }

    public void handleMessage(String msg, Connection c) {
        mainLobbyMessageHandler.handleMessage(msg, c);
    }

    public void createGame(Connection c, int numberOfPlayers) throws IllegalStateException, InvalidNameException, IllegalArgumentException {

        if (Server.THREAD_NUMBER - playingConnection.size() < 4) {
            throw new IllegalStateException();
        }

        String uniqueID = UUID.randomUUID().toString();
        GameLobby gameLobby = new GameLobby(uniqueID, numberOfPlayers);
        gameLobby.enterLobby(c);

        activeGameLobbies.add(gameLobby);
        playingConnection.put(c.getNickname(), waitingConnection.remove(c.getNickname()));

    }

    public void joinGame(Connection c, String id) throws IllegalArgumentException, InvalidNameException, IllegalStateException {

        Optional<GameLobby> gameLobby = activeGameLobbies.stream()
                .filter(gl -> gl.getId().equals(id))
                .findFirst();

        if (gameLobby.isEmpty()) throw new IllegalArgumentException();

        gameLobby.get().enterLobby(c);

    }

}
