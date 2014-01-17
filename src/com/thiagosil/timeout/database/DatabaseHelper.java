package com.thiagosil.timeout.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thiagosil.timeout.Shift;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "timeout.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_SHIFTS = "shifts";

	//Colum names
	private static final String KEY_ID = "id";
	private static final String KEY_CHECK_IN = "check_in";
	private static final String KEY_CHECK_OUT = "check_out";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SHIFTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_CHECK_IN + " BIGINTEGER,"
				+ KEY_CHECK_OUT + " BIGINTEGER" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new RuntimeException("How did we get here?");
	}


	//CRUD Operations

	// Adding new shift
	public void addShift(Shift shift) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CHECK_IN, shift.getCheckIn().getTime()); 
		values.put(KEY_CHECK_OUT, shift.getCheckOut().getTime()); // Contact Phone

		// Inserting Row
		db.insert(TABLE_SHIFTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single shifts
	Shift getShift(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SHIFTS, new String[] { KEY_ID,
				KEY_CHECK_IN, KEY_CHECK_OUT }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Date checkIn =  new Date(Long.parseLong(cursor.getString(1)));
		Date checkOut = new Date(Long.parseLong(cursor.getString(2)));

		Shift shift = new Shift(Integer.parseInt(cursor.getString(0)),
				checkIn, checkOut);
		// return shift
				return shift;
	}

	public List<Shift> getAllShifts() {
		List<Shift> shiftList = new ArrayList<Shift>();

		String selectQuery = "SELECT  * FROM " + TABLE_SHIFTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Shift shift = new Shift();
				shift.setId(Integer.parseInt(cursor.getString(0)));
				shift.setCheckIn(Long.parseLong(cursor.getString(1)));
				shift.setCheckOut(Long.parseLong(cursor.getString(2)));

				shiftList.add(shift);
			} while (cursor.moveToNext());
		}


		return shiftList;
	}

	public int updateShift(Shift shift){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CHECK_IN, shift.getCheckIn().getTime());
		values.put(KEY_CHECK_OUT, shift.getCheckOut().getTime());

		// updating row
		return db.update(TABLE_SHIFTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(shift.getId()) });
	}

	public void deleteShift(Shift shift){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SHIFTS, KEY_ID + " = ?", new String[] { String.valueOf(shift.getId()) });
		db.close();
	}
}
