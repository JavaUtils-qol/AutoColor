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
}
