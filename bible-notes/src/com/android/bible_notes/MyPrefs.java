package com.android.bible_notes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefs extends Activity  implements OnItemSelectedListener ,OnSeekBarChangeListener{
	
	 private SharedPreferences prefs;
     private String prefName = "myversion";
     private static final String VERSION_KEY="version_key";
     private static final int VERSION_DEFAULT=0;
     private static final String SIZE_KEY="size";
     private static final int SIZE_DEFAULT=50;
     private SeekBar bar;
     private TextView textAction ;
     
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mypref);
	        Spinner spinner = (Spinner) findViewById(R.id.vesrionlist);
	     // Create an ArrayAdapter using the string array and a default spinner layout
	     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	             R.array.version, android.R.layout.simple_spinner_item);
	     // Specify the layout to use when the list of choices appears
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	     // Apply the adapter to the spinner
	     spinner.setAdapter(adapter);
	     spinner.setOnItemSelectedListener(this);
	     prefs=this.getSharedPreferences(prefName,Context.MODE_PRIVATE);
		 int size = prefs.getInt(SIZE_KEY,SIZE_DEFAULT );
	     bar = (SeekBar)findViewById(R.id.seekBar1);
	     bar.setProgress(size);
	     bar.setOnSeekBarChangeListener(this);
	     
	     textAction = (TextView)findViewById(R.id.size);
	     textAction.setText("The value is: "+size);
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

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// TODO Auto-generated method stub
			textAction.setText("The value is: "+progress);
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			prefs = getSharedPreferences(prefName, MODE_PRIVATE);
	         SharedPreferences.Editor editor = prefs.edit();
	         editor.putInt(SIZE_KEY,seekBar.getProgress());
	         Log.v("settings",Integer.toString(seekBar.getProgress()));
	         editor.commit();
			
		}
	    
}
