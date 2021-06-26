package it.polimi.ingsw;

import it.polimi.ingsw.model.Resource;

public class ExtraDepositSlot extends Slot {

    private boolean available;
    private Resource slotType;

    public ExtraDepositSlot(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.available = false;
    }

    public Resource getSlotType() {
        return slotType;
    }

    public void setSlotType(Resource slotType) {
        this.slotType = slotType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
