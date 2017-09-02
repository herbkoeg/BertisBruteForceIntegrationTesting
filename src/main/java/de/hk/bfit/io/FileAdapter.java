package de.hk.bfit.io;

import de.hk.bfit.process.TestCase;

import java.io.*;
import java.util.Scanner;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
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

    public static void createFile(String filename) {


    }

    public static void writeFile(String filename, String content) throws IOException {
        logger.info("writing " + filename);

        File f = new File(filename);

        FileUtils.writeStringToFile(f,content,"UTF-8");
    }
}
