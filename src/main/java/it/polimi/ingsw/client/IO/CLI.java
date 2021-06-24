package it.polimi.ingsw.client.IO;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.ReducedModel;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.client.UIType;
import it.polimi.ingsw.controller.client.reducedModel.ReducedDashboard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGame;
import it.polimi.ingsw.controller.client.reducedModel.ReducedGameBoard;
import it.polimi.ingsw.controller.client.reducedModel.ReducedPlayer;
import it.polimi.ingsw.model.Hand;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.marbles.Marble;
import it.polimi.ingsw.model.specialAbilities.*;
import it.polimi.ingsw.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.server.lobby.messages.clientMessages.ClientMessage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLI implements UI {

    private Scanner input;

    private ReducedModel reducedModel;

    private ClientConnection clientConnection;


    public CLI() {
        this.input = new Scanner(System.in);
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

    public void setReducedModel(ReducedModel reducedModel) {
        this.reducedModel = reducedModel;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
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

        printColoredMessageNoNL("CREATEGAME <numberofplayers>", Constants.ANSI_MAGENTA);
        printMessage(" to create a game lobby");

        printColoredMessageNoNL("SHOWGAMES", Constants.ANSI_MAGENTA);
        printMessage(" to see list of available game lobbies");

        printColoredMessageNoNL("JOINGAME <game_id>", Constants.ANSI_MAGENTA);
        printMessage(" to join a game lobby");

        printColoredMessageNoNL("QUIT", Constants.ANSI_MAGENTA);
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

    public void showHand(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            List<LeaderCard> leaderCardList = reducedPlayer.getHand();
            if (leaderCardList.size() != 0) {
                IntStream.range(0, leaderCardList.size()).forEach(i -> {
                    printMessage("");
                    printColoredMessage("------- CARD #" + i + " -------", Constants.BOLD+Constants.ANSI_BLUE);
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

        showResourceCost(leaderCard.getResourceCost(), leaderCard);
        handleSA(leaderCard.getSpecialAbility());

    }

    private void showCard(DevelopmentCard developmentCard) {
        printMessage("CARD VICTORY POINTS: \n" + developmentCard.getVictoryPoints());
        printMessage("");

        printMessageNoNL("CARD WITH BANNER");
        printColoredMessageNoNL(developmentCard.getBanner().toString(),
                handleBannerColor(developmentCard.getBanner().toString()));
        printMessageNoNL(" LEVEL " + developmentCard.getBanner().getLevel());

        showResourceCost(developmentCard.getResourceCost(), developmentCard);

        handleSA(developmentCard.getProductionPower());
    }

    private void showResourceCost(Map<Resource, Integer> resourceCost, Card developmentCard) {
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
            case "YELLOW" -> Constants.ANSI_YELLOW;
            case "BLUE" -> Constants.ANSI_BLUE;
            case "PURPLE" -> Constants.ANSI_MAGENTA;
            case "GREEN" -> Constants.ANSI_GREEN;
            default -> Constants.ANSI_WHITE;
        };
    }

    private String handleResourceColor(String resource) {
        return switch(resource) {
            case "GOLD" -> Constants.ANSI_YELLOW;
            case "SHIELD" -> Constants.ANSI_BLUE;
            case "SERVANT" -> Constants.ANSI_MAGENTA;
            case "STONE" -> Constants.ANSI_WHITE;
            default -> Constants.ANSI_GENERIC_WHITE;
        };
    }

    private void handleSA(SpecialAbility sa) {
        switch(sa.getType()) {
            case DEVELOPMENT_CARD_DISCOUNT -> {
                printMessage("[DVD] Discounts " + ((DevelopmentCardDiscount) sa).getQuantity() + " " +
                        handleResourceColor(((DevelopmentCardDiscount) sa).getResource().toString()) +
                        ((DevelopmentCardDiscount) sa).getResource() + Constants.ANSI_RESET +
                        " when buying a development card");
            }
            case PRODUCTION_POWER -> {
                ProductionPower pp = (ProductionPower) sa;
                Map<Resource, Integer> required = pp.getResourceRequired();
                Map<Resource, Integer> produced = pp.getResourceProduced();
                required.remove(Resource.ANY);
                produced.remove(Resource.ANY);
                printMessage("[PP] Allows to transform");
                if(required.size() > 0) {
                    required.forEach((resource, qty) -> {
                        printMessage(qty + " " + handleResourceColor(resource.toString()) + resource.toString() + Constants.ANSI_RESET);
                    });
                }
                if(pp.getNumRequiredAny() > 0) {
                    printMessage(pp.getNumRequiredAny() + Constants.ANSI_WHITE + " ANY RESOURCE" + Constants.ANSI_RESET);
                }
                printMessage("Into");
                if(produced.size() > 0) {
                    produced.forEach((resource, qty) -> {
                        printMessage(qty + " " + handleResourceColor(resource.toString()) + resource.toString() + Constants.ANSI_RESET);
                    });
                }
                if(pp.getNumProducedAny() > 0) {
                    printMessage(pp.getNumProducedAny() + Constants.ANSI_WHITE + " ANY RESOURCE" + Constants.ANSI_RESET);
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
        String output = "";
        Map<Resource, Integer> required = pp.getResourceRequired();
        Map<Resource, Integer> produced = pp.getResourceProduced();
        printMessage("REQUIRES: ");
        required.forEach((resource, qty) -> {
            printMessage(handleResourceColor(resource.toString()) + qty);
        });
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

            printMessage("PLAYED LEADER CARDS: \n");
            reducedDashboard.getPlayedLeaderCards().forEach(this::showCard);

            System.out.print("PLAYED DEVELOPMENT CARDS: ");
            reducedDashboard.getPlayedDevelopmentCards().stream().filter(stack -> !stack.isEmpty())
                    .map(Stack::peek).forEach(this::showCard);
            System.out.println();
            System.out.println("SUPPLY: ");
            ArrayList<Resource> supply = reducedPlayer.getDashboard().getSupply();
            showSupply(supply);
        } else {
            printMessageNoNL("Player ");
            printColoredMessageNoNL(nickname, Constants.ANSI_CYAN);
            printMessage("does not exist");

        }
    }

    public void showSupply (ArrayList<Resource> supply) {
        int hBarSize = (7*supply.size()) + (4*supply.size()) + 1;

        IntStream.range(0, hBarSize).forEach(i -> printMessageNoNL("―"));
        System.out.println();

        IntStream.range(0, 6).forEach(column -> {
            IntStream.range(0, supply.size()).forEach(i -> {
                printMessageNoNL("| " + renderResourceRow(supply.get(i))[column] + " |");
            });
            printMessage("");
        });
        IntStream.range(0, hBarSize).forEach(i -> printMessageNoNL("―"));
        System.out.println();

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
            System.out.println(nickname + "'s FAITHTRACK: ");
            ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();
            int position = reducedDashboard.getPosition();
            IntStream.range(0, reducedDashboard.getFaithTrackVictoryPoints().length)
                    .forEach(pos -> System.out.print(pos==position ? " x " : " o "));
            System.out.println();
            // TODO print victory points and vatican reports
        } else {
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
        }
    }



    public void showWarehouse(String nickname) {
        // TODO mettere gli empty in bianco
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();
            System.out.println("\nDEPOSIT\n");
            showDeposit(nickname);
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
                                printColoredMessage("    EMPTY", Constants.ANSI_WHITE);
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

    public void showDeposit(String nickname) {
        int topHbar = 11;
        int middleHbar = 26;
        int bottomHbar = 33;
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();
        Resource[] res = reducedDashboard.getDeposit();

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
            IntStream.range(0, 1).forEach(i -> {
                printMessageNoNL("           | " + renderResourceRow(res[i])[column] + " |");
            });
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
                printMessageNoNL("    | " + renderResourceRow(res[i])[column] + " |");
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
                printMessageNoNL("| " + renderResourceRow(res[i])[column] + " |");
            });
            printMessage("");
        });
        IntStream.range(0, bottomHbar).forEach(i -> printMessageNoNL("―"));
        System.out.println();

        /*
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        ReducedDashboard reducedDashboard = reducedPlayer.getDashboard();
        for (int i=0; i < reducedDashboard.getDeposit().length; i++) {
            Resource[] deposit = reducedDashboard.getDeposit();
            if(deposit[i] != null) {
                printColoredMessageNoNL(i + ": " + deposit[i].toString() + " ", handleResourceColor(deposit[i].toString()));
            } else {
                printColoredMessageNoNL(i + ": " + "EMPTY" + " ", Constants.ANSI_WHITE);
            }
            if (i == 0 || i == 2) {
                System.out.println();
            }
        }

         */
    }

    public void printBoldColoredBg(String color) {


    }



    public void showDVGrid() {
        ReducedGameBoard reducedGameBoard = reducedModel.getReducedGameBoard();
        for (int i = 0; i < reducedGameBoard.getGrid().size(); i++) {
            System.out.print(reducedGameBoard.getGrid().get(i).isEmpty() ? " | " : reducedGameBoard.getGrid().get(i).peek().toString() + " | ");
            if ((i+1) % 4 == 0) {
                System.out.println();
            }
        }
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
        printColoredMessageNoNL("GETFROMMARKET <moveIndex>", Constants.ANSI_MAGENTA);
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
        return switch(marble.getType()) {
            case "WHITE" -> Constants.ANSI_BG_WHITE;
            case "BLUE" -> Constants.ANSI_BG_BLUE;
            case "GREY" -> Constants.ANSI_BG_GREY;
            case "PURPLE" -> Constants.ANSI_BG_PURPLE;
            case "RED" -> Constants.ANSI_BG_RED;
            case "YELLOW" -> Constants.ANSI_BG_YELLOW;
            default -> Constants.ANSI_BG_WHITE;
        };
    }

    public String[] getMarblesColours(Marble[] grid, int ind1) {
        String[] colours = new String[4];
        IntStream.range(ind1, ind1+4).forEach(i -> {
            switch(grid[i].getType()) {
                case "WHITE" -> colours[i-ind1] = Constants.ANSI_BG_WHITE;
                case "BLUE" -> colours[i-ind1] = Constants.ANSI_BG_BLUE;
                case "GREY" -> colours[i-ind1] = Constants.ANSI_BG_GREY;
                case "PURPLE" -> colours[i-ind1] = Constants.ANSI_BG_PURPLE;
                case "RED" -> colours[i-ind1] = Constants.ANSI_BG_RED;
                case "YELLOW" -> colours[i-ind1] = Constants.ANSI_BG_YELLOW;
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
        setClientConnection(clientConnection);
        setReducedModel(reducedModel);
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

        printColoredMessageNoNL("LEAVELOBBY", Constants.ANSI_MAGENTA);
        printMessage(" to leave the lobby");

        printColoredMessageNoNL("STARTGAME", Constants.ANSI_MAGENTA);
        printMessage(" to start the game");

        printColoredMessageNoNL("QUIT", Constants.ANSI_MAGENTA);
        printMessage(" to to quit the game");

        printMessage("");
    }


    @Override
    public UIType getType() {
        return UIType.CLI;
    }

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
    }

    private void showAfterMarketMenu() {
        showSupply(reducedModel.getReducedPlayer().getDashboard().getSupply());
        printMessageNoNL("Please use");
        printColoredMessageNoNL("STOREFROMSUPPLY <supplyPos> <depositPos>", Constants.ANSI_MAGENTA);
        printMessage(" to store a resource from your supply to your deposit");
        printColoredMessageNoNL("STOREFROMSUPPLYTOEXTRADEPOSIT <supplyPos> <depositIndex> <depositPos>", Constants.ANSI_MAGENTA);
        printMessage(" to store a resource from your supply to an extra deposit");
        printColoredMessageNoNL("CHANGEDEPOSIT", Constants.ANSI_MAGENTA);
        printMessage(" to rearrange your deposit");
        printMessage("");

    }

    private void showAfterInitialResources() {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        printMessage("");
        showDeposit(reducedGame.getClientPlayer());
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
            printColoredMessage("DISCARDEXCESSCARD <cardIndex>", Constants.ANSI_MAGENTA);
            showHand(rg.getClientPlayer());
        } else {
            int owed = getOwed(rg.getFirstTurns());
            if(owed > 0) {
                printMessage("");
                printMessage("You are owed " + owed + " resources. Please use the command");
                printColoredMessageNoNL("GETINITIALRESOURCES <resource> <depositindex>", Constants.ANSI_MAGENTA);
                printMessage(" to acquire them. ");
                printMessage("");
                showDeposit(rg.getClientPlayer());
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
        printColoredMessage("\nGame is starting", Constants.ANSI_YELLOW);
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
                printColoredMessage("DISCARDEXCESSCARD <cardIndex>", Constants.ANSI_MAGENTA);
                showHand(rg.getClientPlayer());
            }
        } else {
            printMessage(Constants.ANSI_CYAN + "It is now " + rg.getCurrentPlayer()  + "'s turn, please wait..." + Constants.ANSI_RESET);
        }
    }

    private void commonPhaseMenu(ReducedGame rg) {
        if (rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            printMessage(Constants.ANSI_CYAN + "It is now your turn" + Constants.ANSI_RESET);
            printMessage("");
            printMessage("Please choose from the following commands: ");
            printColoredMessage("------ GAME ACTIONS -------", Constants.ANSI_YELLOW);
            printColoredMessageNoNL("GETFROMMARKET <move>", Constants.ANSI_MAGENTA);
            printMessage(" to get from market");
            printColoredMessageNoNL("BUYDEVELOPMENTCARD <row> <column> <placement>", Constants.ANSI_MAGENTA);
            printMessage(" to buy a development card");
            printColoredMessageNoNL("ACTIVATEDASHBOARDPRODUCTION ", Constants.ANSI_MAGENTA);
            printMessage(" to activate a dashboard production");
            printColoredMessageNoNL("ACTIVATEDEVELOPMENTPRODUCTION <cardIndex>", Constants.ANSI_MAGENTA);
            printMessage(" to activate a development card production");
            printColoredMessageNoNL("ACTIVATELEADERPRODUCTION <cardIndex>", Constants.ANSI_MAGENTA);
            printMessage(" to activate a leader card production");
            printMessage("");

            printColoredMessageNoNL("------ LEADER ACTIONS -------", Constants.ANSI_YELLOW);
            printMessage("");
            printColoredMessageNoNL("DISCARDCARD <cardIndex>", Constants.ANSI_MAGENTA);
            printMessage(" to discard a leader card");
            printColoredMessageNoNL("PLAYLEADERCARD <cardIndex>", Constants.ANSI_MAGENTA);
            printMessage(" to play a leader card");
            printMessage("");

            printColoredMessageNoNL("------ PERSONAL ACTIONS -------", Constants.ANSI_YELLOW);
            printMessage("");
            showDepositMenu();

            printColoredMessageNoNL("------ OTHER ACTIONS -------", Constants.ANSI_YELLOW);
            printMessage("");
            showViewMenu();
            printColoredMessageNoNL("ENDTURN ", Constants.ANSI_MAGENTA);
            printMessage(" to end your turn");
            printMessage("");
            printMessage("> ");
        } else {
            printMessage(Constants.ANSI_CYAN + "It is now " + rg.getCurrentPlayer() + "'s turn, please wait..." + Constants.ANSI_RESET);
        }
    }

    public void showViewMenu() {
        ReducedGame rg = reducedModel.getReducedGame();
        printColoredMessageNoNL("SHOW <globalObject>", Constants.ANSI_MAGENTA);
        printMessage(" to show a global object. Available options: ");
        printMessage("DVGRID, MARKET");
        printColoredMessageNoNL("SHOW <object> <nickname>", Constants.ANSI_MAGENTA);
        printMessage(" to show a player object. Available options: ");
        printMessage("HAND, DASHBOARD, FAITHTRACK, WAREHOUSE");
        printMessageNoNL("Player list: | ");
        rg.getPlayers().keySet().forEach(p -> {
            printMessageNoNL(p + " | ");
        });
        printMessage("");
        printMessage("");
    }

    public void showDepositMenu() {
        printColoredMessageNoNL("STOREFROMSUPPLY <supplyPos> <depositPos>", Constants.ANSI_MAGENTA);
        printMessage(" to store a resource from your supply to your deposit");
        printColoredMessageNoNL("STOREFROMSUPPLYTOEXTRADEPOSIT <supplyPos> <depositIndex> <depositPos>", Constants.ANSI_MAGENTA);
        printMessage(" to store a resource from your supply to an extra deposit");
        printColoredMessageNoNL("CHANGEDEPOSIT", Constants.ANSI_MAGENTA);
        printMessage(" to rearrange your deposit");
        printMessage("");
    }

}
