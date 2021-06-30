package it.polimi.ingsw.network.server.lobby;

import java.io.Serializable;

/**
 * Compact class representing the game lobby details
 */
public class GameLobbyDetails implements Serializable {
    public final String id;

    public final String creator;

    public final int connectedPlayers;

    public final int maxPlayers;

    /**
     * Defaul constructor
     *
     * @param id                id of the lobby
     * @param creator           creator of the lobby
     * @param connectedPlayers  players connected to the lobby
     * @param maxPlayers        max player for the lobby
     */
    public GameLobbyDetails(String id, String creator, int connectedPlayers, int maxPlayers) {
        this.id = id;
        this.creator = creator;
        this.connectedPlayers = connectedPlayers;
        this.maxPlayers = maxPlayers;
    }


}
