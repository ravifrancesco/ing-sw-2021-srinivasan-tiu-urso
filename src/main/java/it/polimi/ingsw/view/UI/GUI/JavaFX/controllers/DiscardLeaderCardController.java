package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.view.UI.GUI.GUI;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerDiscardsExcessLeaderCards;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.List;

public class DiscardLeaderCardController {

    @FXML
    HBox leaderCardContainerHBox;

    public GUI gui;

    int selectedCard;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void setCards(List<LeaderCard> leaderCards) {
        leaderCards.forEach(c -> setLeaderCard(c, leaderCards.indexOf(c)));
    }

    private void setLeaderCard(LeaderCard leaderCard, int index) {
        File file = new File("src/main/resources/png/cards/leaderCards/leader_card_" + leaderCard.getId()  + ".png");
        javafx.scene.image.Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        leaderCardContainerHBox.getChildren().add(imageView);
        imageView.setFitHeight(200);
        imageView.setFitWidth(132);
        HBox.setMargin(imageView, new Insets(10,10,10,10));
        imageView.setOnMouseClicked(event -> {
            gui.send(new PlayerDiscardsExcessLeaderCards(index));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        });

    }

    private void setBrightnessLow(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        imageView.setEffect(colorAdjust);
    }

    private void setBrightnessHigh(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        imageView.setEffect(colorAdjust);
    }

    @FXML
    private void onCloseClicked(InputEvent event) {

    }

    @FXML
    private void onPlayClicked(InputEvent event) {

    }

    @FXML
    private void onDiscardClicked(InputEvent event) {

    }

}
