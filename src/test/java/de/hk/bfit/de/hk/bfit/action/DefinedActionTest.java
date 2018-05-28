package de.hk.bfit.de.hk.bfit.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.hk.bfit.action.DefinedAction;
import org.junit.Assert;
import org.junit.Test;

public class DefinedActionTest {

    @Test
    public void whenJavaSerializedToXmlStr_thenCorrect() throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();

        DefinedAction definedAction = new DefinedAction("herbert");


        String xml = xmlMapper.writeValueAsString(definedAction);

        Assert.assertEquals("<DefinedAction><description>define your init commands here</description><rollBackOnError>false</rollBackOnError><name>herbert</name><sqlCommands/></DefinedAction>",xml);

//        System.out.println(xml);
       // AsseassertNotNull(xml);
    }
}
