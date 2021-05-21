package it.polimi.ingsw.controller.client.reducedModel;

import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.utils.Pair;

import java.util.*;

public class ReducedDashboard {

    private int playerPoints;

    // Cards
    static final int NUM_DEVELOPMENT_CARD_STACKS = 3;
    static final int NUM_LEADER_CARDS = 2;
    private final List<LeaderCard> playedLeaderCards;
    private final List<Stack<DevelopmentCard>> playedDevelopmentCards;

    // Warehouse
    static final int MAX_DEPOSIT_SLOTS = 6;
    private ArrayList<Resource> supply;
    private Resource[] deposit;
    private final Map<Resource, Integer> locker;
    private Resource[][] extraDeposits;

    // FaithTrack
    private int position;
    private Map<Pair<Integer, Integer>, Pair<Integer, Integer>> vaticanReports;
    private int[] faithTrackVictoryPoints;

    public ReducedDashboard() {
        this.playedLeaderCards = new ArrayList<>();
        this.playedDevelopmentCards = new ArrayList<>();
        this.locker = new HashMap<>();
    }

}
