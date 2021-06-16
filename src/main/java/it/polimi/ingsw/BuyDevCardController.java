package it.polimi.ingsw;

import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.BuyDevelopmentCardGameMessage;
import it.polimi.ingsw.utils.Pair;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class BuyDevCardController {

    @FXML
    ImageView cardImageView;

    private GUI gui;

    private Pair<Integer, Integer> position;

    public void setParameters(GUI gui, int id, int row, int col) {
        this.gui = gui;
        loadCardImage(id);
        this.position = new Pair<>(row, col);
    }

    public void onBuyPressed() {
        // TODO load giuse fxml
    }

    public void onCancelPressed() {
        // TODO to close card
    }

    public void onConfirmPressed() {
        // TODO giuse your job
        // TODO gui.getClientConnection().send(new BuyDevelopmentCardGameMessage(/* TODO add parameted */));
    }

    public void onCancelResourcePressed() {
        // TODO giuse
    }

    private void loadCardImage(int id) {
        File file = new File("src/main/resources/png/cards/devCards/dev_card_" + id + ".png");
        Image image = new Image(file.toURI().toString());
        cardImageView.setImage(image);
        cardImageView.setFitHeight(446);
        cardImageView.setFitWidth(309);
    }

}
