package de.hk.bfit.helper;

import de.hk.bfit.process.SqlProzessor;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ReplacementUtils {
    static private final Logger logger = Logger.getLogger(SqlProzessor.class);

    public String replaceGermanUmlaute(String input) {
        return "";
    }


    static public String removeTimestamps(String input) {
//        String output = "2016-05-05 03:45:00.907text.txt";
        Pattern pxPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+");

        return removeRegexFromString(input, pxPattern);
    }

    static private String removeRegexFromString(String input, Pattern pxPattern) {
        String output = "";
        Matcher pxMatcher = pxPattern.matcher(input);

        while (pxMatcher.find()) {
            String urlString = pxMatcher.group();
            output = input.replace(urlString, "");
        }

        logger.info("Timestamps gemoved: " + input + " -> " + output);
        System.out.println(output);

        return output;
    }


}
