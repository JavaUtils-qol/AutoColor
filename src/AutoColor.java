import java.util.regex.Pattern;

/**
 * This class aids with usage of colored console using ANSI escape codes.
 * <br>
 * <br>
 * For more info about ANSI escape codes visit:
 * https://en.wikipedia.org/wiki/ANSI_escape_code
 */
public class AutoColor {
    /**
     * Contains all(almost) the modifiers an ANSI color code can take.
     */
    public static enum Config {
        Default,
        Bold,
        Italic,
        Underline,
        Crossed_Out,
        Dim,
        Double_Underline
    }

    private static boolean[] _config = new boolean[] { false, false, false, false, false, false };

    /**
     * Will take true/false as input and color it to green/red.
     * 
     * @param bool - boolean to color
     * @return <b>colored text</b> (String) - green/red
     */
    public static String colorize(boolean bool) {
        return (bool ? "\033[1;32m" : "\033[1;31m") + bool + "\033[0m";
    }

    /**
     * Will take a String and color it according to the given hex value.
     * 
     * @param str      - text to color
     * @param hexColor - #xxxxxx
     * @return <b>colored text</b> (String)
     */
    public static String colorize(String str, String hexColor) {
        int red = Integer.valueOf(hexColor.substring(1, 3), 16);
        int green = Integer.valueOf(hexColor.substring(3, 5), 16);
        int blue = Integer.valueOf(hexColor.substring(5, 7), 16);
        return "\033[" + setupConfigANSI() + "38;2;" + red + ";" + green + ";" + blue + "m" + str + "\033[0m";
    }

    /**
     * Will take a String and split it according to the given char delimiter then
     * will color those according to the given hex values.
     * 
     * @param str       - text to split and then color
     * @param delimiter - character to split the text at
     * @param hexColors - multiple hex values
     * @return <b>colored text</b> (String)
     */
    public static String colorize(String str, char delimiter, String... hexColors) {
        String[] strs = str.split(Pattern.quote(String.valueOf(delimiter)));
        String finalStr = "";
        int index = 0;
        int hexIndex = 0;

        while (index != strs.length) {
            hexIndex = index;
            if (hexIndex >= hexColors.length)
                hexIndex = hexColors.length - 1;
            int red = Integer.valueOf(hexColors[hexIndex].substring(1, 3), 16);
            int green = Integer.valueOf(hexColors[hexIndex].substring(3, 5), 16);
            int blue = Integer.valueOf(hexColors[hexIndex].substring(5, 7), 16);
            finalStr += "\033[" + setupConfigANSI() + "38;2;" + red + ";" + green + ";" + blue + "m" + strs[index]
                    + "\033[0m";
            index++;
        }
        return finalStr;
    }

    /**
     * Will take a String and split it according to the given regex delimiter then
     * will color those according to the given hex values.
     * 
     * @param str        - text to split and then color
     * @param regexDelim - regex to split the text at
     * @param hexColors  - multiple hex values
     * @return <b>colored text</b> (String)
     */
    public static String colorize(String str, String regexDelim, String... hexColors) {
        String[] strs = str.split(regexDelim);
        String finalStr = "";
        int index = 0;
        int hexIndex = 0;

        while (index != strs.length) {
            hexIndex = index;
            if (hexIndex >= hexColors.length)
                hexIndex = hexColors.length - 1;
            int red = Integer.valueOf(hexColors[hexIndex].substring(1, 3), 16);
            int green = Integer.valueOf(hexColors[hexIndex].substring(3, 5), 16);
            int blue = Integer.valueOf(hexColors[hexIndex].substring(5, 7), 16);
            finalStr += "\033[" + setupConfigANSI() + "38;2;" + red + ";" + green + ";" + blue + "m" + strs[index]
                    + "\033[0m";
            index++;
        }
        return finalStr;
    }

    /**
     * Allows the user to modify one of the 6 ANSI modifiers.
     * 
     * @param config - modifier to change
     */
    public static void setConfig(Config config) {
        switch (config) {
            case Default:
                _config = new boolean[] { false, false, false, false, false, false };
                break;
            case Bold:
                _config[0] = !_config[0];
                break;
            case Italic:
                _config[1] = !_config[1];
                break;
            case Underline:
                _config[2] = !_config[2];
                break;
            case Crossed_Out:
                _config[3] = !_config[3];
                break;
            case Dim:
                _config[4] = !_config[4];
                break;
            case Double_Underline:
                _config[5] = !_config[5];
                break;
            default:
                throw new EnumConstantNotPresentException(Config.class, "null");
        }
    }

    /**
     * Allows the user to modify all of the 6 ANSI modifiers.
     * 
     * @param isBold             - true/false
     * @param isItalic           - true/false
     * @param isUnderlined       - true/false
     * @param isCrossedOut       - true/false
     * @param isDim              - true/false
     * @param isDoublyUnderlined - true/false
     */
    public static void setConfig(boolean isBold, boolean isItalic, boolean isUnderlined, boolean isCrossedOut,
            boolean isDim, boolean isDoublyUnderlined) {
        _config = new boolean[] { isBold, isItalic, isUnderlined, isCrossedOut, isDim, isDoublyUnderlined };
    }

    /**
     * Will return the state of the specified modifier.
     * 
     * @param config - modifier to return
     * @return <b>state</b> of the given modifier - true/false
     */
    public static boolean getConfig(Config config) {
        switch (config) {
            case Default:
                return _config == new boolean[] { false, false, false, false, false, false };
            case Bold:
                return _config[0];
            case Italic:
                return _config[1];
            case Underline:
                return _config[2];
            case Crossed_Out:
                return _config[3];
            case Dim:
                return _config[4];
            case Double_Underline:
                return _config[5];
            default:
                throw new EnumConstantNotPresentException(Config.class, "null");
        }
    }

    /**
     * Will return the full modifiers array.
     * 
     * @return <b>Config array</b> - boolean[6] array
     */
    public static boolean[] getConfig() {
        return _config;
    }



    

    private AutoColor() {} // set constructor to private to stop object creation

    private static String setupConfigANSI() {
        String config = "";
        for (int i = 0; i < _config.length; i++) {
            if (_config[i]) {
                switch (i) {
                    case 0: // Bold
                        config += "1;";
                        break;
                    case 1: // Italic
                        config += "3;";
                        break;
                    case 2: // Underline
                        config += "4;";
                        break;
                    case 3: // Crossed Out
                        config += "9;";
                        break;
                    case 4: // Dim
                        config += "2;";
                        break;
                    case 5: // Double Underline
                        config += "21;";
                        break;
                }
            }
        }
        return config;
    }
}
