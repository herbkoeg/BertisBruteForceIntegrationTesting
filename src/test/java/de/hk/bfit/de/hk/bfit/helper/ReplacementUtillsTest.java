package de.hk.bfit.de.hk.bfit.helper;

import de.hk.bfit.helper.ReplacementUtils;
import org.junit.Assert;
import org.junit.Test;

import static de.hk.bfit.helper.ReplacementUtils.*;
import static org.junit.Assert.*;

public class ReplacementUtillsTest {

    @Test
    public void timestampRevomeTest() {
        assertEquals("text.txt", removeTimestamps("2016-05-05 03:45:00.907text.txt"));
    }
}
