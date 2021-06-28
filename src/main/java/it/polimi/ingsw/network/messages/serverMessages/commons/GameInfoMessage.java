package it.polimi.ingsw.network.messages.serverMessages.commons;

import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.view.UI.CLI.CLI;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.view.UI.UIType;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.Serializable;

public class GameInfoMessage implements ServerMessage, Serializable {

    String gameID;
    int numberOfPlayers;

    public GameInfoMessage(String gameID, int numberOfPlayers) {
        this.gameID = gameID;
        this.numberOfPlayers = numberOfPlayers;
    }


    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        clientVirtualView.updateGameInfo(gameID, numberOfPlayers);
        CLI cli;
        if(clientVirtualView.ui.getType() == UIType.CLI) {
            cli = (CLI) clientVirtualView.ui;

            if(numberOfPlayers == 1) {
                cli.printColoredMessage("Creating lobby...", Constants.GOLD_COLOR);
                cli.printColoredMessage("Created successfully! Lobby details: ", Constants.ANSI_GREEN);
            } else {
                // System.out.println("Creating lobby for " + numberOfPlayers + " players...");
                cli.printColoredMessage("\nJoining lobby...", Constants.GOLD_COLOR);
                cli.printColoredMessage("Joined successfully! Lobby details: ", Constants.ANSI_GREEN);
            }
            cli.printMessage("ID: " + gameID + "          Number of players: " + numberOfPlayers);
            cli.showGameLobbyMenu();
        }
    }
}
