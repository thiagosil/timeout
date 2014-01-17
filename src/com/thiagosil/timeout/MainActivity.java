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
import android.widget.Toast;

public class MainActivity extends Activity {

	private Date punchInTime, punchOutTime;
	
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

	private void initComponents() {
		if(isPunchedIn())
		{
			showCheckInTime();
		}
		switchButton();
	}

	public void checkIn(View view){
		checkIn(new Date());
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, R.string.check_in, duration);
		toast.show();
	}
	
	public void checkOut(View view){
		checkOut(new Date());
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, R.string.check_out, duration);
		toast.show();
	}

	private void checkIn(Date date) {
		punchInTime = date;
		saveData(CHECK_IN_KEY, date);
		showCheckInTime();
		switchButton();
	}

	private void checkOut(Date date) {
		punchOutTime = date;
		saveData(CHECK_OUT_KEY, date);
		removeData(CHECK_IN_KEY);
		showCheckInTime();
		switchButton();
	}
	
	private void showCheckInTime() {
		TextView start_time = (TextView) findViewById(R.id.start_time);
		if (isPunchedIn())
		{
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			punchInTime= new Date(sharedPref.getLong(CHECK_IN_KEY, 0));
			SimpleDateFormat dt = new SimpleDateFormat("hh:mm", java.util.Locale.getDefault()); 
			start_time.setText(dt.format(punchInTime));
		}
		else
		{
			start_time.setText("");
		}
		
		
	}

	private void saveData(String key, Date date) {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putLong(key, date.getTime());
		editor.commit();
	}
	
	private void removeData(String key) {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.remove(key);
		editor.commit();
	}
	
	private boolean isPunchedIn(){
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		if (sharedPref.contains(CHECK_IN_KEY)){
			return true;
		}
		return false;
	}
	
	private void switchButton()
	{
		if (isPunchedIn()){
			Button punchIn = (Button) findViewById(R.id.check_in_button);
			punchIn.setVisibility(View.GONE);
			Button punchOut = (Button) findViewById(R.id.check_out_button);
			punchOut.setVisibility(View.VISIBLE);
		}
		else {
			Button punchIn = (Button) findViewById(R.id.check_in_button);
			punchIn.setVisibility(View.VISIBLE);
			Button punchOut = (Button) findViewById(R.id.check_out_button);
			punchOut.setVisibility(View.GONE);
		}
	}
	
}
