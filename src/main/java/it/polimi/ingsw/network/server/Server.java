package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.server.lobby.MainLobby;
import it.polimi.ingsw.view.virtualView.server.ServerVirtualView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int THREAD_NUMBER = 256;
    final int port;
    private final ServerSocket serverSocket;

    private final MainLobby mainLobby;

    public Server(String IP, int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port, 1, InetAddress.getByName(IP));
        this.mainLobby = new MainLobby();
    }

    public void run() {
        System.out.println("Server listening on port: " + port);
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



