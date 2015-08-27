package com.nathanhaze.locationtagnote;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;


public class ShowList extends Activity implements OnMenuItemClickListener {

	private CustomAdapter adapter;
	
	
	static ProgressDialog pd;
    static  Handler handle = new Handler();
		
	DatabaseHandler dh; 
	ListView lv;
	ArrayList<Note> Notes;
	
    static boolean metric = false;
	
    
    public static EditText message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
         setContentView(R.layout.list);
         
         dh = new DatabaseHandler(this);  
         Notes = (ArrayList<Note>) dh.getAllNotes();
         
         lv = (ListView) findViewById(R.id.listview);
         adapter = new CustomAdapter(this,
                 R.id.listview,
                 Notes);
         lv.setAdapter(adapter);     
         lv.setClickable(true);
         lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
	     public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {  
			showMessage(arg2 +1);
		  }
          });  
          
          lv.setOnItemLongClickListener(new OnItemLongClickListener() {

          public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                      int pos, long id) {
                  // TODO Auto-generated method stub
              showYesNo(pos + 1);
                  return true;
              }
          }); 
          
          if(Notes.size() >0){
			  ((Button)findViewById(R.id.button1)).setVisibility(View.VISIBLE);	
          }
    
	}

	public void mapView(View v){
	    Intent list_intent = new Intent(this, MapView.class);
        startActivity(list_intent);
	}
	
	
	public void updateList(){
		List<Note> newItems = dh.getAllNotes();
		adapter.clear();
		adapter.addAll(newItems);
		adapter.notifyDataSetChanged();
		dh.close();
		
        if(newItems.size() == 0){
			  ((Button)findViewById(R.id.button1)).setVisibility(View.GONE);	
        }
		
	}
	public void showDialog(String title, String message){
        AlertDialog a = new AlertDialog.Builder(this).create();
        a.setTitle(title);
        a.setMessage(message);

        a.setButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            return;
          }
        });
        a.show();
	}
	
	public void showYesNo(final int i){

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Would you like to delete this Geo Note?")
	    //Swap Yes and NO
	          .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	             	  dh.deleteNote(i);
	                  updateList();
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	               }
	           });        
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
	
	public void showMessage(int i){
	    Intent list_intent = new Intent(this, ShowNote.class);
     	list_intent.putExtra("index", i);
        startActivity(list_intent);
	}
	
	public void addNote(View v){
	    Intent list_intent = new Intent(this, AddNote.class);
        startActivity(list_intent);
	}
	

	
    public void share(int index){	
    	Intent shareIntent = new Intent();
    	shareIntent.setAction(Intent.ACTION_SEND);	    	
    	shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
        "My Note:  "	+ 	"\n" +
        "Date: " + dh.getNote(index)._date + "\n" +
        "Message: "	+ dh.getNote(index)._message + "\n" +
        "Location: " +
        "http://maps.google.com/maps?&z=10&q=" 
    	+ dh.getNote(index)._latitude + "," + dh.getNote(index)._longitude + "&ll=" + dh.getNote(index)._latitude  + "," 
    	+ dh.getNote(index)._longitude	
        		);
    	startActivity(Intent.createChooser(shareIntent, "Share Image"));
    	
    	//Uri recording = Uri.fromFile(sendFile);
    }
    
    public void doSomethign(){
    
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
   				this);
		message = new EditText(this);
		
	    alertDialogBuilder.setTitle("Disclaimer:");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("This selected data will be uploaded."
						+ "Nothing that will id you will be uploaded. You cannot reverse this. You can use the menu to visit the site.")
				.setCancelable(false)
				.setView(message)
				.setPositiveButton("Upload WITH location",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
					}
				  })
				  .setNeutralButton("Upload WITHOUT location", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	               }   
	            })
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
		AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
		alertDialog.show();
	}

	   @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		   
		// create alert dialog
	    	final Intent mainintent = new Intent(this, ShowList.class);

		   
	    	switch (item.getItemId()) {
   
	    	}
	                return(true);

	        
	    }
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
		//	getMenuInflater().inflate(R.menu.listmenu, menu);
			return true;
		}
		
	    @Override
	    public void onStart() {
	      super.onStart();
	    //  EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    }

	    @Override
	    public void onStop() {
	      super.onStop();
	   //   EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	    }
	    
		   public void menu(View v){
		    	PopupMenu popup = new PopupMenu(this, v);
		        MenuInflater inflater = popup.getMenuInflater();
		    //    inflater.inflate(R.menu.listmenu, popup.getMenu());
		        popup.setOnMenuItemClickListener(this);
		        popup.show();
	    }
		
  
		
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			   
			// create alert dialog
		    	final Intent mainintent = new Intent(this, ShowList.class);

			   
		    	switch (item.getItemId()) {

		    	
        	}
               return(true);  
		}
		

}
