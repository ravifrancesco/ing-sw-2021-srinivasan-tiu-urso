package it.polimi.ingsw.network.messages.clientMessages.lobbyMessage;

import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.server.lobby.Lobby;

import java.io.Serializable;

public class RegisterName extends ClientLobbyMessage implements Serializable {
    String nickname;

    public RegisterName(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void handle(ServerVirtualView serverVirtualView, Lobby lobby) {
        serverVirtualView.setNickname(nickname);
    }
}
