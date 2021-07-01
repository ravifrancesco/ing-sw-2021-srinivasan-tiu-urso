package it.polimi.ingsw.view.UI.GUI.JavaFX.controllers;

import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.network.messages.clientMessages.game.PlayerDiscardsExcessLeaderCards;
import it.polimi.ingsw.view.UI.GUI.GUI;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.List;

public class DiscardLeaderCardController {

    public GUI gui;
    @FXML
    private HBox leaderCardContainerHBox;

    /**
     * Setter for the GUI
     * @param gui the GUI
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Setter for the leader cards
     * @param leaderCards the list of leader cards
     */
    public void setCards(List<LeaderCard> leaderCards) {
        leaderCards.forEach(c -> setLeaderCard(c, leaderCards.indexOf(c)));
    }

    /**
     * Setter for the leader
     * @param leaderCard the leader card
     * @param index the index
     */
    private void setLeaderCard(LeaderCard leaderCard, int index) {
        Image image = new Image(this.getClass().getResourceAsStream("/png/cards/leaderCards/leader_card_" + leaderCard.getId() + ".png"));
        ImageView imageView = new ImageView(image);
        leaderCardContainerHBox.getChildren().add(imageView);
        imageView.setFitHeight(200);
        imageView.setFitWidth(132);
        HBox.setMargin(imageView, new Insets(10, 10, 10, 10));
        imageView.setOnMouseClicked(event -> {
            gui.send(new PlayerDiscardsExcessLeaderCards(index));
            ((Node) (event.getSource())).getScene().getWindow().hide();
        });

    }

}
