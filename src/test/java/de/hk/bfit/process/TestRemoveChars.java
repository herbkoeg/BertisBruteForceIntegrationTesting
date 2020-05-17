package de.hk.bfit.process;

import org.junit.Test;

import java.util.*;

public class TestRemoveChars {

    @Test
    public void removeUmlaute() {
        String sut  = "jawoiaaaäüöÄÜÖß";

        Map replaceMap = new HashMap();
        replaceMap.put("\u00fc", "-ue-");
        replaceMap.put("\u00f6", "-oe-");
        replaceMap.put("\u00e4", "-ae-");
        replaceMap.put("\u00df", "-ss-");
        replaceMap.put("\u00dc(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "-Ue-");
        replaceMap.put("\u00d6(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "-Oe-");
        replaceMap.put("\u00c4(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "-Ae-");
        replaceMap.put("\u00dc", "-UE-");
        replaceMap.put("\u00d6", "-OE-");
        replaceMap.put("\u00c4", "-AE-");

        System.out.println(replaceUmlaute(sut));
        System.out.println("------------------------------");
        System.out.println(replaceStrings(sut,replaceMap));
    }

    private static String replaceStrings(String input, Map<String,String> replacements) {
        Set<String> keySet = replacements.keySet();
        String newString = input;
        for (String key: keySet) {
            newString = newString.replace(key,replacements.get(key));
        }
        return newString;
    }

    private static String replaceUmlaute(String output) {
        String newString = output.replaceAll("\u00fc", "-ue-")
                .replaceAll("\u00f6", "-oe-")
                .replaceAll("\u00e4", "-ae-")
                .replaceAll("\u00df", "-ss-")
                .replaceAll("\u00dc(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "-Ue-")
                .replaceAll("\u00d6(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "-Oe-")
                .replaceAll("\u00c4(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "-Ae-")
                .replaceAll("\u00dc", "-UE-")
                .replaceAll("\u00d6", "-OE-")
                .replaceAll("\u00c4", "-AE-");
        return newString;
    }


}
