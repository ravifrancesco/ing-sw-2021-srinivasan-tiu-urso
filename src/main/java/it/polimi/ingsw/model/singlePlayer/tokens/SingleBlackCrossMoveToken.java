package it.polimi.ingsw.model.singlePlayer.tokens;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.Game;

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
