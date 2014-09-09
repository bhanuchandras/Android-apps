package com.bhanu.sendcontactassms;



import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;

import android.content.Intent;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button btnSendSMS,btnCan;
    String phnumber="";
    String displayname="";
    /** Called when the activity is first created.  */
    @Override

    public void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendSMS =  (Button)  findViewById(R.id.btnSelcon);
        btnSendSMS.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
            	intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            	startActivityForResult(intent, 1);

            	
            }
        }
       
        		);
        
        btnCan =  (Button)  findViewById(R.id.btnCan);
        btnCan.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	finish();

            	
            }
        }
       
        		);

        
    }
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {Phone.NUMBER,Phone.DISPLAY_NAME};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(Phone.NUMBER);
                phnumber = cursor.getString(column);
                column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
                displayname = cursor.getString(column);
                // Do something with the phone number...
                Toast.makeText(this,
		                 "+"+phnumber,
		         Toast.LENGTH_SHORT).show();
                
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume(); 
        if (!phnumber.equals(""))
     	{
         //sendSMS("5556", "Hello my friends!");
         Intent i = new
             Intent(android.content.Intent.ACTION_VIEW);
         
         i.putExtra("sms_body", "Name: "+displayname+" Phone Number: "+phnumber);
         i.setType("vnd.android-dir/mms-sms");
         phnumber="";
         startActivity(i);
                          
     	}
        
    }
    
    
    

}