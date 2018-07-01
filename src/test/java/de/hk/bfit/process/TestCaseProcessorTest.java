package de.hk.bfit.process;

import de.hk.bfit.model.SelectCmd;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCaseProcessorTest {

    TestCaseProcessor cut = null;

    @Before
    public void init() {
        cut = new TestCaseProcessor();
    }

    @Test
    public void testGetDifferencesNoDifferences() {
        SelectCmd selectCmd = getSelectCmd();
        List<String> actualResults = new ArrayList<>();
        actualResults.add("test;bla;bla;test;;");
        System.out.println(cut.determineDifferences(selectCmd, null, actualResults).toString());
        Assert.assertEquals(0, cut.determineDifferences(selectCmd, null, actualResults).size());
    }

    @Test
    public void testGetDifferencesWithOneDifference() {
        SelectCmd selectCmd = getSelectCmd();
        List<String> actualResults = new ArrayList<>();
        actualResults.add("test;bla;bla;test;blub;");
        System.out.println(cut.determineDifferences(selectCmd, null, actualResults).toString());
        Assert.assertEquals(1, cut.determineDifferences(selectCmd, null, actualResults).size());
    }

    private SelectCmd getSelectCmd() {
        SelectCmd selectCmd = new SelectCmd();

        selectCmd.setDescription("ein test");

        selectCmd.setSelect("irgendein select");

        List<String> resultList = new ArrayList<>();

        resultList.add("test;bla;bla;test;;");

        selectCmd.setResults(resultList);

        return selectCmd;
    }


}
