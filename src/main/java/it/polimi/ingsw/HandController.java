package it.polimi.ingsw;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayLeaderCardGameMessage;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerDiscardsExcessLeaderCards;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerDiscardsLeaderCard;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HandController {

    @FXML
    HBox leaderCardContainerHBox;

    @FXML
    Button closeButton;

    @FXML
    Button discardButton;

    @FXML
    Button playButton;

    public GUI gui;

    private List<ImageView> imageViews;

    Integer selectedCard;

    public void initialize() {
        imageViews = new ArrayList<>();
        enableButtons(false);
    }

    private void enableButtons(boolean b) {
        playButton.setVisible(b);
        playButton.setDisable(!b);
        discardButton.setVisible(b);
        discardButton.setDisable(!b);
    }

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
        imageView.setOnMouseClicked(event -> selectedCard(imageView));
        imageViews.add(imageView);
    }

    private void selectedCard(ImageView imageView) {
        selectedCard = imageViews.indexOf(imageView);
        imageViews.forEach(this::setBrightnessHigh);
        setBrightnessLow(imageView);
        enableButtons(true);
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
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void onPlayClicked(InputEvent event) {
        if (selectedCard == null) {
            this.gui.printErrorMessage("Select a card first!");
            return;
        }
        gui.getClientConnection().send(new PlayLeaderCardGameMessage(selectedCard));
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void onDiscardClicked(InputEvent event) {
        if (selectedCard == null) {
            this.gui.printErrorMessage("Select a card first!");
        }
        gui.getClientConnection().send(new PlayerDiscardsLeaderCard(selectedCard));
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void update(List<LeaderCard> leaderCards) {
        leaderCardContainerHBox.getChildren().clear();
        setCards(leaderCards);
    }

}
