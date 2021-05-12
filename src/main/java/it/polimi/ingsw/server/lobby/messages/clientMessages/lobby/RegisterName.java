package it.polimi.ingsw.server.lobby.messages.clientMessages.lobby;

import it.polimi.ingsw.controller.ServerController;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public class RegisterName implements ClientMessage {
    String nickname;

    public RegisterName(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void handle(Connection connection, ServerController serverController) {
        connection.setNickname(nickname);
    }
}
