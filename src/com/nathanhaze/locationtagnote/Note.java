package com.nathanhaze.locationtagnote;

public class Note {
	
	//private variables
	int _id;
	public String _date;
	public String _message;
	public double _latitude;
	public double _longitude;
	// Empty constructor
	public Note(){
		
	}
	// constructor
	public Note(int id, String date, String message, 
			double latitude, double longitude){
		this._id = id;
		this._date = date;
		this._message = message;
		this._latitude = latitude;
		this._longitude = longitude;
	}
	
	// constructor
	public Note(String date, String message, 
			double latitude, double longitude){
		this._date = date;
		this._message = message;
		this._latitude = latitude;
		this._longitude = longitude;
	}
	
	
	// constructor
	public Note(int id, float distance, int maxspeed, String date, String message){
		this._id = id;
		this._date = date;
		this._message = message;
	}
	
	// getting ID
	public int getID(){
		return this._id;
	}
	
	public String getDate(){
		return _date;
	}
	
	public void setDate(String date){
		this._date = date;
	}
	
	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	public double getLatitude(){
		return  _latitude;
	}
	
	public void setLatitude (double latitude){
		this._latitude= latitude;
	}
	
	public double getLongitude(){
		return  _longitude;
	}
	
	public void setLongitude (double longitude){
		this._longitude= longitude;
	}
	
	public String getMessage (){
		return  _message;
	}
	
	public void setMessage (String message){
		this._message = message;
	}
	
}
