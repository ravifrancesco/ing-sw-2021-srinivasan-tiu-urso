package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.Slot;
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

public class DevelopmentCardController {

    private GUI gui;

    @FXML
    private ImageView item;

    private Slot[] slots;

    private int currentSlot;

    /**
     * Method to assign the development card slots
     * @param slots the slots for the development cards
     */
    public void assignSlots(Slot[] slots) {
        this.slots = slots;
    }

    /**
     * Method to create an item
     * @param developmentCard the development card to show
     * @param slot the slot where the card is placed
     */
    public void createItem(DevelopmentCard developmentCard, int slot) {
        String name = "dev_card_" + developmentCard.getId() + ".png";
        Image image = new Image(this.getClass().getResourceAsStream("/png/cards/devCards/" + name));
        item = new ImageView(image);
        item.setFitWidth(slots[slot].getWidth());
        item.setFitHeight(slots[slot].getHeight());

        item.setX(slots[slot].getX());
        item.setY(slots[slot].getY());

        slots[slot].filLSlot();
        currentSlot = slot;
    }

    /**
     * Getter for the item
     * @return the item
     */
    public ImageView getItem() {
        return this.item;
    }

    /**
     * Setter for the GUI
     * @param gui the GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Method to set the card darker
     */
    public void setDarker() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        item.setEffect(colorAdjust);
    }

    /**
     * Method to open ProductionPowerView
     */
    public void openProductionPowerView(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/production_power.fxml"));
            Parent root = fxmlLoader.load();
            DevelopmentProductionController developmentProductionController = fxmlLoader.getController();
            developmentProductionController.setGui(this.gui);
            developmentProductionController.setResources();
            developmentProductionController.setCardIndex(currentSlot);
            Stage stage = new Stage();
            stage.setTitle("Select resources");
            stage.setScene(new Scene(root, 500, 600));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
