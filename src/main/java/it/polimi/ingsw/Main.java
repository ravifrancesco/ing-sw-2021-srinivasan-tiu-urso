package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.view.UI.CLI.CLIInitializer;
import it.polimi.ingsw.view.UI.GUI.GUI;
import javafx.application.Application;

import java.io.IOException;

/**
 * Main class for launching the application
 */
public class Main {

    private static String errorMessage = "Please select the arguments as follows:\n> server <ip> <port>\n> ui cli\n> ui gui";

    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("server")) {
            Server server = null;
            try {
                server = new Server(args[1], Integer.parseInt(args[2]));
            } catch (IOException e) {
                System.out.println(errorMessage);
            }
            server.run();
        } else if (args[0].equalsIgnoreCase("ui")) {
            if (args[1].equalsIgnoreCase("cli")) {
                CLIInitializer CLIInitializer = new CLIInitializer();
                CLIInitializer.run();
            } else if (args[1].equalsIgnoreCase("gui")) {
                new GUI();
                Application.launch(args);
            } else {
                System.out.println(errorMessage);
            }
        } else {
            System.out.println(errorMessage);
        }
    }

}
