package com.example.ufscps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements LocationListener{
	
	Context context = this;
	TextView room_Id, roomDescription, roomBuilding, roomFloor;
	TextView userLatitude, userLongitude, roomLatitude, roomLongitude;
	TextView distance;
	private LocationManager locationManager;
	private double userLat, userLong;
	private Room searchedRoom;
	
	private GoogleMap map;
	
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
        /*
        room_Id = (TextView) findViewById(R.id.roomId);
        roomDescription = (TextView) findViewById(R.id.roomDescription);
        roomBuilding = (TextView) findViewById(R.id.roomBuilding);
        roomFloor = (TextView) findViewById(R.id.roomFloor);
        roomLatitude = (TextView) findViewById(R.id.roomLatitude);
        roomLongitude = (TextView) findViewById(R.id.roomLongitude);
        
        userLatitude = (TextView) findViewById(R.id.userLatitude);
        userLongitude = (TextView) findViewById(R.id.userLongitude);
        
        distance = (TextView) findViewById(R.id.distance);
        
        
        room_Id.setText(searchedRoom.getRoomId());
        roomDescription.setText(searchedRoom.getRoomDescription());
    	roomLatitude.setText("RLat: " + searchedRoom.getLatitude());
    	roomLongitude.setText("RLong: " + searchedRoom.getLongitude());
        roomBuilding.setText(searchedRoom.getRoomBuilding());
        roomFloor.setText(searchedRoom.getRoomFloor());
        */
        //===========================================================================================================
        //============================		 	     MAP STUFF				  =======================================
        //===========================================================================================================
        
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1)).getMap();
        
        Marker room = map.addMarker(new MarkerOptions()
        .position(new LatLng(searchedRoom.getLatitude(), searchedRoom.getLongitude()))
        .title("User")
        .snippet("I'm here!")
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.skyrim)));
        

        
        
        
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
		
    	LatLng userCoordinates = new LatLng(userLat, userLong);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoordinates, 16));
		
        Marker user = map.addMarker(new MarkerOptions()
        .position(userCoordinates)
        .title(searchedRoom.getRoomId())
        .snippet(searchedRoom.getRoomBuilding() + " - " + searchedRoom.getRoomFloor())
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.marvin_gif)));
        
		double d = CalculateDistance(userLat, searchedRoom.getLatitude(), userLong, searchedRoom.getLongitude())*1000;
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
	
	/**
	 * This is the implementation Haversine Distance Algorithm between two places
	 * @author ananth
	 *  R = earth’s radius (mean radius = 6,371km)
	    [DELTAlat = lat2- lat1
	    [DELTA]long = long2- long1
	    a = sin²([DELTA]lat/2) + cos(lat1).cos(lat2).sin²([DELTA]long/2)
	    c = 2.atan2([SQRT]a, [SQRT](1-a))
	    d = R.c
	 *
	 */
    private double CalculateDistance(double lat1, double lat2, double lon1, double lon2) {
        // TODO Auto-generated method stub
        final int R = 6371; // Radious of the earth
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
                   Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;
               
        return distance; 
    }
     
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

}
