package de.hk.bfit.process;

import de.hk.bfit.action.ReferenceAction;
import de.hk.bfit.action.SelectAction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestCaseProcessorTest {

    TestCaseProcessor cut = null;

    @Before
    public void init() {
        cut = new TestCaseProcessor();
    }

    @Test
    public void testGetDifferencesNoDifferences() throws ClassNotFoundException, SQLException, JAXBException, IOException {
        SelectAction selectAction = getSelectAction();
        List<String> actualResults = new ArrayList<>();
        actualResults.add("test;bla;bla;test;;");
        System.out.println(cut.determineDifferences(selectAction,null,actualResults).toString());
        Assert.assertEquals(0,cut.determineDifferences(selectAction,null,actualResults).size());
    }

    @Test
    public void testGetDifferencesWithOneDifference() throws ClassNotFoundException, SQLException, JAXBException, IOException {
        SelectAction selectAction = getSelectAction();
        List<String> actualResults = new ArrayList<>();
        actualResults.add("test;bla;bla;test;blub;");
        System.out.println(cut.determineDifferences(selectAction,null,actualResults).toString());
        Assert.assertEquals(1,cut.determineDifferences(selectAction,null,actualResults).size());
    }

    private SelectAction getSelectAction() {
        SelectAction selectAction = new SelectAction();

        selectAction.setDescription("ein test");

        selectAction.setSelect("irgendein select");

        List<String> resultList = new ArrayList<>();

        resultList.add("test;bla;bla;test;;");

        selectAction.setResult(resultList);

        return selectAction;
    }



}
