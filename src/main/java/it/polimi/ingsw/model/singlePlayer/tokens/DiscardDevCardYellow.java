package it.polimi.ingsw.model.singlePlayer.tokens;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

public class DiscardDevCardYellow implements Token, Serializable {

    public static int COLOR_INDEX = 2;

    @Override
    public boolean useToken(Game game) {
        List<Stack<DevelopmentCard>> developmentCardGrid = game.getGameBoard().getDevelopmentCardGrid().getGrid();
        discardCard(developmentCardGrid);
        return discardCard(developmentCardGrid);
    }

    private boolean discardCard(List<Stack<DevelopmentCard>> developmentCardGrid) {
        if (!developmentCardGrid.get(COLOR_INDEX).isEmpty()) {
            developmentCardGrid.get(COLOR_INDEX).pop();
        } else if (!developmentCardGrid.get(COLOR_INDEX + 4).isEmpty()) {
            developmentCardGrid.get(COLOR_INDEX + 4).pop();
        } else if (!developmentCardGrid.get(COLOR_INDEX + 2*4).isEmpty()) {
            developmentCardGrid.get(COLOR_INDEX + 2*4).pop();
        } else {
            return true;
        }
        return false;
    }
}
