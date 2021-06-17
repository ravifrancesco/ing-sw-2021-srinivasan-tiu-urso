package it.polimi.ingsw;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ChooseResourceController {

    private static final int NUM_SHELFES = 6;
    private Slot[] depositSlots;

    @FXML
    private Pane pane;

    // TODO leaderCard WE

    @FXML
    public void initialize() {
        depositSlots = new Slot[NUM_SHELFES];
        depositSlots[0] = new Slot(230, 20, 40, 40);
        depositSlots[1] = new Slot(210, 60, 40, 40);
        depositSlots[2] = new Slot(250, 60, 40, 40);
        depositSlots[3] = new Slot(190, 100, 40, 40);
        depositSlots[4] = new Slot(230, 100, 40, 40);
        depositSlots[5] = new Slot(270, 100, 40, 40);

        for (Slot depositSlot : depositSlots) {
            depositSlot.setStroke(Color.RED);
            pane.getChildren().add(depositSlot.getRectangle());
        }


    }
}
