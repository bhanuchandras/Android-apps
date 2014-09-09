package com.android.bible_notes;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NotesList extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		refreshList();
		
	}
	public void refreshList()
	{
		DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAllNotes();
        int i=0;
        List<String>  Listofnote = new ArrayList<String>();
        if (c.moveToFirst())
        {
            do {
            	// Log.v("create","i= "+i);
            	Listofnote.add(getBook(c,0));
            	i++;
               //  DisplayContact(c);
            } while (c.moveToNext());
        }
        db.close();
        if(Listofnote.size() == 0)
        	Listofnote.add("No Notes found");
    	/*Toast.makeText(this,
                 "No Notes Avail",Toast.LENGTH_LONG).show();
    	 */
   
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			R.layout.books, Listofnote);
	setListAdapter(adapter);
	}

	
	 public String getBook(Cursor c,int i)
	 {
	    	return(c.getString(i));
	 }
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	        MenuInflater menuInflater = getMenuInflater();
	        menuInflater.inflate(R.menu.menu_notes, menu);
	        return true;
	    }
	 
	 public boolean onOptionsItemSelected(MenuItem item)
     {
    	 switch (item.getItemId())
         {
         case R.id.new_note:
             // Toast.makeText(this, "Notes Selected", Toast.LENGTH_SHORT).show();
             startActivity(new Intent(this, NewNote.class));
             return true;
  
         case R.id.menu_settings:
        	 startActivity(new Intent(this,MyPrefs.class));
             return true;

         default:
             return super.onOptionsItemSelected(item);
         }
         
     }
     @Override
 	public void onListItemClick(ListView l, View v, int position, long id) {
    	 String item = (String) getListAdapter().getItem(position);
    	 
    	 Intent i = new Intent(this, ViewNote.class);
	     i.putExtra("Title", item);
	     startActivity(i);
	     	                   	 
     }
	 
	 
	 
	 @Override
	 public void onResume()
	 {
		 super.onResume();
		 refreshList();
	 }
	

}
