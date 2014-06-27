package com.example.ufscps;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;



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
        
        myMap = new CustomMap(getSupportFragmentManager());

        myMap.InsertRoomMarker(searchedRoom);
        
        //===========================================================================================================
        //============================		 		GPS HANDLER				  =======================================
        //===========================================================================================================
        
        myGPS = new GPSAdapter(context);
        
        Thread timer = new Thread() {
            public void run () {
                for (;;) {
                    // do stuff in a separate thread
                    uiCallback.sendEmptyMessage(0);
                    try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}    // sleep for 3 seconds
                }
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
