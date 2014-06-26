package com.example.ufscps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;



public class MapActivity extends FragmentActivity implements LocationListener{
	
	Context context = this;
	TextView room_Id, roomDescription, roomBuilding, roomFloor;
	TextView userLatitude, userLongitude, roomLatitude, roomLongitude;
	TextView distance;
	private LocationManager locationManager;
	private double userLat, userLong;
	private Room searchedRoom;
	
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
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        /* CAL METHOD requestLocationUpdates */
        
        // Parameters :
        //   First(provider)    :  the name of the provider with which to register 
        //   Second(minTime)    :  the minimum time interval for notifications, 
        //                         in milliseconds. This field is only used as a hint 
        //                         to conserve power, and actual time between location 
        //                         updates may be greater or lesser than this value. 
        //   Third(minDistance) :  the minimum distance interval for notifications, in meters 
        //   Fourth(listener)   :  a {#link LocationListener} whose onLocationChanged(Location) 
        //                         method will be called for each location update 
       
	    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 1000, 10, this);
	      
	    final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	      @Override
	      public void run() {
	        RefreshLocation();
	        handler.postDelayed(this, 200);
	      }
	    }, 150);
	}
	
    public void RefreshLocation() {

    }

    
    
	@Override
	public void onLocationChanged(Location location) {
		userLat = location.getLatitude();
		userLong = location.getLongitude();
		
		/*
    	LatLng userCoordinates = new LatLng(userLat, userLong);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoordinates, 16));
		

        
		double d = CalculateDistance(userLat, searchedRoom.getLatitude(), userLong, searchedRoom.getLongitude())*1000;
		*/
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	


}
