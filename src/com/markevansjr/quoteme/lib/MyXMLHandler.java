package com.markevansjr.quoteme.lib;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class MyXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = null;
	public static QuoteList quoteList = null;

	public static QuoteList getSitesList() {
		return quoteList;
	}

	public static void setSitesList(QuoteList sitesList) {
		MyXMLHandler.quoteList = sitesList;
		sitesList.equals("results");
	}

	// Called when tag starts (ex:- <name>AndroidPeople</name>)
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals("results"))
		{
			// Start
			quoteList = new QuoteList();
		}
	}

	// Called when tag closing (ex:- <name>AndroidPeople</name>)
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		// set value
		if (localName.equalsIgnoreCase("quote")){
			quoteList.setQuote(currentValue);
		} else if (localName.equalsIgnoreCase("author")){
			quoteList.setAuthor(currentValue);
		}
	}

	// Called to get tag characters ( ex:- <name>AndroidPeople</name> to get AndroidPeople Character )
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = new String(ch, start, length);
			currentElement = false;
		}
	}
}
