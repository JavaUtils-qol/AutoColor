public class Test {
    public static void main(String[] args) {
        AutoColor.setConfig(AutoColor.Config.Bold);
        System.out.println(AutoColor.colorize(true));
        System.out.println(AutoColor.colorize("white", "#f0f0f0"));
        System.out.println(AutoColor.colorize("red-skip yellow||blue|green|-noMoreColors", '|', "#eb3434", "#eeff00", "#00fbff", "#6ba600"));
        System.out.println(AutoColor.colorize("red- to- green", "- ", "#ff0000", "#b48600", "#3ab200"));
        System.out.println(AutoColor.colorize("Hi I'm a gradient!", new String[]{ "#db0000", "#0007d6" }, AutoColor.Accuracy.Smooth, false));
        System.out.println(AutoColor.colorize("                                                                   ", new String[]{ "#db0000", "#eeff00", "#0007d6" }, AutoColor.Accuracy.Smooth, true));
        System.out.println();
        System.out.println(AutoColor.colorize("                                                                   ", new String[]{ "#db0000", "#eeff00", "#0007d6" }, AutoColor.Accuracy.Accurate, true));
        System.out.println();
        System.out.println(AutoColor.colorize("                                                                   ", new String[]{ "#db0000", "#eeff00", "#0007d6" }, AutoColor.Accuracy.Average, true));
    }
}
