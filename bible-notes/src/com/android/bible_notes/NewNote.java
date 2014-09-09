package com.android.bible_notes;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewNote extends Activity {

	EditText title_text,body_text;
	@Override
	 public void onCreate(Bundle savedInstanceState)   {
	        super.onCreate(savedInstanceState);
	       setContentView(R.layout.notes_layout);
	       Bundle extras = getIntent().getExtras();
	       if (extras != null) {
	    	   String title = extras.getString("Title");
	    	   String body = extras.getString("Body");
	    	   String id = extras.getString("id");
	    	   title_text = (EditText) findViewById(R.id.editText1);
	   		   body_text = (EditText) findViewById(R.id.editText2);
	   		   title_text.setText(title);
	   		   body_text.setText(body);
	       }
	 }
	
	public void savenote(View v)
	{
		title_text = (EditText) findViewById(R.id.editText1);
		body_text = (EditText) findViewById(R.id.editText2);
		String title = title_text.getText().toString();
		String body = body_text.getText().toString();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String id = extras.getString("id");
			long l=0;
			try {
		         l = Long.parseLong(id);
		         Log.v("Newnote","long l = " + l);
		      } catch (NumberFormatException nfe) {
		    	  Log.v("Newnote","NumberFormatException: " + nfe.getMessage());
		      }
			updateNote(l,title,body);
			
		}else
			savenotedb(title,body);
		this.finish();
		
	}
	
	public boolean savenotedb(String title,String body)
	{
		if(title.length() > 0 && body.length() > 0)
		{
			DBAdapter db = new DBAdapter(this);
	        db.open();
	        db.insertNote(title, body);
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
