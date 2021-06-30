package it.polimi.ingsw.model.full.tokens;

import it.polimi.ingsw.model.full.table.Dashboard;
import it.polimi.ingsw.model.full.table.Game;

import java.io.Serializable;

public class SingleBlackCrossMoveToken implements Token, Serializable {

    /**
     * Moves Lorenzo faith track marker by 1, reads all the used tokens and shuffles them. (in order to never run out)
     */
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
