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

public class FileAdapter {

    public static TestCase loadTestCase(String filename) throws IOException, JAXBException {
        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
        String content = readFile(filename);
        System.out.println(content);
        return genericXmlHandler.convertXMLToObject(TestCase.class, content);
    }

    public static String readFile(String filename) throws FileNotFoundException {
        System.out.println("reading '" + filename + "'");
        StringBuilder text;
        text = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        try (Scanner scanner = new Scanner(new FileInputStream(filename), "UTF-8")) {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append(newLine);
            }
        }
        return text.toString();
    }

    public static void writeFile(String filename, String content) throws IOException {
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filename), "UTF8"))) {
            out.append(content);
            out.flush();
        }
    }
}
