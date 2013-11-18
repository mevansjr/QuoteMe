package com.markevansjr.quoteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.markevansjr.quoteme.lib.FileStuff;
import com.markevansjr.quoteme.lib.MainListener;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SavedFragmentTab extends Fragment {
	
	View _view;
	ArrayList<Map<String, String>> _data = new ArrayList<Map<String,String>>();
	GridView _gridView;
	MainListener listener;
	ArrayList<String> _recentQuote;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
    	
    	_view = inflater.inflate(R.layout.saved_frag, container, false);
    	_gridView = (GridView) _view.findViewById(R.id.gridView1);
    	
    	// Check recents
    	Object stored = FileStuff.readObjectFile(_view.getContext(), "recentQuote", false);
      	if (stored == null) {
    			Log.i("RECENTS", "NO RECENTS FOUND");
    			_recentQuote = new ArrayList<String>();
    	} else {
    			_recentQuote = (ArrayList<String>) stored;
    	}
    	
    	ArrayList<Map<String, String>> datafromfile = FileStuff.readArrayFile(_view.getContext(), "data", false);
		if (!(datafromfile == null)){
			_data = datafromfile;
			Log.i("TOTAL ARRAY", String.valueOf(_data.toArray().length));
		} else {
			_data = new ArrayList<Map<String, String>>();
			Log.i("TOTAL ARRAY", String.valueOf(_data.toArray().length));
		}
		 
    	if (_data.toArray().length > 0){
						
	       String[] from = {"image","savedQuote"};
	       int[] to = { R.id.grid_item_image,R.id.grid_item_label};
	       SimpleAdapter adapter = new SimpleAdapter(_view.getContext(), _data, R.layout.custom_grid_item, from, to);
				 
			_gridView.setAdapter(adapter);
						 
			_gridView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					HashMap<String, String> o = (HashMap<String, String>) _gridView.getItemAtPosition(position);
					String quote = o.get("savedQuote");
					_recentQuote.add(quote);
					String q = o.get("quote");
					String a = o.get("author");
					listener.pass(quote, "NO", "", 0, q, a);
					listener.checkButton("d");
				}
			});
		} else {
			Toast toast2 = Toast.makeText(_view.getContext(), "NO SAVED QUOTES", Toast.LENGTH_SHORT);
			toast2.show();
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
