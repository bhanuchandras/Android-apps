package com.android.bible_notes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewFragment;
import android.widget.Toast;

public class FragView extends Fragment  {
	WebView view1;
	String bookn="";
	String chap="";
	static String vtext="";
    private SharedPreferences prefs;
    private String prefName = "myversion";
    private String KEY="size";

    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.detailsv, container, false);
		return view;
	}

	public void setText(String item) {
		
		view1 = (WebView) getView().findViewById(R.id.WebText);
		WebSettings settings= view1.getSettings();
		prefs=getActivity().getSharedPreferences(prefName,Context.MODE_PRIVATE);
		int version = prefs.getInt(KEY, 0);
		switch(version/25)
		{
		case 0:
			settings.setTextSize(WebSettings.TextSize.SMALLEST);
			break;
		case 1:
			settings.setTextSize(WebSettings.TextSize.SMALLER);
			break;
		case 2:
			settings.setTextSize(WebSettings.TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(WebSettings.TextSize.LARGER);
			break;
		case 4:
			settings.setTextSize(WebSettings.TextSize.LARGEST);
			break;
		default:
			settings.setTextSize(WebSettings.TextSize.NORMAL);
			break;
			
		}

		view1.loadData("<div>"+item+"</div>", "text/html", "UTF-8");
		
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	


   private String DownloadText(String URL)
	  {
		  
	      int BUFFER_SIZE = 2000;
	      InputStream in = null;
	      try {
	          in = OpenHttpConnection(URL);
	      } catch (IOException e) {
	      	Log.d("NetworkingActivity", e.getLocalizedMessage());
	          return "";
	      }
	      Log.v("URL",URL);
	      InputStreamReader isr = new InputStreamReader(in);
	      int charRead;
	      String str = "";
	      char[] inputBuffer = new char[BUFFER_SIZE];          
	      try {
	          while ((charRead = isr.read(inputBuffer))>0) {                    
	              //---convert the chars to a String---
	              String readString = 
	                  String.copyValueOf(inputBuffer, 0, charRead);                    
	              str += readString;
	              inputBuffer = new char[BUFFER_SIZE];
	          }
	          in.close();
	      } catch (IOException e) {
	      	Log.d("NetworkingActivity", e.getLocalizedMessage());
	          return "";
	      }    
	      return str;        
	  }    
	  
		private class DownloadTextTask extends AsyncTask<String, Void, String> {
			ProgressDialog progressDialog;
			protected String doInBackground(String... urls) {
				return DownloadText(urls[0]);
			}
			
			@Override
		    protected void onPreExecute()
		    {
		        progressDialog= ProgressDialog.show(getActivity(), "Connecting to ESV","Getting "+bookn + ":"+ chap +" ", true);

		        //do initialization of required objects objects here                
		    };
			

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Log.v("ESV",result);
				 progressDialog.dismiss();
				setText(result);
				vtext=result;
			}
		}

	
	  public void getverse(String book,String chapter)
	  {
		  bookn = book;
		  chap = chapter;
		  
		  //new DownloadTextTask().execute("http://iheartquotes.com/api/v1/random?max_characters=256&max_lines=10");
		   new DownloadTextTask().execute("http://www.esvapi.org/v2/rest/passageQuery?key=IP&passage="+book+":"+chapter);
		  // String st = DownloadText("http://www.esvapi.org/v2/rest/passageQuery?key=IP&passage="+book+":"+chapter);
		  
	  }
	  public void getverseoffline(String book,String chapter)
	  {
		  String text="";
			DBAdapter db = new DBAdapter(getActivity());
	        db.open();
	        Cursor c = db.getverse(book,chapter);
	        String fullText="<h3>"+book+" "+chapter+"</h3></br>";
	        if (c.moveToFirst())        
	        	do {
	            	// Log.v("create","i= "+i);
	        		fullText+= "["+getBook(c,0)+"] "+getBook(c,1)+"</br>";
	            	
	               //  DisplayContact(c);
	            } while (c.moveToNext());
	        else
	            Toast.makeText(getActivity(), "No Text found", Toast.LENGTH_LONG).show();
	        db.close();
	       //  Log.v("FragView",""+fullText);
	        this.setText(fullText);
	        
	  }
	  private InputStream OpenHttpConnection(String urlString) throws IOException
	  {
		
	      InputStream in = null;
	      int response = -1;

	      URL url = new URL(urlString);
	      
	      URLConnection conn = url.openConnection();

	      Log.v("inopenhttp","http...");
	      if (!(conn instanceof HttpURLConnection))
	          throw new IOException("Not an HTTP connection");
	      try{
	    	  
	          HttpURLConnection httpConn = (HttpURLConnection) conn;
	          httpConn.setAllowUserInteraction(false);
	          httpConn.setInstanceFollowRedirects(true);
	          httpConn.setRequestMethod("GET");
	          httpConn.connect();
	          response = httpConn.getResponseCode();
	          Log.v("inopenhttp","http...");
	          if (response == HttpURLConnection.HTTP_OK) {
	              in = httpConn.getInputStream();
	          }
	      }
	      catch (Exception ex)
	      {
	    	 
	          Log.d("Networking", ex.getLocalizedMessage());
	          throw new IOException("Error connecting");
	      }
	      return in;
	  }

	  public String getBook(Cursor c,int i)
		 {
		    	return(c.getString(i));
		 }
	  
	  @Override
	  public void onResume()
	  {
		  super.onResume();
		  
		 
	  }
	  
	  public void refresh()
	  {
		  if(vtext.length()>0)
		  {
			  setText(vtext);
		  }
	  }
		





}
