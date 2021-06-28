package it.polimi.ingsw.view.virtualView.client;

import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;

public interface ClientVirtualViewIF {

    void send(ClientMessage message);

}
