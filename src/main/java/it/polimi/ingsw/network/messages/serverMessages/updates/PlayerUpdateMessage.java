package it.polimi.ingsw.network.messages.serverMessages.updates;

import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.specialAbilities.DevelopmentCardDiscount;
import it.polimi.ingsw.model.full.table.Player;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to send Player updates to the clients.
 */
public class PlayerUpdateMessage implements ServerMessage, Serializable {

    private final String playerNickname;
    private final List<LeaderCard> hand;
    private final int handSize;
    private final Resource[] wmrs;
    private final ArrayList<DevelopmentCardDiscount> discounts;

    /**
     * Constructor.
     *
     * @param player player for the update.
     */
    public PlayerUpdateMessage(Player player) {
        this.playerNickname = player.getNickname();
        this.hand = player.getHand();
        this.handSize = player.getHandSize();
        this.wmrs = player.getActivatedWMR();
        this.discounts = player.getActiveDiscounts();
    }

    @Override
    public void updateClient(ClientVirtualView clientVirtualView, String nickname) {
        if (!playerNickname.equals(nickname)) {
            hand.clear();
        }
        clientVirtualView.updateReducedPlayer(this.playerNickname, hand, handSize, wmrs, discounts);
    }
}