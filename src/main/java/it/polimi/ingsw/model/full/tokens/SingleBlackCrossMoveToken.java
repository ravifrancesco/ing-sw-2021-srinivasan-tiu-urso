package it.polimi.ingsw.model.full.tokens;

import it.polimi.ingsw.model.full.table.Dashboard;
import it.polimi.ingsw.model.full.table.Game;

import java.io.Serializable;

public class SingleBlackCrossMoveToken implements Token, Serializable {

    @Override
    public boolean useToken(Game game) {

        Dashboard dashboard = game.getPlayers()
                .get(game.getCurrentPlayer()).getDashboard();

        dashboard.getFaithTrack().moveLorenzoIlMagnificoMarker(1);

        game.resetTokens();

        return dashboard.checkGameEnd();

    }

    @Override
    public String getType() {
        return "one";
    }

}
