package com.capg.huaweiprofilestracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener,OnPreferenceClickListener {
   private EditTextPreference sample_rate;
   private EditTextPreference stimer;
   private CheckBoxPreference sedl;
   private Preference clear_historyPreference;
   private Preference resetPreference;
   private SharedPreferences mySharedPreferences;
   private String pathName="/sdcard/HuaweiProfilesTracker/";  
   private String fileName="history.txt";   
   private File file ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		sample_rate = (EditTextPreference)findPreference("sample_rate");
		stimer = (EditTextPreference)findPreference("stimer");
		sedl = (CheckBoxPreference)findPreference("sedl");
		clear_historyPreference = (Preference)findPreference("clear_history");
		resetPreference = (Preference)findPreference("reset");
		sample_rate.setOnPreferenceChangeListener(this);
		stimer.setOnPreferenceChangeListener(this);
		sedl.setOnPreferenceChangeListener(this);
		clear_historyPreference.setOnPreferenceClickListener(this);
		resetPreference.setOnPreferenceClickListener(this);
		mySharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
		sample_rate.setText(Integer.toString(mySharedPreferences.getInt("sample_rate",50)));
		stimer.setText(Float.toString(mySharedPreferences.getFloat("stimer",5)));
		sedl.setChecked(mySharedPreferences.getBoolean("sedl", true));
	    file = new File(pathName + fileName); 
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		Editor editor = mySharedPreferences.edit();
		if (preference == sample_rate) {
			try {
				editor.putInt(preference.getKey(), Integer.valueOf(newValue.toString()));
				sample_rate.setText(newValue.toString());
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "It is not a Integer", Toast.LENGTH_SHORT).show();
				editor.putInt(preference.getKey(), 50);
			}
			
		}
		if (preference == stimer) {
			try {
				editor.putFloat(preference.getKey(), Float.valueOf(newValue.toString()));
				stimer.setText(newValue.toString());
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "It is not a Float", Toast.LENGTH_SHORT).show();
				editor.putFloat(preference.getKey(), 50);
			}
			
		}
		if (preference == sedl) {
			try {
				editor.putBoolean(preference.getKey(), Boolean.valueOf(newValue.toString()));
				sedl.setChecked(Boolean.valueOf(newValue.toString()));
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "It is not a Boolean", Toast.LENGTH_SHORT).show();
				editor.putBoolean(preference.getKey(), true);
			}
		}
		editor.commit();
		return false;
	}


	@Override
	public boolean onPreferenceClick(Preference preference) {
		Editor editor = mySharedPreferences.edit();
		if(preference.getKey().compareTo("clear_history") == 0){
			if(sedl.isChecked()){
				FileOutputStream stream;
				try {
					stream = new FileOutputStream(file);
					String s = "";
				    byte[] buf = s.getBytes();  
				    stream.write(buf);
				    stream.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
		       
			}
			else{
				Toast.makeText(getApplicationContext(), "You didn't store histoty locally!", Toast.LENGTH_LONG).show();
			}
		}
		if(preference.getKey().compareTo("reset") == 0){
			editor.putInt(sample_rate.getKey(), 50);
			sample_rate.setText("50");
			editor.putFloat(stimer.getKey(), 5);
			stimer.setText("5");
			editor.putBoolean(sedl.getKey(), true);
			sedl.setChecked(true);
		}
		// TODO Auto-generated method stub
		editor.commit();
		return false;
	}


}
