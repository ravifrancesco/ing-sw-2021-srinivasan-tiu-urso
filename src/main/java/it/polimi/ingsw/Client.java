package it.polimi.ingsw;


import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        GameClient gameClient = new GameClient("127.0.0.1", 53510);

        gameClient.startClient();
        System.err.println("prova");
    }
}
