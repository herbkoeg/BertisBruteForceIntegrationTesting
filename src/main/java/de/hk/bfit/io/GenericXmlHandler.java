/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hk.bfit.io;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @see http
 *      ://www.codesuggestions.com/java/jaxb-marshalling-unmarshalling-using-
 *      generics/
 * @author palmherby
 *
 */
public class GenericXmlHandler {

	public GenericXmlHandler() {
		super();
	}

	private HashMap<String, Object> getDefaultProperties() {
		HashMap<String, Object> defaultProperties = new HashMap<String, Object>();
		defaultProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		defaultProperties.put(Marshaller.JAXB_ENCODING, "ISO-8859-1");
		return defaultProperties;
	}

	public <T> String convertObjectToXML(T object) throws JAXBException {
		return marshall(object);
	}

	public <T> String marshall(T object) throws JAXBException {
		return marshall(object, getDefaultProperties());
	}

	public <T> String marshall(T object, HashMap<String, Object> properties) throws JAXBException {
		StringWriter stringWriter = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(object.getClass());
		Marshaller marshaller = context.createMarshaller();
		Iterator<Entry<String, Object>> it = properties.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> propertieEntry = it.next();
			marshaller.setProperty(propertieEntry.getKey(), propertieEntry.getValue());
		}

		marshaller.marshal(object, stringWriter);
		return stringWriter.toString();
	}

	public <T> T convertXMLToObject(Class clazz, String xml) throws JAXBException {
		return unmarshall(clazz, xml);
	}

	public <T> T unmarshall(Class clazz, String xml) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller um = context.createUnmarshaller();
		return (T) um.unmarshal(new StringReader(xml));
	}

}

