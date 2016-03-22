package de.hk.bfit.io;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;
import javax.xml.bind.JAXBException;

public class FileAdapter {

    private SqlReferenzFile getSqlReferenzFile(String filename) throws IOException, JAXBException {
        GenericXmlHandler genericXmlHandler = new GenericXmlHandler();
        String content = readFile(filename);
        return genericXmlHandler.convertXMLToObject(SqlReferenzFile.class, content);
    }

    public static String readFile(String filename) throws FileNotFoundException {
        System.out.println("reading '" + filename + "'");
        StringBuffer text = new StringBuffer();
        String newLine = System.getProperty("line.separator");
        Scanner scanner = new Scanner(new FileInputStream(filename), "UTF-8");
        try {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + newLine);
            }
        } finally {
            scanner.close();
        }
        return text.toString();
    }

    public static void writeFile(String filename, String content) throws IOException {
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filename), "UTF8"));
        out.append(content);
        out.flush();
        out.close();
    }
}
