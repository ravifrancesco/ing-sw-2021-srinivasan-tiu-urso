package it.polimi.ingsw.network.messages.clientMessages.lobbyMessage;

import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;
import it.polimi.ingsw.network.server.lobby.Lobby;

import javax.naming.InvalidNameException;
import java.io.Serializable;

public class QuitGame extends ClientLobbyMessage implements Serializable {
    @Override
    public void handle(ServerVirtualView serverVirtualView, Lobby lobby) throws InvalidNameException {
        // TODO works if uncommented but calls quit() 3 times and doesn't kill reading thread
        // 1 time from call here
        // 1 time because catches IOException when sending CorrectHandlingMessage
        // 1 time in the "finally" whilst running the connection
        // connection.close();
    }
}
