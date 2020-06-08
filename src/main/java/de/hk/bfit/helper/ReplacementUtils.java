package de.hk.bfit.helper;

import de.hk.bfit.process.SqlProzessor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.hk.bfit.helper.BfiRegEx.DATE_YYYY_MM_DD;
import static de.hk.bfit.helper.BfiRegEx.TIMESTAMP;

public abstract class ReplacementUtils {
    static private final Logger logger = Logger.getLogger(SqlProzessor.class);

    /**
     * Removes YYYY-MM-DD HH:MM:SS.MMM from String
     * @param input
     * @return
     */
    static public String removeTimestamps(String input) {
        Pattern pxPattern = Pattern.compile(TIMESTAMP);
        return removeRegexFromString(input, pxPattern);
    }

    /**
     * Removes YYYY-MM-DD from String
     * @param input
     * @return
     */
    static public String removeDates(String input) {
        Pattern DATE_PATTERN = Pattern.compile(DATE_YYYY_MM_DD);
        return removeRegexFromString(input, DATE_PATTERN);
    }

    static public String removeRegexFromString(String input, List<String> pxPatternList) {
        String output = input;
        for (String pxPattern:pxPatternList) {
            output = removeRegexFromString(output,Pattern.compile(pxPattern));
        }
        return output;
    }

    static private String removeRegexFromString(String input, Pattern pxPattern) {
        String output = input;
        Matcher pxMatcher = pxPattern.matcher(input);

        while (pxMatcher.find()) {
            String urlString = pxMatcher.group();
            output = input.replace(urlString, "");
        }

        logger.info(" remove pattern: " + pxPattern + ": " + input + " -> " + output);
        return output;
    }

    static public String replaceStrings(String input, Map<String,String> replaceMap) {
        if(replaceMap==null) {
            return input;
        }
        Set<String> keySet = replaceMap.keySet();
        String newString = input;
        for (String key: keySet) {
            newString = newString.replace(key,replaceMap.get(key));
        }
        return newString;
    }


}
