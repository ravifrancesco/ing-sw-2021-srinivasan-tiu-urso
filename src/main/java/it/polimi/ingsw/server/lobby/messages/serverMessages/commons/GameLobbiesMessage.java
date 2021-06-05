package it.polimi.ingsw.server.lobby.messages.serverMessages.commons;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class GameLobbiesMessage implements ServerMessage, Serializable {
    ArrayList<GameLobbyDetails> gameLobbies;

    public GameLobbiesMessage(ArrayList<GameLobbyDetails> gameLobbies) {
        this.gameLobbies = gameLobbies;
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.ui.showGameLobbies(gameLobbies);
    }
}
