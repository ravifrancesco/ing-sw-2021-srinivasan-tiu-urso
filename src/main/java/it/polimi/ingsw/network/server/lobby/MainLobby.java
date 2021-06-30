package it.polimi.ingsw.network.server.lobby;

import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.ClientLobbyMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import javax.naming.InvalidNameException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainLobby implements Lobby {


    private final List<ServerVirtualView> serverVirtualViews;
    private final Map<String, ServerVirtualView> waitingConnection;
    private final Map<String, ServerVirtualView> playingConnection;

    private final List<GameLobby> activeGameLobbies;

    private final ExecutorService executor = Executors.newFixedThreadPool(Server.THREAD_NUMBER);

    public MainLobby() {
        this.serverVirtualViews = new ArrayList<>();
        this.waitingConnection = new HashMap<>();
        this.playingConnection = new HashMap<>();
        this.activeGameLobbies = new ArrayList<>();
    }


    public synchronized void enterLobby(ServerVirtualView c) throws InvalidNameException {
        String nickname = c.getNickname();
        if (waitingConnection.containsKey(nickname) || playingConnection.containsKey(nickname))
            throw new InvalidNameException();
        else {
            waitingConnection.put(nickname, c);
            c.enterLobby(this);
        }
    }

    public synchronized void registerConnection(ServerVirtualView c) {
        serverVirtualViews.add(c);

        executor.submit(c);
    }

    public synchronized void deregisterConnection(ServerVirtualView c) {
        serverVirtualViews.remove(c);
        waitingConnection.remove(c.getNickname());
        playingConnection.remove(c.getNickname()); // TODO reconnection
    }

    @Override
    public synchronized void handleMessage(ClientMessage clientMessage, ServerVirtualView c) throws InvalidNameException {
        ((ClientLobbyMessage) clientMessage).handle(c, this);
    }

    public void createGame(ServerVirtualView c, int numberOfPlayers) throws IllegalStateException, InvalidNameException, IllegalArgumentException {
        if (Server.THREAD_NUMBER - playingConnection.size() < 4) {
            throw new IllegalStateException();
        }

        String uniqueID = UUID.randomUUID().toString();
        GameLobby gameLobby = new GameLobby(uniqueID, numberOfPlayers);

        gameLobby.enterLobby(c);

        activeGameLobbies.add(gameLobby);
        playingConnection.put(c.getNickname(), waitingConnection.remove(c.getNickname()));
    }


    public void joinGame(ServerVirtualView c, String id) throws IllegalArgumentException, InvalidNameException, IllegalStateException {

        Optional<GameLobby> gameLobby = activeGameLobbies.stream()
                .filter(gl -> gl.getId().equals(id))
                .findFirst();

        if (gameLobby.isEmpty()) throw new IllegalArgumentException("Lobby doesn't exist");

        gameLobby.get().enterLobby(c);

    }

    public LobbyType getType() {
        return LobbyType.MAIN_LOBBY;
    }

    public List<GameLobby> getActiveGameLobbies() {
        return activeGameLobbies;
    }

    public void removeGameLobby(GameLobby gameLobby) {
        activeGameLobbies.remove(gameLobby);
    }


}
