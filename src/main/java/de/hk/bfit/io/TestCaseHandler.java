package de.hk.bfit.io;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.hk.bfit.model.TestCase;

import java.io.*;
import java.nio.charset.Charset;

public class TestCaseHandler {

    private static String inputStreamToString(InputStream is, String charsetName) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is,charsetName));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public static TestCase loadTestCase(String filename) throws IOException {
        return loadTestCase(filename, Charset.defaultCharset().name());
    }

    public static TestCase loadTestCase(String filename, String charsetName) throws IOException {
        File file = new File(filename);
        XmlMapper xmlMapper = new XmlMapper();
        String xml = inputStreamToString(new FileInputStream(file),charsetName);
        return xmlMapper.readValue(xml, TestCase.class);
    }

    public static void writeTestcase(TestCase testCase, String filename) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.writeValue(new File(filename), testCase);
    }

}
