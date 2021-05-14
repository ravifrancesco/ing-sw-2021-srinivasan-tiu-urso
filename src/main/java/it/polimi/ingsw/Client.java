package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.specialAbilities.*;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.CreateGameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.JoinGameLobby;
import it.polimi.ingsw.server.lobby.messages.clientMessages.lobbyMessage.lobby.RegisterName;
import it.polimi.ingsw.server.lobby.messages.serverMessages.commons.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.stream.IntStream;

public class Client {
    private String ip;
    private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection has been made!");
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);
        WelcomeMessage welcomeMessage = (WelcomeMessage) ois.readObject();
        System.out.println(welcomeMessage.toString());
        String toSend;
        int numberOfPlayers;
        RegisterName rn;
        Object obj;
        boolean flag = true;

        do {
            System.out.println("Insert a nickname");

            toSend = stdin.nextLine();
            rn = new RegisterName(toSend);
            oos.writeObject(rn);
            oos.flush();

            obj = ois.readObject();
            try {
                RegisteredNameMessage rnm = (RegisteredNameMessage) obj;
                System.out.println(rnm.toString());
                flag = false;
            } catch (ClassCastException e) {
                InvalidNameMessage inm = (InvalidNameMessage) obj;
                System.out.println(inm.toString());
            }
        } while(flag);

        System.out.println("Choose 1 to create a game, 2 to join a game");

        int choice = Integer.parseInt(stdin.nextLine());

        switch (choice) {
            case 1 -> {
                System.out.println("Insert number of players");
                numberOfPlayers = Integer.parseInt(stdin.nextLine());
                CreateGameLobby cgl = new CreateGameLobby(buildGameSettings(), numberOfPlayers);
                oos.writeObject(cgl);
                oos.flush();
                try {
                    obj = ois.readObject();
                    SuccessfulConnectionMessage successfulConnectionMessage = (SuccessfulConnectionMessage) obj;
                    successfulConnectionMessage.showMessage();
                } catch (ClassCastException e) {
                    ErrorMessage errorMessage = (ErrorMessage) obj;
                    System.out.println("Server error");
                }
            }
            case 2 -> {
                System.out.println("Insert the GAME ID");
                toSend = stdin.nextLine();
                JoinGameLobby jgl = new JoinGameLobby(toSend);
                oos.writeObject(jgl);
                oos.flush();
                try {
                    obj = ois.readObject();
                    SuccessfulConnectionMessage successfulConnectionMessage = (SuccessfulConnectionMessage) obj;
                    successfulConnectionMessage.showMessage();
                } catch (ClassCastException e) {
                    ErrorMessage errorMessage = (ErrorMessage) obj;
                    System.out.println("Server error");
                }
            }
        }

        while(true) {
            stdin.nextLine();
        }
    }

    private GameSettings buildGameSettings() {


        DevelopmentCard[] developmentCards = developmentCardDeckBuilder();

        int leaderCardNum = 16;
        LeaderCard[] leaderCards = leaderCardDeckBuilder(leaderCardNum);

        ProductionPower dashboardProductionPower = productionPowerBuilder();

        int vaticanReportsNum = 3;
        List<VaticanReport> vaticanReports = vaticanReportsListBuilder();

        int[] faithTrackVictoryPoints = {0,0,0,1,0,0,2,0,0,4,0,0,6,0,0,9,0,0,12,0,0,16,0,0,0};

        return new GameSettings(developmentCards, leaderCardNum, leaderCards,
                dashboardProductionPower, vaticanReports, faithTrackVictoryPoints);

    }

    private DevelopmentCard[] developmentCardDeckBuilder() {

        Map<Resource, Integer> resourceCost = new HashMap<>();
        resourceCost.put(Resource.SERVANT, 2);
        resourceCost.put(Resource.GOLD, 1);
        resourceCost.put(Resource.SHIELD, 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        ProductionPower p = new ProductionPower(resourceRequired, resourceProduced,2);

        return IntStream.range(0, GameSettings.DEVELOPMENT_CARD_NUM)
                .boxed()
                .map(i -> new DevelopmentCard(i, 4, resourceCost, p, chooseBanner(i)))
                .toArray(DevelopmentCard[]::new);

    }

    private LeaderCard[] leaderCardDeckBuilder(int leaderCardNum) {

        Map<Resource, Integer> resourceCost = new HashMap<>();

        resourceCost.put(Resource.SHIELD, 1);

        Map<Banner, Integer> bannerCost = new HashMap<>();

        bannerCost.put(new Banner(BannerEnum.GREEN, 1), 2);
        bannerCost.put(new Banner(BannerEnum.BLUE, 2), 1);

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.ANY, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        SpecialAbility[] SAs = new SpecialAbility[4];
        SAs[0] = new ProductionPower(resourceRequired, resourceProduced, 1);
        SAs[1] = new DevelopmentCardDiscount(Resource.GOLD, 3);
        SAs[2] = new WarehouseExtraSpace(Resource.SERVANT);
        SAs[3] = new WhiteMarbleResource(Resource.SHIELD);

        return  IntStream.range(0, leaderCardNum)
                .boxed()
                .map(i -> new LeaderCard(i, 2, bannerCost, resourceCost, SAs[i%4]))
                .toArray(LeaderCard[]::new);

    }

    private ProductionPower productionPowerBuilder() {

        Map<Resource, Integer> resourceRequired = new HashMap<>();
        resourceRequired.put(Resource.GOLD, 1);
        resourceRequired.put(Resource.STONE, 1);

        Map<Resource, Integer> resourceProduced = new HashMap<>();
        resourceProduced.put(Resource.SHIELD, 1);

        return new ProductionPower(resourceRequired, resourceProduced,2);

    }

    private List<VaticanReport> vaticanReportsListBuilder() {

        List<VaticanReport> vaticanReportsList = new ArrayList<>();
        vaticanReportsList.add(new VaticanReport(5, 8, 2));
        vaticanReportsList.add(new VaticanReport(12, 16, 3));
        vaticanReportsList.add(new VaticanReport(19, 24, 4));

        return vaticanReportsList;


    }

    private Banner chooseBanner(int val) {
        return switch (val % 12) {
            case 0 -> new Banner(BannerEnum.GREEN, 1);
            case 1 -> new Banner(BannerEnum.GREEN, 2);
            case 2 -> new Banner(BannerEnum.GREEN, 3);
            case 3 -> new Banner(BannerEnum.YELLOW, 1);
            case 4 -> new Banner(BannerEnum.YELLOW, 2);
            case 5 -> new Banner(BannerEnum.YELLOW, 3);
            case 6 -> new Banner(BannerEnum.BLUE, 1);
            case 7 -> new Banner(BannerEnum.BLUE, 2);
            case 8 -> new Banner(BannerEnum.BLUE, 3);
            case 9 -> new Banner(BannerEnum.PURPLE, 1);
            case 10 -> new Banner(BannerEnum.PURPLE, 2);
            case 11 -> new Banner(BannerEnum.PURPLE, 3);
            default -> null;
        };
    }

    private int getRow(int val) {
        return switch (val % 12) {
            case 0 -> 1;
            case 1 -> 1;
            case 2 -> 1;
            case 3 -> 1;
            case 4 -> 2;
            case 5 -> 2;
            case 6 -> 2;
            case 7 -> 2;
            case 8 -> 3;
            case 9 -> 3;
            case 10 -> 3;
            case 11 -> 3;
            default -> -1;
        };
    }

    private int getColumn(int val) {
        return switch (val % 12) {
            case 0 -> 1;
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 1;
            case 5 -> 2;
            case 6 -> 3;
            case 7 -> 4;
            case 8 -> 1;
            case 9 -> 2;
            case 10 -> 3;
            case 11 -> 4;
            default -> -1;
        };
    }

    private Banner getBanner(int val) {
        return switch (val % 12) {
            case 0 -> new Banner(BannerEnum.GREEN, 1);
            case 1 -> new Banner(BannerEnum.BLUE, 1);
            case 2 -> new Banner(BannerEnum.YELLOW, 1);
            case 3 -> new Banner(BannerEnum.PURPLE, 1);
            case 4 -> new Banner(BannerEnum.GREEN, 2);
            case 5 -> new Banner(BannerEnum.BLUE, 2);
            case 6 -> new Banner(BannerEnum.YELLOW, 2);
            case 7 -> new Banner(BannerEnum.PURPLE, 2);
            case 8 -> new Banner(BannerEnum.GREEN, 3);
            case 9 -> new Banner(BannerEnum.BLUE, 3);
            case 10 -> new Banner(BannerEnum.YELLOW, 3);
            case 11 -> new Banner(BannerEnum.PURPLE, 3);
            default -> null;
        };
    }
}

