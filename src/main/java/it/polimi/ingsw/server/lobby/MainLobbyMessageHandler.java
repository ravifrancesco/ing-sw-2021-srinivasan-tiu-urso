package it.polimi.ingsw.server.lobby;

import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.ServerMessages;

import javax.naming.InvalidNameException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainLobbyMessageHandler {

    private final MainLobby mainLobby;

    private final List<GameLobby> gameLobbies;

    public MainLobbyMessageHandler(MainLobby mainLobby) {
        this.mainLobby = mainLobby;
        this.gameLobbies = new ArrayList<>();
    }

    public synchronized void handleMessage(String msg, Connection c) {

        switch (LobbyMessages.valueOf(msg.toUpperCase())) {
            case GET_SERVER_STATUS:
                getServerStatus(c);
            case CREATE_GAME:
                createGame(c);
            case JOIN_GAME:
                joinGame(c);
            default:
                c.send(ServerMessages.ERROR);
        }

    }

    public void getServerStatus(Connection c) {
        c.asyncSend(this.gameLobbies.stream()
                .collect(Collectors.toMap(GameLobby::getId, GameLobby::getLobbyStatus)));
    }

    public void createGame(Connection c) {

        try {
            int numberOfPlayers = (int) c.receive();
            mainLobby.createGame(c, numberOfPlayers);
        } catch (IOException | ClassNotFoundException e) {
            c.send(ServerMessages.ERROR);
        } catch (InvalidNameException e) {
            c.send(LobbyMessages.INVALID_NAME);
        } catch (IllegalStateException e) {
            c.send(ServerMessages.SERVER_FULL);
        }

    }

    public void joinGame(Connection c) {

        try {
            String gameID = (String) c.receive();
            mainLobby.joinGame(c, gameID);
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            c.send(ServerMessages.ERROR);
        } catch (InvalidNameException e) {
            c.send(LobbyMessages.INVALID_NAME);
        } catch (IllegalStateException e) {
            c.send(LobbyMessages.GAME_FULL);
        }

    }


}
