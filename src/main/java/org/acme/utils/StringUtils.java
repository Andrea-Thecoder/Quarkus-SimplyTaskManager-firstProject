package org.acme.utils;

public class StringUtils {

    //Big O (1) <--- lineare
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String inputTrimmer = input.trim();
        // è il rispettivo di slice di js. spacca la stringa ad index 0 fino ad index 1 non incluso e poi la concatena con index1 fin oalla fine.
        return inputTrimmer.substring(0, 1).toUpperCase() + inputTrimmer.substring(1).toLowerCase();
    }

    public static String capitalizeOnlyFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String inputTrimmer = input.trim();
        return inputTrimmer.substring(0, 1).toUpperCase() + inputTrimmer.substring(1);
    }
}
