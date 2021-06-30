package it.polimi.ingsw.network.server.lobby;

import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessages.lobbyMessage.ClientLobbyMessage;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import javax.naming.InvalidNameException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the main lobby. It handles the main game lobby functions.
 *
 * It also contains the reference to all the ServerVirtualViews connencted
 * to the clients.
 */
public class MainLobby implements Lobby {


    private final List<ServerVirtualView> serverVirtualViews;
    private final Map<String, ServerVirtualView> waitingConnection;
    private final Map<String, ServerVirtualView> playingConnection;

    private final List<GameLobby> activeGameLobbies;

    private final ExecutorService executor = Executors.newFixedThreadPool(Server.THREAD_NUMBER);

    /**
     * Default constructor
     */
    public MainLobby() {
        this.serverVirtualViews = new ArrayList<>();
        this.waitingConnection = new HashMap<>();
        this.playingConnection = new HashMap<>();
        this.activeGameLobbies = new ArrayList<>();
    }


    /**
     * @see Lobby#enterLobby(ServerVirtualView c) throws InvalidNameException, IllegalStateException
     */
    @Override
    public synchronized void enterLobby(ServerVirtualView c) throws InvalidNameException {
        String nickname = c.getNickname();
        if (waitingConnection.containsKey(nickname) || playingConnection.containsKey(nickname))
            throw new InvalidNameException();
        else {
            waitingConnection.put(nickname, c);
            c.enterLobby(this);
        }
    }

    /**
     * Registers a ServerVirtualView on the server
     *
     * @param c ServerVirtualView to register on the server
     */
    public synchronized void registerConnection(ServerVirtualView c) {
        serverVirtualViews.add(c);

        executor.submit(c);
    }

    /**
     * Deregisters a ServerVirtualView on the server
     *
     * @param c ServerVirtualView to register on the server
     */
    public synchronized void deregisterConnection(ServerVirtualView c) {
        serverVirtualViews.remove(c);
        waitingConnection.remove(c.getNickname());
        playingConnection.remove(c.getNickname()); // TODO reconnection
    }

    /**
     * @see Lobby#handleMessage(ClientMessage clientMessage, ServerVirtualView c)
     */
    @Override
    public synchronized void handleMessage(ClientMessage clientMessage, ServerVirtualView c) throws InvalidNameException {
        ((ClientLobbyMessage) clientMessage).handle(c, this);
    }

    /**
     * Handles the creation of a game lobby
     *
     * @param c                             ServerVirtualView that is creating the lobby
     * @param numberOfPlayers               max number of players
     * @throws IllegalStateException        cannot create a game
     * @throws InvalidNameException         name of the ServerVirtualView is not valid
     * @throws IllegalArgumentException     number of players is not valid
     */
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

    /**
     * Handles joining of a game
     *
     * @param c                             ServerVirtualView that is joining the lobby
     * @param id                            id of the lobby to join
     * @throws IllegalArgumentException     id of the lobby is not valid
     * @throws InvalidNameException         name of the ServerVirtualView is not valid
     * @throws IllegalStateException        lobby cannot be joined
     */
    public void joinGame(ServerVirtualView c, String id) throws IllegalArgumentException, InvalidNameException, IllegalStateException {

        Optional<GameLobby> gameLobby = activeGameLobbies.stream()
                .filter(gl -> gl.getId().equals(id))
                .findFirst();

        if (gameLobby.isEmpty()) throw new IllegalArgumentException("Lobby doesn't exist");

        gameLobby.get().enterLobby(c);

    }

    /**
     * @see Lobby#getType()
     */
    @Override
    public LobbyType getType() {
        return LobbyType.MAIN_LOBBY;
    }

    /**
     * Getter for the active game lobbies
     *
     * @return  the active game lobbies
     */
    public List<GameLobby> getActiveGameLobbies() {
        return activeGameLobbies;
    }

    /**
     * Removes a game lobby
     *
     * @param gameLobby GameLobby to remove
     */
    public void removeGameLobby(GameLobby gameLobby) {
        activeGameLobbies.remove(gameLobby);
    }


}
