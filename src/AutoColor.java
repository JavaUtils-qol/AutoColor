public class AutoColor {
    public static String colorize(boolean bool){
        return (bool ? "\033[1;32m" : "\033[1;31m") + bool + "\033[0m";
    }

    public static String colorize(String str, String hexColor){
        int red = Integer.valueOf(hexColor.substring(1, 3), 16);
        int green = Integer.valueOf(hexColor.substring(3, 5), 16);
        int blue = Integer.valueOf(hexColor.substring(4, 7), 16);
        return "\033[38;5;" + red + ";" + green + ";" + blue + "m" + str + "\033[0m"; //\033[(fore/background);(5/2 4-bit/r;g;b);(color)m
        // {4-bit} for bright you need 60 more  and for background you add 10
    }
}
