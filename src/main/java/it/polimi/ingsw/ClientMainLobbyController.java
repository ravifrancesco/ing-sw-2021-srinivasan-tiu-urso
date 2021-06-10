package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.AskGameLobbies;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.GameLobbiesMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class ClientMainLobbyController {

    GUI gui;

    @FXML private Button refreshButton;

    @FXML private VBox listOfServersVBox;

    @FXML
    public void handleRefreshClick(InputEvent event) {
        this.gui.getClientConnection().send(new AskGameLobbies());
    }

    @FXML
    public void handleJoinClick(InputEvent event) {
        // TODO
    }

    @FXML
    public void handleCreateGameClick(InputEvent event) {
        // TODO
    }

    @FXML
    public void handleCancelClick(InputEvent event) {
        // TODO
    }

    public void updateServerList(ArrayList<GameLobbyDetails> gameLobbies) {
        Node[] nodes = new Node[gameLobbies.size()];
        for (int i = 0; i < nodes.length; i++) {
            try {
                final int j = i;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/server_item.fxml"));
                nodes[i] = loader.load();
                ServerItemController serverItemController = loader.getController();
                serverItemController.setFields(gameLobbies.get(i));
                //give the items some effect
                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : #02030A");
                });
                listOfServersVBox.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }


}
