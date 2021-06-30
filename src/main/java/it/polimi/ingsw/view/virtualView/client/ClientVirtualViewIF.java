package it.polimi.ingsw.view.virtualView.client;

import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;

public interface ClientVirtualViewIF {

    /**
     * Sends a client message
     *
     * @param message the client message
     */
    void send(ClientMessage message);

}
