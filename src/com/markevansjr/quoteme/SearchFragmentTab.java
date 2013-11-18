package com.markevansjr.quoteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.markevansjr.quoteme.lib.MainListener;
import com.markevansjr.quoteme.lib.QuoteList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchFragmentTab extends Fragment{
	QuoteList _quoteList = null;
	ArrayList<Map<String, String>> _data = new ArrayList<Map<String,String>>();
	ArrayList<String> _authors = new ArrayList<String>();
	ArrayList<String> _quotes = new ArrayList<String>();
	SimpleAdapter _adapter;
	SimpleAdapter _adapter2;
	JSONArray _results;
	static ListView _lv;
	static String _theQuote;
	static String _theAuthor;
	String _stored;
	String _fullQuote;
	View _view;
	EditText _et;
	Button _searchBtn;
	List<Map<String, String>> _data2;
	MainListener listener;
	
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
    	
		_view = inflater.inflate(R.layout.search_frag, container, false);	
		_lv = (ListView) _view.findViewById(R.id.List_View);
		_et = (EditText) _view.findViewById(R.id.editText1);
		
		Typeface tf = Typeface.createFromAsset(_view.getContext().getAssets(), "fonts/m-reg.ttf");
		_et.setTypeface(tf);
		_searchBtn = (Button) _view.findViewById(R.id.Search_Button);
		
		_searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConnectivityManager connec = (ConnectivityManager)_view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
				if (connec != null && (connec.getNetworkInfo(1).isAvailable() == true) ||
						(connec.getNetworkInfo(0).isAvailable() == true)){
					
					InputMethodManager imm = (InputMethodManager) getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(_et.getWindowToken(), 0);
					
					if (_et.getText().toString().equals("") || _et.getText().toString().equals(" ") || 
							_et.getText().toString() == "" || _et.getText().toString() == " ")
					{ 
						getQuotesParse("");
						Toast toast = Toast.makeText(_view.getContext(), "NO AUTHOR ENTERED.. SHOWING ALL", Toast.LENGTH_SHORT);
						toast.show();
					} else{
						getQuotesParse(_et.getText().toString());
					}
				} else {
					Toast toast = Toast.makeText(_view.getContext(), "NO CONNECTION", Toast.LENGTH_SHORT);
					toast.show();
				}
    		}
		});
		
	    return _view;
	}
	
	public void getQuotesParse(String theAuthor){
		if (theAuthor.equals("") || theAuthor.equals(" ")){
			ParseQuery query = new ParseQuery("ListOfQuotes");
			Log.i("TAG GET PARSE", "SHOW ALL");
			query.findInBackground(new FindCallback() {
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {
	    
						if (objects.toArray().length > 0){
						_data2 = new ArrayList<Map<String, String>>();
	    
						for(int ii=0;ii<objects.toArray().length;ii++){							
							ParseObject s = objects.get(ii);
							Map<String, String> map = new HashMap<String, String>(2);
							map.put("theQuote", s.getString("quote"));
							map.put("theAuthor", s.getString("author"));
							if (s.getString("quote").length() >= 30){
								map.put("pTheQuote", s.getString("quote").substring(0,30)+"..");
							} else {
								map.put("pTheQuote", s.getString("quote"));
							}
							map.put("theId", s.getObjectId());
							_data2.add(map);
						}
	      
						// List adapter is set
						_adapter2 = new SimpleAdapter(_view.getContext(), _data2, R.layout.list_row_layout,
									new String[] {"pTheQuote", "theAuthor", "theQuote", "theId"},
									new int[] {R.id.theText1,
									R.id.theText2});
						_lv.setAdapter(_adapter2);
	      
						_lv.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
								@SuppressWarnings("unchecked")
								HashMap<String, String> o = (HashMap<String, String>) _lv.getItemAtPosition(position);
								Log.i("TAG O", o.toString());
								String quote = o.get("theQuote");
								String author = o.get("theAuthor");
								String theid = o.get("theId");
								listener.pass(quote+"\r\n\n"+author, "YES", theid, 0, quote, author);
								listener.checkButton("s");
							}
						});
	   
					} else {
						Log.i("FROM PARSE::", "NO DATA STORED");
					}
	      
					} else {
						Log.e("ERROR::", "ERROR");
					}
				}
			});
		} else {
			ParseQuery query = new ParseQuery("ListOfQuotes");
			if (theAuthor.length() >= 2){
				query.whereContains("author", theAuthor.substring(0,1));
			} else {
				query.whereContains("author", theAuthor);
			}
			query.findInBackground(new FindCallback() {
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {
	    
						if (objects.toArray().length > 0){
						_data2 = new ArrayList<Map<String, String>>();
	    
						for(int ii=0;ii<objects.toArray().length;ii++){							
							ParseObject s = objects.get(ii);
							Map<String, String> map = new HashMap<String, String>(2);
							map.put("theQuote", s.getString("quote"));
							map.put("theAuthor", s.getString("author"));
							if (s.getString("quote").length() >= 30){
								map.put("pTheQuote", s.getString("quote").substring(0,30)+"..");
							} else {
								map.put("pTheQuote", s.getString("quote"));
							}
							map.put("theId", s.getObjectId());
							_data2.add(map);
						}
	      
						// List adapter is set
						_adapter2 = new SimpleAdapter(_view.getContext(), _data2, R.layout.list_row_layout,
									new String[] {"pTheQuote", "theAuthor", "theQuote", "theId"},
									new int[] {R.id.theText1,
									R.id.theText2});
						_lv.setAdapter(_adapter2);
	      
						_lv.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        		
								@SuppressWarnings("unchecked")
								HashMap<String, String> o = (HashMap<String, String>) _lv.getItemAtPosition(position);
								Log.i("TAG O", o.toString());
								String quote = o.get("theQuote");
								String author = o.get("theAuthor");
								String theid = o.get("theId");
								listener.pass(quote+"\r\n\n"+author, "YES", theid, 0, quote, author);
								listener.checkButton("s");
							}
						});
	   
					} else {
						Log.i("FROM PARSE::", "NO DATA STORED");
					}
	      
					} else {
						Log.e("ERROR::", "ERROR");
					}
				}
			});
		}
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