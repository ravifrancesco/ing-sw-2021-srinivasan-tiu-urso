package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.utils.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlayerJoinedMessage implements ServerMessage, Serializable {

    private String player;

    public PlayerJoinedMessage(String player) {
        this.player = player;
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.ui.printMessage("Player " + player + " joined the lobby");
    }
}
