package it.polimi.ingsw;


import java.io.IOException;

public class GameClient {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Client client = new Client("127.0.0.1", 53510);

        client.startClient();
    }
}
