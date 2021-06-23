package it.polimi.ingsw;

import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.AskGameLobbies;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class NicknameChoiceController {

    private GUI gui;

    @FXML private TextField nicknameField;

    @FXML
    private void handleChooseClick(InputEvent event) {
        if (!nicknameField.getText().equals("")) {
            try {
                gui.getClientConnection().registerName();
                if (gui.getClientConnection().isNameRegistered()) {
                    gui.printMessage("Nickname choosen");
                    gui.getClientConnection().run();
                    openClientMainLobbyWindow(event);
                }
            } catch (IllegalArgumentException e) {
                gui.printErrorMessage("Invalid ip/port name");
            } catch (IOException e) {
                gui.printErrorMessage("IOException");
            } catch (ClassNotFoundException e) {
                gui.printErrorMessage("Class not found exception");
            }
        }
        else {
            gui.printErrorMessage("Fill nickname field");
        }
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public String getNickname() {
        return nicknameField.getText();
    }

    private void openClientMainLobbyWindow(InputEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/client_main_lobby.fxml"));
            Parent root = fxmlLoader.load();
            gui.setClientMainLobbyController(fxmlLoader.getController());
            gui.getClientMainLobbyController().setGui(gui);
            gui.getClientConnection().send(new AskGameLobbies());
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
