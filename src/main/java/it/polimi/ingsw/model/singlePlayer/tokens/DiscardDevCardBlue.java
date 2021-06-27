package it.polimi.ingsw.model.singlePlayer.tokens;

import it.polimi.ingsw.model.DevelopmentCardGrid;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public class DiscardDevCardBlue implements Token, Serializable {

    public static int COLOR_INDEX = 1;

    @Override
    public boolean useToken(Game game) {
        DevelopmentCardGrid developmentCardGrid = game.getGameBoard().getDevelopmentCardGrid();
        developmentCardGrid.discardCard(COLOR_INDEX);
        return developmentCardGrid.discardCard(COLOR_INDEX);
    }
}
