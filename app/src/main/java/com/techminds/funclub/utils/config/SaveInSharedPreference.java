package com.techminds.funclub.utils.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveInSharedPreference {

	static Context context;
	static SaveInSharedPreference saveInSharedPreference;

	//SaveInSharedPreference.getInSharedPreference(Splash.this).methodNmae();
	
	public SaveInSharedPreference(Context context){
		this.context = context;
	}

	public static SaveInSharedPreference getInSharedPreference(Context context) {
		if (saveInSharedPreference == null) {
			saveInSharedPreference = new SaveInSharedPreference(context);
		}
		return saveInSharedPreference;
	}
	
	
	//making session method start
	public void sessionMethod(String key, boolean data){
		    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	 		SharedPreferences.Editor editor = preferences.edit();
	 		editor.putBoolean(key, data);
	 		editor.commit();	 			 		
	}
	//making session method end
	
			//get session method start
			public static boolean getBoolean(String key){
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				boolean value = prefs.getBoolean(key, false);			
				return value;
			}
			//get session method end
	
	
		//making session method start
		public void sessionMethod(String key, String data){
			    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		 		SharedPreferences.Editor editor = preferences.edit();
		 		editor.putString(key, data);		 		
		 		editor.commit();	 			 		
		}
		//making session method end

		
		//get session method start
		public static String getValues(String key){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			String value = prefs.getString(key, null);
			return value;
		}
		//get session method end
}
