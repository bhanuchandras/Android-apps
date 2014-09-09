package com.bhanu.memory.widget;


import java.text.NumberFormat;


import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;



public class FreeMemWidgetActivity extends AppWidgetProvider {
	private static final String ACTION_WIDGET_RECEIVER = "ActionRecieverWidget";
	static String strWidgetText = "";
	static int THRESHOLD=15;
	
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {

    	ComponentName thisWidget = new ComponentName(context,
    			FreeMemWidgetActivity.class);
    	int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(),
				UpdateService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

          context.startService(intent);
        
         
     }
    
    public static class UpdateService extends Service {
        @Override
        public void onStart(Intent intent, int startId) {
            // Build the widget update for today
        
            
        
            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(this, FreeMemWidgetActivity.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
         
            int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            RemoteViews updateViews = buildUpdate(this,allWidgetIds);
             manager.updateAppWidget(thisWidget, updateViews);
             
            
        }

        /**
         * Build a widget update to show the current Wiktionary
         * "Word of the day." Will block until the online API returns.
         */
        public RemoteViews buildUpdate(Context context,int[] allWidgetIds) {
            // Pick out month names from resources
        	RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_word);

        	updateViews.setTextViewText(R.id.word_title, "Mem Left%");
        	updateViews.setTextViewText(R.id.word_title2, "Values");
        	String percent=getSpaceStats("SD CARD",false);
        	String finaltxt="SD:"+" "+percent+"%";
        	updateViews.setTextViewText(R.id.SDCARD,finaltxt);
        	
        	finaltxt = getSpaceStats("SD CARD", true);
        	updateViews.setTextViewText(R.id.SDCARD2,finaltxt);
        	
        	
        	if(Integer.parseInt(percent) > THRESHOLD)
            	{
        		updateViews.setInt(R.id.SDCARD, "setBackgroundResource", R.drawable.rounded_edges_mem_green);
        		updateViews.setInt(R.id.SDCARD2, "setBackgroundResource", R.drawable.rounded_edges_mem_green);
        		
            	}
            	else
            	{
            	updateViews.setInt(R.id.SDCARD, "setBackgroundResource", R.drawable.rounded_edges_mem_red);
            	updateViews.setInt(R.id.SDCARD2, "setBackgroundResource", R.drawable.rounded_edges_mem_red);
            	}
        	
        	percent=getSpaceStats("Local Mem",false);
        	updateViews.setTextViewText(R.id.PhoneMem,"Ph:"+" "+percent+"%");
        	finaltxt = getSpaceStats("Local Mem", true);
        	updateViews.setTextViewText(R.id.PhoneMem2,finaltxt);
        	
        	
        	if(Integer.parseInt(percent) > THRESHOLD)
        	{
        	updateViews.setInt(R.id.PhoneMem, "setBackgroundResource", R.drawable.rounded_edges_mem_green);
        	updateViews.setInt(R.id.PhoneMem2, "setBackgroundResource", R.drawable.rounded_edges_mem_green);
        	}
        	else
        	{
        	updateViews.setInt(R.id.PhoneMem, "setBackgroundResource", R.drawable.rounded_edges_mem_red);
        	updateViews.setInt(R.id.PhoneMem2, "setBackgroundResource", R.drawable.rounded_edges_mem_red);
        	}
        	
            
        	
        	
        	Intent clickIntent = new Intent(this.getApplicationContext(),
					FreeMemWidgetActivity.class);

			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					allWidgetIds);
			clickIntent.putExtra("CLICK", true);

        	PendingIntent pendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			updateViews.setOnClickPendingIntent(R.id.PhoneMem, pendingIntent);
			updateViews.setOnClickPendingIntent(R.id.SDCARD, pendingIntent);
			updateViews.setOnClickPendingIntent(R.id.word_title, pendingIntent);
			updateViews.setOnClickPendingIntent(R.id.SDCARD2, pendingIntent);
			updateViews.setOnClickPendingIntent(R.id.PhoneMem2, pendingIntent);
			
			
		
            return updateViews;
        }
        

        @Override
        public IBinder onBind(Intent intent) {
            // We don't need to bind to this service
            return null;
        }
    }
   
    
    public static String getSpaceStats(String device,boolean values)
    {    
    	 long free = 0;
         long tot = 0;
         long per = 0;
         StatFs filesys;
         String percent="";
         
    	if (device.equalsIgnoreCase("SD CARD"))
    	{
    		 filesys =  new StatFs(Environment.getExternalStorageDirectory().getPath());
    	}
    	else
    	{
    		filesys =  new StatFs(Environment.getDataDirectory().getPath());
    	}
    	
    	try
    	{
    		
    	
            free = (long)filesys.getAvailableBlocks() * filesys.getBlockSize() / 1024 / 1024;
            tot = (long)filesys.getBlockCount() * filesys.getBlockSize() / 1024 / 1024;
         
            
            per = (free*100)/tot;
            
            Log.v("getSpaceStats","Free "+device+":"+String.valueOf(free));
            Log.v("getSpaceStats","Total "+device+":"+String.valueOf(tot));
            Log.v("getSpaceStats"," %  "+String.valueOf(per));
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);            
            nf.setGroupingUsed(false); 
            
            if(free/1024 > 0)
            {
            	float f=free;
            	percent=nf.format(f/1024)+"GB";
            }
            else
            {
            percent=String.valueOf(free)+"MB";
            }
            if(tot/1024 > 0)
            {
            	float f=tot;
            	percent=percent+"/"+nf.format(f/1024)+"GB";
            }
            else
            {
            percent=percent+"/"+String.valueOf(tot)+"MB";
            }
            
            
         	}catch(Exception e) {
         		  Log.v("New method","error occured   "+String.valueOf(per));
         		   
          e.printStackTrace();
         	}
    	if(values)  
    	{
    		return percent;
    	}
    	else
    	{
    	return String.valueOf(per);
    	}
    	
    }
    @Override

public void onReceive(Context context, Intent intent) {
// v1.5 fix that doesn't call onDelete Action
    final String action = intent.getAction();
    boolean b = false;
    Bundle extras = intent.getExtras();
    if (extras != null) {
         b= extras.getBoolean("CLICK");
    }
   
    
    
	if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) 
	{
		final int appWidgetId = intent.getExtras().getInt(
		AppWidgetManager.EXTRA_APPWIDGET_ID,
		AppWidgetManager.INVALID_APPWIDGET_ID);
		
		
		
		if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) 
		{
		this.onDeleted(context, new int[] { appWidgetId });
		}
	} 	else {
		super.onReceive(context, intent);
	}
	 if(b)
	{
		 String toastcontent = "SD CARD Memory :"+getSpaceStats("SD CARD",true)+" \n"+"Phone Memory :"+getSpaceStats("Local",true)+" ";
		 LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		 View layout = inflater.inflate(R.layout.toast_layout,null);
		 TextView text = (TextView) layout.findViewById(R.id.text);
		 text.setText(toastcontent);
		 Toast toast = new Toast(context);
		 toast.setGravity(Gravity.BOTTOM, 0, 50);
		 toast.setDuration(Toast.LENGTH_LONG);
		 toast.setView(layout);
		 toast.show();
		
		/*Toast.makeText(context,toastcontent,
                Toast.LENGTH_LONG).show();*/
				
	} 
}
    

    public void onDisabled(Context context) {
        context.stopService(new Intent(context,UpdateService.class));
        super.onDisabled(context);
    }


    
}
