package com.example.ufscps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;



public class MapActivity extends FragmentActivity {
	
	Context context = this;
	private Room searchedRoom;
	GPSAdapter myGPS;
	CustomMap myMap;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        //===========================================================================================================
        //============================		 		INTENT DATA				  =======================================
        //===========================================================================================================
        
        final String roomId = getIntent().getStringExtra("ROOM_ID");
        searchedRoom = new Room(context, roomId);
        
        //===========================================================================================================
        //============================		 		INTERFACE INIT			  =======================================
        //===========================================================================================================
  
        //===========================================================================================================
        //============================		 	     MAP STUFF				  =======================================
        //===========================================================================================================
        
        myMap = new CustomMap(context, getSupportFragmentManager());

        myMap.InsertRoomMarker(searchedRoom);
        
        //===========================================================================================================
        //============================		 		GPS HANDLER				  =======================================
        //===========================================================================================================
        
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
	
    private Handler uiCallback = new Handler () {
        public void handleMessage (Message msg) {
        		myMap.moveMapTo(myGPS.getUserLatLng(), 16);
        		
        }
    };
    
}
