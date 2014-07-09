package com.example.ufscps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;



public class MapActivity extends FragmentActivity {
	/** MAP ACTIVITY **/
    /** Called when the map is loaded */
	
	Context context = this;
	private Room searchedRoom; // object with all the info about the room the user wanna go
	GPSAdapter myGPS; // object that manages the GPS locations of the device
	CustomMap myMap; // manages the google map
	
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
     ** Go to configuration Screen
     **========================================================================================================*/
	public void OnConfig(View view) {
	        Intent intent = new Intent(context, ConfigActivity.class);
	    	startActivity(intent);
    }
    
}
