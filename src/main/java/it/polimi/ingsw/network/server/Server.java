package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.server.lobby.MainLobby;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//  TODO Logger
public class Server {
    public static final int THREAD_NUMBER = 256;
    static final int PORT = 3001;
    private final ServerSocket serverSocket;

    private final MainLobby mainLobby;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.mainLobby = new MainLobby();
    }

    public void run() {
        System.out.println("Server listening on port: " + PORT);
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                ServerVirtualView serverVirtualView = new ServerVirtualView(socket, mainLobby);
                mainLobby.registerConnection(serverVirtualView);

                System.out.println("Connection registered!");
            } catch (IOException e) {
                System.err.println("Connection error!");
            }
        }
    }


}



