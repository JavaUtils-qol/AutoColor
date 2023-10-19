public class Test {
    public static void main(String[] args) throws Exception {
        AutoColor.setConfig(AutoColor.Config.Bold);
        System.out.println(AutoColor.colorize(true));
        System.out.println(AutoColor.colorize("test", "#f0f0f0"));
        System.out.println(AutoColor.colorize("test|test|test", '|', "#eb3434", "#00fbff"));
        System.out.println(AutoColor.colorize("blue-to-green", "()", "#00348a", "#0045a0", "#0055b2", "#0064be", "#0072c4", "#0080c2", "#008dba", "#0099ac", "#00a497", "#00af7e", "#00b962", "#00c340", "#21ca00"));
    }
}
