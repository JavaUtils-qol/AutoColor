import java.util.regex.Pattern;

/**
 * This class aids with usage of colored console using ANSI escape codes.
 * <br>
 * <br>
 * For more info about ANSI escape codes visit:
 * https://en.wikipedia.org/wiki/ANSI_escape_code
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
        Smooth,
        Accurate,
        Average
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
        AutoColor obj = new AutoColor();
        // create colored text
        return obj.new Color(hexColor).ANSIfy(setupModifiers(), str, false);
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
        int hexIndex = 0;

        while (index != strs.length) {
            // stop hexIndex from being too big
            hexIndex = index;
            if (hexIndex >= hexColors.length)
                hexIndex = hexColors.length - 1;
            // create colored text
            finalStr += obj.new Color(hexColors[hexIndex]).ANSIfy(setupModifiers(), strs[index], false);
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
        int hexIndex = 0;

        while (index != strs.length) {
            // stop hexIndex from being too big
            hexIndex = index;
            if (hexIndex >= hexColors.length)
                hexIndex = hexColors.length - 1;
            // create colored text
            finalStr += obj.new Color(hexColors[hexIndex]).ANSIfy(setupModifiers(), strs[index], false);
            index++;
        }
        return finalStr;
    }

    /**
     * Will take a String and color each character according to a gradient with the given start, intermediate and end colors.
     * <br><br>
     * The accuracy parameter works as follows: 
     * <ul>
     * <li><b>Smooth</b>: will most likely not pass through the original values but will instead always use the previous color value.
     * <li><b>Accurate</b>: will always pass through the original values at the cost of a less smooth gradient.
     * <li><b>Average</b>: will take the average between the smooth and accurate gradients at a ratio of 5:1.
     * </ul>
     * 
     * @param str          - text to color
     * @param hexColors    - multiple hex values that serve as setpoints for the gradient
     * @param isAccurate   - accuracy of the gradient to the given hex values
     * @param isBackground - whether or not to set the gradient to the background
     * @return <b>colored text</b> (String)
     */
    public static String colorize(String str, String[] hexColors, Accuracy isAccurate, boolean isBackground) {
        String finalStr = "";
        Color[] gradient = new Color[str.length() - 1];

        if(isAccurate == Accuracy.Average) {
            Color[] firstGradient = generateGradient(str.length(), hexColors, true);
            Color[] secondGradient = generateGradient(str.length(), hexColors, false);

            int i = 0;
            while(i != secondGradient.length) {
                gradient[i] = firstGradient[i].average(secondGradient[i]);
                i++;
            }
        } else {
            gradient = generateGradient(str.length(), hexColors, isAccurate == Accuracy.Accurate);
        }

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





    private AutoColor() {} // set constructor to private to stop object creation

    private class Color {
        private final int r;
        private final int g;
        private final int b;

        private Color(int red, int green, int blue) {
            this.r = red;
            this.g = green;
            this.b = blue;
        }

        private Color(String hex) {
            this.r = Integer.valueOf(hex.substring(1, 3), 16);
            this.g = Integer.valueOf(hex.substring(3, 5), 16);
            this.b = Integer.valueOf(hex.substring(5, 7), 16);
        }

        private String ANSIfy(String modifiers, String str, boolean isBackground) {
            return setupANSI(str, r, g, b, isBackground);
        }

        private String ANSIfy(String modifiers, char str, boolean isBackground) {
            return setupANSI(str, r, g, b, isBackground);
        }

        private Color average(Color secondColor) {
            return new Color((r*5+secondColor.r)/6, (g*5+secondColor.g)/6, (b*5+secondColor.b)/6);
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

    private static String setupANSI(String str, int red, int green, int blue) {
        return "\033[" + setupModifiers() + "38;2;" + red + ";" + green + ";" + blue + "m" + str + "\033[0m";
    }
    
    private static String setupANSI(char str, int red, int green, int blue) {
        return "\033[" + setupModifiers() + "38;2;" + red + ";" + green + ";" + blue + "m" + str + "\033[0m";
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

        while(i != hexColors.length - 1) {
            int redDif = (Integer.valueOf(hexColors[i].substring(1, 3), 16) - Integer.valueOf(hexColors[i+1].substring(1, 3), 16)) / gradientStep;
            int greenDif = (Integer.valueOf(hexColors[i].substring(3, 5), 16) - Integer.valueOf(hexColors[i+1].substring(3, 5), 16)) / gradientStep;
            int blueDif = (Integer.valueOf(hexColors[i].substring(5, 7), 16) - Integer.valueOf(hexColors[i+1].substring(5, 7), 16)) / gradientStep;
            
            int red = Integer.valueOf(hexColors[i].substring(1, 3), 16);
            int green = Integer.valueOf(hexColors[i].substring(3, 5), 16);
            int blue = Integer.valueOf(hexColors[i].substring(5, 7), 16);

            if(i == 0) {
                finalColors[j] = obj.new Color(red, green, blue);
                j++;
            }
    
            while(j != (gradientStep * (i + 1)) - 1) {
                red -= redDif;
                green -= greenDif;
                blue -= blueDif;
                
                finalColors[j] = obj.new Color(red, green, blue);
                j++;
            }

            if(i != hexColors.length - 2 && isAccurate) {
                red = Integer.valueOf(hexColors[i+1].substring(1, 3), 16);
                green = Integer.valueOf(hexColors[i+1].substring(3, 5), 16);
                blue = Integer.valueOf(hexColors[i+1].substring(5, 7), 16);
                
                finalColors[j] = obj.new Color(red, green, blue);
            } else if (i != hexColors.length - 2) {
                red -= redDif;
                green -= greenDif;
                blue -= blueDif;

                hexColors[i+1] = decimalToHex(red, green, blue);
                
                finalColors[j] = obj.new Color(red, green, blue);
            } else if (isAccurate) {
                red = Integer.valueOf(hexColors[i+1].substring(1, 3), 16);
                green = Integer.valueOf(hexColors[i+1].substring(3, 5), 16);
                blue = Integer.valueOf(hexColors[i+1].substring(5, 7), 16);
                
                while(j != characters) {
                    finalColors[j-1] = obj.new Color(red, green, blue);
                    j++;
                }
            } else {
                while(j != characters) {
                red -= redDif;
                green -= greenDif;
                blue -= blueDif;

                finalColors[j-1] = obj.new Color(red, green, blue);
                j++;
                }
            }
            j++;
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
