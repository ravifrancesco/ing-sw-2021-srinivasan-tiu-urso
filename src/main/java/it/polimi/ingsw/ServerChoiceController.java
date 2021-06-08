package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerChoiceController {

    GUI gui;

    @FXML private TextField ipField;
    @FXML private TextField portField;

    @FXML
    private void handleConnectClick(InputEvent event) {
        if (!ipField.getText().equals("") && !portField.getText().equals("")) {
            try {
                gui.setClientConnection(new ClientConnection(ipField.getText(), Integer.parseInt(portField.getText()), gui));
                gui.getClientConnection().connectToServer();
                openNicknameWindow(event);
            } catch (IllegalArgumentException e) {
                gui.printErrorMessage("Invalid ip/port name");
            } catch (IOException e) {
                gui.printErrorMessage("IOException");
            }
        }
        else {
            gui.printErrorMessage("Fill all fields");
        }
    }

    private void openNicknameWindow(InputEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NicknameChoice.fxml"));
            Parent root = fxmlLoader.load();
            gui.setNicknameChoiceController(fxmlLoader.getController());
            gui.getNicknameChoiceController().setGui(gui);
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

    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
