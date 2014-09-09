/**
 * 
 */
package com.android.bible_notes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class ViewNote extends Activity {

	TextView vtitle,vbody;
	String title="";
	String body="";
	String id="0";
	@Override
	 public void onCreate(Bundle savedInstanceState)   {
	        super.onCreate(savedInstanceState);
	       setContentView(R.layout.view_note);
	       Log.v("ViewNote","starting");
	       Bundle extras = getIntent().getExtras();
	       if (extras != null) {
	    	   Log.v("ViewNote","Bundle");
	           title = extras.getString("Title");
	           vtitle = (TextView) findViewById(R.id.titleText);
	           vbody = (TextView) findViewById(R.id.bodyText);
	            DBAdapter db = new DBAdapter(this);
		        db.open();
		        Cursor c = db.getNote(title);
		        Log.v("ViewNote","in db");
		        if (c.moveToFirst())
		        {
		        	body = getcursor(c,2);
		        	id = getcursor(c,0);
		              
		        }
		        else
		        {
		        	Toast.makeText(this, "Notes Not Found", Toast.LENGTH_SHORT).show();
		        }
		        db.close();
		        
	           vtitle.setText(title);
	           vbody.setText(body);
	        
	           
	       }
	       else
	       {
	    	 	Toast.makeText(this, "Notes Not Found", Toast.LENGTH_SHORT).show();
	       }
	       
	 }
	public String getcursor(Cursor c,int i)
	 {
	    	return(c.getString(i));
	 }
	
	public void editNote(View v)
	{
		Intent i = new Intent(this,NewNote.class);
		i.putExtra("Title", title);
		i.putExtra("Body", body);
		i.putExtra("id", id);
		startActivity(i);
		this.finish();
		
	}
	
	
	public boolean updateNote(long id,String title,String body)
	{
		if(title.length() > 0 && body.length() > 0)
		{
			DBAdapter db = new DBAdapter(this);
	        db.open();
	        db.updateNote(id, title, body);
	        db.close();
	        Toast.makeText(this, "Note Saved", Toast.LENGTH_LONG).show();
			return true;
			
		}
		else
		{
			Toast.makeText(this, "Notes Not saved Title/Content empty", Toast.LENGTH_LONG).show();
			return false;
		}
	}
	

}
