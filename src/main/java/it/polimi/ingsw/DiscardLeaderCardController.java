package it.polimi.ingsw;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.lobby.messages.clientMessages.gameMessages.game.PlayerDiscardsExcessLeaderCards;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DiscardLeaderCardController {

    @FXML
    HBox leaderCardContainerHBox;

    public GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void setCards(List<LeaderCard> leaderCards) {
        System.out.println("DEBUG " + leaderCards.size());
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
            gui.getClientConnection().send(new PlayerDiscardsExcessLeaderCards(index));
            ((Node)(event.getSource())).getScene().getWindow().hide();
        });

    }
}
