package it.polimi.ingsw.view.UI.CLI.IO;

public class Constants {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String SHIELD_COLOR = "\u001B[34m";
    public static final String GOLD_COLOR = "\u001b[33m";
    public static final String SERVANT_COLOR = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String STONE_COLOR = "\u001b[37m";

    public static final String SHIELD_COLOR_BG = "\u001b[48;5;21m";
    public static final String ANSI_BG_RED = "\u001b[48;5;196m";
    public static final String STONE_COLOR_BG = "\u001b[48;5;245m";
    public static final String SERVANT_COLOR_BG = "\u001b[48;5;93m";
    public static final String ANSI_BG_WHITE = "\u001b[48;5;231m";
    public static final String GOLD_COLOR_BG = "\u001b[48;5;226m";

    public static final String BLACK = "\u001b[38;5;0m";



    public static final String FT_EMPTY = "\u001b[48;5;229m";
    public static final String FT_POS = "\u001b[48;5;160m";
    public static final String FT_SPACE_UNACTIVATED = "\u001b[48;5;9m";
    public static final String FT_SPACE_MISSED = "\u001b[48;5;124m";
    public static final String FT_SPACE_ACTIVATED = "\u001b[48;5;46m";


    public static final String FT_SEP = "\u001b[48;5;229m";





    public static final String[] servant = {
            "  " + concatColored("   ", SERVANT_COLOR_BG) + "  ",
            " " +  concatColored("     ", SERVANT_COLOR_BG) + " ",
            " " +  concatColored("     ", SERVANT_COLOR_BG) + " ",
            "  " + concatColored("   ", SERVANT_COLOR_BG) + "  ",
            " " +  concatColored("     ", SERVANT_COLOR_BG) + " ", concatColored("       ", SERVANT_COLOR_BG) };

    public static final String[] shield = {
            "  " +  concatColored("   ", SHIELD_COLOR_BG) + "  ", // 2
            concatColored("       ", SHIELD_COLOR_BG), // 0
            concatColored("       ", SHIELD_COLOR_BG), // 0
            " " + concatColored("     ", SHIELD_COLOR_BG) + " ", // 1
            " " + concatColored("     ", SHIELD_COLOR_BG) + " ", // 1
            "  " + concatColored("   ", SHIELD_COLOR_BG) + "  " // 2
    };

    public static final String[] stone = {
            concatColored("    ", STONE_COLOR_BG) + "   ",
            concatColored("    ", STONE_COLOR_BG) + "   ",
            concatColored("      ", STONE_COLOR_BG) + " ",
            concatColored("       ", STONE_COLOR_BG),
            concatColored("       ", STONE_COLOR_BG),
            concatColored("       ", STONE_COLOR_BG)
    };

    public static final String[] gold = {
            "  " + concatColored("    ", GOLD_COLOR_BG) + "  ",
            " " + concatColored("      ", GOLD_COLOR_BG) + " ",
            " " + concatColored("      ", GOLD_COLOR_BG) + " ",
            " " + concatColored("      ", GOLD_COLOR_BG) + " ",
            " " + concatColored("      ", GOLD_COLOR_BG) + " ",
            "  " + concatColored("    ", GOLD_COLOR_BG) + "  ",
    };

    public static final String[] empty = {
            "       ",
            "       ",
            "       ",
            "       ",
            "       ",
            "       "
    };



