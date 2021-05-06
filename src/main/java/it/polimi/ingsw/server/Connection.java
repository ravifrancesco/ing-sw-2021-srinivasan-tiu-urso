/*
package it.polimi.ingsw.server;

import com.sun.tools.javac.Main;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

public class Connection extends Observable implements Runnable {

    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private Server server;
    private String name;
    private MainLobby mainLobby;
    private boolean active = true;

    public Connection(Socket socket, Server server, MainLobby mainLobby){
        this.socket = socket;
        this.server = server;
        this.mainLobby = mainLobby;
    }

    private synchronized boolean isActive(){
        return active;
    }

    public void send(String message){
        out.println(message);
        out.flush();
    }

    public void asyncSend(final String message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    public synchronized void closeConnection(){
        send("Connection closed from the server side");
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        active = false;
    }

    private void close(){
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void run() {
        try{
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            send("Welcome! What's your name?");
            name = in.nextLine();
            server.lobby(this, name);
            while(isActive()){
                String read = in.nextLine();
                notify(read);
            }
        } catch(IOException e){
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }

    public void registerName(Scanner in, Scanner out, MainLobby mainLobby) throws InvalidNameException {
        name = in.nextLine();
        try {
            mainLobby.enterLobby(name);
        } catch (InvalidNameException e) {
            // TODO
            throw new InvalidNameException();
        }
    }
}

 */
