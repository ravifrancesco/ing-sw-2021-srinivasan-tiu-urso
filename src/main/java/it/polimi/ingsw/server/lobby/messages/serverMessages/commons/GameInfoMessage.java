package it.polimi.ingsw.server.lobby.messages.serverMessages.commons;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.IO.Constants;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
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
        CLI cli;
        if(clientConnection.ui.getType() == UIType.CLI) {
            cli = (CLI) clientConnection.ui;

            if(numberOfPlayers == 1) {
                cli.printColoredMessage("Creating lobby...", Constants.ANSI_YELLOW);
                cli.printColoredMessage("Created successfully! Lobby details: ", Constants.ANSI_GREEN);
            } else {
                // System.out.println("Creating lobby for " + numberOfPlayers + " players...");
                cli.printColoredMessage("\nJoining lobby...", Constants.ANSI_YELLOW);
                cli.printColoredMessage("Joined successfully! Lobby details: ", Constants.ANSI_GREEN);
            }
            cli.printMessage("ID: " + gameID + "          Number of players: " + numberOfPlayers);
            cli.showGameLobbyMenu();
        }
    }
}
