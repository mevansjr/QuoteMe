package com.markevansjr.quoteme.lib;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;
 
public class GetService  extends IntentService{
    URL finalURL = null;
    int _result;
	String _response = null;
	JSONArray _results;
	String _passedItem = null;
 
    public GetService() {
        super("GetService");
    }
 
    @Override
    protected void onHandleIntent(Intent intent) {
    
    	Bundle extras = intent.getExtras();
        if (extras != null) {
        	// Searched Item is received
        	_passedItem = (String) extras.get("item");
        	String responseString = "http://www.iheartquotes.com/api/v1/random?format=json&source="+_passedItem;
            Log.i("onHandleIntent::", responseString);
            ConnectivityManager connec = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    		if (connec != null && (connec.getNetworkInfo(1).isAvailable() == true) ||
    				(connec.getNetworkInfo(0).isAvailable() == true)){
            try{
            	// API string response is generated below
    			finalURL = new URL(responseString);
    			_response = WebStuff.getURLStringResponse(finalURL);
    			if (_response.length() > 0) _result = 0;
    		} catch (MalformedURLException e){
    			Log.e("BAD URL", "MALFORMED URL");
    			finalURL = null;
    		}
            // Messenger is received and API response is sent back via a message object
        	Messenger messenger = (Messenger) extras.get("messenger");
        	Message msg = Message.obtain();
        	msg.arg1 = _result;
        	msg.obj = _response;
        	Log.i("msg.obj::", msg.obj.toString());
        	try{
        		messenger.send(msg);
        	} catch (android.os.RemoteException e){
        		Log.e(getClass().getName(), "EXCEPTION sending message", e);
        	}
    		} else {
    			Toast toast = Toast.makeText(getApplicationContext(), "NO CONNECTION", Toast.LENGTH_SHORT);
    			toast.show();
    		}
        } 
    }
}