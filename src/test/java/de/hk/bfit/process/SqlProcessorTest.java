package de.hk.bfit.process;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SqlProcessorTest {


    private SqlProzessor cut = new SqlProzessor(null);

    @Test
    public void firstTest() {
        Map<String, String> variables = new HashMap<>();
        variables.put("VNR","1234");
        variables.put("ID","abc");
        Assert.assertEquals("select * from person where vnr=1234 and id=abc",
                cut.setVariables("select * from person where vnr=$VNR and id=$ID",variables));
    }


}
