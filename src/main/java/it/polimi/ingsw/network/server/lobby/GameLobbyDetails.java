package it.polimi.ingsw.network.server.lobby;

import java.io.Serializable;

public class GameLobbyDetails implements Serializable {
    public final String id;

    public final String creator;

    public final int connectedPlayers;

    public final int maxPlayers;

    public GameLobbyDetails(String id, String creator, int connectedPlayers, int maxPlayers) {
        this.id = id;
        this.creator = creator;
        this.connectedPlayers = connectedPlayers;
        this.maxPlayers = maxPlayers;
    }


}
