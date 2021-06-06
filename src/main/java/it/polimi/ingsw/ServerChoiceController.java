package it.polimi.ingsw;

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

    @FXML private TextField ipfield;
    @FXML private TextField portfield;

    @FXML
    private void handleButtonAction(InputEvent event) {
        if (!ipfield.getText().equals("") && !portfield.getText().equals("")) {
            System.out.println("IP = " + ipfield.getText() + " PORT = " + portfield.getText());
            openNicknameWindow(event);
        } else {
            System.out.println("Fill all fields");
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
}
