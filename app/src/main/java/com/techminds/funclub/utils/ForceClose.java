package com.techminds.funclub.utils;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ForceClose extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Device model
		
		int width=getResources().getDisplayMetrics().widthPixels;
		int height=getResources().getDisplayMetrics().heightPixels;
		
		final String msg= "Device Detail:\nManufacturer: "+ android.os.Build.MANUFACTURER+"\nModel: "+
		                   android.os.Build.MODEL+"\nAndroid Version: "+
				           android.os.Build.VERSION.RELEASE+
				           "\nScren Size: "+width+"*"+height
				           +"\n\nReport:\n"+getIntent().getStringExtra("crash");

		AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton("Send Report", new DialogInterface.OnClickListener() {
			   
	        public void onClick(DialogInterface dialog, int which) {
	        	Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"justchill008@gmail.om"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Crash report ");
				i.putExtra(Intent.EXTRA_TEXT   , msg);
				try {
				    startActivity(Intent.createChooser(i, "Send Carsh Report"));
				} catch (android.content.ActivityNotFoundException ex) {
				   // Toast.makeText(AdvertiseActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
					        	
	           finish();
	        } } ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        	   
	            public void onClick(DialogInterface dialog, int which) {
	           
	               finish();
	            } }).create();
		
		alertDialog.setTitle("Bzzz");
        alertDialog.setMessage("Send us the crash report for  improve application.\n\n"+msg);
       
        alertDialog.show();     
		
		
	}
	
	

}
