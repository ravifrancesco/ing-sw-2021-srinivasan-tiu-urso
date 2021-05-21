package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.ServerMessageHandler;
import it.polimi.ingsw.client.UI;

import java.io.IOException;
import java.io.PrintWriter;
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
        System.out.println("insert ip");
        return input.nextLine();
    }

    public int getPort() {
        System.out.println("insert port");
        int port = input.nextInt();
        input.nextLine();
        return port;
    }

    public String readCommand() {
        System.out.println("Enter command");
        return input.nextLine();
    }

    public void printErrorMessage(String error) {
        System.out.println(error);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public String getNickname() {
        System.out.println("insert nickname");
        return input.nextLine();
    }
}
