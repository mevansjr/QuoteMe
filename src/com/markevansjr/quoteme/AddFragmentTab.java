package com.markevansjr.quoteme;


import com.markevansjr.quoteme.lib.FileStuff;
import com.markevansjr.quoteme.lib.MainListener;
import com.parse.ParseObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFragmentTab extends Fragment {
	
	View _view;
	MainListener listener;
	String _user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
    	
    	_view = inflater.inflate(R.layout.add_frag, container, false);
    	
    	// Check for User
    	_user = FileStuff.readStringFile(_view.getContext(), "userName", false);
   		Log.i("USER HOME", _user);
    	
    	// Set Elements
    	final EditText quoteText = (EditText) _view.findViewById(R.id.edit_quote);
    	final EditText authorText = (EditText) _view.findViewById(R.id.edit_author);
    	Button submitBtn = (Button) _view.findViewById(R.id.submit_btn);
    	
    	// Set Typeface
    	Typeface tf = Typeface.createFromAsset(_view.getContext().getAssets(), "fonts/m-reg.ttf");
    	Typeface tf2 = Typeface.createFromAsset(_view.getContext().getAssets(), "fonts/m-bold.ttf");
    	quoteText.setTypeface(tf);
    	authorText.setTypeface(tf);
    	submitBtn.setTypeface(tf2);
    	
    	// Check for Connectivity
    	ConnectivityManager connec = (ConnectivityManager)_view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    	if (connec != null && (connec.getNetworkInfo(1).isAvailable() == true) ||
 			(connec.getNetworkInfo(0).isAvailable() == true)){
    			
    		submitBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					InputMethodManager imm = (InputMethodManager) _view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(quoteText.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(authorText.getWindowToken(), 0);
					
					if(quoteText.getText().toString().equals("") || authorText.getText().toString().equals("")){
						Toast toast = Toast.makeText(_view.getContext(), "CHECK QUOTE AND AUTHOR FIELDS!", Toast.LENGTH_SHORT);
						toast.show();
					} else {
						ParseObject savedFavObject = new ParseObject(_user);
						savedFavObject.put("savedQuote", '"'+quoteText.getText().toString()+'"');
						savedFavObject.put("savedAuthor", authorText.getText().toString());
						savedFavObject.saveInBackground();	
						ParseObject savedToList = new ParseObject("ListOfQuotes");
						savedToList.put("quote", '"'+quoteText.getText().toString()+'"');
						savedToList.put("author", authorText.getText().toString());
						savedToList.saveInBackground();
						Toast toast = Toast.makeText(_view.getContext(), "QUOTE SAVED", Toast.LENGTH_SHORT);
						toast.show();
						quoteText.setText("");
						authorText.setText("");
					}
				}
			});
    		
    	} else {
    		Toast toast = Toast.makeText(_view.getContext(), "NO CONNECTION", Toast.LENGTH_SHORT);
    		toast.show();
    	}
    	
		return _view;
    }
    
    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			listener = (MainListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MainListener");
		}
	}
}
