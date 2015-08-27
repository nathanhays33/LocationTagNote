package com.nathanhaze.locationtagnote;

import java.io.File;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MapView extends FragmentActivity {

	float zoom = 15;
	GoogleMap mMap;
	static DatabaseHandler db;
	
	public static List<Note> Notes;
	
	int index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		
		 /***** Setup Map ****/	
	   	 mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
	     setMap();
	     
	    /**** Database *****/
	    db = new DatabaseHandler(this);   
        Notes = db.getAllNotes();       

        for (Note cn : Notes) {
           addMarker(cn.getLatitude(), cn.getLongitude(), cn.getMessage(), cn.getID());
       }
        
        mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
			   index = Integer.parseInt(marker.getSnippet());
			    
			  ((Button)findViewById(R.id.button1)).setVisibility(View.VISIBLE);	
			  return false;
			}
        });
        
        
	}
	
	
	public void addMarker(Double lat, Double lng, String title, int i){
        mMap.addMarker(new MarkerOptions()
        .position(new LatLng(lat ,lng))
        .title(title) // AM
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_green))
        .snippet(Integer.toString(i))
        );	
    
	}
	

	public void openNote(View v){
	    Intent list_intent = new Intent(this, ShowNote.class);
     	list_intent.putExtra("index", index);
        startActivity(list_intent);
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
