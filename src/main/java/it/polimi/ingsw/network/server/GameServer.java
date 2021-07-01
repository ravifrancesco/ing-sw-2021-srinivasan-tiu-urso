package it.polimi.ingsw.network.server;

import java.io.IOException;

public class GameServer {
    // TODO remove class
    public static void main(String[] args) throws IOException {
        Server server = new Server(args[0], Integer.parseInt(args[1]));

        server.run();
    }
}
