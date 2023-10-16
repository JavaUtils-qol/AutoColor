public class AutoColor {
    public static String colorize(boolean bool){
        return (bool ? "\033[1;32m" : "\033[1;31m") + bool + "\033[0m";
    }
}
