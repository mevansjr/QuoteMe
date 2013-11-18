package com.markevansjr.quoteme.lib;

import java.util.ArrayList;

public class QuoteList {

	private ArrayList<String> quote = new ArrayList<String>();
	private ArrayList<String> author = new ArrayList<String>();
	
	public ArrayList<String> getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote.add(quote);
	}
	
	public ArrayList<String> getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.quote.add(author);
	}
}
