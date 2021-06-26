package it.polimi.ingsw;

import it.polimi.ingsw.model.marbles.Marble;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerGetsFromMarket;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class MarketViewController {

    private final int NUM_OF_ROWS = 3;
    private final int NUM_OF_COLUMNS = 4;

    private GUI gui;

    @FXML
    GridPane marketGridPane;

    ImageView freeMarbleIW;
    ArrayList<ImageView> slotsIW;
    ArrayList<ImageView> marblesIW;

    @FXML
    Button backButton;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void initialize() {

        freeMarbleIW = new ImageView();
        slotsIW = new ArrayList<>();
        marblesIW = new ArrayList<>();
        marketGridPane.add(freeMarbleIW, 0, 5);

        for (int i = 1; i < 1 + NUM_OF_ROWS; i++) {
            ImageView slot;
            slot = new ImageView();
            marketGridPane.add(slot, i, 5);
            slotsIW.add(slot);
        }
        for (int i = 1; i < 1 + NUM_OF_COLUMNS; i++) {
            ImageView slot;
            slot = new ImageView();
            marketGridPane.add(slot, 4, i);
            slotsIW.add(slot);
        }


        for (int i = 1; i < 1 + NUM_OF_ROWS; i++) {
            for (int j = 1; j < 1 + NUM_OF_COLUMNS; j++) {
                ImageView marbleImage = new ImageView();
                marketGridPane.add(marbleImage, i, j);
                marblesIW.add(marbleImage);
            }
        }

        setDraggableFreeMarble();
    }

    public void update(Marble[][] marblesGrid, Marble freeMarble) {
        loadImage(freeMarbleIW, freeMarble);
        slotsIW.forEach(iw -> iw.setImage(null));
        for (int i = 0; i < NUM_OF_ROWS; i++) {
            for (int j = 0; j < NUM_OF_COLUMNS; j++) {
                loadImage(marblesIW.get(i * NUM_OF_COLUMNS + j), marblesGrid[i][j]);
            }
        }
    }

    private void loadImage(ImageView imageView, Marble marble) {
        String color = marble.getMarbleColor().toString().toLowerCase();
        File file = new File("src/main/resources/png/marbles/" + color + "_marble.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageView.setFitHeight(55);
        imageView.setFitWidth(55);
    }

    public void setDraggableFreeMarble() {
        freeMarbleIW.setOnDragDetected(event -> {
            /* drag was detected, start a drag-and-drop gesture*/
            /* allow any transfer mode */
            Dragboard db = freeMarbleIW.startDragAndDrop(TransferMode.MOVE);

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString("Dragging");
            db.setContent(content);

            event.consume();
        });

        slotsIW.forEach(t -> t.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != t &&
                    event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        }));

        slotsIW.forEach(t -> t.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
             * and if it has a string data */
            if (event.getGestureSource() != t &&
                    event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        }));

        slotsIW.forEach(t -> t.setOnDragEntered(event -> {
            /* the drag-and-drop gesture entered the target */
            /* show to the user that it is an actual gesture target */
            if (event.getGestureSource() != t &&
                    event.getDragboard().hasString()) {
                t.setStyle("-fx-background-color: GREEN");
            }
            event.consume();
        }));

        slotsIW.forEach(t -> t.setOnDragExited(event -> {
            /* mouse moved away, remove the graphical cues */
            t.setStyle("-fx-background-color: transparent");

            event.consume();
        }));

        slotsIW.forEach(t -> t.setOnDragDropped(event -> {
            /* data dropped */
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                t.setImage(freeMarbleIW.getImage());
                gui.getClientConnection().send(new PlayerGetsFromMarket(slotsIW.indexOf(t), new ArrayList<>())); // TODO chane to include wmr
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        }));

        freeMarbleIW.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag and drop gesture ended */
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    freeMarbleIW.setImage(null);
                }
                event.consume();
            }
        });

    }

    public void onBackPressed() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    
}
