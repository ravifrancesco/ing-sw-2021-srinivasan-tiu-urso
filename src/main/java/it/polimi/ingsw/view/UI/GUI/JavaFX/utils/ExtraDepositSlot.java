package it.polimi.ingsw.view.UI.GUI.JavaFX.utils;

import it.polimi.ingsw.model.full.table.Resource;

public class ExtraDepositSlot extends Slot {

    private boolean available;
    private Resource slotType;

    /**
     * Constructor for an extra deposit slot
     * @param x the X of the slot
     * @param y the Y of the slot
     * @param width the width of the slot
     * @param height the height of the slot
     */
    public ExtraDepositSlot(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.available = false;
    }

    /**
     * Getter for the slot type
     * @return the type of resource of the slot
     */
    public Resource getSlotType() {
        return slotType;
    }

    /**
     * Setter for the slot type
     * @param slotType the type of resource of the slot
     */
    public void setSlotType(Resource slotType) {
        this.slotType = slotType;
    }

    /**
     * Getter for available
     * @return true if the slot is available, false otherwise
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Setter for available
     * @param available the availability of the slot
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
