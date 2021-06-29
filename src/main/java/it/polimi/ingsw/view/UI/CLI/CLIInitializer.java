package it.polimi.ingsw.view.UI.CLI;

import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;

import java.io.IOException;
import java.util.Scanner;

public class CLIInitializer {

    /**
     * Runs the cli
     * @param args the arguments
     */
    public static void main(String[] args) {
        CLIInitializer CLIInitializer = new CLIInitializer();
        CLIInitializer.run();
    }

    /**
     * Runs the CLI, handling the connection to the game either locally or online
     */
    public void run() {
        CLI cli = new CLI();
        Scanner input = new Scanner(System.in);
        String choice;
        System.out.println(Constants.logo);
        System.out.println("Please choose");
        System.out.println("LOCAL to play locally");
        System.out.println("ONLINE to play online");
        System.out.print("> ");
        while (true) {
            choice = input.nextLine();
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

    /**
     * Handles the multiplayer game setup
     * @param cli the CLI
     */
    public void handleServer(CLI cli) {
        boolean connected = false;
        try {
            ClientVirtualView clientVirtualView;
            do {
                String ip = cli.getIp();
                int port = cli.getPort();
                 clientVirtualView = new ClientVirtualView(ip, port, cli);
                try {
                    clientVirtualView.connectToServer();
                    connected = true;
                } catch (Exception e) {
                    System.out.println("Connection to server failed, please try again.");
                }
            } while (!connected);

            while (!clientVirtualView.isNameRegistered()) {
                clientVirtualView.registerName();
            }
            clientVirtualView.run();
        } catch (IllegalArgumentException e) {
            cli.printErrorMessage("Invalid ip/port name");
        } catch (IOException e) {
            cli.printErrorMessage("IOException");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the local game setup
     * @param cli the CLI
     */
    public void handleLocal(CLI cli) {
        OfflineClientVirtualView offlineClientVirtualView = new OfflineClientVirtualView(cli);
        offlineClientVirtualView.run();
    }

}