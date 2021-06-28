package it.polimi.ingsw.model.singlePlayer.tokens;

import it.polimi.ingsw.model.Dashboard;
import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.Game;

import java.io.Serializable;

public class DoubleBlackCrossMoveToken implements Token, Serializable {

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
