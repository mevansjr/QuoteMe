package com.markevansjr.quoteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.markevansjr.quoteme.lib.FileStuff;
import com.markevansjr.quoteme.lib.MainListener;
import com.markevansjr.quoteme.lib.QuoteList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class HomeFragmentTab extends Fragment{
	QuoteList _quoteList = null;
	ArrayList<String> _data = new ArrayList<String>();
	ArrayList<String> _quotes = new ArrayList<String>();
	ArrayList<String> _authors = new ArrayList<String>();
	static TextView _tv;
	static ArrayList<String> _recentQuote = new ArrayList<String>();
	HashMap<String, String> _recent = new HashMap<String, String>();
	ArrayList<Map<String, String>> _newdata;
	static String _theQuote;
	static String _theAuthor;
	String _stored;
	String _fullQuote;
	String _fullQuoteStr;
	String _fullSourceStr;
	List<Map<String, String>> _data2 = new ArrayList<Map<String, String>>();
	static View _view;
	static Button _save_btn;
	static Button _delete_btn;
	JSONObject json;
	String r;
	MainListener listener;
	String _savedQuote;
	public static String _savedButton = "YES";
	String _theId;
	String _checkNull;
	String _savedQuote2;
	String _currentQuote;
	String _quote;
	String _author;
	
//	_data2 = new ArrayList<Map<String, String>>();
//	_recentQuote = new ArrayList<String>();
//	_data2.clear();
//	_recentQuote.clear();
//	FileStuff.storeArrayFile(_view.getContext(), "data", (ArrayList<Map<String, String>>) _data2, false);
//	FileStuff.storeObjectFile(_view.getContext(), "recentQuote", _recentQuote, false);
	
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
		_view = inflater.inflate(R.layout.home_frag, container, false);
		
		Log.i("H ERROR", "1");
		// Check for Recents
		getRecents();
		
		Log.i("H ERROR", "2");
		ArrayList<Map<String, String>> datafromfile = FileStuff.readArrayFile(_view.getContext(), "data", false);
		if (!(datafromfile == null)){
			_data2 = datafromfile;
			Log.i("TOTAL ARRAY", String.valueOf(_data2.toArray().length));
		} else {
			_data2 = new ArrayList<Map<String, String>>();
			Log.i("TOTAL ARRAY", String.valueOf(_data2.toArray().length));
		}
		
		Log.i("H ERROR", "3");
		// Set TextView and Typeface
		_tv = (TextView) _view.findViewById(R.id.home_quote_text);
		_tv.setMovementMethod(new ScrollingMovementMethod());
		Typeface tf = Typeface.createFromAsset(_view.getContext().getAssets(), "fonts/m-reg.ttf");
		Typeface tf2 = Typeface.createFromAsset(_view.getContext().getAssets(), "fonts/m-bold.ttf");
		_tv.setTypeface(tf);
		
		Log.i("H ERROR", "4");
		// Set Buttons and Typeface
		_save_btn = (Button) _view.findViewById(R.id.home_save_btn);
		_delete_btn = (Button) _view.findViewById(R.id.home_delete_btn);
		_save_btn.setTypeface(tf2);
		_delete_btn.setTypeface(tf2);
		
		Log.i("H ERROR", "5");
		// Stored Preferences
		_savedButton = FileStuff.readStringFile(_view.getContext(), "buttonSave", false);
		Log.i("------TAG BUTTON SAVE CHECK-------", _savedButton);
		_theId = FileStuff.readStringFile(_view.getContext(), "theId", false);
		Log.i("------TAG MAIN ID-------", _theId);
		_savedQuote = FileStuff.readStringFile(_view.getContext(), "savedQuote", false);
		Log.i("------TAG MAIN SAVE QUOTE-------", _savedQuote);
		_savedQuote2 = FileStuff.readStringFile(_view.getContext(), "QOD", false);
		Log.i("------TAG MAIN QOD-------", _savedQuote2);
		_quote = FileStuff.readStringFile(_view.getContext(), "finalQuote", false);
		Log.i("------TAG QUOTE-------", _quote);
		_author = FileStuff.readStringFile(_view.getContext(), "finalAuthor", false);
		Log.i("------TAG AUTHOR ------", _author);
		
		Log.i("H ERROR", "6");
		// Check for Connectivity
		ConnectivityManager connec = (ConnectivityManager)_view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connec != null && (connec.getNetworkInfo(1).isAvailable() == true) ||
					(connec.getNetworkInfo(0).isAvailable() == true)){
				
				Log.i("H ERROR", "7");
				// Get Quote of the Day
				getQOD();
			} else {
				
				// No Connection
    			Toast toast = Toast.makeText(_view.getContext(), "NO CONNECTION", Toast.LENGTH_SHORT);
    			toast.show();
    		}
			
			Log.i("H ERROR", "8");
			// Check Button condition
			if (_savedButton.equals("YES")){
				_delete_btn.setVisibility(View.GONE);
				_save_btn.setVisibility(View.VISIBLE);
			} else {
				_delete_btn.setVisibility(View.VISIBLE);
				_save_btn.setVisibility(View.GONE);
			}
				
				// Save Button Onclick
				_save_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						String q = _quote;
						String a = _author;
						Log.i("q", q);
						Log.i("a", a);
						
						_recent = new HashMap<String, String>();
						_recent.put("savedQuote", q+"\r\n\n"+a);
						_recent.put("quote", q);
						_recent.put("author", a);
						_recent.put("image", Integer.toString(R.drawable.ic_gridpage));
						
						getRecents();
						
						for (int ii=0;ii<_data2.toArray().length;ii++){
							HashMap<String, String> o = (HashMap<String, String>) _data2.get(ii);
							String thequote = o.get("savedQuote");
							Log.i("quote", thequote);
							_recentQuote.add(thequote);
						}
						FileStuff.storeObjectFile(_view.getContext(), "recentQuote", _recentQuote, false);
						
						if (_recentQuote.contains(q+"\r\n\n"+a)){
							Toast toast = Toast.makeText(_view.getContext(), "ALREADY SAVED", Toast.LENGTH_SHORT);
			    			toast.show();
						} else {
							_data2.add(_recent);
							FileStuff.storeArrayFile(_view.getContext(), "data", (ArrayList<Map<String, String>>) _data2, false);
							Log.i("ARRAY ADD", String.valueOf(_data2.toArray().length));
							Toast toast = Toast.makeText(_view.getContext(), "QUOTE SAVED", Toast.LENGTH_SHORT);
			    			toast.show();
						}
						
						_delete_btn.setVisibility(View.VISIBLE);
						_save_btn.setVisibility(View.GONE);
					}
				});
			
				
				// Delete Button Onclick
				_delete_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						String q = _quote;
						String a = _author;
						Log.i("q", q);
						Log.i("a", a);
						
						_recent = new HashMap<String, String>();
						_recent.put("savedQuote", q+"\r\n\n"+a);
						_recent.put("quote", q);
						_recent.put("author", a);
						_recent.put("image", Integer.toString(R.drawable.ic_gridpage));
						
						getRecents();
						
						for (int ii=0;ii<_data2.toArray().length;ii++){
							HashMap<String, String> o = (HashMap<String, String>) _data2.get(ii);
							String thequote = o.get("savedQuote");
							Log.i("quote", thequote);
							_recentQuote.remove(thequote);
						}
						FileStuff.storeObjectFile(_view.getContext(), "recentQuote", _recentQuote, false);
						
						if (_recentQuote.contains(q+"\r\n\n"+a)){
							Toast toast = Toast.makeText(_view.getContext(), "QUOTE DOES NOT EXSIT", Toast.LENGTH_SHORT);
			    			toast.show();
						} else {
							_data2.remove(_recent);
							FileStuff.storeArrayFile(_view.getContext(), "data", (ArrayList<Map<String, String>>) _data2, false);
							Log.i("ARRAY REMOVE", String.valueOf(_data2.toArray().length));
							Toast toast = Toast.makeText(_view.getContext(), "QUOTE DELETED", Toast.LENGTH_SHORT);
			    			toast.show();
						}
						_delete_btn.setVisibility(View.GONE);
						_save_btn.setVisibility(View.VISIBLE);
					}
				});
				
	    return _view;
	}
    
	private void getQOD() {
		Log.i("H ERROR", "9");
		_tv.setText(_quote+"\r\n\n"+_author);
    }
  
  @SuppressWarnings("unchecked")
	private ArrayList<String> getRecents() {
	  Log.i("H ERROR", "10");
  	Object stored = FileStuff.readObjectFile(_view.getContext(), "recentQuote", false);
  	
  	if (stored == null) {
			Log.i("RECENTS", "NO RECENTS FOUND");
			_recentQuote = new ArrayList<String>();
		} else {
			_recentQuote = (ArrayList<String>) stored;
		}
  	return _recentQuote;
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
