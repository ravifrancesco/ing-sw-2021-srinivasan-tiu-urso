package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.network.server.lobby.GameLobbyDetails;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ServerItemController {

    String gameID;

    @FXML
    Text hostNameTextView;

    @FXML
    Text playersConnectedTextView;

    @FXML
    Text maxPlayersTextView;

    public void setFields(GameLobbyDetails gameLobbyDetails) {

        this.gameID = gameLobbyDetails.id;

        hostNameTextView.setText(gameLobbyDetails.creator);
        playersConnectedTextView.setText(String.valueOf(gameLobbyDetails.connectedPlayers));
        maxPlayersTextView.setText(String.valueOf(gameLobbyDetails.maxPlayers));

    }

    public String getGameID() {
        return gameID;
    }
}
