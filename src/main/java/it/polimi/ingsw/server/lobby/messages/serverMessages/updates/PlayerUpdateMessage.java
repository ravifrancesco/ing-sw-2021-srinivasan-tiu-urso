package it.polimi.ingsw.server.lobby.messages.serverMessages.updates;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.lobby.messages.serverMessages.ServerMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to send Player updates to the clients.
 */
public class PlayerUpdateMessage implements ServerMessage, Serializable {

    private final String nickname;
    private List<LeaderCard> hand;
    private int handSize;

    /**
     * Constructor.
     *
     * @param player player for the update.
     */
    public PlayerUpdateMessage(Player player) {
        this.nickname = player.getNickname();
        if (this.nickname.equals(player.getNickname())) {
            this.hand = player.getHand();
        }
        else {
            this.hand = new ArrayList<>();
        }
        this.handSize = player.getHandSize();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        clientConnection.updateReducedPlayer(nickname, hand, handSize);
        //clientConnection.cli.printMessage(this.nickname);
        //hand.forEach(card -> clientConnection.cli.printMessage(card.toString()));
    }
}