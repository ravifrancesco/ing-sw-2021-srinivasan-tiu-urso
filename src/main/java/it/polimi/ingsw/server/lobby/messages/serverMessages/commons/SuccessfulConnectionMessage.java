package it.polimi.ingsw.server.lobby.messages.serverMessages.commons;

import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class SuccessfulConnectionMessage implements ServerMessage, Serializable {

    String id;

    public SuccessfulConnectionMessage(String id) {
        this.id = id;
    }

    public void showMessage() {
        System.out.println("GAME ID: " + id);
    }

    @Override
    public void updateClient(Object client, String nickname) {
        // TODO
    }
}
