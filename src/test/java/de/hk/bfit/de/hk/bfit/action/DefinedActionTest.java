package de.hk.bfit.de.hk.bfit.action;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.hk.bfit.model.DefinedExecutionAction;
import org.junit.Assert;

import java.io.IOException;

class DefinedActionTest {

    //http://www.baeldung.com/jackson-xml-serialization-and-deserialization

    public void whenJavaSerializedToXmlStr_thenCorrect() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();

        DefinedExecutionAction definedExecutionAction = new DefinedExecutionAction();
        definedExecutionAction.setName("herbert");


        String xml = xmlMapper.writeValueAsString(definedExecutionAction);

        Assert.assertEquals("<DefinedExecutionAction><description>define your init commands here</description><rollBackOnError>false</rollBackOnError><name>herbert</name><sqlCommands/></DefinedExecutionAction>", xml);

        System.out.println(xml);

        DefinedExecutionAction definedExecutionAction2 = xmlMapper.readValue(xml, DefinedExecutionAction.class);
        // AsseassertNotNull(xml);
    }
}
