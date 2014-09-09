package com.android.bible_notes;


import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragChap extends ListFragment  implements OnSharedPreferenceChangeListener {
	
	String Bookname="john";
    private SharedPreferences prefs;
    private String prefName = "myversion";
    private String KEY="version_key";
    private Boolean flag=false;
    OnSharedPreferenceChangeListener listener;
    String chap="3";
    private static final String SIZE_KEY="size";

    public Boolean getflag()
    {
    	return flag;
    }
	public void setBookname(String bn)
	{
		this.Bookname=bn;
	}
	public String getBookname()
	{
		return(this.Bookname);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
	    	  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
	    	    // Implementation
	    		Log.v("MainAct","Listner inside create ");
	    		if(getflag()){
	    			setFragText(chap);
	    		}
	    		
	    	  }
	    	};
	    	prefs = getActivity().getSharedPreferences(prefName,Context.MODE_PRIVATE );
	    	prefs.registerOnSharedPreferenceChangeListener(listener);
	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.detail_frag_text, container, false);
//		return view;
//	}

	public void setText(String item) {
		TextView view = (TextView) getView().findViewById(R.id.FullText);
		view.setText(item);
	}
	public void addchaps(String[] alist)
	{
		
		
		setListAdapter(null);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.books, alist);
		
		setListAdapter(adapter);
		flag=true;
		
	}
    @Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		chap = (String) getListAdapter().getItem(position);
		/*FragText frag = (FragText) getFragmentManager().findFragmentById(
				R.id.frag_text); 
		if (frag != null && frag.isInLayout()) {
			
			String text="";
			DBAdapter db = new DBAdapter(getActivity());
	        db.open();
	        Cursor c = db.getverse(Bookname,item);
	        String fullText=Bookname+" "+item+"\n\n";
	        if (c.moveToFirst())        
	        	do {
	            	// Log.v("create","i= "+i);
	        		fullText+= "["+getBook(c,0)+"] "+getBook(c,1)+"\n";
	            	
	               //  DisplayContact(c);
	            } while (c.moveToNext());
	        else
	            Toast.makeText(getActivity(), "No Text found", Toast.LENGTH_LONG).show();
	        db.close();*/
	              
	      //  frag.setText(fullText); 
		setFragText(chap);
		

	}
    public String getBook(Cursor c,int i)
	 {
	    	return(c.getString(i));
	 }
    public boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni!=null && ni.isAvailable() && ni.isConnected()) {
            return true;
        } else {
            return false; 
        }
    }
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		if (key.equals(SIZE_KEY) ) {
			  int i =sharedPreferences.getInt(key, 0);
			  Log.v("FragChap","Listner inside outer");
	        } 
		
	}
	public void setFragText(String item){
		FragView fv = (FragView) getFragmentManager().findFragmentById(
				R.id.frag_text);
		//prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		prefs=getActivity().getSharedPreferences(prefName,Context.MODE_PRIVATE);
		
		int version = prefs.getInt(KEY, 0);
		Log.v("Bible", "+"+ Integer.toString(version));
		if(version == 0)
		{
			 if(isOnline(this.getActivity()))
			{			
			       fv.getverse(Bookname, item);
			 
			}
			else
			{
				fv.getverseoffline(Bookname, item);
				Toast.makeText(getActivity(),
		                 "No Network...Switching to KJV",
		         Toast.LENGTH_SHORT).show();
			}
		}else
		{
			fv.getverseoffline(Bookname, item);
		}
	}
    
}

