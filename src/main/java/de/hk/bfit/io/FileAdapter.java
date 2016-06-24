package de.hk.bfit.io;

import de.hk.bfit.process.TestCase;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;

public class FileAdapter {
    private static final Logger logger = Logger.getLogger(FileAdapter.class);

    public static TestCase loadTestCase(String filename) throws IOException, JAXBException {
        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
        String content = readFile(filename);
        final TestCase testcase = genericXmlHandler.convertXMLToObject(TestCase.class, content);
        logger.info("-> " + testcase.getDescription());
        return testcase;
    }

    public static String readFile(String filename) throws FileNotFoundException {
        logger.info("reading '" + filename + "'");
        StringBuilder text;
        text = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        Scanner scanner = new Scanner(new FileInputStream(filename), "UTF-8") ;
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append(newLine);
            }
        
        
        return text.toString();
    }

    public static void writeFile(String filename, String content) throws IOException {
        logger.info("writing " + filename);
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filename), "UTF8")) ;
            out.append(content);
            out.flush();
    }
}
