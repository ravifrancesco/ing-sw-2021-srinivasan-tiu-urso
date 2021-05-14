package it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.Lobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.ClientLobbyMessage;

public class RegisterName extends ClientLobbyMessage {
    String nickname;

    public RegisterName(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void handle(Connection connection, Lobby lobby) {
        connection.setNickname(nickname);
    }
}
