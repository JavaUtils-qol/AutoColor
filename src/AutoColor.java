import java.util.regex.Pattern;

public class AutoColor {
    public static String colorize(boolean bool){
        return (bool ? "\033[1;32m" : "\033[1;31m") + bool + "\033[0m";
    }

    public static String colorize(String str, String hexColor){
        int red = Integer.valueOf(hexColor.substring(1, 3), 16);
        int green = Integer.valueOf(hexColor.substring(3, 5), 16);
        int blue = Integer.valueOf(hexColor.substring(5, 7), 16);
        return "\033[1;38;2;" + red + ";" + green + ";" + blue + "m" + str + "\033[0m";
    }

    public static String colorize(String str, char delimiter, String... hexColors) {
        String[] strs = str.split(Pattern.quote(String.valueOf(delimiter)));
        String finalStr = "";
        int index = 0;
        int hexIndex = 0;
        while(index != strs.length) {
            hexIndex = index;
            if(hexIndex >= hexColors.length) hexIndex = hexColors.length - 1;
            int red = Integer.valueOf(hexColors[hexIndex].substring(1, 3), 16);
            int green = Integer.valueOf(hexColors[hexIndex].substring(3, 5), 16);
            int blue = Integer.valueOf(hexColors[hexIndex].substring(5, 7), 16);
            finalStr = finalStr + "\033[1;38;2;" + red + ";" + green + ";" + blue + "m" + strs[index] + "\033[0m";
            index++;
        }
        return finalStr;
    }
}
