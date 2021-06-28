package it.polimi.ingsw.network.messages.serverMessages.commons;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.network.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class GameLobbiesMessage implements ServerMessage, Serializable {
    ArrayList<GameLobbyDetails> gameLobbies;

    public GameLobbiesMessage(ArrayList<GameLobbyDetails> gameLobbies) {
        this.gameLobbies = gameLobbies;
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.ui.showGameLobbies(gameLobbies);
    }
}
