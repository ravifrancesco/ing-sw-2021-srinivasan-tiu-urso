package it.polimi.ingsw.server.lobby.messages.serverMessages.commons;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class GameInfoMessage implements ServerMessage, Serializable {

    String gameID;
    int numberOfPlayers;

    public GameInfoMessage(String gameID, int numberOfPlayers) {
        this.gameID = gameID;
        this.numberOfPlayers = numberOfPlayers;
    }


    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateGameInfo(gameID, numberOfPlayers);
        clientConnection.cli.printMessage("GAME ID: " + gameID + "          Number of players: " + numberOfPlayers);
    }
}
