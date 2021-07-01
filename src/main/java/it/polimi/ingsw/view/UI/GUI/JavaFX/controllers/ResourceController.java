package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerChangesExtraDeposit;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerStoresFromSupply;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerStoresFromSupplyToExtraDeposit;
import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.ExtraDepositSlot;
import it.polimi.ingsw.view.UI.GUI.JavaFX.utils.Slot;
import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class ResourceController {

    private GUI gui;

    @FXML
    private ImageView item;

    private double x;

    private double y;

    private Resource resourceType;

    private Slot[] slots;

    private ExtraDepositSlot[] leaderSlots;

    private int currentSlot;

    private int supplyPos;

    /**
     * Method to assign resources slots
     * @param slots the deposit slots
     * @param leaderSlots the leader slots
     */
    public void assignSlots(Slot[] slots, ExtraDepositSlot[] leaderSlots) {
        this.slots = slots;
        this.leaderSlots = leaderSlots;
    }

    /**
     * Method to create an item
     * @param resourceType the type of resource
     * @param pos the position of the resource
     */
    public void createItem(Resource resourceType, int pos) {
        String name = "";
        int newPos;
        this.resourceType = resourceType;
        switch (resourceType) {
            case GOLD -> name = "gold";
            case SHIELD -> name = "shield";
            case STONE -> name = "stone";
            case SERVANT -> name = "servant";
        }
        Image image = new Image(this.getClass().getResourceAsStream("/png/resources/" + name + ".png"));
        item = new ImageView(image);
        item.setFitWidth(25);
        item.setFitHeight(25);
        item.setOnMousePressed(this::pressed);
        item.setOnMouseDragged(this::dragged);
        item.setOnMouseReleased(this::released);

        if (pos < GameController.NUM_SHELFES) {
            setX((slots[pos].getX() + slots[pos].getWidth() / 2) - (item.getFitWidth() / 2));
            setY((slots[pos].getY() + slots[pos].getHeight() / 2) - (item.getFitWidth() / 2));
            slots[pos].filLSlot();
        } else {
            newPos = pos - GameController.NUM_SHELFES;
            setX((leaderSlots[newPos].getX() + leaderSlots[newPos].getWidth() / 2) - (item.getFitWidth() / 2));
            setY((leaderSlots[newPos].getY() + leaderSlots[newPos].getHeight() / 2) - (item.getFitWidth() / 2));
            leaderSlots[newPos].filLSlot();
        }
        currentSlot = pos;
        supplyPos = -1;
    }

    /**
     * Method to create a supply item
     * @param resourceType the type of resource
     * @param pos the position of the resource in supply
     */
    public void createSupplyItem(Resource resourceType, int pos) {
        String name = "";
        this.resourceType = resourceType;
        switch (resourceType) {
            case GOLD -> name = "gold";
            case SHIELD -> name = "shield";
            case STONE -> name = "stone";
            case SERVANT -> name = "servant";
        }
        Image image = new Image(this.getClass().getResourceAsStream("/png/resources/" + name + ".png"));
        item = new ImageView(image);
        item.setFitWidth(25);
        item.setFitHeight(25);
        item.setOnMousePressed(this::pressed);
        item.setOnMouseDragged(this::dragged);
        item.setOnMouseReleased(this::released);

        setX(280 + 70 * pos - item.getFitWidth() / 2);
        setY(90 - item.getFitHeight() / 2);
        currentSlot = -1;
        supplyPos = pos;
    }

    /**
     * Set disable
     */
    public void setDisable() {
        item.setDisable(true);
    }

    /**
     * Set enable
     */
    public void setEnable() {
        item.setDisable(false);
    }

    /**
     * Setter for the GUI
     * @param gui the GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Setter for the x of imageviev
     * @param x the x coordinate
     */
    public void setX(double x) {
        this.x = x;
        item.setX(this.x);
    }

    /**
     * Setter for the y of imageviev
     * @param y the y coordinate
     */
    public void setY(double y) {
        this.y = y;
        item.setY(this.y);
    }

    /**
     * Getter for the item
     * @return the imageview of the resource
     */
    public ImageView getItem() {
        return this.item;
    }

    /**
     * Method to handle pressed event on a resource
     */
    @FXML
    public void pressed(MouseEvent event) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        item.setEffect(colorAdjust);
    }

    /**
     * Method to handle dragged event on a resource
     */
    private void dragged(MouseEvent event) {
        item.setX(event.getX() - item.getFitWidth() / 2);
        item.setY(event.getY() - item.getFitHeight() / 2);
    }

    /**
     * Method to handle released event on a resource
     */
    private void released(MouseEvent event) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        item.setEffect(colorAdjust);
        computePosition(event);
    }

    /**
     * Method to compute a position of a resource after releasing
     */
    private void computePosition(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        for (int i = 0; i < GameController.NUM_SHELFES; i++) {
            if (slots[i].isPointInSlot(x, y) && slots[i].isEmpty()) {
                setX((slots[i].getX() + slots[i].getWidth() / 2) - (item.getFitWidth() / 2));
                setY((slots[i].getY() + slots[i].getHeight() / 2) - (item.getFitWidth() / 2));
                slots[i].filLSlot();
                if (currentSlot != -1) {
                    if (currentSlot < GameController.NUM_SHELFES) {
                        slots[currentSlot].freeSlot();
                        if (supplyPos == -1) {
                            gui.setDisableNoDeposit();
                            gui.setDisableImageViews();
                            gui.showWarehouseButtons();
                            gui.changeResourceController(currentSlot, i);
                        }
                    } else {
                        leaderSlots[currentSlot - GameController.NUM_SHELFES].freeSlot();
                        if (supplyPos == -1) {
                            gui.changeResourceController(currentSlot, i);
                            int lcIndex = (currentSlot - GameController.NUM_SHELFES) / 2;
                            gui.send(new PlayerChangesExtraDeposit(gui.getDepositView(), gui.getExtraDepositView(lcIndex), lcIndex));
                        }
                    }
                }
                currentSlot = i;
                if (supplyPos != -1) {
                    gui.changeSupplyController(supplyPos, currentSlot);
                    gui.send(new PlayerStoresFromSupply(supplyPos, currentSlot));
                    supplyPos = -1;
                }
                return;
            }
        }

        for (int i = 0; i < GameController.SIZE_EXTRA_DEPOSITS; i++) {
            if (leaderSlots[i].isPointInSlot(x, y) && leaderSlots[i].isEmpty() && leaderSlots[i].isAvailable() && resourceType == leaderSlots[i].getSlotType()) {
                setX((leaderSlots[i].getX() + leaderSlots[i].getWidth() / 2) - (item.getFitWidth() / 2));
                setY((leaderSlots[i].getY() + leaderSlots[i].getHeight() / 2) - (item.getFitWidth() / 2));
                leaderSlots[i].filLSlot();
                if (currentSlot != -1) {
                    if (currentSlot < GameController.NUM_SHELFES) {
                        slots[currentSlot].freeSlot();
                    } else {
                        leaderSlots[currentSlot - GameController.NUM_SHELFES].freeSlot();
                    }
                    if (supplyPos == -1) {
                        gui.changeResourceController(currentSlot, i + GameController.NUM_SHELFES);
                        gui.send(new PlayerChangesExtraDeposit(gui.getDepositView(), gui.getExtraDepositView(i / 2), i / 2));
                    }
                }
                currentSlot = i + GameController.NUM_SHELFES;
                if (supplyPos != -1) {
                    gui.changeSupplyController(supplyPos, currentSlot);
                    gui.send(new PlayerStoresFromSupplyToExtraDeposit(i / 2, supplyPos, i % 2));
                    supplyPos = -1;
                }
                return;
            }
        }

        setX(this.x);
        setY(this.y);
    }

    /**
     * Getter for the resource type
     * @return the resource
     */
    public Resource getResourceType() {
        return resourceType;
    }
}
