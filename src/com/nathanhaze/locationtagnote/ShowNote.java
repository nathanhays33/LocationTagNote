package com.nathanhaze.locationtagnote;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;


public class ShowNote extends  FragmentActivity implements LocationListener {

	float zoom = 15;
	
	GoogleMap mMap;
	
	private TextView note, date;
	
	static DatabaseHandler db;
	
	static LocationManager locationManager;
	static Location location;
	static LocationListener locationListener;
	static Boolean isGPSEnabled = false;
	static Double lat =0.0, lng =0.0;
	
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
        
        /***** Get user location ****/
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates
           
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
           isGPSEnabled = locationManager
                   .isProviderEnabled(LocationManager.GPS_PROVIDER);
           
           location = getLastKnownLocation();
           
   		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Chunkfive.otf");
  		((Button)findViewById(R.id.button1)).setTypeface(tf);
  		((Button)findViewById(R.id.button2)).setTypeface(tf);
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
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Note " + currentNote.getMessage() + " Coordinates: " + currentNote.getLatitude() + "," + currentNote.getLongitude());
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
	
	  public void getDirections(View v){
		  
	     if(!isGPSEnabled){
	           showSettingsAlert();
	      }
	     else{
	    	 directions();
	     }
    }
	  
	  public void directions(){
		  if(lat == 0){
	          Location here = getLastKnownLocation();       
		  	  lat = here.getLatitude();
	  	      lng = here.getLongitude();    
			}
	        if(lat !=0){ 
		        String url = "http://maps.google.com/maps?saddr="+Double.toString(lat)+","+Double.toString(lng)+"&daddr="+ currentNote.getLatitude() +","+ currentNote.getLongitude()  +"&mode=driving";

	  	        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
	  	        Uri.parse(url));
	  	        startActivity(intent);  
	        }
	        else{
	      	    Toast.makeText(this, "I do not know where you are? Try enabling GPS or try again in a few moments",
	  	        Toast.LENGTH_LONG).show();
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

	@Override
	public void onLocationChanged(Location location) {
		lat = (double) (location.getLatitude());
	    lng = (double) (location.getLongitude());
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng) , zoom) );
	}

	
    @Override
    public void onProviderDisabled(String provider) {

    }
    
	
	@Override
	public void onProviderEnabled(String provider) {
	    directions();
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
	             directions();
	            dialog.cancel();
	            }
	        });
	  
	        // Showing Alert Message
	        alertDialog.show();
	    }
	 
		protected void onStop() {
		    super.onStop();  
		    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		}
		
	    @Override
	    public void onStart() {
	      super.onStart();
	      EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    }
}
