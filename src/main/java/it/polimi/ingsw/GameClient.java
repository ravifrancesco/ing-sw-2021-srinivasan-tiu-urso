package it.polimi.ingsw;

import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.CreateGameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.RegisterName;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.WelcomeMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    private String ip;
    private int port;

    public GameClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection has been made!");
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);
        WelcomeMessage welcomeMessage = (WelcomeMessage) ois.readObject();
        System.out.println(welcomeMessage.toString());
        String toSend;
        RegisterName rn;

        while(true) {
            toSend = stdin.nextLine();
            rn = new RegisterName(toSend);
            oos.writeObject(rn);
            CreateGameLobby cgl = new CreateGameLobby();
            oos.writeObject(cgl);
        }

    }
}

