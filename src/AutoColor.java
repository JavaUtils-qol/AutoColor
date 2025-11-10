import java.util.regex.Pattern;

/**
 * This class aids with usage of colored console using ANSI escape codes.
 * <br>
 * <br>
 * Can also be used only to create gradients.
 * <br>
 * <br>
 * For more info about ANSI escape codes visit:
 * https://en.wikipedia.org/wiki/ANSI_escape_code
 */
public class AutoColor {
    /**
     * Contains all (almost all) the modifiers an ANSI color code can take.
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

    public static enum Accuracy {
        /**
         * Will most likely not pass through the original values but will instead always use the previous color value as reference.
         * <br><br>
         * This effect will become more apparent the longer the String is.
         */
        Smooth,
        /**
         * Will always pass through the original values at the cost of a less smooth gradient.
         * <br><br>
         * This effect will become more apparent the longer the String is.
         */
        Accurate
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
        // create colored text
        return new Color(hexColor).ANSIfy(setupModifiers(), str, false);
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
        // split str into multiple sections
        String[] strs = str.split(Pattern.quote(String.valueOf(delimiter)));
        
        String finalStr = "";
        int index = 0;
        int hexIndex = 0;

        while (index != strs.length) {
            // stop hexIndex from being too big
            hexIndex = index;
            if (hexIndex >= hexColors.length)
                hexIndex = hexColors.length - 1;
            // create colored text
            finalStr += new Color(hexColors[hexIndex]).ANSIfy(setupModifiers(), strs[index], false);
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
        // split str into multiple sections 
        String[] strs = str.split(regexDelim);

        String finalStr = "";
        int index = 0;
        int hexIndex = 0;

        while (index != strs.length) {
            // stop hexIndex from being too big
            hexIndex = index;
            if (hexIndex >= hexColors.length)
                hexIndex = hexColors.length - 1;
            // create colored text
            finalStr += new Color(hexColors[hexIndex]).ANSIfy(setupModifiers(), strs[index], false);
            index++;
        }
        return finalStr;
    }

