package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.Scanner;

public class Client {
    private CLI cli;


    public void run() {
        CLI cli = new CLI();
        while (true) {
            try {
                String ip = cli.getIp();
                int port = cli.getPort();
                ClientConnection clientConnection = new ClientConnection(ip, port, cli);
                clientConnection.connectToServer();
                clientConnection.registerName();
                return;
            } catch (IllegalArgumentException e) {
                cli.printErrorMessage("Invalid ip/port name");
            } catch (IOException e) {
                cli.printErrorMessage("IOException");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }
}