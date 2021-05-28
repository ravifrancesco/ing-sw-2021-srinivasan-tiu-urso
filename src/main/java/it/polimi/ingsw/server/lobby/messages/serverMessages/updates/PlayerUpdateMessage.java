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

    private final String playerNickname;
    private List<LeaderCard> hand;
    private int handSize;

    /**
     * Constructor.
     *
     * @param player player for the update.
     */
    public PlayerUpdateMessage(Player player) {
        this.playerNickname = player.getNickname();
        this.hand = player.getHand();
        this.handSize = player.getHandSize();
    }

    @Override
    public void updateClient(ClientConnection clientConnection, String nickname) {
        if (!playerNickname.equals(nickname)) {
            hand.clear();
        }
        clientConnection.updateReducedPlayer(this.playerNickname, hand, handSize);
    }
}