package de.hk.bfit.io;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.hk.bfit.model.TestCase;

import java.io.*;

public class TestCaseHandler {

    private static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static TestCase loadTestCase(String filename) throws IOException {
        File file = new File(filename);
        XmlMapper xmlMapper = new XmlMapper();
        String xml = inputStreamToString(new FileInputStream(file));
        return xmlMapper.readValue(xml, TestCase.class);
    }

    public static void writeTestcase(TestCase testCase, String filename) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.writeValue(new File(filename), testCase);
    }

}
