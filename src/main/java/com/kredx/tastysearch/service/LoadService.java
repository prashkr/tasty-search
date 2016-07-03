package com.kredx.tastysearch.service;

import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by prashkr on 7/4/16.
 */
public class LoadService {
    /**
     * List of all the words in all the reviews. No duplicates here.
     * Todo: Remove stop words such as 'a', 'of', 'the', 'if', etc.
     *
     */
    public static List<String> words = new ArrayList<>();

    /**
     * Loads words from all reviews into the above mentioned structure.
     */
    public void loadWords() {
        Map<String, Boolean> wordMap = new HashMap<>();

        for (Review review : ReviewCollection.sampledReviews) {
            String reviewText = review.getFilteredText();
            StringTokenizer st = new StringTokenizer(reviewText);

            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();

                if (!wordMap.containsKey(token)) {
                    //Todo: exclude stop words
                    wordMap.put(token, true);
                }
            }
        }

        words.addAll(wordMap.keySet());
    }

    /**
     * Generates test set and writes it to a file in the following format:
     *
     * token1 token2 token3...
     * token4 token5 token6...
     * .
     * .
     *
     * @param testSetFileName Write test set to this file
     * @param testSetSize Number of tests to generate
     * @param maxTestTokens Maximum possible tokens allowed in a test
     * @throws IOException
     */
    public static void generateTestSet(String testSetFileName, int testSetSize, int maxTestTokens) throws IOException {
        List<String> lines = new ArrayList<>();

        for (int i = 0; i < testSetSize; i++) {
            int numTokens = RandomNumberService.generateRandomNumberBetween(1, maxTestTokens);
            String line = generateTest(numTokens);
            lines.add(line);
        }

        Path file = Paths.get(testSetFileName);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

    /**
     * Generates a line in the following form:
     *
     * "token1 token2 token3..."
     *
     * @param numTokens
     * @return
     */
    private static String generateTest(int numTokens) {
        Map<Integer, Boolean> tokenMap = new HashMap<>();
        int size = words.size();
        String line = "";

        for (int i = 0; i < numTokens; i++) {
            int wordIndex = RandomNumberService.generateRandomNumberBetween(0, size - 1);
            if (!tokenMap.containsKey(wordIndex)) {
                line += words.get(wordIndex) + " ";
            }
        }

        return line.trim();
    }
}
