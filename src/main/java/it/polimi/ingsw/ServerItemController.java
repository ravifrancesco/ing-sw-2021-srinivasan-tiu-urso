package it.polimi.ingsw;

import it.polimi.ingsw.server.lobby.GameLobbyDetails;
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

        gameID = gameLobbyDetails.id;

        hostNameTextView.setText(gameLobbyDetails.creator);
        playersConnectedTextView.setText(String.valueOf(gameLobbyDetails.connectedPlayers));
        maxPlayersTextView.setText(String.valueOf(gameLobbyDetails.maxPlayers));

    }

}