    /**
     * Will take a String and color each character according to a gradient with the given start, intermediate and end colors.
     * 
     * @param str          - text to color
     * @param hexColors    - multiple hex values that serve as setpoints for the gradient
     * @param isAccurate   - accuracy of the gradient to the given hex values
     * @param isBackground - whether or not to set the gradient to the background
     * @return <b>colored text</b> (String)
     */
    public static String colorize(String str, String[] hexColors, Accuracy isAccurate, boolean isBackground) {
        str += " "; // to let the last character be displayed
        // declaring vars
        String finalStr = "";
        Color[] gradient = new Color[str.length() - 1];

        gradient = generateGradient(str.length(), hexColors, isAccurate);

        // ANSIfy the gradient
        int i = 0;
        while(i != gradient.length) {
            finalStr += gradient[i].ANSIfy(setupModifiers(), str.charAt(i), isBackground);
            i++;
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
    public static void setConfig(boolean isBold, boolean isItalic, boolean isUnderlined, boolean isCrossedOut, boolean isDim, boolean isDoublyUnderlined) {
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

    /**
     * A class to store Colors in rgb format
     */
    public static class Color {
        public final int red;
        public final int green;
        public final int blue;

        public Color(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public Color(String hex) {
            this.red = Integer.valueOf(hex.substring(1, 3), 16);
            this.green = Integer.valueOf(hex.substring(3, 5), 16);
            this.blue = Integer.valueOf(hex.substring(5, 7), 16);
        }

        public String toHex() {
            String redHex = Integer.toHexString(this.red).length() == 1 ? "0" + Integer.toHexString(this.red) : Integer.toHexString(this.red);
            String greenHex = Integer.toHexString(this.green).length() == 1 ? "0" + Integer.toHexString(this.green) : Integer.toHexString(this.green);
            String blueHex = Integer.toHexString(this.blue).length() == 1 ? "0" + Integer.toHexString(this.blue) : Integer.toHexString(this.blue);
            return "#" + redHex + greenHex + blueHex;
        }

        public String toHSV() {
            return "TODO";
        }

        private String ANSIfy(String modifiers, String str, boolean isBackground) {
            return setupANSI(str, red, green, blue, isBackground);
        }

        private String ANSIfy(String modifiers, char str, boolean isBackground) {
            return setupANSI(str, red, green, blue, isBackground);
        }
    }

    /**
     * Generates a gradient based on hexadecimal color values
     * @param numOfColors - the number of colors to generate
     * @param hexColors - the base colors to use in the gradient
     * @param accuracy - the accuracy of the colors corresponding to the base colors
     * @return the gradient in {@code Color[]} form
     */
    public static Color[] generateGradient(int numOfColors, String[] hexColors, Accuracy accuracy) {
        // transform hex colors into rgb ones
        Color[] colors = new Color[hexColors.length];
        for (int i = 0; i < hexColors.length; i++) {
            colors[i] = new Color(
                    Integer.valueOf(hexColors[i].substring(1, 3), 16),
                    Integer.valueOf(hexColors[i].substring(3, 5), 16),
                    Integer.valueOf(hexColors[i].substring(5, 7), 16));
        }

        return generateGradient(numOfColors, colors, accuracy);
    }
    
    /**
     * Generates a gradient
     * @param numOfColors - the number of colors to generate
     * @param hexColors - the base colors to use in the gradient
     * @param accuracy - the accuracy of the colors corresponding to the base colors
     * @return the gradient in {@code Color[]} form
     */
    public static Color[] generateGradient(int numOfColors, Color[] colors, Accuracy accuracy) {
        Color[] finalColors = new Color[numOfColors - 1];
        int gradientStep = numOfColors / (colors.length - 1);
        int i = 0;
        int j = 0;

        while(i != colors.length - 1) {
            int redDif = (colors[i].red - colors[i+1].red) / gradientStep;
            int greenDif = (colors[i].green - colors[i+1].green) / gradientStep;
            int blueDif = (colors[i].blue - colors[i+1].blue) / gradientStep;
        
            int red = colors[i].red;
            int green = colors[i].green;
            int blue = colors[i].blue;

            if(i == 0) {
                finalColors[j] = new Color(red, green, blue);
                j++;
            }
    
            while(j != (gradientStep * (i + 1)) - 1) {
                red -= redDif;
                green -= greenDif;
                blue -= blueDif;
                
                finalColors[j] = new Color(red, green, blue);
                j++;
            }

            if(i != colors.length - 2 && accuracy == Accuracy.Accurate) {
                red = colors[i].red;
                green = colors[i].green;
                blue = colors[i].blue;
                
                finalColors[j] = new Color(red, green, blue);
                j++;
            } else if (i != colors.length - 2) {
                red -= redDif;
                green -= greenDif;
                blue -= blueDif;

                colors[i+1] = new Color(red, green, blue);
                
                finalColors[j] = new Color(red, green, blue);
                j++;
            } else if (accuracy == Accuracy.Accurate) { 
                red = colors[i+1].red;
                green = colors[i+1].green;
                blue = colors[i+1].blue;
                
                while(j != numOfColors) {
                    finalColors[j-1] = new Color(red, green, blue);
                    j++;
                }
            } else {
                while(j != numOfColors) {
                    red -= redDif;
                    green -= greenDif;
                    blue -= blueDif;

                    finalColors[j-1] = new Color(red, green, blue);
                    j++;
                }
            }
            i++;
        }

        return finalColors;
    }




    private AutoColor() {} // set constructor to private to stop object creation

    private static String setupModifiers() {
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

    private static String setupANSI(String str, int red, int green, int blue, boolean isBackground) {
        return "\033[" + setupModifiers() + (isBackground ? "48;2;" : "38;2;") + red + ";" + green + ";" + blue + "m" + str + "\033[0m";
    }

    private static String setupANSI(char str, int red, int green, int blue, boolean isBackground) {
        return "\033[" + setupModifiers() + (isBackground ? "48;2;" : "38;2;") + red + ";" + green + ";" + blue + "m" + str + "\033[0m";
    }
}
