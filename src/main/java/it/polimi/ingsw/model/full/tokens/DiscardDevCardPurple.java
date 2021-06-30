package it.polimi.ingsw.model.full.tokens;

import it.polimi.ingsw.model.full.table.DevelopmentCardGrid;
import it.polimi.ingsw.model.full.table.Game;

import java.io.Serializable;

public class DiscardDevCardPurple implements Token, Serializable {

    /**
     * Discards two purple bannered cards of level 1. If no level 1 cards are available, level 2 cards will be discarded.
     * If no level 2 cards are available, level 3 cards will be discarded. If no level 3 cards are available, Lorenzo
     * has won the game.
     */
    public static int COLOR_INDEX = 3;

    @Override
    public boolean useToken(Game game) {
        DevelopmentCardGrid developmentCardGrid = game.getGameBoard().getDevelopmentCardGrid();
        developmentCardGrid.discardCard(COLOR_INDEX);
        return developmentCardGrid.discardCard(COLOR_INDEX);
    }

    @Override
    public String getType() {
        return "purple";
    }
}
