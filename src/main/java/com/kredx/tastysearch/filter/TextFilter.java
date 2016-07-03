package com.kredx.tastysearch.filter;

/**
 * Created by prashkr on 7/3/16.
 */
public class TextFilter {

    public static String input;

    /**
     * Filters the given string in the following sequence:
     *
     * 1. Removes all html tags.
     * 2. Removes all non alpha numeric characters.
     * 3. Converts all letters to lowercase.
     * 4. TODO: Pass through stremmer
     *
     * @param input The input string
     * @return Filtered string
     */
    public static String filter(String input) {
        input = filterHtmlTags(input);
        input = filterNonAlphaNumeric(input);
        input = convertToLowerCase(input);
        // Todo: pass through stemmer

        return input;
    }

    /**
     * Replaces all html tags in the input string to empty string
     *
     * @param input The input string
     * @return Filtered string
     */
    public static String filterHtmlTags(String input) {
        return input.replaceAll("<[^>]*>", " ");
    }

    /**
     * Replaces all non-alpha numeric characters with space.
     *
     * @param input The input string
     * @return String with non-alpha numeric characters removed
     */
    public static String filterNonAlphaNumeric(String input) {
        return input.replaceAll("[^A-Za-z0-9]", " ");
    }

    /**
     * Converts complete string to lowercase
     *
     * @param input The input string
     * @return Lower cased string
     */
    public static String convertToLowerCase(String input) {
        return input.toLowerCase();
    }
}
