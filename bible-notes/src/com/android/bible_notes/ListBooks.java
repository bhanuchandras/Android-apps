package com.android.bible_notes;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListBooks extends ListFragment {
	
	FragChap frag;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 
		DBAdapter db = new DBAdapter(getActivity());
	        db.open();
	        Cursor c = db.getAllContacts();
	        int i=0;
	        String[] Listofbooks = new String[66];
	        //Log.v("create","b4 toast");
	        if (c.moveToFirst())
	        {
	            do {
	            	// Log.v("create","i= "+i);
	            	Listofbooks[i]=getBook(c,1);
	            	i++;
	               //  DisplayContact(c);
	            } while (c.moveToNext());
	        }
	        db.close();
	        
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.books, Listofbooks);
		setListAdapter(adapter);
		frag = (FragChap) getFragmentManager().findFragmentById(
 				R.id.frag_chap);
		SharedPreferences prefs=getActivity().getSharedPreferences("myversion",Context.MODE_PRIVATE);
		Log.v("ListBooks","The value "+frag.getflag().toString());
		if (frag.getflag())
		{
		frag.onSharedPreferenceChanged(prefs, "size");
		}
		
		
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		SharedPreferences prefs=getActivity().getSharedPreferences("myversion",Context.MODE_PRIVATE);
		if (frag.getflag())
		{
		frag.onSharedPreferenceChanged(prefs, "size");
		}
	}

	
	 public String getBook(Cursor c,int i)
	 {
	    	return(c.getString(i));
	 }
	 
     public void DisplayContact(Cursor c)
     {
         Toast.makeText(getActivity(),
                 "id: " + c.getString(0),
         Toast.LENGTH_LONG).show();
    }


     
     @Override
 	public void onListItemClick(ListView l, View v, int position, long id) {
 		String item = (String) getListAdapter().getItem(position);
 		
 		
 		Log.v("ListBooks","posi"+position);
 		frag.setBookname(item);  
 		 
 		if (frag != null && frag.isInLayout()) {
 			
 		
 			DBAdapter db = new DBAdapter(getActivity());
	        db.open();
	        Cursor c = db.getbookChap(item);
	        String maxchap="0";
	        if (c.moveToFirst())        
	        	maxchap = getBook(c,0);
	        else
	            Toast.makeText(getActivity(), "No Text found", Toast.LENGTH_LONG).show();
	        db.close();
	        int i = Integer.parseInt(maxchap);
	        String[] chaplist = new String[i];
	        
	       
	        for (int j=1;j<=i;j++)
	        	chaplist[j-1]=""+j;	 
        
	        frag.addchaps(chaplist);
	        
	        
	       
	        
      
 		}
 		Log.v("ListBooks",item);
 	}
     
   

}

