package de.hk.bfit.process;

import java.util.HashMap;

abstract public class Replacements {

    // see https://stackoverflow.com/questions/32696273/java-replace-german-umlauts
    public static final HashMap<String, String> REPLACE_UMLAUTE = new HashMap<>();
    static {
        REPLACE_UMLAUTE.put("\u00fc", "*ue*");
        REPLACE_UMLAUTE.put("\u00f6", "*oe*");
        REPLACE_UMLAUTE.put("\u00e4", "*ae*");
        REPLACE_UMLAUTE.put("\u00df", "*ss*");
        REPLACE_UMLAUTE.put("\u00dc(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "*Ue*");
        REPLACE_UMLAUTE.put("\u00d6(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "*Oe*");
        REPLACE_UMLAUTE.put("\u00c4(?=[a-z\u00e4\u00f6\u00fc\u00df ])", "*Ae*");
        REPLACE_UMLAUTE.put("\u00dc", "*UE*");
        REPLACE_UMLAUTE.put("\u00d6", "*OE*");
        REPLACE_UMLAUTE.put("\u00c4", "*AE*");
    }

    public static final HashMap<String, String> REPLACE_NUMBERS = new HashMap<>();
    static {
        REPLACE_NUMBERS.put("0", "*#*");
        REPLACE_NUMBERS.put("1", "*#*");
        REPLACE_NUMBERS.put("2", "*#*");
        REPLACE_NUMBERS.put("3", "*#*");
        REPLACE_NUMBERS.put("4", "*#*");
        REPLACE_NUMBERS.put("5", "*#*");
        REPLACE_NUMBERS.put("6", "*#*");
        REPLACE_NUMBERS.put("7", "*#*");
        REPLACE_NUMBERS.put("8", "*#*");
        REPLACE_NUMBERS.put("9", "*#*");
    }

}
