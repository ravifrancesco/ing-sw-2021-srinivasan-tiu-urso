package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GUI extends Application implements UI {

    private static ClientConnection clientConnection;

    @FXML private TextField ipField;
    @FXML private TextField portField;
    @FXML private TextField nicknameField;

    @FXML
    private void handleConnectClick(InputEvent event) {
        if (!ipField.getText().equals("") && !portField.getText().equals("")) {
            try {
                clientConnection = new ClientConnection(ipField.getText(), Integer.parseInt(portField.getText()), this);
                clientConnection.connectToServer();
                openNicknameWindow(event);
            } catch (IllegalArgumentException e) {
                printErrorMessage("Invalid ip/port name");
            } catch (IOException e) {
                printErrorMessage("IOException");
            }
        }
        else {
            printErrorMessage("Fill all fields");
        }
    }

    @FXML
    private void handleChooseClick(InputEvent event) {
        if (!getNickname().equals("")) {
            try {
                clientConnection.registerName();
                if (clientConnection.isNameRegistered()) {
                    printMessage("Nickname choosen");
                    // TODO change screen
                }
            } catch (IllegalArgumentException e) {
                printErrorMessage("Invalid ip/port name");
            } catch (IOException e) {
                printErrorMessage("IOException");
            } catch (ClassNotFoundException e) {
                printErrorMessage("Class not found exception");
            }
        }
        else {
            printErrorMessage("Fill nickname field");
        }
    }

    private void openNicknameWindow(InputEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/NicknameChoice.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Choose a nickname");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ServerChoice.fxml"));
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
        return nicknameField.getText();
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
}
