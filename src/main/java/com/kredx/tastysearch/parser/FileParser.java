package com.kredx.tastysearch.parser;

import com.kredx.tastysearch.review.Review;
import com.kredx.tastysearch.review.ReviewCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by prashkr on 7/2/16.
 */
public class FileParser {
    private final String EMPTY_STRING = "";
    private final String NEW_LINE = "\n";
    private final int SAMPLE_SIZE = 100000;
    private final Random rand = new Random();

    /**
     *
     * @return
     */
    private String getDataFileName() {
        return "foods.txt";
    }

    /**
     *
     * @return
     */
    private BufferedReader getBufferedReader() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(getDataFileName());
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     *
     */
    public void parse() {
        try {
            BufferedReader buff = getBufferedReader();
            parseReviews(buff);
            buff.close();
            System.out.println("File Parsing Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads line by line from file and parses reviews
     *
     * @param buff
     * @throws IOException
     */
    private void parseReviews(BufferedReader buff) throws IOException {
        int countReviewsParsed = 0;
        String reviewString = EMPTY_STRING;
        String line;

        while ((line = buff.readLine()) != null)   {

            if (EMPTY_STRING.equals(line)) {
                // marking the end of a single review. So parse and check through
                // reservoir sampling whether to include it in the sample or not.
                doReservoirSampling(reviewString, countReviewsParsed);
                reviewString = EMPTY_STRING;
                countReviewsParsed++;
            } else {
                reviewString += line + NEW_LINE;
            }

            // Todo: remove the below code.
//            if (countReviewsParsed >= 100) {
//                break;
//            }
        }
    }

    /**
     * Reservoir sampling steps:
     * 1. If number of reviews read till now is less than the chosen sample size
     *    then include this review it in the sample.
     *
     * 2. Else:
     *      a) With SAMPLE_SIZE/countReviewsRead probability keep this review and replace
     *         old one with this.
     *      b) Otherwise ignore this review.
     *
     * @param reviewString
     * @param countReviewsParsed
     */
    private void doReservoirSampling(String reviewString, int countReviewsParsed) {
        if (countReviewsParsed < SAMPLE_SIZE) {
            Review parsedReview = ReviewParser.parse(reviewString);
            ReviewCollection.add(parsedReview);
        } else {
            int index = generateRandomIndex(0, countReviewsParsed);
            if (index < SAMPLE_SIZE) {
                Review parsedReview = ReviewParser.parse(reviewString);
                ReviewCollection.update(index, parsedReview);
            } else {
                //ignore this review
            }
        }
    }

    /**
     * Generates random integer between [min, max]
     *
     * @param min lower bound
     * @param max upper bound
     * @return
     */
    private int generateRandomIndex(int min, int max) {
        return min + rand.nextInt(1 + (max - min));
    }
}
