import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * This class aids with usage of colored console using ANSI escape codes.
 * <br>
 * <br>
 * For more info about ANSI escape codes visit:
 * <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI escape code Wikipedia</a>
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
        Accurate,
        /**
         * Will take the average between the smooth and accurate gradients at a ratio of 5:1.
         * <br><br>
         * This requires more computer resources and time.
         */
        Average
    }

    private static boolean[] _config = new boolean[]{false, false, false, false, false, false};

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
        // declare vars
        AutoColor obj = new AutoColor();
        String finalStr = "";
        int index = 0;
        int hexIndex;

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
        // declare vars
        AutoColor obj = new AutoColor();
        String finalStr = "";
        int index = 0;
        int hexIndex;

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
     * @param isBackground - whether to set the gradient to the background
     * @return <b>colored text</b> (String)
     */
    public static String colorize(String str, String[] hexColors, Accuracy isAccurate, boolean isBackground) {
        str += " "; // to let the last character be displayed
        // declaring vars
        String finalStr = "";
        Color[] gradient = new Color[str.length() - 1];

        // check Accuracy is average
        if (isAccurate == Accuracy.Average) {
            // calculating the gradients to be averaged
            Color[] firstGradient = generateGradient(str.length(), hexColors, true);
            Color[] secondGradient = generateGradient(str.length(), hexColors, false);

            // calculating the average between the two gradients
            int i = 0;
            while (i != secondGradient.length) {
                gradient[i] = Color.average(firstGradient[i], secondGradient[i]);
                i++;
            }
        } else {
            gradient = generateGradient(str.length(), hexColors, isAccurate == Accuracy.Accurate);
        }

        // ANSIfy the gradient
        int i = 0;
        while (i != gradient.length) {
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
                _config = new boolean[]{false, false, false, false, false, false};
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
     * Allows the user to modify all the 6 ANSI modifiers.
     *
     * @param isBold             - true/false
     * @param isItalic           - true/false
     * @param isUnderlined       - true/false
     * @param isCrossedOut       - true/false
     * @param isDim              - true/false
     * @param isDoublyUnderlined - true/false
     */
    public static void setConfig(boolean isBold, boolean isItalic, boolean isUnderlined, boolean isCrossedOut, boolean isDim, boolean isDoublyUnderlined) {
        _config = new boolean[]{isBold, isItalic, isUnderlined, isCrossedOut, isDim, isDoublyUnderlined};
    }

    /**
     * Will return the state of the specified modifier.
     *
     * @param config - modifier to return
     * @return <b>state</b> of the given modifier - true/false
     */
    public static boolean getConfig(Config config) {
        return switch (config) {
            case Default -> Arrays.equals(_config, new boolean[]{false, false, false, false, false, false});
            case Bold -> _config[0];
            case Italic -> _config[1];
            case Underline -> _config[2];
            case Crossed_Out -> _config[3];
            case Dim -> _config[4];
            case Double_Underline -> _config[5];
            default -> throw new EnumConstantNotPresentException(Config.class, "null");
        };
    }

    /**
     * Will return the full modifiers array.
     *
     * @return <b>Config array</b> - boolean[6] array
     */
    public static boolean[] getConfig() {
        return _config;
    }


    private AutoColor() {
    } // set constructor to private to stop object creation

    private static class Color {
        private final int _kRed;
        private final int _kGreen;
        private final int _kBlue;

        private Color(int red, int green, int blue) {
            this._kRed = red;
            this._kGreen = green;
            this._kBlue = blue;
        }

        private Color(String hex) {
            this._kRed = Integer.valueOf(hex.substring(1, 3), 16);
            this._kGreen = Integer.valueOf(hex.substring(3, 5), 16);
            this._kBlue = Integer.valueOf(hex.substring(5, 7), 16);
        }

        private String ANSIfy(String modifiers, String str, boolean isBackground) {
            return setupANSI(str, _kRed, _kGreen, _kBlue, isBackground);
        }

        private String ANSIfy(String modifiers, char str, boolean isBackground) {
            return setupANSI(str, _kRed, _kGreen, _kBlue, isBackground);
        }

        private static Color average(Color firstColor, Color secondColor) {
            return new Color((firstColor._kRed * 5 + secondColor._kRed) / 6, (firstColor._kGreen * 5 + secondColor._kGreen) / 6, (firstColor._kBlue * 5 + secondColor._kBlue) / 6);
        }
    }

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

    private static Color[] generateGradient(int characters, String[] hexColors, boolean isAccurate) {
        AutoColor obj = new AutoColor();
        Color[] finalColors = new Color[characters - 1];
        int gradientStep = characters / (hexColors.length - 1);
        int i = 0;
        int j = 0;

        while (i != hexColors.length - 1) {
            int redDif = (Integer.valueOf(hexColors[i].substring(1, 3), 16) - Integer.valueOf(hexColors[i + 1].substring(1, 3), 16)) / gradientStep;
            int greenDif = (Integer.valueOf(hexColors[i].substring(3, 5), 16) - Integer.valueOf(hexColors[i + 1].substring(3, 5), 16)) / gradientStep;
            int blueDif = (Integer.valueOf(hexColors[i].substring(5, 7), 16) - Integer.valueOf(hexColors[i + 1].substring(5, 7), 16)) / gradientStep;

            int red = Integer.valueOf(hexColors[i].substring(1, 3), 16);
            int green = Integer.valueOf(hexColors[i].substring(3, 5), 16);
            int blue = Integer.valueOf(hexColors[i].substring(5, 7), 16);

            if (i == 0) {
                finalColors[j] = new Color(red, green, blue);
                j++;
            }

            while (j != (gradientStep * (i + 1)) - 1) {
                red -= redDif;
                green -= greenDif;
                blue -= blueDif;

                finalColors[j] = new Color(red, green, blue);
                j++;
            }

            if (i != hexColors.length - 2 && isAccurate) {
                red = Integer.valueOf(hexColors[i + 1].substring(1, 3), 16);
                green = Integer.valueOf(hexColors[i + 1].substring(3, 5), 16);
                blue = Integer.valueOf(hexColors[i + 1].substring(5, 7), 16);

                finalColors[j] = new Color(red, green, blue);
                j++;
            } else if (i != hexColors.length - 2) {
                red -= redDif;
                green -= greenDif;
                blue -= blueDif;

                hexColors[i + 1] = decimalToHex(red, green, blue);

                finalColors[j] = new Color(red, green, blue);
                j++;
            } else if (isAccurate) {
                red = Integer.valueOf(hexColors[i + 1].substring(1, 3), 16);
                green = Integer.valueOf(hexColors[i + 1].substring(3, 5), 16);
                blue = Integer.valueOf(hexColors[i + 1].substring(5, 7), 16);

                while (j != characters) {
                    finalColors[j - 1] = new Color(red, green, blue);
                    j++;
                }
            } else {
                while (j != characters) {
                    red -= redDif;
                    green -= greenDif;
                    blue -= blueDif;

                    finalColors[j - 1] = new Color(red, green, blue);
                    j++;
                }
            }
            i++;
        }

        return finalColors;
    }

    private static String decimalToHex(int red, int green, int blue) {
        String redHex = Integer.toHexString(red).length() == 1 ? "0" + Integer.toHexString(red) : Integer.toHexString(red);
        String greenHex = Integer.toHexString(green).length() == 1 ? "0" + Integer.toHexString(green) : Integer.toHexString(green);
        String blueHex = Integer.toHexString(blue).length() == 1 ? "0" + Integer.toHexString(blue) : Integer.toHexString(blue);
        return "#" + redHex + greenHex + blueHex;
    }
}
