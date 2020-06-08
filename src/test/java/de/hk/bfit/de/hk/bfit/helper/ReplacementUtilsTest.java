package de.hk.bfit.de.hk.bfit.helper;

import de.hk.bfit.helper.BfiRegEx;
import de.hk.bfit.helper.ReplacementUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static de.hk.bfit.helper.BfiRegEx.DATE_YYYY_MM_DD;
import static de.hk.bfit.helper.BfiRegEx.TIMESTAMP;
import static de.hk.bfit.helper.ReplacementUtils.*;
import static de.hk.bfit.process.enums.Replacements.REPLACE_UMLAUTE;
import static org.junit.Assert.*;

public class ReplacementUtilsTest {

    @Test
    public void timestampRevomeTest() {
        assertEquals("text.txt", removeTimestamps("2016-05-05 03:45:00.907text.txt"));
        assertEquals("blatext.txt", removeTimestamps("bla2016-05-05 03:45:00.907text.txt"));
        assertEquals("blatext.txtblablup", removeTimestamps("bla2016-05-05 03:45:00.907text.txtbla2016-05-05 03:45:00.907blup"));
        assertEquals("text.txt", removeTimestamps("2016-05-05 03:45:00.907123text.txt"));
    }

    @Test
    public void timestampRevome_when_keinTimestamp() {
        assertEquals("bla", removeTimestamps("bla"));
    }

    @Test
    public void timestampRevome_when_leererString() {
        assertEquals("", removeTimestamps(""));
    }

    @Test
    public void timestampRevome_when_nurTimestamp() {
        assertEquals("", removeTimestamps("2016-05-05 03:45:00.907"));
        assertEquals("", removeTimestamps("2016-05-05 03:45:00.907123"));
    }

    @Test
    public void dateRemoveTest() {
        assertEquals("blablup", removeDates("bla2016-12-12blup"));
    }

    @Test
    public void removeRexexListTest() {
        List<String> patternList = new ArrayList<>();

        patternList.add(DATE_YYYY_MM_DD);
        patternList.add(TIMESTAMP);
        assertEquals("bliblablup",removeRegexFromString("bli2016-05-05 03:45:00.907123bla2016-12-12blup",patternList));
    }

    @Test
    public void testReplaceMap() {
        assertEquals("*ae**ue**oe**AE**UE**OE**ss*", replaceStrings("äüöÄÜÖß", REPLACE_UMLAUTE));
    }

}
