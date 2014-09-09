package com.android.bible_notes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;


import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;

public class MainActivity extends Activity 
implements OnItemSelectedListener
{
    /** Called when the activity is first created.  */
	 private SharedPreferences prefs;
	 OnSharedPreferenceChangeListener listener;
     private String prefName = "myversion";
     private static final String VERSION_KEY="version_key";
     private static final int VERSION_DEFAULT=0;
     private static final String SIZE_KEY="size";
    @Override
    public void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_bible);
       
        try {
            String destPath = "/data/data/com.android.bible_notes/databases/MyDB";
            File f = new File(destPath);
            if (!f.exists())  {
                CopyDB( getBaseContext().getAssets().open("new_bible.db"),
                    new FileOutputStream(destPath));
            }
        } catch (FileNotFoundException e)  {
            e.printStackTrace();
        } catch (IOException e)  {
            e.printStackTrace();
        }

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        
        // Spinner 
        Spinner spinner = (Spinner) findViewById(R.id.vesrionlist);
	     // Create an ArrayAdapter using the string array and a default spinner layout
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	             R.array.version, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when the list of choices appears
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     // Apply the adapter to the spinner
	     spinner.setAdapter(adapter);
	     
	     spinner.setOnItemSelectedListener(this);
        
	     /* listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
	    	  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
	    	    // Implementation
	    		Log.v("MainAct","Listner inside");
	    	  }
	    	};
	    	prefs = getSharedPreferences(prefName, MODE_PRIVATE);
	    	prefs.registerOnSharedPreferenceChangeListener(listener);
       // Log.v("create",Listofbooks[0].toString());
*/     
     
     }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_bible, menu);
        return true;
    }
    
     public void CopyDB(InputStream inputStream,
    		  OutputStream outputStream)
    		  throws IOException {
    		      //---copy 1K bytes at a time---
    		      byte[] buffer = new byte[1024];
    		      int length;
    		      while ((length = inputStream.read(buffer)) > 0)  {
    		          outputStream.write(buffer, 0, length);
    		      }
    		      inputStream.close();
    		      outputStream.close();
    		  }
     public boolean onOptionsItemSelected(MenuItem item)
     {
    	 switch (item.getItemId())
         {
         case R.id.new_note:
             // Toast.makeText(this, "Notes Selected", Toast.LENGTH_SHORT).show();
             startActivity(new Intent(this, NewNote.class));
             return true;
             
         case R.id.add_note:
             startActivity(new Intent(this, NotesList.class));
             return true;
         
         case R.id.Prev:
             Toast.makeText(this, "Prevs Selected", Toast.LENGTH_SHORT).show();
             // startActivity(new Intent(this, NotesList.class));
             return true;
  
         case R.id.menu_settings:
        	 startActivity(new Intent(this,MyPrefs.class));
             return true;

         default:
             return super.onOptionsItemSelected(item);
         }
         
     }
	 public void onItemSelected(AdapterView<?> parent, View view, 
	            int pos, long id) {

      prefs = getSharedPreferences(prefName, MODE_PRIVATE);
      SharedPreferences.Editor editor = prefs.edit();
      editor.putInt(VERSION_KEY,pos);
      editor.commit();
      Log.v("settings",Integer.toString(pos));
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	         prefs = getSharedPreferences(prefName, MODE_PRIVATE);
	         SharedPreferences.Editor editor = prefs.edit();
	         editor.putInt(VERSION_KEY,VERSION_DEFAULT);
	         Log.v("settings",Integer.toString(VERSION_DEFAULT));
	         editor.commit();
	    }
	    
	
		@Override
		public void onResume()
		{
			super.onResume();
	
		}
	    
     
}

