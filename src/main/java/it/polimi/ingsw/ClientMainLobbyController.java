package it.polimi.ingsw;

import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.AskGameLobbies;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.CreateGameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.JoinGameLobby;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class ClientMainLobbyController {

    String selectedGame = null;

    GUI gui;

    @FXML private VBox listOfServersVBox;
    Node[] nodes;
    ServerItemController[] serverItemControllers;

    private final Background focusBackground = new Background( new BackgroundFill( Color.web( "#000000" ), CornerRadii.EMPTY, Insets.EMPTY ) );
    private final Background unfocusBackground = new Background( new BackgroundFill( Color.web( "#F4F4F4" ), CornerRadii.EMPTY, Insets.EMPTY ) );

    private int selectedNode = -1;

    @FXML
    public void handleRefreshClick(InputEvent event) {
        gui.getClientConnection().send(new AskGameLobbies());
    }

    @FXML
    public void handleJoinClick(InputEvent event) {
        if (selectedGame == null) {
            gui.printErrorMessage("Please select a game");
        } else {
            gui.getClientConnection().send(new JoinGameLobby(selectedGame));
            // TODO change windows
        }
    }

    @FXML
    public void handleCreateGameClick(InputEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Create game");
        alert.setHeaderText("You are creating a new game");
        alert.setContentText("Select the number of players");

        ButtonType buttonTypeOne = new ButtonType("Two");
        ButtonType buttonTypeTwo = new ButtonType("Three");
        ButtonType buttonTypeThree = new ButtonType("Four");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            gui.getClientConnection().send(new CreateGameLobby(2));
        } else if (result.get() == buttonTypeTwo) {
            gui.getClientConnection().send(new CreateGameLobby(3));
        } else if (result.get() == buttonTypeThree) {
            gui.getClientConnection().send(new CreateGameLobby(4));
        }
    }

    @FXML
    public void handleQuitClick(InputEvent event) {
        System.exit(0);
    }

    public void updateServerList(ArrayList<GameLobbyDetails> gameLobbies) {

        Platform.runLater(
                () -> {
                    listOfServersVBox.getChildren().clear();
                    nodes = new Node[gameLobbies.size()];
                    serverItemControllers = new ServerItemController[gameLobbies.size()];
                    for (int i = 0; i < nodes.length; i++) {
                        try {
                            final int j = i;
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/server_item.fxml"));
                            nodes[i] = loader.load();
                            serverItemControllers[i] = loader.getController();
                            serverItemControllers[i].setFields(gameLobbies.get(i));
                            //give the items some effect
                            nodes[i].setOnMouseEntered(event -> {   if (j != selectedNode)   { nodes[j].setStyle("-fx-background-color : #808080"); }});
                            nodes[i].setOnMouseExited(event -> {    if (j != selectedNode)   { nodes[j].setStyle("-fx-background-color : #FFFFFF"); }});
                            nodes[i].setOnMouseClicked(event -> {
                                if (selectedNode != -1) {
                                    nodes[selectedNode].setStyle("-fx-background-color : #FFFFFF");
                                }
                                selectedNode = j;
                                nodes[j].setStyle("-fx-background-color : #808080");
                                selectedGame = serverItemControllers[j].getGameID();
                            });
                            listOfServersVBox.getChildren().add(nodes[i]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public Scene getScene() {
        return listOfServersVBox.getScene();
    }


}
