package it.polimi.ingsw.network.messages.serverMessages;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

/**
 * Messages for the clients updates.
 */
public interface ServerMessage {

    /**
     * Updates the client in response to an action or to an observer update
     * @param clientVirtualView the client view
     * @param nickname client nickname
     */
    void updateClient(ClientVirtualView clientVirtualView, String nickname);

}

