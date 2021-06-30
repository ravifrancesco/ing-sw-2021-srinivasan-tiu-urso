package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.network.server.lobby.GameLobbyDetails;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ServerItemController {

    private String gameID;

    @FXML
    private Text hostNameTextView;

    @FXML
    private Text playersConnectedTextView;

    @FXML
    private Text maxPlayersTextView;

    /**
     * Setter for the fields
     * @param gameLobbyDetails an object that represents the details of a game lobby
     */
    public void setFields(GameLobbyDetails gameLobbyDetails) {

        this.gameID = gameLobbyDetails.id;

        hostNameTextView.setText(gameLobbyDetails.creator);
        playersConnectedTextView.setText(String.valueOf(gameLobbyDetails.connectedPlayers));
        maxPlayersTextView.setText(String.valueOf(gameLobbyDetails.maxPlayers));

    }

    /**
     * Getter for the Game ID
     * @return the Game ID
     */
    public String getGameID() {
        return gameID;
    }
}
