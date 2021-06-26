package it.polimi.ingsw;

import it.polimi.ingsw.model.cards.LeaderCard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LeaderCardController {

    private GUI gui;

    @FXML private ImageView item;

    private LeaderCard leaderCard;

    private Slot[] slots;

    private int currentSlot;

    public void assignSlots(Slot[] slots) {
        this.slots = slots;
    }

    public void createItem(LeaderCard leaderCard, int slot) {
        String name = "leader_card_" + leaderCard.getId() + ".png";
        File file = new File("src/main/resources/png/cards/leaderCards/"+name);
        Image image = new Image(file.toURI().toString());
        item = new ImageView(image);
        item.setFitWidth(slots[slot].getWidth());
        item.setFitHeight(slots[slot].getHeight());

        // TODO

        //item.setOnMousePressed(this::pressed);
        //item.setOnMouseDragged(this::dragged);
        //item.setOnMouseReleased(this::released);

        item.setX(slots[slot].getX());
        item.setY(slots[slot].getY());

        slots[slot].filLSlot();
        currentSlot = slot;
        this.leaderCard = leaderCard;
    }

    public ImageView getItem() {
        return this.item;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void setDarker() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        item.setEffect(colorAdjust);
    }

    public void openProductionPowerView(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/leader_production.fxml"));
            Parent root = fxmlLoader.load();
            LeaderProductionController leaderProductionController = fxmlLoader.getController();
            leaderProductionController.setGui(this.gui);
            leaderProductionController.setResources();
            leaderProductionController.setCardIndex(currentSlot);
            Stage stage = new Stage();
            stage.setTitle("Select resources");
            stage.setScene(new Scene(root, 500, 700));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
