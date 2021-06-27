package it.polimi.ingsw.client;

import it.polimi.ingsw.client.IO.CLI;
import it.polimi.ingsw.client.IO.Constants;

import java.io.IOException;
import java.util.Scanner;

public class CLIClient {

    public void run() {
        CLI cli = new CLI();
        Scanner input = new Scanner(System.in);
        String choice;
        System.out.println(Constants.logo);
        System.out.println("Please choose");
        System.out.println("LOCAL to play locally");
        System.out.println("ONLINE to play online");
        while (true) {
            choice = input.nextLine();
            System.out.println(choice);
            switch (choice.toUpperCase()) {
                case "LOCAL" -> {
                    handleLocal(cli);
                    return; }
                case "ONLINE" -> {
                    handleServer(cli);
                    return; }
                default -> System.out.println("Invalid input, please try again");
            }
        }
    }

    public void handleServer(CLI cli) {
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

    public void handleLocal(CLI cli) {
        SinglePlayerView singlePlayerView = new SinglePlayerView(cli);
        singlePlayerView.run();

    }

}