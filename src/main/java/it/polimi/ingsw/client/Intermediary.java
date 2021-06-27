package it.polimi.ingsw.client;

import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

public interface Intermediary {

    void send(ClientMessage message);

}
