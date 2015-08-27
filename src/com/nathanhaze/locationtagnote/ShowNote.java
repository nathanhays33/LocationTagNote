package com.nathanhaze.locationtagnote;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ShowNote extends  FragmentActivity {

	float zoom = 15;
	
	GoogleMap mMap;
	
	private TextView note, date;
	
	static DatabaseHandler db;
	
	Note currentNote;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_note);
		
		 /***** Setup Map ****/	
	   	 mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	     setMap();
	     
	    /**** Database *****/
	    db = new DatabaseHandler(this);   
	   
	    note = (TextView)findViewById(R.id.note);
        date = (TextView)findViewById(R.id.date);
        Bundle b = getIntent().getExtras();  
        
        int i = 9999;
        if(b != null){
      	  i = b.getInt("index");
        }
        
        if( i != 9999){
		    currentNote = db.getNote(i);
		    note.setText(currentNote.getMessage()); 
		    date.setText(currentNote.getDate());
		    addMarker();
        }else{
        	note.setText("There was a problem getting the message");
        } 
	}

	private void setMap() {
	    	int type = 0;
	    	/*
	    	if(sharedPrefs == null){
	    		sharedPrefs = PreferenceManager
		                .getDefaultSharedPreferences(this);
	    	}
	    	try{
	    	type = Integer.parseInt(sharedPrefs.getString("maptype", "0"));  //null
	        } catch (NullPointerException e) {
	        type = 0;
	        }
	    	*/
	    	 if (mMap != null) {
		    	 mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	    		    if (mMap != null) {
	    		    	switch(type){
	    		    	case(0):
	    		    		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    		    		break;
	    		    	case(1):
	    		    		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	    		    		break;
	    		    	case(2):
	    		    		mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    		    		break;
	    		    	case(3):
	    		    		mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
	    		    		break;
	    		    	}
	    		    	mMap.setMyLocationEnabled(true); // null
	    		    }
	    		}
	}
	
    public void share(View v){    	
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Geo Note " + currentNote.getDate());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, currentNote.getMessage());
    	startActivity(Intent.createChooser(sharingIntent, "Share Geo Note"));
    }
	
	
	public void addMarker(){
        mMap.addMarker(new MarkerOptions()
        .position(new LatLng(currentNote.getLatitude() ,currentNote.getLongitude()))
        .title("Your Note Location") // AM
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_green))
        );	
        
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(currentNote.getLatitude() ,currentNote.getLongitude()) , zoom) );

	}
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.showlist:
    	    goShowList();
        break;
	}
            return(true);  
            
	}

	public void goShowList(){
	    Intent list_intent = new Intent(this, ShowList.class);
        startActivity(list_intent);
	}
	
	public boolean onMenuItemClick(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.showlist:
    	    Intent list_intent = new Intent(this, ShowList.class);
            startActivity(list_intent);
        break;
    
	}
            return(true);  
	}
}
