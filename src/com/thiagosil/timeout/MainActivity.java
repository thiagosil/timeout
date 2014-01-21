package com.thiagosil.timeout;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thiagosil.timeout.database.DatabaseHelper;

public class MainActivity extends Activity {

	private Shift currentShift;
	
	private String CHECK_IN_KEY = "checkIn", CHECK_OUT_KEY = "checkOut";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initComponents();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//Check which buttons should be shown and loads ChechIn time
	private void initComponents() {
		currentShift = new Shift();
		if(isCheckedIn())
		{
			showCheckInTime();
		}
		switchButton();
	}

	public void checkIn(View view){
		checkIn(new Date());
	}
	
	public void checkOut(View view){
		checkOut(new Date());
	}

	private void checkIn(Date date) {
		currentShift.setCheckIn(date);
		saveDate(CHECK_IN_KEY, date);
		showCheckInTime();
		switchButton();
	}

	private void checkOut(Date date) {
		currentShift.setCheckOut(date);
		saveDate(CHECK_OUT_KEY, date);
		removeData(CHECK_IN_KEY);
		showCheckInTime();
		switchButton();
		saveShift();
	}
	
	//Show current checkIn time accordingly
	private void showCheckInTime() {
		TextView start_time = (TextView) findViewById(R.id.start_time);
		if (isCheckedIn())
		{
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			currentShift = new Shift();
			currentShift.setCheckIn(new Date(sharedPref.getLong(CHECK_IN_KEY, 0)));
			SimpleDateFormat dt = new SimpleDateFormat("hh:mm", java.util.Locale.getDefault()); 
			start_time.setText(dt.format(currentShift.getCheckIn()));
		}
		else
		{
			start_time.setText("");
		}
	}

	//Saves a date on the sharedPreferences file.
	private void saveDate(String key, Date date) {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putLong(key, date.getTime());
		editor.commit();
	}
	
	//Saves a key from the the sharedPreferences file.
	private void removeData(String key) {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.remove(key);
		editor.commit();
	}
	
	
	//Verifies if the user is checked in.
	private boolean isCheckedIn(){
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		if (sharedPref.contains(CHECK_IN_KEY)){
			return true;
		}
		return false;
	}
	
	//Changes the currently displayed button given the current state of the application.
	private void switchButton()
	{
		if (isCheckedIn()){
			Button checkInButton = (Button) findViewById(R.id.check_in_button);
			checkInButton.setVisibility(View.GONE);
			Button checkOutButton = (Button) findViewById(R.id.check_out_button);
			checkOutButton.setVisibility(View.VISIBLE);
		}
		else {
			Button checkInButton = (Button) findViewById(R.id.check_in_button);
			checkInButton.setVisibility(View.VISIBLE);
			Button checkOutButton = (Button) findViewById(R.id.check_out_button);
			checkOutButton.setVisibility(View.GONE);
		}
	}
	
	//Saves the current shift to the database.
	private void saveShift(){
		DatabaseHelper db = new DatabaseHelper(this);
		
		db.addShift(currentShift);
		removeData(CHECK_IN_KEY);
		currentShift = null;
	}
	
}
