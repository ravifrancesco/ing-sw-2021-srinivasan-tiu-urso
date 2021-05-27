package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;

import java.util.ArrayList;
import java.util.Scanner;

public class CLI implements UI {

    private Scanner input;

    private ReducedModel reducedModel;
    private String ip;
    private int port;

    public CLI() {
        this.input = new Scanner(System.in);
        this.reducedModel = new ReducedModel();
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

    public ReducedModel getReducedModel() {
        return reducedModel;
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
        if (activeGameLobbies.size() > 0) {
            activeGameLobbies.forEach(gameLobbyDetails ->
                    System.out.println(
                            "ID: " + gameLobbyDetails.id +
                                    "         CREATOR: " + gameLobbyDetails.creator +
                                    "         PLAYERS: " +
                                    gameLobbyDetails.connectedPlayers + "/" + gameLobbyDetails.maxPlayers));
        } else {
            System.out.println("No game lobbies found");
        }


    }


}
