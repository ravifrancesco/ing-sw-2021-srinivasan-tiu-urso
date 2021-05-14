package it.polimi.ingsw.server;

import java.io.IOException;

public class GameServer {
    public static void main(String[] args) throws IOException {
        Server server = new Server();

        server.run();
    }
}
