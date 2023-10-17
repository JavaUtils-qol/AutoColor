public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println(AutoColor.colorize(true));
        System.out.println(AutoColor.colorize("test", "#f0f0f0"));
        System.out.println(AutoColor.colorize("test|test|test", '|', "#eb3434", "#00fbff"));
    }
}
