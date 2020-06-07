package de.hk.bfit.process;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRemoveChars {

    private static String replaceStrings(String input, Map<String,String> replacements) {
        Set<String> keySet = replacements.keySet();
        String newString = input;
        for (String key: keySet) {
            newString = newString.replace(key,replacements.get(key));
        }
        return newString;
    }

    @Test
    public void replaceTimestamp() throws ParseException {

        String temp = "2016-05-05 03:45:00.907text.txt";
        Pattern pxPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d+");

        Matcher pxMatcher = pxPattern.matcher(temp);

        while (pxMatcher.find()) {
            System.out.println(pxMatcher.group());
            String urlString = pxMatcher.group();
            temp = temp.replace(urlString, "");
        }
        System.out.println(temp);
    }

}
