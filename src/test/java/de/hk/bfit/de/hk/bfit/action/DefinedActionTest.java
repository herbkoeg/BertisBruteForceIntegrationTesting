package de.hk.bfit.de.hk.bfit.action;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.hk.bfit.model.DefinedAction;
import org.junit.Assert;

import java.io.IOException;

public class DefinedActionTest {

    //http://www.baeldung.com/jackson-xml-serialization-and-deserialization

    public void whenJavaSerializedToXmlStr_thenCorrect() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();

        DefinedAction definedAction = new DefinedAction("herbert");


        String xml = xmlMapper.writeValueAsString(definedAction);

        Assert.assertEquals("<DefinedAction><description>define your init commands here</description><rollBackOnError>false</rollBackOnError><name>herbert</name><sqlCommands/></DefinedAction>",xml);

        System.out.println(xml);

        DefinedAction definedAction2 =  xmlMapper.readValue(xml,DefinedAction.class);
       // AsseassertNotNull(xml);
    }
}
