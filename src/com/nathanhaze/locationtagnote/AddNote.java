package com.nathanhaze.locationtagnote;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class AddNote  extends FragmentActivity implements LocationListener, OnMenuItemClickListener {

	float zoom = 15;
	
	GoogleMap mMap;
	
	static LocationManager locationManager;
	static Location location;
	static LocationListener locationListener;
	static Boolean isGPSEnabled = false;
	static Double lat =0.0, lng =0.0;
	
	private EditText note;
	
	static DatabaseHandler db;
	
	public static List<Note> Notes; // needs to be removed
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
	 /***** Setup Map ****/	
   	 mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
     setMap();
     
     
     /***** Get user location ****/
     locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
     // Register the listener with the Location Manager to receive location updates
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        
        location = getLastKnownLocation();
	    // getting GPS status

        if(!isGPSEnabled){
        	showSettingsAlert();
        }
        
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()) , zoom) );
        
        note = (EditText)findViewById(R.id.note);
        note.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard();
                }

            }
        });
        
        /**** Database *****/
        db = new DatabaseHandler(this);      
        // Reading all Trips
        Notes = db.getAllNotes();       
 /*
        for (Note cn : Notes) {
             Log.d("NOTES", cn.getMessage());
        }
        */
	}
	
	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(note.getWindowToken(), 0);
    }
	
	 public void showSettingsAlert(){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	      
	        // Setting Dialog Title
	        alertDialog.setTitle("GPS is settings");
	  
	        // Setting Dialog Message
	        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
	  
	        // Setting Icon to Dialog
	        //alertDialog.setIcon(R.drawable.delete);
	  
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                startActivity(intent);
	            }
	        });
	  
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });
	  
	        // Showing Alert Message
	        alertDialog.show();
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

	public void saveNote(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("You want to save this Geo Note?")
	          .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
                      addNote();
                      goShowList();
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	               }
	           });        
		AlertDialog alertDialog = builder.create();
		alertDialog.show();	
	}
	
	public void addNote(){
   		String message = ((TextView)findViewById(R.id.note)).getText().toString();
	    db.addNote(new Note(getTimeStamp(), message, lat, lng));
	    Toast.makeText(this, "The note was inserted into the database!",
	              Toast.LENGTH_LONG).show();
	}
	
	public String getTimeStamp() {
	     String timeStamp= new SimpleDateFormat("MM-dd' 'HH:mm").format(Calendar.getInstance().getTime());
		 return timeStamp;	
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
	@Override
	public boolean onMenuItemClick(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.showlist:
    	    Intent list_intent = new Intent(this, ShowList.class);
            startActivity(list_intent);
        break;
    
	}
            return(true);  
	}

	@Override
	public void onLocationChanged(Location location) {
		lat = (double) (location.getLatitude());
	    lng = (double) (location.getLongitude());
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng) , zoom) );
	}

	
    @Override
    public void onProviderDisabled(String provider) {
      Toast.makeText(this, "GPS was Disabled",
          Toast.LENGTH_SHORT).show();
    }
    
	
	@Override
	public void onProviderEnabled(String provider) {
	    Toast.makeText(this, "Enabled new provider " + provider,
	            Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private Location getLastKnownLocation() {
	    List<String> providers = locationManager.getProviders(true);
	    Location bestLocation = null;
	    for (String provider : providers) {
	        Location l = locationManager.getLastKnownLocation(provider);
	        if (l == null) {
	            continue;
	        }
	        if (bestLocation == null
	                || l.getAccuracy() < bestLocation.getAccuracy()) {
	            bestLocation = l;
	        }
	    }
	    if (bestLocation == null) {
	    	if(isGPSEnabled){
	    		bestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    	}
	    	else{
	    	   bestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    	}
	   
	    }
	    return bestLocation;
	}
	   
}
