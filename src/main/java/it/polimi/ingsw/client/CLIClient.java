package it.polimi.ingsw.client;

import it.polimi.ingsw.client.IO.CLI;

import java.io.IOException;

public class CLIClient {
    private CLI cli;

    public void run() {
        CLI cli = new CLI();
        while (true) {
            try {
                String ip = cli.getIp();
                int port = cli.getPort();
                ClientConnection clientConnection = new ClientConnection(ip, port, cli);
                clientConnection.connectToServer();
                while (!clientConnection.isNameRegistered()) {
                    clientConnection.registerName();
                }

                clientConnection.run();
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