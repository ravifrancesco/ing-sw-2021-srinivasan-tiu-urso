package it.polimi.ingsw.view.UI.CLI;

import it.polimi.ingsw.model.full.cards.DevelopmentCard;
import it.polimi.ingsw.model.full.cards.LeaderCard;
import it.polimi.ingsw.model.full.marbles.Marble;
import it.polimi.ingsw.model.full.specialAbilities.*;
import it.polimi.ingsw.model.full.table.Resource;
import it.polimi.ingsw.model.full.table.TurnPhase;
import it.polimi.ingsw.model.full.tokens.Token;
import it.polimi.ingsw.model.reduced.*;
import it.polimi.ingsw.model.utils.GameSettings;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.server.lobby.GameLobbyDetails;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.view.UI.CLI.IO.ClientMessageInputParser;
import it.polimi.ingsw.view.UI.CLI.IO.Constants;
import it.polimi.ingsw.view.UI.CLI.IO.FaithTrackCLI;
import it.polimi.ingsw.view.UI.UI;
import it.polimi.ingsw.view.UI.UIType;
import it.polimi.ingsw.view.virtualView.client.ClientVirtualView;
import it.polimi.ingsw.view.virtualView.client.OfflineClientVirtualView;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CLI implements UI {

    private final Scanner input;
    private final FaithTrackCLI ftcli;
    private ReducedModel reducedModel;
    private ClientVirtualView clientVirtualView;
    private OfflineClientVirtualView offlineClientVirtualView;
    private boolean local;

    /**
     * CLI for the game.
     */
    public CLI() {
        this.input = new Scanner(System.in);
        this.ftcli = new FaithTrackCLI();
    }

    /**
     * Prints a colored message on the CLI.
     *
     * @param message the message to print
     * @param color   the ansi code for the color in string format
     */
    public void printColoredMessage(String message, String color) {
        System.out.println(color + message + Constants.ANSI_RESET);
    }

    /**
     * Method to get a string with the IP to connect to
     *
     * @return the IP string
     * @throws IOException if the string is not parsed correctly
     */
    public String getIp() throws IOException {
        System.out.println();
        System.out.println("Insert the server ip: ");
        System.out.print("> ");
        return input.nextLine();
    }

    /**
     * Method to get the port †o connect on
     *
     * @return the port integer
     */
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

    /**
     * Used to get an input from the user.
     *
     * @return the command (string) the user entered
     */
    public String readCommand() {
        System.out.print("> ");
        return input.nextLine();
    }

    /**
     * Reads the next line from the input stream
     *
     * @return the read string
     */
    public String readLine() {
        return input.nextLine();
    }

    /**
     * Prints a red colored message for errors.
     *
     * @param error the message to print
     */
    public void printErrorMessage(String error) {
        System.out.println(Constants.ANSI_RED + error + Constants.ANSI_RESET);
    }

    /**
     * Prints the message with a new line character
     *
     * @param message the message to print
     */
    public void printMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints a message without the new line character
     *
     * @param message the message to print
     */
    public void printMessageNoNL(String message) {
        System.out.print(message);
    }

    /**
     * Prints a colored message without the new line character
     *
     * @param message the message to print
     * @param color   ansi code for the color
     */
    public void printColoredMessageNoNL(String message, String color) {
        System.out.print(color + message + Constants.ANSI_RESET);
    }

    /**
     * Prints the menu for when the user is inside the main lobby
     */
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

    /**
     * Starts the reading thread, which will keep asking the user for an input
     */
    public void startReadingThread() {
        new Thread(() -> {
            while (true) {
                String command = this.readCommand();
                ClientMessage clientMessage = ClientMessageInputParser.parseInput(command, this);
                sendMessage(clientMessage);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Sends a client message to the server
     *
     * @param clientMessage the message to send with the handle method inside to auto-handle itself
     */
    private void sendMessage(ClientMessage clientMessage) {
        if (local && clientMessage != null) {
            offlineClientVirtualView.send(clientMessage);
        } else if (clientMessage != null) {
            try {
                clientVirtualView.send(clientMessage);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Used to read the nickname by the user's input
     *
     * @return a string with the nickname
     */
    public String getNickname() {
        System.out.println("Insert nickname:");
        System.out.print("> ");
        return input.nextLine();
    }

    /**
     * Shows the game lobbies
     *
     * @param activeGameLobbies an array list with all the information about the available game lobby
     */
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
    public void enterGamePhase(boolean isHost, boolean isOffline) {
        // no action
    }

    /**
     * Shows the hand full of a cards of the player
     *
     * @param nickname of the player of which the hand has to be shown
     */
    public void showHand(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            List<LeaderCard> leaderCardList = reducedPlayer.getHand();
            if (leaderCardList.size() != 0) {
                IntStream.range(0, leaderCardList.size()).forEach(i -> {
                    printMessage("");
                    printColoredMessage("------- CARD #" + i + " -------", Constants.BOLD + Constants.SHIELD_COLOR);
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

    /**
     * Shows a leader card on the CLI
     *
     * @param leaderCard the leader card to show
     */
    private void showCard(LeaderCard leaderCard) {
        if (leaderCard != null) {
            printMessage("CARD VICTORY POINTS: \n" + leaderCard.getVictoryPoints());
            printMessage("");

            if (leaderCard.getBannerCost().size() != 0) {
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

    /**
     * Shows a development card on the CLI
     *
     * @param developmentCard the development card to show
     */
    private void showCard(DevelopmentCard developmentCard) {

        printMessage("CARD VICTORY POINTS: \n" + developmentCard.getVictoryPoints());
        printMessage("");

        printMessageNoNL("CARD WITH BANNER ");
        printColoredMessageNoNL(developmentCard.getBanner().toString().split("=")[1].split(":")[0],
                handleBannerColor(developmentCard.getBanner().toString().split("=")[1].split(":")[0]));

        printMessage(" LEVEL " + developmentCard.getBanner().getLevel());
        showResourceCost(developmentCard.getResourceCost());

        handleSA(developmentCard.getProductionPower());
    }

    /**
     * Shows the resource a cost for cards
     *
     * @param resourceCost a map with the full costs
     */
    private void showResourceCost(Map<Resource, Integer> resourceCost) {
        if (resourceCost.size() != 0) {
            printMessage("RESOURCE COST:");
            resourceCost.forEach((key, value) -> {
                printColoredMessageNoNL("" + key, handleResourceColor(key.toString()));
                printMessage(": " + value);
            });
            printMessage("");
        }

        printMessageNoNL("SPECIAL ABILITY: ");
    }

    /**
     * Returns an ANSI color code based on the input
     *
     * @param banner the input string representing a banner
     * @return correct ANSI color code
     */
    private String handleBannerColor(String banner) {
        return switch (banner) {
            case "YELLOW" -> Constants.GOLD_COLOR;
            case "BLUE" -> Constants.SHIELD_COLOR;
            case "PURPLE" -> Constants.SERVANT_COLOR;
            case "GREEN" -> Constants.ANSI_GREEN;
            default -> Constants.STONE_COLOR;
        };
    }

    /**
     * Chooses the color to print the resources with
     *
     * @param resource the resource to decide the color for
     * @return the correct color associated to that resource
     */
    private String handleResourceColor(String resource) {
        return switch (resource) {
            case "GOLD" -> Constants.GOLD_COLOR;
            case "SHIELD" -> Constants.SHIELD_COLOR;
            case "SERVANT" -> Constants.SERVANT_COLOR;
            case "STONE" -> Constants.STONE_COLOR;
            default -> Constants.ANSI_GENERIC_WHITE;
        };
    }

    /**
     * Checks what type of special ability is passed and prints information about it
     *
     * @param sa the special ability to print
     */
    private void handleSA(SpecialAbility sa) {
        switch (sa.getType()) {
            case DEVELOPMENT_CARD_DISCOUNT -> printMessage("[DVD] Discounts " + ((DevelopmentCardDiscount) sa).getQuantity() + " " +
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
                if (required.size() > 0) {
                    required.forEach((resource, qty) ->
                            printMessage(qty + " " + handleResourceColor(resource.toString()) + resource + Constants.ANSI_RESET)
                    );
                }
                if (pp.getNumRequiredAny() > 0) {
                    printMessage(pp.getNumRequiredAny() + Constants.STONE_COLOR + " ANY RESOURCE" + Constants.ANSI_RESET);
                }
                printMessage("Into");

                if (produced.size() > 0) {
                    produced.forEach((resource, qty) ->
                            printMessage(qty + " " + handleResourceColor(resource.toString()) + resource + Constants.ANSI_RESET)
                    );
                }
                if (pp.getNumProducedAny() > 0) {
                    printMessage(pp.getNumProducedAny() + Constants.STONE_COLOR + " ANY RESOURCE" + Constants.ANSI_RESET);
                }
                if (pp.getNumberFaithPoints() != 0) {
                    printColoredMessage("" + pp.getNumberFaithPoints() + " FAITH POINT", Constants.RED_ISH);
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

    /**
     * Parses a card toString output in order to get the banner string
     *
     * @param banner the card toString
     * @return the banner color string
     */
    private String handleBanner(String banner) {
        String[] split = banner.split("=");
        return split[1].split(":")[0];
    }

    /**
     * Shows a player dashboard
     *
     * @param nickname the player of which the dashboard has to be shown
     */
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
                if (i < reducedDashboard.getPlayedLeaderCards().size()) {
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
                if (reducedDashboard.getPlayedDevelopmentCards().get(i).isEmpty()) {
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

    /**
     * Shows a supply on the CLI
     *
     * @param supply the supply to show
     */
    public void showSupply(ArrayList<Resource> supply) {
        int hBarSize = (7 * supply.size()) + (4 * supply.size()) + 1;
        if (supply.size() > 0) {
            IntStream.range(0, hBarSize).forEach(i -> {
                // printing indexes
                switch (i) {
                    case (5) -> printMessageNoNL("0");
                    case (16) -> printMessageNoNL("1");
                    case (27) -> printMessageNoNL("2");
                    case (38) -> printMessageNoNL("3");
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

    /**
     * Chooses the array of strings with the correct rows for market/deposit visualization
     *
     * @param r the resource to print
     * @return an array of string with ANSI codes to print for market/deposit
     */
    public String[] renderResourceRow(Resource r) {
        if (r == null) {
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

    /**
     * Shows a player faith track, plus calls to see Lorenzo's one if in single player
     *
     * @param nickname the player nickname
     */
    public void showFaithTrack(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        if (reducedPlayer != null) {
            if (reducedModel.getReducedGame().getNumberOfPlayers() == 1) {
                showLorenzoFT(nickname);
                System.out.println("\n\n YOUR FAITHTRACK: \n");
            }
            int position = reducedPlayer.getDashboard().getPosition();
            ftcli.setPos(position);
            List<Pair<Integer, Integer>> vc = getPassedVaticanReports(reducedPlayer.getDashboard());
            IntStream.range(0, 3).forEach(i -> ftcli.handleVr(i + 1, vc.get(i).second));
            ftcli.showFTCLI();
        } else {
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
        }
    }

    /**
     * Shows Lorenzo's faith track
     *
     * @param nickname player nickname (used to get the dashboard which contains information about Lorenzo)
     */
    public void showLorenzoFT(String nickname) {
        ReducedPlayer reducedPlayer = reducedModel.getReducedGame().getPlayers().get(nickname);
        System.out.println("\nLORENZO's FAITHTRACK: ");
        int lorenzoPosition = reducedPlayer.getDashboard().getLorenzoIlMagnificoPosition();
        ftcli.setPos(lorenzoPosition);
        List<Pair<Integer, Integer>> vc = getPassedVaticanReports(reducedPlayer.getDashboard());
        IntStream.range(0, 3).forEach(i -> ftcli.handleVr(i + 1, vc.get(i).second));
        ftcli.showFTCLI();
    }

    /**
     * Returns the passed vatican reports by a certain player
     *
     * @param reducedDashboard the player dashboard
     * @return a list of pair of integer that represents the state of the vatican reports
     */
    public List<Pair<Integer, Integer>> getPassedVaticanReports(ReducedDashboard reducedDashboard) {
        return reducedDashboard.getVaticanReports().entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getKey().second))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }


    /**
     * Shows a player warehouse
     *
     * @param nickname the player nickname
     */
    public void showWarehouse(String nickname) {
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
            showAllExtraDeposits(extraDeposits);
        } else {
            printErrorMessage("PLAYER " + nickname + " DOESN'T EXISTS");
        }
    }

    /**
     * Shows all player's extra deposits
     *
     * @param extraDeposits the player's extra deposits array
     */
    private void showAllExtraDeposits(Resource[][] extraDeposits) {
        IntStream.range(0, extraDeposits.length).forEach(i -> {
            if (extraDeposits[i] != null) {
                Resource res = ((WarehouseExtraSpace) reducedModel.getReducedPlayer().
                        getDashboard().getPlayedLeaderCards().get(i).getSpecialAbility()).getStoredResource();
                printMessageNoNL("EXTRA DEPOSIT #" + i + " FOR RESOURCE ");
                printColoredMessage("" + res, handleResourceColor(res.toString()));
                showExtraDeposit(extraDeposits[i]);
            }
        });
    }

    /**
     * Shows a single extra deposit
     *
     * @param deposit the extra deposit to show
     */
    public void showExtraDeposit(Resource[] deposit) {
        if (deposit[0] == null) {
            printMessage("  INDEX 0: EMPTY");
        } else {
            printMessage("  INDEX 0: " + deposit[0]);
        }
        if (deposit[1] == null) {
            printMessage("  INDEX 1: EMPTY");
        } else {
            printMessage("  INDEX 1: " + deposit[1]);
        }
    }

    /**
     * Shows the deposit
     *
     * @param deposit the deposit to show
     */
    public void showDeposit(Resource[] deposit) {
        int topHbar = 11;
        int middleHbar = 26;
        int bottomHbar = 33;

        printMessageNoNL("           ");

        IntStream.range(0, topHbar).forEach(i -> {
                    if (i == 5) {
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
                    if (i == 5) {
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
            if (i == 5) {
                printMessageNoNL("" + 3);
            } else if (i == 16) {
                printMessageNoNL("" + 4);
            } else if (i == 27) {
                printMessageNoNL("" + 5);
            } else {
                printMessageNoNL("―");
            }
        });
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


    /**
     * Shows the development card grid
     */
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
        ReducedPlayer reducedPlayer = reducedModel.getReducedPlayer();
        if (reducedPlayer.getDiscounts() != null
                && reducedPlayer.getDiscounts().size() != 0) {
            printMessage("\n\nKeep in mind that due to your activated ability(s)\n");
            reducedPlayer.getDiscounts().forEach((resource, qty) -> {
                printMessageNoNL("" + qty);
                printColoredMessageNoNL(" " + resource, handleResourceColor(resource.toString()));
                printMessage(" will be discounted\n\n");
            });
        }
        switch (choice.toUpperCase()) {
            case "1", "2", "3" -> showDvRow(Integer.parseInt(choice));
            case "GREEN", "BLUE", "YELLOW", "PURPLE" -> showDvColumn(choice);
            default -> printColoredMessage("Wrong input", Constants.ANSI_RED);
        }
    }

    /**
     * Returns the row-column pair of indexes starting from the 1D array index (help method for dvgrid)
     *
     * @param position the 1D array index
     * @return the pair row-column
     */
    public Pair<Integer, Integer> invertIndex(int position) {
        return switch (position) {
            case (1) -> new Pair<>(1, 2);
            case (2) -> new Pair<>(1, 3);
            case (3) -> new Pair<>(1, 4);
            case (4) -> new Pair<>(2, 1);
            case (5) -> new Pair<>(2, 2);
            case (6) -> new Pair<>(2, 3);
            case (7) -> new Pair<>(2, 4);
            case (8) -> new Pair<>(3, 1);
            case (9) -> new Pair<>(3, 2);
            case (10) -> new Pair<>(3, 3);
            case (11) -> new Pair<>(3, 4);
            default -> new Pair<>(1, 1);
        };
    }

    /**
     * Shows a dv grid row of cards
     *
     * @param level the level of the cards, which has an associated row
     */
    public void showDvRow(int level) {
        int startIndex = 4 * (level - 1);
        IntStream.range(0, 4).forEach(i -> {
            if (!reducedModel.getReducedGameBoard().getGrid().get(startIndex + i).isEmpty()) {

                printColoredMessage("------- CARD ROW/COL ("
                        + invertIndex(startIndex + i).first + "," + invertIndex(startIndex + i).second + ")" +
                        " -------", Constants.SHIELD_COLOR);
                showCard(reducedModel.getReducedGameBoard().getGrid().get(startIndex + i).peek());
            }
        });

        printMessage("\nPlease use");
        printColoredMessageNoNL("BUYDEVELOPMENTCARD <row> <column> <position>", Constants.SERVANT_COLOR);
        printMessage(" to buy a development card");
        printMessageNoNL("\nAvailable empty slots: \n");
        IntStream.range(0, ReducedDashboard.NUM_DEVELOPMENT_CARD_STACKS).forEach(i -> {
            if (reducedModel.getReducedPlayer().getDashboard().getPlayedDevelopmentCards().get(i).isEmpty()) {
                printMessageNoNL("" + i + " ");
            }
        });
        printMessage("");
    }

    /**
     * Shows a dv grid column of cards
     *
     * @param color the color of the cards, which has an associated column
     */
    public void showDvColumn(String color) {
        int index = getShift(color);
        // se green allora 0, 4, 8
        // se blue allora 1, 5, 9
        // se yellow allora 2, 6, 10
        // se purple allora 3, 7, 11
        IntStream.range(0, 3).forEach(i -> {
            if (!reducedModel.getReducedGameBoard().getGrid().get(index + (i * 4)).isEmpty()) {
                printColoredMessage("------- CARD ROW/COL ("
                        + invertIndex(index + (i * 4)).first + "," + invertIndex(index + (i * 4)).second + ")" +
                        " -------", Constants.SHIELD_COLOR);
                showCard(reducedModel.getReducedGameBoard().getGrid().get(index + (i * 4)).peek());
            }
        });

        printMessage("Please use");
        printColoredMessageNoNL("BUYDEVELOPMENTCARD <row> <column> <position>", Constants.SERVANT_COLOR);
        printMessage(" to buy a development card");
        printMessageNoNL("Available empty slots: ");
        IntStream.range(0, ReducedDashboard.NUM_DEVELOPMENT_CARD_STACKS).forEach(i -> {
            if (reducedModel.getReducedPlayer().getDashboard().getPlayedDevelopmentCards().get(i).isEmpty()) {
                printMessageNoNL("" + i + " ");
            }
        });
        printMessage("");
    }

    /**
     * Returns amount of shift between cards for development card grid visualization
     *
     * @param color the color for which the shift is asked
     * @return the integer with the shift
     */
    public int getShift(String color) {
        return switch (color) {
            case "GREEN" -> 0;
            case "BLUE" -> 1;
            case "YELLOW" -> 2;
            case "PURPLE" -> 3;
            default -> 0;
        };
    }

    /**
     * Shows the game market
     */
    public void showMarket() {
        System.out.println();
        ReducedGameBoard reducedGameBoard = reducedModel.getReducedGameBoard();
        Marble[] marblesGrid = reducedGameBoard.getMarblesGrid();
        int gridIndex;
        String[] colours;
        int ctr = 0;
        for (gridIndex = 0; gridIndex < 12; gridIndex += 4) {
            colours = getMarblesColours(marblesGrid, gridIndex);
            printMarblesRow(colours[0], colours[1], colours[2], colours[3], ctr);
            ctr += 1;
        }
        System.out.println("     3          4          5         6      ");
        System.out.println();
        printMarble(getMarbleColor(marblesGrid[12]));
        System.out.println("FREE MARBLE");
        System.out.println();

        printMessage("Move indexes can be found next to the respective row or column. ");
        printMessage("Please insert:");
        printColoredMessageNoNL("GETFROMMARKET <moveIndex>", Constants.SERVANT_COLOR);
        printMessage(" to get resources from market.");
        printMessage("");
    }

    /**
     * Returns the correct color to print a marble with
     *
     * @param marble the marble to print
     * @return the color used to print the marlbe
     */
    public String getMarbleColor(Marble marble) {
        return switch (marble.getMarbleColor().name()) {
            case "WHITE" -> Constants.ANSI_BG_WHITE;
            case "BLUE" -> Constants.SHIELD_COLOR_BG;
            case "GREY" -> Constants.STONE_COLOR_BG;
            case "PURPLE" -> Constants.SERVANT_COLOR_BG;
            case "RED" -> Constants.ANSI_BG_RED;
            case "YELLOW" -> Constants.GOLD_COLOR_BG;
            default -> Constants.ANSI_BG_WHITE;
        };
    }

    /**
     * Gets marble color but for multiple ones (used for market and succession of marbles)
     *
     * @param grid marbles grid
     * @param ind1 index for the row to parse colours from
     * @return the array of colours to use
     */
    public String[] getMarblesColours(Marble[] grid, int ind1) {
        String[] colours = new String[4];
        IntStream.range(ind1, ind1 + 4).forEach(i -> {
            switch (grid[i].getMarbleColor().name()) {
                case "WHITE" -> colours[i - ind1] = Constants.ANSI_BG_WHITE;
                case "BLUE" -> colours[i - ind1] = Constants.SHIELD_COLOR_BG;
                case "GREY" -> colours[i - ind1] = Constants.STONE_COLOR_BG;
                case "PURPLE" -> colours[i - ind1] = Constants.SERVANT_COLOR_BG;
                case "RED" -> colours[i - ind1] = Constants.ANSI_BG_RED;
                case "YELLOW" -> colours[i - ind1] = Constants.GOLD_COLOR_BG;
            }
        });
        return colours;
    }

    /**
     * Prints a row of marbles
     *
     * @param color1 color of marble one on the row
     * @param color2 color of marble two on the row
     * @param color3 color of marble three on the row
     * @param color4 color of marble four on the row
     * @param move   the integer of the move
     */
    public void printMarblesRow(String color1, String color2, String color3, String color4, int move) {
        int k1;
        int k2 = 4;

        for (k1 = 3; k1 > 0; k1--) {

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

            System.out.println();
            k2 += 2;
        }

        k2 = 10;
        for (k1 = 0; k1 < 4; k1++) {

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

            if (k1 == 0) {
                System.out.print(" " + move);
            }

            System.out.println();
            k2 -= 2;
        }
        System.out.println();
    }

    /**
     * Prints a marble
     *
     * @param color marble color
     */
    public void printMarble(String color) {
        int k1;
        int k2 = 4;

        for (k1 = 3; k1 > 0; k1--) {

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");

            System.out.println();
            k2 += 2;
        }

        k2 = 10;
        for (k1 = 0; k1 < 4; k1++) {

            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            IntStream.range(0, k2).forEach(i -> printColoredMessageNoNL(" ", color));
            IntStream.range(0, k1).forEach(i -> System.out.print(" "));
            System.out.print(" ");
            System.out.println();
            k2 -= 2;
        }
        System.out.println();
    }

    /**
     * See UI class
     */
    @Override
    public void startUI(ClientVirtualView clientVirtualView, ReducedModel reducedModel) {
        local = false;
        this.clientVirtualView = clientVirtualView;
        this.reducedModel = reducedModel;
    }

    /**
     * See UI class
     */
    @Override
    public void startUI(OfflineClientVirtualView offlineClientVirtualView, ReducedModel reducedModel) {
        local = true;
        this.offlineClientVirtualView = offlineClientVirtualView;
        this.reducedModel = reducedModel;
    }

    /**
     * Prints payment costs
     *
     * @param golds    amount of gold to pay
     * @param servants amount of servants to pay
     * @param shields  amount of shields to pay
     * @param stones   amount of stones to pay
     */
    public void printPaymentCost(int golds, int servants, int shields, int stones) {
        if ((golds + servants + shields + stones) != 0) {
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

    /**
     * Shows the game lobby menu
     */
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


    /**
     * See UI
     *
     * @return
     */
    @Override
    public UIType getType() {
        return UIType.CLI;
    }

    /**
     * Shows a certain menu based on the parameter string code
     *
     * @param menuCode the string representing a code for the menu to show
     */
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
        if ("game_has_ended".equals(menuCode)) {
            gameHasEnded();
        }
        if ("game_has_ended_single".equals(menuCode)) {
            gameHasEndedSingle();
        }

        if ("back_in_lobby".equals(menuCode)) {
            System.out.println("\n\nYou are being redirected to the main lobby...\n");
            System.out.println("Back to the main lobby!\n");
            showMainLobbyMenu();
        }
        if ("after_end_turn_single".equals(menuCode)) {
            showAfterEndTurnSingleMenu();
        }
        if ("force_disconnection".equals(menuCode)) {
            forceDisconnection();
        }
    }

    /*
    Disconnect the client after the game is finished.
     */
    private void forceDisconnection() {
        printColoredMessage("\n\nDisconnecting...", Constants.BOLD + Constants.ANSI_RED );
        System.exit(0);

    }

    /**
     * Shows who has won in a single player game
     */
    private void gameHasEndedSingle() {
        showAfterEndTurnSingleMenu();
        int winner = establishWinner();
        if (winner == 0) {
            printColoredMessage("Lorenzo has won!", Constants.BOLD + Constants.ANSI_RED);
        } else {
            printColoredMessage("You've won!", Constants.BOLD + Constants.ANSI_GREEN);
        }
    }

    /**
     * Establishes who the winner is in a single player game
     *
     * @return 0 if Lorenzo has won, 1 if player has won;
     */
    private int establishWinner() {
        ReducedDashboard reducedDashboard = reducedModel.getReducedPlayer().getDashboard();
        ReducedGameBoard reducedGameBoard = reducedModel.getReducedGameBoard();
        if (checkFTEnd(reducedDashboard) || checkDvGridEnd(reducedGameBoard)) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Checks to see if a Lorenzo has finished the faith track
     *
     * @param reducedDashboard the player's reduced dashboard
     * @return true if the faith track was finished by Lorenzp
     */
    private boolean checkFTEnd(ReducedDashboard reducedDashboard) {
        return reducedDashboard.getLorenzoIlMagnificoPosition() == GameSettings.FAITH_TRACK_LENGTH - 1;
    }

    /**
     * Checks to see if Lorenzo has won the game by discarding the entirety of cards of a color
     *
     * @param reducedGameBoard the reduced game board with the cards
     * @return true if Lorenzo has won by discarding all cards of a color
     */
    private boolean checkDvGridEnd(ReducedGameBoard reducedGameBoard) {
        for (int i = 8; i < 12; i++) {
            if (reducedGameBoard.getGrid().get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Shows the menu for when the player has just ended his turn in singleplayer.
     */
    private void showAfterEndTurnSingleMenu() {
        printMessage("\n\n");
        showActivatedToken(reducedModel.getReducedGame().getToken());
        IntStream.range(0, 12).forEach(i -> {
            String color = decideColor(i);
            String level = decideLevel(i);
            if (color != null && level != null) {
                printMessageNoNL("Level " + level + " ");
                printColoredMessageNoNL(color, handleBannerColor(color));
                printMessage(" cards remaining: " + reducedModel.getReducedGameBoard().getGrid().get(i).size());
            } else {
                printMessage("Remaining card with index " + i + ": " + reducedModel.getReducedGameBoard().getGrid().get(i).size());
            }
        });
        showLorenzoFT(reducedModel.getReducedPlayer().getNickname());
    }

    /**
     * Shows the last activated token by Lorenzo
     *
     * @param token the token activated
     */
    private void showActivatedToken(Token token) {
        printMessageNoNL("\nLorenzo has activated the following token: ");
        switch (token.getType()) {
            case "one" -> printColoredMessage("Single Black Cross move\n", Constants.RED_ISH);
            case "two" -> printColoredMessage("Double Black Cross move", Constants.RED_ISH);
            case "yellow" -> printColoredMessage("Discard Yellow Development Card\n", Constants.GOLD_COLOR);
            case "purple" -> printColoredMessage("Discard Purple Development Card\n", Constants.SERVANT_COLOR);
            case "blue" -> printColoredMessage("Discard Blue Development Card\n", Constants.SHIELD_COLOR);
            case "green" -> printColoredMessage("Discard Green Development Card\n", Constants.ANSI_GREEN);
        }
    }

    /**
     * Color of the cards based on the index (dvgrid)
     *
     * @param i index of the card
     * @return the color string
     */
    private String decideColor(int i) {
        return switch (i) {
            case 0, 4, 8 -> "GREEN";
            case 1, 5, 9 -> "BLUE";
            case 2, 6, 10 -> "YELLOW";
            case 3, 7, 11 -> "PURPLE";
            default -> null;
        };
    }

    /**
     * Level of the cards based on the index (dvgrid)
     *
     * @param i index of the card
     * @return the index integer
     */
    private String decideLevel(int i) {
        return switch (i) {
            case 0, 1, 2, 3 -> "1";
            case 4, 5, 6, 7 -> "2";
            case 8, 9, 10, 11 -> "3";
            default -> null;
        };
    }

    /**
     * Shows the notification for when the end game phase has started
     */
    private void endGameHasStarted() {
        printColoredMessage("END GAME PHASE HAS STARTED! " +
                "Game will end once it is the first player's turn.", Constants.BOLD + Constants.ANSI_RED);
    }

    /**
     * Shows the notification for when the game has ended.
     */
    private void gameHasEnded() {
        printColoredMessage("GAME HAS ENDED!", Constants.BOLD + Constants.ANSI_RED);

        printColoredMessage("\nPLAYER POINTS: ", Constants.BOLD);
        reducedModel.getReducedGame().getPlayers().forEach((nickname, reducedPlayer) -> {
            System.out.println(nickname + ": " + reducedPlayer.getDashboard().getPlayerPoints());
        });
    }

    /**
     * Shows the menu for when a player has just finished storing a resource inside its supply
     */
    private void showAfterSupply() {
        ArrayList<Resource> supply = reducedModel.getReducedPlayer().getDashboard().getSupply();
        if (!supply.isEmpty()) {
            System.out.println("DEPOSIT: ");
            showDeposit(reducedModel.getReducedPlayer().getDashboard().getDeposit());

            if (reducedModel.getReducedPlayer().getDashboard().getExtraDeposits()[0] != null) {
                showSingleExtraWh(0);
            }
            if (reducedModel.getReducedPlayer().getDashboard().getExtraDeposits()[1] != null) {
                showSingleExtraWh(1);
            }
            System.out.println("SUPPLY: ");
            showSupply(reducedModel.getReducedPlayer().getDashboard().getSupply());
            System.out.println();
            System.out.println("Unstored resources at the end of the turn will give faith points to other players.");
        } else {
            System.out.println("Supply is empty, insert HELP to see full command or list or insert one");
        }

    }

    /**
     * Shows an extra warehouse from the deposit
     *
     * @param i the extra warehouse index
     */
    private void showSingleExtraWh(int i) {
        Resource res = ((WarehouseExtraSpace) reducedModel.getReducedPlayer().
                getDashboard().getPlayedLeaderCards().get(i).getSpecialAbility()).getStoredResource();
        printMessageNoNL("EXTRA DEPOSIT #" + i + " FOR RESOURCE ");
        printColoredMessage("" + res, handleResourceColor(res.toString()));
        showExtraDeposit(reducedModel.getReducedPlayer().getDashboard().getExtraDeposits()[i]);
        System.out.println();
    }

    /**
     * Shows the menu for when the player has just finished getting resources from the market
     */
    private void showAfterMarketMenu() {
        System.out.println("DEPOSIT: ");
        showDeposit(reducedModel.getReducedPlayer().getDashboard().getDeposit());
        Resource[][] extraDeposits = reducedModel.getReducedPlayer().getDashboard().getExtraDeposits();

        if (extraDeposits[0] != null || extraDeposits[1] != null) {
            System.out.println("EXTRA DEPOSIT(s): ");
        }
        if (reducedModel.getReducedPlayer().getDashboard().getExtraDeposits()[0] != null) {
            showSingleExtraWh(0);
        }
        if (reducedModel.getReducedPlayer().getDashboard().getExtraDeposits()[1] != null) {
            showSingleExtraWh(1);
        }

        System.out.println("SUPPLY: ");
        showSupply(reducedModel.getReducedPlayer().getDashboard().getSupply());
        printMessage("Please use");
        printColoredMessageNoNL("STOREFROMSUPPLY <supplyPos> <depositPos>", Constants.SERVANT_COLOR);
        printMessage(" to store a resource from your supply to your deposit");
        printColoredMessageNoNL("STOREFROMSUPPLYED <supplyPos> <edPos> <leaderCardPos>", Constants.SERVANT_COLOR);
        printMessage(" to store a resource from your supply to an extra deposit");
        printColoredMessageNoNL("CHANGEDEPOSIT", Constants.SERVANT_COLOR);
        printMessage(" to rearrange your deposit");
        printMessage("");

    }

    /**
     * Shows the menu for when a player has just finished getting his initial resources
     */
    private void showAfterInitialResources() {
        ReducedGame reducedGame = reducedModel.getReducedGame();
        printMessage("");
        showDeposit(reducedModel.getReducedPlayer().getDashboard().getDeposit());
        printMessage("");
        int owed = getOwed(reducedGame.getFirstTurns());
        if (owed - Arrays.stream(reducedGame.getPlayers()
                .get(reducedGame.getClientPlayer()).getDashboard().getDeposit()).filter(Objects::nonNull).count() == 0) {
            sendMessage(ClientMessageInputParser.parseInput("ENDTURN", this));
        } else {
            printMessage("You are owed " + owed + " more resources.");

        }
    }

    /**
     * Shows the menu for when a player has just finished discarding a card
     */
    private void showNextCardDiscardMenu() {
        ReducedGame rg = reducedModel.getReducedGame();
        int toDiscard = rg.getPlayers().get(rg.getClientPlayer()).getHand().size() - 2;
        if (toDiscard > 0) {
            printMessage("");
            printMessage("Please discard " + toDiscard + " more cards with the command ");
            printColoredMessage("DISCARDEXCESSCARD <cardIndex>", Constants.SERVANT_COLOR);
            showHand(rg.getClientPlayer());
        } else {
            int owed = getOwed(rg.getFirstTurns());
            if (owed > 0) {
                printMessage("");
                printMessage("You are owed " + owed + " resources. Please use the command");
                printColoredMessageNoNL("GETINITIALRESOURCES <resource> <depositindex>", Constants.SERVANT_COLOR);
                printMessage(" to acquire them. ");
                printMessage("Here is your deposit currently: ");
                showDeposit(reducedModel.getReducedPlayer().getDashboard().getDeposit());
                printMessage("");
            } else {
                sendMessage(ClientMessageInputParser.parseInput("ENDTURN", this));
            }
        }
    }

    /**
     * Decides how many resources a player is owed based on the turn
     *
     * @param firstTurns the turn index (amount of players that completed the first turn)
     * @return the amount of resources owed
     */
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

    /**
     * Shows the menu for when the player has just finished its turn
     */
    private void showAfterEndTurnMenu() {
        ReducedGame rg = reducedModel.getReducedGame();
        TurnPhase phase = reducedModel.getReducedGame().getTurnPhase();
        if (phase == TurnPhase.FIRST_TURN) {
            firstPhaseMenu(rg);
        }
        if (phase == TurnPhase.COMMON) {
            commonPhaseMenu(rg);
        }
    }

    /**
     * Shows the menu for when the game has just started and the player is in turn
     *
     * @param rg
     */
    private void firstPhaseMenu(ReducedGame rg) {
        if (rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            printMessage(Constants.ANSI_CYAN + "It is now your turn" + Constants.ANSI_RESET);
            printMessage("");
            int toDiscard = rg.getPlayers().get(rg.getClientPlayer()).getHand().size() - 2;
            if (toDiscard > 0) {
                printMessage("Here is your hand. Please discard " + toDiscard + " more cards with the command ");
                printColoredMessage("DISCARDEXCESSCARD <cardIndex>", Constants.SERVANT_COLOR);
                showHand(rg.getClientPlayer());
            }
        } else {
            printMessage(Constants.ANSI_CYAN + "It is now " + rg.getCurrentPlayer() + "'s turn, please wait..." + Constants.ANSI_RESET);
        }
    }

    /**
     * Shows the menu for when a player is in turn in the middle of the game
     *
     * @param rg
     */
    private void commonPhaseMenu(ReducedGame rg) {
        if (rg.getClientPlayer().equals(rg.getCurrentPlayer())) {
            printMessage(Constants.ANSI_CYAN + "It is now your turn" + Constants.ANSI_RESET);
            showGeneralMenu();
            printMessageNoNL("> ");

        } else {
            printMessage(Constants.ANSI_CYAN + "It is now " + rg.getCurrentPlayer() + "'s turn, please wait..." + Constants.ANSI_RESET);
        }
    }

    /**
     * Shows the menu for all the view commands
     */
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
        if (reducedModel.getReducedGame().getNumberOfPlayers() == 1) {
            printMessage("Lorenzo's faith track can be shown by calling to see your own");
        }
        printMessage("");
    }

    /**
     * Shows the menu for all the deposit commands
     */
    public void showDepositMenu() {
        printColoredMessageNoNL("STOREFROMSUPPLY <supplyPos> <depositPos>", Constants.SERVANT_COLOR);
        printMessage(" to store a resource from your supply to your deposit");
        printColoredMessageNoNL("STOREFROMSUPPLYED <supplyPos> <edPos> <leaderCardPos>", Constants.SERVANT_COLOR);
        printMessage(" to store a resource from your supply to an extra deposit");
        printColoredMessageNoNL("CHANGEDEPOSIT", Constants.SERVANT_COLOR);
        printMessage(" to rearrange your deposit");
        printMessage("");
    }

    /**
     * Shows the menu with all the commands
     */
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
