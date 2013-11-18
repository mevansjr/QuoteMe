package com.markevansjr.quoteme;


import com.markevansjr.quoteme.R;
import com.markevansjr.quoteme.lib.FileStuff;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


public class WidgetActivity extends AppWidgetProvider {
	public static WidgetActivity Widget = null;
	public static Context context;
	public static AppWidgetManager appWidgetManager;
	public static int appWidgetIds[];	
	public static String _text;
	public static String _pText;
	static int _random;
	
	@Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds ){		
		if (null == context){
			context = WidgetActivity.context;
		}
		else if (null == appWidgetManager){
			appWidgetManager = WidgetActivity.appWidgetManager;
		}
		else if (null == appWidgetIds){
			appWidgetIds = WidgetActivity.appWidgetIds;
		}
	    
	    WidgetActivity.Widget = this;
	    WidgetActivity.context = context;
	    WidgetActivity.appWidgetManager = appWidgetManager;
	    WidgetActivity.appWidgetIds = appWidgetIds;
	    
		Log.i("TAG", "onUpdate");
		
		final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];  
            updateAppWidget(context,appWidgetManager, appWidgetId);            
        }
        
    }

	
	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
		
		//QUOTE OF THE DAY
        _text = FileStuff.readStringFile(context, "savedQuote", false);
                
        Intent intent = new Intent(context, UpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.widget_textview, pendingIntent);
        
        PackageManager pm = context.getPackageManager();
        try{
        	String packageName = "com.markevansjr.quoteme";
        	Intent btnIntent = pm.getLaunchIntentForPackage(packageName);
        	PendingIntent pi = PendingIntent.getActivity(context, 0, btnIntent, 0);
        	
        	remoteViews.setOnClickPendingIntent(R.id.widget_button, pi);
        } catch (Exception e1){
        	Log.i("EXCEPTION", e1.toString());
        }
        
		remoteViews.setTextViewText(R.id.widget_textview, _text);
		
        // Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
	
	
	
	public static class UpdateService extends Service {
        @Override
        public void onStart(Intent intent, int startId) {
        	WidgetActivity.Widget.onUpdate(context, appWidgetManager, appWidgetIds);
        	Toast.makeText(context, "Update Widget", Toast.LENGTH_SHORT).show();
        }

		@Override
		public IBinder onBind(Intent arg0) {
			return null;
		}
    }
	
}