    public static final String logo =
            """
                                                ███▄ ▄███▓    ▄▄▄         ▓█████      ██████    ▄▄▄█████▓    ██▀███      ██▓                               \s
                                               ▓██▒▀█▀ ██▒   ▒████▄       ▓█   ▀    ▒██    ▒    ▓  ██▒ ▓▒   ▓██ ▒ ██▒   ▓██▒                               \s
                                               ▓██    ▓██░   ▒██  ▀█▄     ▒███      ░ ▓██▄      ▒ ▓██░ ▒░   ▓██ ░▄█ ▒   ▒██▒                               \s
                                               ▒██    ▒██    ░██▄▄▄▄██    ▒▓█  ▄      ▒   ██▒   ░ ▓██▓ ░    ▒██▀▀█▄     ░██░                               \s
                                               ▒██▒   ░██▒  4 ▓█   ▓██▒   ░▒████▒   ▒██████▒▒     ▒██▒ ░    ░██▓ ▒██▒   ░██░                               \s
                                               ░ ▒░   ░  ░    ▒▒   ▓▒█░   ░░ ▒░ ░   ▒ ▒▓▒ ▒ ░     ▒ ░░      ░ ▒▓ ░▒▓░   ░▓                                 \s
                                               ░  ░      ░     ▒   ▒▒ ░    ░ ░  ░   ░ ░▒  ░ ░       ░         ░▒ ░ ▒░    ▒ ░                               \s
                                               ░      ░        ░   ▒         ░      ░  ░  ░       ░           ░░   ░     ▒ ░                               \s
                                                      ░            ░  ░      ░  ░         ░                    ░         ░                                 \s
                                                                                                                                                           \s
                                                                    ▓█████▄    ▓█████     ██▓                                                              \s
                                                                    ▒██▀ ██▌   ▓█   ▀    ▓██▒                                                              \s
                                                                    ░██   █▌   ▒███      ▒██░                                                              \s
                                                                    ░▓█▄   ▌   ▒▓█  ▄    ▒██░                                                              \s
                                                                    ░▒████▓    ░▒████▒   ░██████▒                                                          \s
                                                                     ▒▒▓  ▒    ░░ ▒░ ░   ░ ▒░▓  ░                                                          \s
                                                                     ░ ▒  ▒     ░ ░  ░   ░ ░ ▒  ░                                                          \s
                                                                     ░ ░  ░       ░        ░ ░                                                             \s
                                                                       ░          ░  ░       ░  ░                                                          \s
                                                                     ░                                                                                     \s
                     ██▀███      ██▓    ███▄    █     ▄▄▄           ██████     ▄████▄      ██▓    ███▄ ▄███▓   ▓█████     ███▄    █    ▄▄▄█████▓    ▒█████ \s
                    ▓██ ▒ ██▒   ▓██▒    ██ ▀█   █    ▒████▄       ▒██    ▒    ▒██▀ ▀█     ▓██▒   ▓██▒▀█▀ ██▒   ▓█   ▀     ██ ▀█   █    ▓  ██▒ ▓▒   ▒██▒  ██▒
                    ▓██ ░▄█ ▒   ▒██▒   ▓██  ▀█ ██▒   ▒██  ▀█▄     ░ ▓██▄      ▒▓█    ▄    ▒██▒   ▓██    ▓██░   ▒███      ▓██  ▀█ ██▒   ▒ ▓██░ ▒░   ▒██░  ██▒
                    ▒██▀▀█▄     ░██░   ▓██▒  ▐▌██▒   ░██▄▄▄▄██      ▒   ██▒   ▒▓▓▄ ▄██▒   ░██░   ▒██    ▒██    ▒▓█  ▄    ▓██▒  ▐▌██▒   ░ ▓██▓ ░    ▒██   ██░
                    ░██▓ ▒██▒   ░██░   ▒██░   ▓██░    ▓█   ▓██▒   ▒██████▒▒   ▒ ▓███▀ ░   ░██░   ▒██▒   ░██▒   ░▒████▒   ▒██░   ▓██░     ▒██▒ ░    ░ ████▓▒░
                    ░ ▒▓ ░▒▓░   ░▓     ░ ▒░   ▒ ▒     ▒▒   ▓▒█░   ▒ ▒▓▒ ▒ ░   ░ ░▒ ▒  ░   ░▓     ░ ▒░   ░  ░   ░░ ▒░ ░   ░ ▒░   ▒ ▒      ▒ ░░      ░ ▒░▒░▒░\s
                      ░▒ ░ ▒░    ▒ ░   ░ ░░   ░ ▒░     ▒   ▒▒ ░   ░ ░▒  ░ ░     ░  ▒       ▒ ░   ░  ░      ░    ░ ░  ░   ░ ░░   ░ ▒░       ░         ░ ▒ ▒░\s
                      ░░   ░     ▒ ░      ░   ░ ░      ░   ▒      ░  ░  ░     ░            ▒ ░   ░      ░         ░         ░   ░ ░      ░         ░ ░ ░ ▒ \s
                       ░         ░              ░          ░  ░         ░     ░ ░          ░            ░         ░  ░            ░                    ░ ░ \s
                                                                              ░                                                                            \s""";





    public static final String ANSI_GENERIC_WHITE = "\u001b[38;5;255m";
    public static final String BOLD = "\u001b[1m";


    public static String concatColored(String text, String color) {
        return color + text + ANSI_RESET;
    }

}
