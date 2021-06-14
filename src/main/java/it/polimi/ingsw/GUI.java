package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application implements UI {

    private ClientConnection clientConnection;
    private NicknameChoiceController nicknameChoiceController;
    private ServerChoiceController serverChoiceController;
    private ClientMainLobbyController clientMainLobbyController;
    private GameController gameController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ServerChoice.fxml"));
        Parent root = fxmlLoader.load();
        serverChoiceController = fxmlLoader.getController();
        serverChoiceController.setGui(this);
        primaryStage.setTitle("Choose a server");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

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
        return nicknameChoiceController.getNickname();
    }

    @Override
    public void printMessage(String s) {
        System.out.println(s);
    }

    @Override
    public void printErrorMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error box");
        alert.setHeaderText("");
        alert.setContentText(s);
        alert.showAndWait();
    }

    @Override
    public void printColoredMessage(String message, String ansiGreen) {
        // TODO
    }

    @Override
    public void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies) {
        clientMainLobbyController.updateServerList(gameLobbies);
    }

    @Override
    public void enterGamePhase() {
        Platform.runLater(
                () -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
                        Parent root = fxmlLoader.load();
                        gameController = fxmlLoader.getController();
                        gameController.setGui(this);
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root, 1000, 714.82));
                        stage.show();
                        clientMainLobbyController.getScene().getWindow().hide();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    public void setNicknameChoiceController(NicknameChoiceController nicknameChoiceController) {
        this.nicknameChoiceController = nicknameChoiceController;
    }

    public NicknameChoiceController getNicknameChoiceController() {
        return nicknameChoiceController;
    }

    public void setClientMainLobbyController(ClientMainLobbyController clientMainLobbyController) {
        this.clientMainLobbyController = clientMainLobbyController;
    }

    public ClientMainLobbyController getClientMainLobbyController() {
        return clientMainLobbyController;
    }

}
