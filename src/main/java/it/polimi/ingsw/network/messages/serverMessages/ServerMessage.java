package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

/**
 * Messages for the clients updates.
 */
public interface ServerMessage {

    void updateClient(ClientVirtualView clientVirtualView, String nickname);

}

