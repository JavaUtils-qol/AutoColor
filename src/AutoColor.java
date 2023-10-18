import java.util.regex.Pattern;

/**
 * This class aids with usage of colored console using ANSI escape codes.
 * <br><br>
 * For more info about ANSI escape codes visit: https://en.wikipedia.org/wiki/ANSI_escape_code
 */
public class AutoColor {
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

    public static String colorize(boolean bool) {
        return (bool ? "\033[1;32m" : "\033[1;31m") + bool + "\033[0m";
    }

    public static String colorize(String str, String hexColor) {
        int red = Integer.valueOf(hexColor.substring(1, 3), 16);
        int green = Integer.valueOf(hexColor.substring(3, 5), 16);
        int blue = Integer.valueOf(hexColor.substring(5, 7), 16);
        return "\033["+ setupConfigANSI() + "38;2;" + red + ";" + green + ";" + blue + "m" + str + "\033[0m";
    }

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
            finalStr += "\033[" + setupConfigANSI() + "38;2;" + red + ";" + green + ";" + blue + "m" + strs[index] + "\033[0m";
            index++;
        }
        return finalStr;
    }

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
            finalStr += "\033[" + setupConfigANSI() + "38;2;" + red + ";" + green + ";" + blue + "m" + strs[index] + "\033[0m";
            index++;
        }
        return finalStr;
    }

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
        }
    }

    public static void setConfig(boolean isBold, boolean isItalic, boolean isUnderlined, boolean isCrossedOut, boolean isDim, boolean isDoublyUnderlined) {
        _config = new boolean[] { isBold, isItalic, isUnderlined, isCrossedOut, isDim, isDoublyUnderlined };
    }

    public static boolean[] getConfig() {
        return _config;
    }


    
    

    private static String setupConfigANSI(){
        String config = "";
        for(int i = 0; i < _config.length; i++){
            if(_config[i]) {
                switch(i) {
                    case 0 : // Bold
                    config += "1;";
                    break;
                    case 1 : // Italic
                    config += "3;";
                    break;
                    case 2 : // Underline
                    config += "4;";
                    break;
                    case 3 : // Crossed Out
                    config += "9;";
                    break;
                    case 4 : // Dim
                    config += "2;";
                    break;
                    case 5 : // Double Underline
                    config += "21;";
                    break;
                }
            }
        }
        return config;
    }
}
