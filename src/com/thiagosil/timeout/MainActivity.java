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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Date punchInTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initComponents();
	}

	private void initComponents() {
		if(isPunchedIn())
		{
			showCheckInTime();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void checkIn(View view){
		checkIn(new Date());
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, R.string.check_in, duration);
		toast.show();
	}
	

	private void checkIn(Date date) {
		punchInTime = date;
		String key = "checkIn";
		saveData(key, date);
		showCheckInTime();
	}
	
	private void showCheckInTime() {
		if (punchInTime == null)
		{
			SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
			punchInTime= new Date(sharedPref.getLong("checkIn", 0));
		}
		
		SimpleDateFormat dt = new SimpleDateFormat("hh:mm", java.util.Locale.getDefault()); 
		TextView start_time = (TextView) findViewById(R.id.start_time);
		start_time.setText(dt.format(punchInTime));
	}

	private void saveData(String key, Date date) {
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		editor.putLong(key, date.getTime());
		editor.commit();
	}
	
	private boolean isPunchedIn(){
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		if (sharedPref.getLong("checkIn", 0) != 0){
			return true;
		}
		return false;
	}
}
