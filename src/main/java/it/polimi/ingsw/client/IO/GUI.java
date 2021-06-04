package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;

import java.util.ArrayList;

public class GUI implements UI {

    @Override
    public void startUI(ClientConnection clientConnection, ReducedModel reducedModel) {
        // TODO
    }

    @Override
    public UIType getType() {
        return UIType.GUI;
    }

    @Override
    public String getNickname() {
        // TODO
        return null;
    }

    @Override
    public void printMessage(String s) {
        // TODO
    }

    @Override
    public void printErrorMessage(String s) {
        // TODO
    }

    @Override
    public void printColoredMessage(String message, String ansiGreen) {
        // TODO
    }

    @Override
    public void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies) {
        // TODO
    }
}
