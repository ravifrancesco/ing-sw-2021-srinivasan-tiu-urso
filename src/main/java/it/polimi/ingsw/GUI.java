package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;

public class GUI extends Application implements UI {

    private ClientConnection clientConnection;
    private NicknameChoiceController nicknameChoiceController;
    private ServerChoiceController serverChoiceController;

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
        System.err.println(s);
    }

    @Override
    public void printColoredMessage(String message, String ansiGreen) {
        // TODO
    }

    @Override
    public void showGameLobbies(ArrayList<GameLobbyDetails> gameLobbies) {
        // TODO
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
}
