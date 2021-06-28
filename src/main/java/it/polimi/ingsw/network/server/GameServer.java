package it.polimi.ingsw.network.server;

import java.io.IOException;

public class GameServer {
    public static void main(String[] args) throws IOException {
        Server server = new Server();

        server.run();
    }
}
