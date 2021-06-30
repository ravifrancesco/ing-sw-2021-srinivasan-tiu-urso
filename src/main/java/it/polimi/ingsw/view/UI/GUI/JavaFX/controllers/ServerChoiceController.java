package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;
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

    private GUI gui;

    @FXML
    private TextField ipField;
    @FXML
    private TextField portField;

    /**
     * Method to handle connect click event
     */
    @FXML
    private void handleConnectClick(InputEvent event) {
        if (!ipField.getText().equals("") && !portField.getText().equals("")) {
            try {
                new ClientVirtualView(ipField.getText(), Integer.parseInt(portField.getText()), gui);
                gui.getClientConnection().connectToServer();
                openNicknameWindow(event);
            } catch (IllegalArgumentException e) {
                gui.printErrorMessage("Invalid ip/port name");
            } catch (IOException e) {
                gui.printErrorMessage("IOException");
            }
        } else {
            gui.printErrorMessage("Fill all fields");
        }
    }

    /**
     * Method to handle offline click event
     */
    @FXML
    private void handleOfflineClick(InputEvent event) {
        new OfflineClientVirtualView(gui, "local_host");
        gui.enterGamePhase(true, true);
    }

    /**
     * Method to open Nickname Window
     */
    private void openNicknameWindow(InputEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/nickname_choice.fxml"));
            Parent root = fxmlLoader.load();
            gui.setNicknameChoiceController(fxmlLoader.getController());
            gui.getNicknameChoiceController().setGui(gui);
            Stage stage = new Stage();
            stage.setTitle("Choose a nickname");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for scene
     * @return the scene
     */
    public Scene getScene() {
        return ipField.getScene();
    }

    /**
     * Setter for the GUI
     * @param gui the GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
