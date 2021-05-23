package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.ServerMessageHandler;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.lobby.GameLobby;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI implements UI {

    private Scanner input;

    private ReducedModel reducedModel;
    private ServerMessageHandler serverMessageHandler;
    private String ip;
    private int port;

    public CLI() {
        this.input = new Scanner(System.in);
    }

    public String getIp() {
        System.out.println("Insert the server ip: ");
        System.out.print("> ");
        return input.nextLine();
    }

    public int getPort() {
        System.out.println("Insert the server port: ");
        System.out.print("> ");
        int port = input.nextInt();
        input.nextLine();
        return port;
    }

    public String readCommand() {
        System.out.println("Enter command:");
        System.out.print("> ");
        return input.nextLine();
    }

    public void printErrorMessage(String error) {
        System.out.println(error);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public String getNickname() {
        System.out.println("Insert nickname:");
        System.out.print("> ");
        return input.nextLine();
    }

    public void showGameLobbies(ArrayList<GameLobbyDetails> activeGameLobbies) {
        activeGameLobbies.forEach(gameLobbyDetails ->
                System.out.println(
                        "ID: " + gameLobbyDetails.id +
                                "         CREATOR: " + gameLobbyDetails.creator +
                                "         PLAYERS: " + gameLobbyDetails.connectedPlayers + "/" + gameLobbyDetails.maxPlayers));
    }
}
