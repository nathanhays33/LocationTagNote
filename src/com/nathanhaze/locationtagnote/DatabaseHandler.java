package com.nathanhaze.locationtagnote;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "notesManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "notes";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date_number";
	private static final String MESSAGE = "message";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT," + MESSAGE + " TEXT," + 
				LATITUDE + " DOUBLE," +  LONGITUDE + " DOUBLE" + 
				")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new note
	void addNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, note.getDate()); 
		values.put(MESSAGE, note.getMessage()); 
		values.put(LATITUDE, note.getLatitude()); 
		values.put(LONGITUDE, note.getLongitude()); 
		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single note
	Note getNote(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
			    KEY_DATE, MESSAGE, LATITUDE, LONGITUDE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Note note = new Note(Integer.parseInt(cursor.getString(0)),
			 cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4));
		// return note
		return note;
	}
	
	// Getting All Notes
	public List<Note> getAllNotes() {
		List<Note> noteList = new ArrayList<Note>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Note note = new Note();
				note.setID(Integer.parseInt(cursor.getString(0)));
				note.setDate(cursor.getString(1));
				note.setMessage(cursor.getString(2));
				note.setLatitude(cursor.getDouble(3));
				note.setLongitude(cursor.getDouble(4));
				// Adding note to list
				noteList.add(note);
			} while (cursor.moveToNext());
		}

		// return note list
		return noteList;
	}

	// Updating single note
	public int updateNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, note.getDate());
		values.put(MESSAGE, note.getMessage());
		values.put(LATITUDE, note.getLatitude());
		values.put(LONGITUDE, note.getLongitude());
		// updating row
		return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(note.getID()) });
	}

	// Deleting single note
	public void deleteNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(note.getID()) });
		db.close();
	}
	
	public void deleteNote(int note) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(note) });
		db.close();
	}

	// Getting notes Count
	public int getNoteCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		return cursor.getCount();
	}

}
