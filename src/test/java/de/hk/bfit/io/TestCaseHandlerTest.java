package de.hk.bfit.io;

import de.hk.bfit.model.ReferenceAction;
import de.hk.bfit.model.ResetAction;
import de.hk.bfit.model.SelectCmd;
import de.hk.bfit.model.TestCase;
import de.hk.bfit.process.IBfiTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class TestCaseHandlerTest implements IBfiTest {


    public static final String SOMETESTCASE_XML = "sometestcase.xml";

    @Test
    public void readWriteTestcase() throws IOException {

        ReferenceAction referenceAction = new ReferenceAction("bla", new ArrayList<SelectCmd>());

        TestCase testCase = new TestCase(referenceAction);
        ResetAction resetAction = new ResetAction();
        List<String> sqlCommands = Arrays.asList("insert", "update");
        resetAction.setSqlCommands(sqlCommands);
        testCase.setResetAction(resetAction);
        TestCaseHandler.writeTestcase(testCase, BASE_PATH_GENERATED + SOMETESTCASE_XML);

        TestCase testCaseRed = TestCaseHandler.loadTestCase(BASE_PATH_GENERATED + SOMETESTCASE_XML);
        Assert.assertEquals(2, testCaseRed.getResetAction().getSqlCommands().size());
    }

    @Test
    public void integerationTestOnlyResetAction() throws Exception {
        Map<String, String> variables = new HashMap<>();

        String filename = BASE_PATH_TESTCASES + "BfitFirstTestOnlyReset.xml";
        TestCase testCase = TestCaseHandler.loadTestCase(filename);
        Assert.assertNotNull(testCase);
    }

    @Test
    public void selectActionWithMoreResults() throws Exception {
        Map<String, String> variables = new HashMap<>();

        String filename = BASE_PATH_TESTCASES + "BfitFirstTestReferenceActionWithTwoResults.xml";
        TestCase testCase = TestCaseHandler.loadTestCase(filename);
        Assert.assertNotNull(testCase);
        Assert.assertEquals(2, testCase.getReferenceActionAfter().getSelectCmds().get(0).getResults().size());
    }

    //   "BfitFirstTest.xml"


}