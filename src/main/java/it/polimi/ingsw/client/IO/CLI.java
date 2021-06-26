package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.controller.*;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.marbles.Marble;
import it.polimi.ingsw.model.specialAbilities.*;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLI implements UI {

    private final Scanner input;

    private ReducedModel reducedModel;

    private ClientConnection clientConnection;

    private FaithTrackCLI ftcli;


    public CLI() {
        this.input = new Scanner(System.in);
        this.ftcli = new FaithTrackCLI();
    }

    public void printColoredMessage(String message, String color) {
        System.out.println(color + message + Constants.ANSI_RESET);
    }

    public String getIp() throws IOException {
        System.out.println(Constants.logo);
        System.out.println();
        System.out.println("Insert the server ip: ");
        System.out.print("> ");
        return input.nextLine();
    }

    public int getPort() {
        System.out.println("Insert the server port: ");
        System.out.print("> ");
        int port = input.nextInt();
        input.nextLine();
        return port;
    }

    public ReducedModel getReducedModel() {
        return reducedModel;
    }

    public String readCommand() {
        System.out.print("> ");
        return input.nextLine();
    }

    public String readLine() {
        return input.nextLine();
    }

    public void printErrorMessage(String error) {
        System.out.println(Constants.ANSI_RED + error + Constants.ANSI_RESET);
    }

    public void printMessage(String message) { System.out.println(message); }

    public void printMessageNoNL(String message) { System.out.print(message); }

    public void printColoredMessageNoNL(String message, String color) { System.out.print(color + message + Constants.ANSI_RESET); }

    public void showMainLobbyMenu() {
        printMessage("Please insert:");

        printColoredMessageNoNL("CREATEGAME <numberofplayers>", Constants.SERVANT_COLOR);
        printMessage(" to create a game lobby");

        printColoredMessageNoNL("SHOWGAMES", Constants.SERVANT_COLOR);
        printMessage(" to see list of available game lobbies");

        printColoredMessageNoNL("JOINGAME <game_id>", Constants.SERVANT_COLOR);
        printMessage(" to join a game lobby");

        printColoredMessageNoNL("QUIT", Constants.SERVANT_COLOR);
        printMessage(" to quit the game");
        printMessage("");
    }

    public void startReadingThread() {
        // TODO how to kill this thread if the receivingThread dies?
        new Thread(() -> {
            while(true) {
                String command = this.readCommand();
                ClientMessage clientMessage = ClientMessageInputParser.parseInput(command, this);
                if (clientMessage != null) {
                    try {
                        clientConnection.send(clientMessage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                // TODO ugly asf, need to find a way to show the "enter command" after server answers
                // and client is shown the response to its command
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public String getNickname() {
        System.out.println("Insert nickname:");
        System.out.print("> ");
        return input.nextLine();
    }

    public void showGameLobbies(ArrayList<GameLobbyDetails> activeGameLobbies) {
        if (activeGameLobbies.size() > 0) {
            activeGameLobbies.forEach(gameLobbyDetails ->
                    System.out.println(
                            "ID: " + gameLobbyDetails.id +
                                    "         CREATOR: " + gameLobbyDetails.creator +
                                    "         PLAYERS: " +
                                    gameLobbyDetails.connectedPlayers + "/" + gameLobbyDetails.maxPlayers));
        } else {
            printErrorMessage("No game lobbies found");
        }
    }

    @Override
    public void enterGamePhase(boolean isHost) {
        // TODO
    }

    public void showHand(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            List<LeaderCard> leaderCardList = reducedPlayer.getHand();
            if (leaderCardList.size() != 0) {
                IntStream.range(0, leaderCardList.size()).forEach(i -> {
                    printMessage("");
                    printColoredMessage("------- CARD #" + i + " -------", Constants.BOLD+Constants.SHIELD_COLOR);
                    showCard(leaderCardList.get(i));
                    System.out.println();
                });
            } else {
                System.out.println(reducedPlayer.getHandSize() + " cards");
            }
        } else {
            printErrorMessage("Player " + nickname + " doesn't exists");
        }
    }

    private void showCard(LeaderCard leaderCard) {
        if(leaderCard != null) {
            printMessage("CARD VICTORY POINTS: \n" + leaderCard.getVictoryPoints());
            printMessage("");

            if(leaderCard.getBannerCost().size() != 0) {
                printMessage("REQUIRED BANNERS: ");
                leaderCard.getBannerCost().forEach((key, value) -> {
                    printMessageNoNL("Level " + value + " ");
                    String banner = handleBanner(key.toString());
                    printColoredMessage(banner, handleBannerColor(banner));
                });
                printMessage("");
            }

            showResourceCost(leaderCard.getResourceCost());
            handleSA(leaderCard.getSpecialAbility());
        } else {
            printColoredMessage("EMPTY", Constants.ANSI_RED);
        }


    }

    private void showCard(DevelopmentCard developmentCard) {

        printMessage("CARD VICTORY POINTS: \n" + developmentCard.getVictoryPoints());
        printMessage("");

        printMessageNoNL("CARD WITH BANNER ");
        printColoredMessage(developmentCard.getBanner().toString().split("=")[1].split(":")[0],
                handleBannerColor(developmentCard.getBanner().toString().split("=")[1].split(":")[0]));

        printMessage("LEVEL " + developmentCard.getBanner().getLevel());
        showResourceCost(developmentCard.getResourceCost());

        handleSA(developmentCard.getProductionPower());
    }

    private void showResourceCost(Map<Resource, Integer> resourceCost) {
        if(resourceCost.size() != 0) {
            printMessage("RESOURCE COST:");
            resourceCost.forEach((key, value) ->  {
                printColoredMessageNoNL("" + key, handleResourceColor(key.toString()));
                printMessage(": " + value);
            });
            printMessage("");
        }

        printMessageNoNL("SPECIAL ABILITY: ");
    }

    private String handleBannerColor(String banner) {
        return switch(banner) {
            case "YELLOW" -> Constants.GOLD_COLOR;
            case "BLUE" -> Constants.SHIELD_COLOR;
            case "PURPLE" -> Constants.SERVANT_COLOR;
            case "GREEN" -> Constants.ANSI_GREEN;
            default -> Constants.STONE_COLOR;
        };
    }

    private String handleResourceColor(String resource) {
        return switch(resource) {
            case "GOLD" -> Constants.GOLD_COLOR;
            case "SHIELD" -> Constants.SHIELD_COLOR;
            case "SERVANT" -> Constants.SERVANT_COLOR;
            case "STONE" -> Constants.STONE_COLOR;
            default -> Constants.ANSI_GENERIC_WHITE;
        };
    }

    private void handleSA(SpecialAbility sa) {
        switch(sa.getType()) {
            case DEVELOPMENT_CARD_DISCOUNT ->
                printMessage("[DVD] Discounts " + ((DevelopmentCardDiscount) sa).getQuantity() + " " +
                        handleResourceColor(((DevelopmentCardDiscount) sa).getResource().toString()) +
                        ((DevelopmentCardDiscount) sa).getResource() + Constants.ANSI_RESET +
                        " when buying a development card");

            case PRODUCTION_POWER -> {
                ProductionPower pp = (ProductionPower) sa;
                Map<Resource, Integer> required = pp.getResourceRequired();
                Map<Resource, Integer> produced = pp.getResourceProduced();
                required.remove(Resource.ANY);
                produced.remove(Resource.ANY);
                printMessage("[PP] Allows to transform");
                if(required.size() > 0) {
                    required.forEach((resource, qty) ->
                        printMessage(qty + " " + handleResourceColor(resource.toString()) + resource + Constants.ANSI_RESET)
                    );
                }
                if(pp.getNumRequiredAny() > 0) {
                    printMessage(pp.getNumRequiredAny() + Constants.STONE_COLOR + " ANY RESOURCE" + Constants.ANSI_RESET);
                }
                printMessage("Into");
                // TODO qualcosa non va qua, non funziona bene se ha i faith track

                if(produced.size() > 0) {
                    produced.forEach((resource, qty) ->
                        printMessage(qty + " " + handleResourceColor(resource.toString()) + resource + Constants.ANSI_RESET)
                    );
                }
                if(pp.getNumProducedAny() > 0) {
                    printMessage(pp.getNumProducedAny() + Constants.STONE_COLOR + " ANY RESOURCE" + Constants.ANSI_RESET);
                }
            }
            case WHITE_MARBLE_RESOURCES -> {
                Resource res = ((WhiteMarbleResource) sa).getRes();
                printMessage("[WMR] Allows to transforms a white marble in a "
                        + handleResourceColor(res.toString()) + res + Constants.ANSI_RESET);
            }
            case WAREHOUSE_EXTRA_SPACE -> {
                Resource res = ((WarehouseExtraSpace) sa).getStoredResource();
                printMessage("[WES] Allows to add an extra deposit to store " + handleResourceColor(res.toString())
                        + res + Constants.ANSI_RESET);
            }
        }
    }

    private String handlePp(ProductionPower pp) {

        Map<Resource, Integer> required = pp.getResourceRequired();
        Map<Resource, Integer> produced = pp.getResourceProduced();
        printMessage("REQUIRES: ");
        required.forEach((resource, qty) ->
            printMessage(handleResourceColor(resource.toString()) + qty)
        );
        // TODO finire
        return "";
    }



    private String handleBanner(String banner) {
        String[] split = banner.split("=");
        return split[1].split(":")[0];
    }

    public void showDashboard(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();

            printMessage("VICTORY POINTS: \n" + reducedDashboard.getPlayerPoints());

            printMessage("DASHBOARD PRODUCTION: ");
            printMessage("Transforms 2 of any resources into a single resource of your choice");
            printMessage("");

            printMessage("PLAYED LEADER CARDS: \n");
            IntStream.range(0, ReducedDashboard.NUM_LEADER_CARDS).forEach(i -> {
                printColoredMessage("LEADER CARD #" + i, Constants.BOLD);
                if(i < reducedDashboard.getPlayedLeaderCards().size()) {
                    System.out.println("*****************************************");
                    showCard(reducedDashboard.getPlayedLeaderCards().get(i));
                    System.out.println("*****************************************");
                } else {
                    printColoredMessage("EMPTY", Constants.ANSI_RED);
                }
            });

            System.out.println("PLAYED DEVELOPMENT CARDS: ");
            System.out.println();
            IntStream.range(0, ReducedDashboard.NUM_DEVELOPMENT_CARD_STACKS).forEach(i -> {
                printColoredMessage("DEVELOPMENT CARD #" + i, Constants.BOLD);
                if(reducedDashboard.getPlayedDevelopmentCards().get(i).isEmpty()) {
                    printColoredMessage("EMPTY", Constants.ANSI_RED);
                } else {
                    System.out.println("*****************************************");
                    showCard(reducedDashboard.getPlayedDevelopmentCards().get(i).peek());
                    System.out.println("*****************************************");
                    System.out.println();
                }
            });

            System.out.println();
            System.out.println("SUPPLY: ");
            ArrayList<Resource> supply = reducedPlayer.getDashboard().getSupply();
            showSupply(supply);
        } else {
            printMessageNoNL("Player ");
            printColoredMessageNoNL(nickname, Constants.ANSI_CYAN);
            printMessage(" does not exist");

        }
    }

    public void showSupply (ArrayList<Resource> supply) {
        int hBarSize = (7*supply.size()) + (4*supply.size()) + 1;
        if(supply.size() > 0) {
            IntStream.range(0, hBarSize).forEach(i -> {
                // printing indexes
                switch(i) {
                    case(5) -> printMessageNoNL("0");
                    case(16) -> printMessageNoNL("1");
                    case(27) -> printMessageNoNL("2");
                    case(38) -> printMessageNoNL("3");
                    default -> printMessageNoNL("―");
                }
            });
            System.out.println();

            IntStream.range(0, 6).forEach(column -> {
                IntStream.range(0, supply.size()).forEach(i ->
                    printMessageNoNL("| " + renderResourceRow(supply.get(i))[column] + " |")
                );
                printMessage("");
            });
            IntStream.range(0, hBarSize).forEach(i -> printMessageNoNL("―"));
            System.out.println();
        } else {
            printColoredMessage("EMPTY", Constants.ANSI_RED);
        }
    }

    public String[] renderResourceRow(Resource r) {
        if(r == null) {
            return Constants.empty;
        } else {
            return switch (r.toString()) {
                case "STONE" -> Constants.stone;
                case "GOLD" -> Constants.gold;
                case "SERVANT" -> Constants.servant;
                case "SHIELD" -> Constants.shield;
                default -> null;
            };
        }
    }

    public void showFaithTrack(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            int position = reducedPlayer.getDashboard().getPosition();
            System.out.println("position is: " + position);
            ftcli.setPos(position);
            List<Pair<Integer, Integer>> vc = getPassedVaticanReports();
            IntStream.range(0, 3).forEach(i -> ftcli.handleVr(i+1, vc.get(i).second));
            ftcli.showFTCLI();
        } else {
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
        }
    }

    public List<Pair<Integer, Integer>> getPassedVaticanReports () {
        ReducedDashboard reducedDashboard = reducedModel.getReducedPlayer().getDashboard();
        return  reducedDashboard.getVaticanReports().entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().second))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }



    public void showWarehouse(String nickname) {
        // TODO mettere gli empty in bianco
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();
            System.out.println("\nDEPOSIT\n");
            showDeposit(reducedPlayer.getDashboard().getDeposit());
            System.out.println("\n\nLOCKER:\n");
            reducedDashboard.getLocker().forEach((key, value) -> {
                printColoredMessageNoNL(key.toString(), handleResourceColor(key.toString()));
                System.out.println(":  " + value);
            });
            System.out.println();

            Resource[][] extraDeposits = reducedDashboard.getExtraDeposits();
            if (Arrays.stream(extraDeposits).anyMatch(Objects::nonNull)) {
                System.out.println("EXTRA DEPOSITS: ");
                IntStream.range(0, extraDeposits.length).forEach(i -> {
                    if(extraDeposits[i] != null) {
                        System.out.println("INDEX " + i);
                        IntStream.range(0, extraDeposits[i].length).forEach(j -> {
                            if (extraDeposits[i][j] == null) {
                                printColoredMessage("    EMPTY", Constants.STONE_COLOR);
                            } else {
                                printColoredMessage("    ", handleResourceColor(extraDeposits[i][j].toString()));
                            }
                        });
                    }
                });

            }
        } else
        {
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
        }
    }

    public void showDeposit(Resource[] deposit) {
        int topHbar = 11;
        int middleHbar = 26;
        int bottomHbar = 33;

        printMessageNoNL("           ");

        IntStream.range(0, topHbar).forEach(i -> {
            if(i == 5) {
                printMessageNoNL("" + 0);
            } else {
                printMessageNoNL("―");
            }
        }
        );
        System.out.println();

        IntStream.range(0, 6).forEach(column -> {
            IntStream.range(0, 1).forEach(i ->
                printMessageNoNL("           | " + renderResourceRow(deposit[i])[column] + " |")
            );
            printMessage("");
        });
        printMessageNoNL("           ");

        IntStream.range(0, topHbar).forEach(i -> printMessageNoNL("―"));
        System.out.println();


        printMessageNoNL("    ");
        IntStream.range(0, middleHbar).forEach(i -> {
            if(i == 5) {
                printMessageNoNL("" + 1);
            } else if (i == 20) {
                printMessageNoNL("" + 2);
            } else {
                printMessageNoNL("―");
            }
        }
        );
        System.out.println();

        IntStream.range(0, 6).forEach(column -> {
            IntStream.range(1, 3).forEach(i -> {
                printMessageNoNL("    | " + renderResourceRow(deposit[i])[column] + " |");
            });
            printMessage("");
        });
        printMessageNoNL("    ");
        IntStream.range(0, middleHbar).forEach(i -> printMessageNoNL("―"));
        System.out.println();


        IntStream.range(0, bottomHbar).forEach(i -> {
            if(i == 5) {
                printMessageNoNL("" + 3);
            } else if (i == 16) {
                printMessageNoNL("" + 4);
            } else if (i == 27) {
                printMessageNoNL("" + 5);
            } else {
                printMessageNoNL("―");
            }});
        System.out.println();

        IntStream.range(0, 6).forEach(column -> {
            IntStream.range(3, 6).forEach(i -> {
                printMessageNoNL("| " + renderResourceRow(deposit[i])[column] + " |");
            });
            printMessage("");
        });
        IntStream.range(0, bottomHbar).forEach(i -> printMessageNoNL("―"));
        System.out.println();

    }





    public void showDVGrid() {
        printMessage("");
        printMessage("Insert a color or level to see the corresponding column or row");
        printMessage("Options: ");
        printMessageNoNL("Color: ");
        printColoredMessageNoNL("GREEN", handleBannerColor("GREEN"));
        printMessageNoNL(", ");
        printColoredMessageNoNL("BLUE", handleBannerColor("BLUE"));
        printMessageNoNL(", ");
        printColoredMessageNoNL("YELLOW", handleBannerColor("YELLOW"));
        printMessageNoNL(", ");
        printColoredMessageNoNL("PURPLE", handleBannerColor("PURPLE"));
        System.out.println();
        printMessage("Levels: 1, 2, 3");
        System.out.println();
        System.out.print("> ");
        String choice = readLine();
        switch(choice.toUpperCase()) {
            case "1", "2", "3" -> showDvRow(Integer.parseInt(choice));
            case "GREEN", "BLUE", "YELLOW", "PURPLE" -> showDvColumn(choice);
            default -> printColoredMessage("Wrong input", Constants.ANSI_RED);
        };
        /*
               ReducedGameBoard reducedGameBoard = reducedModel.getReducedGameBoard();
        for (int i = 0; i < reducedGameBoard.getGrid().size(); i++) {
            System.out.print(reducedGameBoard.getGrid().get(i).isEmpty() ? " | " : reducedGameBoard.getGrid().get(i).peek().toString() + " | ");
            if ((i+1) % 4 == 0) {
                System.out.println();
            }
        }
         */
    }

    public Pair<Integer, Integer> invertIndex(int position) {
        return switch(position) {
            case(1) -> new Pair<>(1, 2);
            case(2) -> new Pair<>(1, 3);
            case(3) -> new Pair<>(1, 4);
            case(4) -> new Pair<>(2, 1);
            case(5) -> new Pair<>(2, 2);
            case(6) -> new Pair<>(2, 3);
            case(7) -> new Pair<>(2, 4);
            case(8) -> new Pair<>(3, 1);
            case(9) -> new Pair<>(3, 2);
            case(10) -> new Pair<>(3, 3);
            case(11) -> new Pair<>(3, 4);
            default -> new Pair<>(1, 1);
        };
    }

    public void showDvRow(int level) {
        // se 1 allora 0, 1, 2, 3
        // se 2 allora 4, 5, 6, 7
        // se 3 allora 8, 9, 10, 11
        int startIndex = 4 * (level-1);
        IntStream.range(0, 4).forEach(i -> {
            if (!reducedModel.getReducedGameBoard().getGrid().get(startIndex + i).isEmpty()) {

                printColoredMessage("------- CARD ROW/COL ("
                        + invertIndex(startIndex+i).first + "," + invertIndex(startIndex+i).second + ")" +
                        " -------" , Constants.SHIELD_COLOR);
                showCard(reducedModel.getReducedGameBoard().getGrid().get(startIndex + i).peek());
            }
        });

        printMessage("\nPlease use");
        printColoredMessageNoNL("BUYDEVELOPMENTCARD <row> <column> <position>", Constants.SERVANT_COLOR);
        printMessage(" to buy a development card");
        printMessageNoNL("\nAvailable empty slots: \n");
        IntStream.range(0, ReducedDashboard.NUM_DEVELOPMENT_CARD_STACKS).forEach(i -> {
            if(reducedModel.getReducedPlayer().getDashboard().getPlayedDevelopmentCards().get(i).isEmpty()) {
                printMessageNoNL("" + i + " ");
            }
        });
        printMessage("");


    }

    public void showDvColumn(String color) {
        int index = getShift(color);
        // se green allora 0, 4, 8
        // se blue allora 1, 5, 9
        // se yellow allora 2, 6, 10
        // se purple allora 3, 7, 11
        IntStream.range(0, 3).forEach(i -> {
            if (!reducedModel.getReducedGameBoard().getGrid().get(index + (i*4)).isEmpty()) {
                printColoredMessage("------- CARD ROW/COL ("
                        + invertIndex(index + (i*4)).first + "," + invertIndex(index + (i*4)).second + ")" +
                        " -------", Constants.SHIELD_COLOR);
                showCard(reducedModel.getReducedGameBoard().getGrid().get(index + (i*4)).peek());
            }
        });

        printMessage("Please use");
        printColoredMessageNoNL("BUYDEVELOPMENTCARD <row> <column> <position>", Constants.SERVANT_COLOR);
        printMessage(" to buy a development card");
        printMessageNoNL("Available empty slots: ");
        IntStream.range(0, ReducedDashboard.NUM_DEVELOPMENT_CARD_STACKS).forEach(i -> {
                if(reducedModel.getReducedPlayer().getDashboard().getPlayedDevelopmentCards().get(i).isEmpty()) {
                    printMessageNoNL("" + i + " ");
                }
        });
        printMessage("");
    }

    public int getShift(String color)  {
        return switch(color) {
            case "GREEN" -> 0;
            case "BLUE" -> 1;
            case "YELLOW" -> 2;
            case "PURPLE" -> 3;
            default -> 0;
        };
    }

    public void showMarket() {
        System.out.println();
        ReducedGameBoard reducedGameBoard = reducedModel.getReducedGameBoard();
        Marble[] marblesGrid = reducedGameBoard.getMarblesGrid();
        int gridIndex;
        String[] colours;
        int ctr = 0;
        for(gridIndex = 0; gridIndex < 12; gridIndex += 4) {
            colours = getMarblesColours(marblesGrid, gridIndex);
            printMarblesRow(colours[0], colours[1], colours[2], colours[3], ctr);
            ctr += 1;
        }
        System.out.println("     3          4          5         6      ");
        System.out.println("");
        printMarble(getMarbleColor(marblesGrid[12]));
        System.out.println("FREE MARBLE");
        System.out.println("");

        printMessage("Move indexes can be found next to the respective row or column. ");
        printMessage("Please insert:");
        printColoredMessageNoNL("GETFROMMARKET <moveIndex>", Constants.SERVANT_COLOR);
        printMessage(" to get resources from market.");
        printMessage("");



        /*
        for (int i = 0; i < reducedGameBoard.getMarblesGrid().length-1; i++) {
            //System.out.print(reducedGameBoard.getMarblesGrid()[i].toString() + " | ");
            if ((i+1) % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println("FREE MARBLE: " + reducedGameBoard.getMarblesGrid()[reducedGameBoard.getMarblesGrid().length-1].toString());
         */
    }
    public String getMarbleColor(Marble marble) {
        return switch(marble.getMarbleColor().name()) {
            case "WHITE" -> Constants.ANSI_BG_WHITE;
            case "BLUE" -> Constants.SHIELD_COLOR_BG;
            case "GREY" -> Constants.STONE_COLOR_BG;
            case "PURPLE" -> Constants.SERVANT_COLOR_BG;
            case "RED" -> Constants.ANSI_BG_RED;
            case "YELLOW" -> Constants.GOLD_COLOR_BG;
            default -> Constants.ANSI_BG_WHITE;
        };
    }

    public String[] getMarblesColours(Marble[] grid, int ind1) {
        String[] colours = new String[4];
        IntStream.range(ind1, ind1+4).forEach(i -> {
            switch(grid[i].getMarbleColor().name()) {
                case "WHITE" -> colours[i-ind1] = Constants.ANSI_BG_WHITE;
                case "BLUE" -> colours[i-ind1] = Constants.SHIELD_COLOR_BG;
                case "GREY" -> colours[i-ind1] = Constants.STONE_COLOR_BG;
                case "PURPLE" -> colours[i-ind1] = Constants.SERVANT_COLOR_BG;
                case "RED" -> colours[i-ind1] = Constants.ANSI_BG_RED;
                case "YELLOW" -> colours[i-ind1] = Constants.GOLD_COLOR_BG;
            }
        });
        return colours;
    }

    public void printMarblesRow(String color1, String color2, String color3, String color4, int move) {
        int color_base_value = 4;
        int space_base_value = 3;
        int k1;
        int k2 = 4;

        for(k1 = 3; k1 > 0; k1--) {

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color1));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color2));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color3));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color4));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            System.out.println("");
            k2 += 2;
        }

        k2 = 10;
        for(k1 = 0; k1 < 4; k1++) {

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color1));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color2));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color3));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color4));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            if(k1 == 0) {
                System.out.print(" "+ move);
            }

            System.out.println("");
            k2 -= 2;
        }
        System.out.println("");
    }

    public void printMarble(String color) {
        int color_base_value = 4;
        int space_base_value = 3;
        int k1;
        int k2 = 4;

        for(k1 = 3; k1 > 0; k1--) {

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            System.out.println("");
            k2 += 2;
        }

        k2 = 10;
        for(k1 = 0; k1 < 4; k1++) {

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");
            System.out.println("");
            k2 -= 2;
        }
        System.out.println("");
    }

    @Override
    public void startUI(ClientConnection clientConnection, ReducedModel reducedModel) {
        this.clientConnection = clientConnection;
        this.reducedModel = reducedModel;
    }

    public void printPaymentCost(int golds, int servants, int shields, int stones) {
        if((golds + servants + shields + stones) != 0) {
            printMessageNoNL("You have to pay: ");
            if (golds != 0) {
                printMessageNoNL("(GOLD, " + golds + ") ");
            }
            if (servants != 0) {
                printMessageNoNL("(SERVANT, " + servants + ") ");
            }
            if (shields != 0) {
                printMessageNoNL("(SHIELD, " + shields + ") ");
            }
            if (stones != 0) {
                printMessageNoNL("(STONE, " + stones + ") ");
            }
            printMessageNoNL("\n");
        }
    }

    public void showGameLobbyMenu() {
        printMessage("Please insert:");

        printColoredMessageNoNL("LEAVELOBBY", Constants.SERVANT_COLOR);
        printMessage(" to leave the lobby");

        printColoredMessageNoNL("STARTGAME", Constants.SERVANT_COLOR);
        printMessage(" to start the game");

        printColoredMessageNoNL("QUIT", Constants.SERVANT_COLOR);
        printMessage(" to to quit the game");

        printMessage("");
    }


    @Override
    public UIType getType() {
        return UIType.CLI;
    }

    @Override
    public void handleMenuCode(String menuCode) {
        if ("after_game_start".equals(menuCode)) {
            showAfterGameStartMenu();
        }
        if ("after_end_turn".equals(menuCode)) {
            showAfterEndTurnMenu();
        }
        if ("next_card_discard".equals(menuCode)) {
            showNextCardDiscardMenu();
        }
        if ("after_initial_resources".equals(menuCode)) {
            showAfterInitialResources();
        }
        if ("after_getfrommarket".equals(menuCode)) {
            showAfterMarketMenu();
        }
        if ("after_store_supply".equals(menuCode)) {
            showAfterSupply();
        }
        if ("end_game_has_started".equals(menuCode)) {
            endGameHasStarted();

        }
        if("game_has_ended".equals(menuCode)) {
            gameHasEnded();
        }
        if("back_in_lobby". equals(menuCode)) {
            System.out.println("\n\n\n\nYou are being redirected to the main lobby...\n");
            System.out.println("Back to the main lobby!\n");
            showMainLobbyMenu();
        }
    }

    private void endGameHasStarted() {
        printColoredMessage("END GAME PHASE HAS STARTED! " +
                "Game will end once it is the first player's turn.", Constants.BOLD + Constants.ANSI_RED);
    }

    private void gameHasEnded() {
        System.out.println("GAME HAS ENDED!");

        System.out.println("PLAYER POINTS: ");
        reducedModel.getReducedGame().getPlayers().forEach((nickname, reducedPlayer) -> {
            System.out.println(nickname + ": " + reducedPlayer.getDashboard().getPlayerPoints());
        });
    }

    private void showAfterSupply() {
        ArrayList<Resource> supply = reducedModel.getReducedPlayer().getDashboard().getSupply();
        if(!supply.isEmpty()) {
            System.out.println("DEPOSIT: ");
            showDeposit(reducedModel.getReducedPlayer().getDashboard().getDeposit());
            System.out.println("SUPPLY: ");
            showSupply(reducedModel.getReducedPlayer().getDashboard().getSupply());
            System.out.println();
            System.out.println("Unstored resources at the end of the turn will give faith points to other players.");
        } else {
            System.out.println("Supply is empty, insert HELP to see full command or list or insert one");
        }

    }


    private void showAfterMarketMenu() {
        System.out.println("DEPOSIT: ");
        showDeposit(reducedModel.getReducedPlayer().getDashboard().getDeposit());
        System.out.println("SUPPLY: ");
        showSupply(reducedModel.getReducedPlayer().getDashboard().getSupply());
        printMessage("Please use");
        printColoredMessageNoNL("STOREFROMSUPPLY <supplyPos> <depositPos>", Constants.SERVANT_COLOR);
        printMessage(" to store a resource from your supply to your deposit");
        printColoredMessageNoNL("STOREFROMSUPPLYTOEXTRADEPOSIT <supplyPos> <depositIndex> <depositPos>", Constants.SERVANT_COLOR);
        printMessage(" to store a resource from your supply to an extra deposit");
        printColoredMessageNoNL("CHANGEDEPOSIT", Constants.SERVANT_COLOR);
        printMessage(" to rearrange your deposit");
        printMessage("");

    }

    private void showAfterInitialResources() {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        printMessage("");
        showDeposit(reducedModel.getReducedPlayer().getDashboard().getDeposit());
        printMessage("");
        int owed = getOwed(reducedGame.getFirstTurns());
        if(owed - Arrays.stream(reducedGame.getPlayers()
                .get(reducedGame.getClientPlayer()).getDashboard().getDeposit()).filter(Objects::nonNull).count() == 0) {
            clientConnection.send(ClientMessageInputParser.parseInput("ENDTURN", this));
        } else {
            printMessage("You are owed " + owed + " more resources.");

        }
    }

    private void showNextCardDiscardMenu() {
        ReducedGame rg = reducedModel.getReducedGame();;
        int toDiscard = rg.getPlayers().get(rg.getClientPlayer()).getHand().size() - 2;
        if(toDiscard > 0) {
            printMessage("");
            printMessage("Please discard " + toDiscard + " more cards with the command ");
            printColoredMessage("DISCARDEXCESSCARD <cardIndex>", Constants.SERVANT_COLOR);
            showHand(rg.getClientPlayer());
        } else {
            int owed = getOwed(rg.getFirstTurns());
            if(owed > 0) {
                printMessage("");
                printMessage("You are owed " + owed + " resources. Please use the command");
                printColoredMessageNoNL("GETINITIALRESOURCES <resource> <depositindex>", Constants.SERVANT_COLOR);
                printMessage(" to acquire them. ");
                printMessage("Here is your deposit currently: ");
                showDeposit(reducedModel.getReducedPlayer().getDashboard().getDeposit());
                printMessage("");
            } else {
                clientConnection.send(ClientMessageInputParser.parseInput("ENDTURN", this));
            }
        }
    }

    private int getOwed(int firstTurns) {
        return switch (firstTurns) {
            case 1, 2 -> 1;
            case 3 -> 2;
            default -> 0;
        };
    }

    private void showAfterGameStartMenu() {
        ReducedGame rg = reducedModel.getReducedGame();
        printColoredMessage("\nGame is starting", Constants.GOLD_COLOR);
        firstPhaseMenu(rg);

    }

    private void showAfterEndTurnMenu() {
        ReducedGame rg = reducedModel.getReducedGame();
        TurnPhase phase = reducedModel.getReducedGame().getTurnPhase();
        if(phase == TurnPhase.FIRST_TURN) {
            firstPhaseMenu(rg);
        }
        if(phase == TurnPhase.COMMON) {
            commonPhaseMenu(rg);
        }
    }

    private void firstPhaseMenu(ReducedGame rg) {
        if(rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            printMessage(Constants.ANSI_CYAN + "It is now your turn" + Constants.ANSI_RESET);
            printMessage("");
            int toDiscard = rg.getPlayers().get(rg.getClientPlayer()).getHand().size() - 2;
            if(toDiscard > 0) {
                printMessage("Here is your hand. Please discard " + toDiscard + " more cards with the command ");
                printColoredMessage("DISCARDEXCESSCARD <cardIndex>", Constants.SERVANT_COLOR);
                showHand(rg.getClientPlayer());
            }
        } else {
            printMessage(Constants.ANSI_CYAN + "It is now " + rg.getCurrentPlayer()  + "'s turn, please wait..." + Constants.ANSI_RESET);
        }
    }

    private void commonPhaseMenu(ReducedGame rg) {
        if (rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            printMessage(Constants.ANSI_CYAN + "It is now your turn" + Constants.ANSI_RESET);
            showGeneralMenu();
            printMessageNoNL("> ");

        } else {
            printMessage(Constants.ANSI_CYAN + "It is now " + rg.getCurrentPlayer() + "'s turn, please wait..." + Constants.ANSI_RESET);
        }
    }

    public void showViewMenu() {
        ReducedGame rg = reducedModel.getReducedGame();
        printColoredMessageNoNL("SHOW <globalObject>", Constants.SERVANT_COLOR);
        printMessage(" to show a global object. Available options: ");
        printMessage("DVGRID, MARKET");
        printColoredMessageNoNL("SHOW <object> <nickname>", Constants.SERVANT_COLOR);
        printMessage(" to show a player object. Available options: ");
        printMessage("HAND, DASHBOARD, FAITHTRACK, WAREHOUSE");
        printMessageNoNL("Player list: | ");
        rg.getPlayers().keySet().forEach(p ->
            printMessageNoNL(p + " | ")
        );
        printMessage("");
    }

    public void showDepositMenu() {
        printColoredMessageNoNL("STOREFROMSUPPLY <supplyPos> <depositPos>", Constants.SERVANT_COLOR);
        printMessage(" to store a resource from your supply to your deposit");
        printColoredMessageNoNL("STOREFROMSUPPLYTOEXTRADEPOSIT <supplyPos> <depositIndex> <depositPos>", Constants.SERVANT_COLOR);
        printMessage(" to store a resource from your supply to an extra deposit");
        printColoredMessageNoNL("CHANGEDEPOSIT", Constants.SERVANT_COLOR);
        printMessage(" to rearrange your deposit");
        printMessage("");
    }

    public void showGeneralMenu() {
        printMessage("");
        printMessage("Please choose from the following commands: ");
        printColoredMessage("------ GAME ACTIONS -------", Constants.GOLD_COLOR);
        printColoredMessageNoNL("GETFROMMARKET <move>", Constants.SERVANT_COLOR);
        printMessage(" to get from market");
        printColoredMessageNoNL("BUYDEVELOPMENTCARD <index> <position>", Constants.SERVANT_COLOR);
        printMessage(" to buy a development card");
        printColoredMessageNoNL("ACTIVATEDASHBOARDPRODUCTION ", Constants.SERVANT_COLOR);
        printMessage(" to activate a dashboard production");
        printColoredMessageNoNL("ACTIVATEDEVELOPMENTPRODUCTION <cardIndex>", Constants.SERVANT_COLOR);
        printMessage(" to activate a development card production");
        printColoredMessageNoNL("ACTIVATELEADERPRODUCTION <cardIndex>", Constants.SERVANT_COLOR);
        printMessage(" to activate a leader card production");
        printMessage("");

        printColoredMessageNoNL("------ LEADER ACTIONS -------", Constants.GOLD_COLOR);
        printMessage("");
        printColoredMessageNoNL("DISCARDCARD <cardIndex>", Constants.SERVANT_COLOR);
        printMessage(" to discard a leader card");
        printColoredMessageNoNL("PLAYLEADERCARD <cardIndex>", Constants.SERVANT_COLOR);
        printMessage(" to play a leader card");
        printMessage("");

        printColoredMessageNoNL("------ PERSONAL ACTIONS -------", Constants.GOLD_COLOR);
        printMessage("");
        showDepositMenu();

        printColoredMessageNoNL("------ OTHER ACTIONS -------", Constants.GOLD_COLOR);
        printMessage("");
        showViewMenu();

        printColoredMessageNoNL("ENDTURN ", Constants.SERVANT_COLOR);
        printMessage(" to end your turn");
        printMessage("");

        printColoredMessageNoNL("HELP ", Constants.SERVANT_COLOR);
        printMessage(" to show the full list of commands again");
    }

}
