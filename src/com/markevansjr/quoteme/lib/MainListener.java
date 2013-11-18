package com.markevansjr.quoteme.lib;

public interface MainListener {
	public String currentQuote = null;
	public void pass(String pass, String buttonSave, String id, int number, String quote, String author);
	public void checkButton(String state);
}

//TODO
	// method to set currentQuote
