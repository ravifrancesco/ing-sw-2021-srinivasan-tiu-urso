package it.polimi.ingsw.client;


import java.io.IOException;

public class GameClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client();

        client.run();
    }
}
