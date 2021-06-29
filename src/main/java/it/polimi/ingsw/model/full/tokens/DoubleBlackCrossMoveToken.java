package it.polimi.ingsw.model.full.tokens;

import it.polimi.ingsw.model.full.table.Dashboard;
import it.polimi.ingsw.model.full.table.Game;

import java.io.Serializable;

public class DoubleBlackCrossMoveToken implements Token, Serializable {
    /**
     * Moves Lorenzo faith track marker by two positions
     */

    @Override
    public boolean useToken(Game game) {
        Dashboard dashboard = game.getPlayers()
                .get(game.getCurrentPlayer()).getDashboard();

        dashboard.getFaithTrack().moveLorenzoIlMagnificoMarker(2);

        return dashboard.checkGameEnd();

    }

    @Override
    public String getType() {
        return "two";
    }

}
