package com.example.ufscps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;



public class MapActivity extends FragmentActivity {
	/** MAP ACTIVITY **/
    /** Called when the map is loaded */
	
	Context context = this;
	DataBaseAdapter mDbHelper; 
	private Room searchedRoom; // object with all the info about the room the user wanna go
	GPSAdapter myGPS; // object that manages the GPS locations of the device
	CustomMap myMap; // manages the google map
	Thread alarm;
	AlertDialog alertDialog;
	boolean threadStatus = true;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map); // show activity_map on the screen
        
        /**==========================================================================================================
         **	Get the intent Data
         **========================================================================================================*/
        
        final String roomId = getIntent().getStringExtra("ROOM_ID");
        searchedRoom = new Room(context, roomId);
        
        /**==========================================================================================================
         ** initialization of the database
         **========================================================================================================*/
        
        mDbHelper = new DataBaseAdapter(this);
        
        mDbHelper.createDatabase();   
        mDbHelper.open();
        
        /**==========================================================================================================
         **	Setup the Map and insert roomMarker
         **========================================================================================================*/
        
        myMap = new CustomMap(context, getSupportFragmentManager());

        myMap.InsertRoomMarker(searchedRoom);
        
        /**==========================================================================================================
         **	Setup the GPS and update the location once
         **========================================================================================================*/
        
        myGPS = new GPSAdapter(context, myMap.getUserMarker());
        
        Thread timer = new Thread() {        	
            public void run () {
                    try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}    // sleep for 3 seconds
                    
                    uiCallback.sendEmptyMessage(0);
                    this.interrupt();
            }
        };
        timer.start();
        
        /**==========================================================================================================
         **	Create the thread that will handle the alarm
         **========================================================================================================*/
                
        alarm = new Thread() {       	
            public void run () {
                try {
                	Thread.sleep(3000);
					while(true) {
						if(threadStatus) alarmCallback.sendEmptyMessage(0);
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   // sleep for 3 seconds
            }
        };
        alarm.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		threadStatus = false;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		threadStatus = true;
	}
	
    /**==========================================================================================================
     **	Handler called by the thread timer only once 3 seconds after the creation of the thread
     **========================================================================================================*/
    @SuppressLint("HandlerLeak")
	private Handler uiCallback = new Handler () {
        public void handleMessage (Message msg) {
        		myMap.moveMapTo(myGPS.getUserLatLng(), 16);
        		
        }
    };
    
    /**==========================================================================================================
     **	Handler called by the thread Alarm to check and display the warning
     **========================================================================================================*/
    @SuppressLint("HandlerLeak")
	private Handler alarmCallback = new Handler () {
        public void handleMessage (Message msg) {
        	if(mDbHelper.GetAlarmConfigState() && mDbHelper.GetAlarmConfigDistance() >= myMap.getDistance()) {
        		try {
        	 	    /**==========================================================================================================
        	 	     ** Configuration of the sounds of the alarm
        	 	     **========================================================================================================*/	     
         			
        		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        		    final Ringtone r = RingtoneManager.getRingtone(context, notification);
        		    r.play();
        		    
        		    /**==========================================================================================================
        		     ** Prevents pop-up windows to stack up in the screen
        		     **========================================================================================================*/
        		    
        		    if( alertDialog != null && alertDialog.isShowing() ) return;
        		    
        			/**==========================================================================================================
        		     ** The string with all the information in the pop-up window
        		     **========================================================================================================*/
        	         String infoString = "Você está a " + myMap.getDistance() + " metros da sala " + searchedRoom.getRoomId() + ".";
        	    	
        	         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        	 	    /**==========================================================================================================
        	 	     ** Configuration of the alert window
        	 	     **========================================================================================================*/	     
        			alertDialogBuilder
        				.setTitle("Quase la...")
        				.setMessage(infoString)
        				.setCancelable(false)
        				.setPositiveButton("Ok, Desligar alarme!",new DialogInterface.OnClickListener() {
        					public void onClick(DialogInterface dialog,int id) {
        						// if this button is clicked, close
        						// current activity and stop the ringtone
        						r.stop();
        						mDbHelper.updateConfig("Off", "" + mDbHelper.GetAlarmConfigDistance());
        						dialog.cancel();
        					}
        				  });

        			/**==========================================================================================================
        			 ** Create and show the alert dialog previously configurated
        			 **========================================================================================================*/
        			
        			alertDialog = alertDialogBuilder.create();
        			alertDialog.show();
        			
        		} catch (Exception e) {
        		    e.printStackTrace();
        		}
        	}	
        }
    };
    
    /**==========================================================================================================
     ** Go to configuration Screen
     **========================================================================================================*/
	public void OnConfig(View view) {
	        Intent intent = new Intent(context, ConfigActivity.class);
	    	startActivity(intent);
    }   
}
