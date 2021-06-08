package it.polimi.ingsw;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;

import java.io.IOException;

public class NicknameChoiceController {

    GUI gui;

    @FXML private TextField nicknameField;

    @FXML
    private void handleChooseClick(InputEvent event) {
        if (!nicknameField.getText().equals("")) {
            try {
                gui.getClientConnection().registerName();
                if (gui.getClientConnection().isNameRegistered()) {
                    gui.printMessage("Nickname choosen");
                    // TODO change screen
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
}
