package com.example.ufscps;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class GPSAdapter extends FragmentActivity implements LocationListener {
	
	private LocationManager locationManager;
	private LatLng userLatLng;
	private Marker userMarker;
	
	public GPSAdapter(Context c, Marker userMarker) {
		
        /**==========================================================================================================
         ** Set up the locationManager to get the position of the device
         **========================================================================================================*/
        locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        
        /** CAL METHOD requestLocationUpdates 
        ** @param provider		:  the name of the provider with which to register 
        ** @param minTime)    	:  the minimum time interval for notifications, 
        **                         in milliseconds. This field is only used as a hint 
        **                         to conserve power, and actual time between location 
        **                         updates may be greater or lesser than this value. 
        ** @param minDistance 	:  the minimum distance interval for notifications, in meters 
        ** @param listener		:  a {#link LocationListener} whose onLocationChanged(Location) 
        **                         method will be called for each location update 
        **/
        
	    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 100, 10, this);
	    
	    /**==========================================================================================================
         ** UFSC default coordinates set as default for initialization
         *  these coordinates will remain the user coordinates in case the GPS module of the device is inactive
         **========================================================================================================*/
	    userLatLng = new LatLng(-27.6012, -48.5197);
	    
	    /**==========================================================================================================
         ** This class must know the userMarker in the map to update easily the position of the user in the map 
         **========================================================================================================*/
	    this.userMarker = userMarker;
	}

	
	 /**==========================================================================================================
     ** @return the user position in the format LatLng used by the google maps
     **========================================================================================================*/
	public LatLng getUserLatLng() {
		return userLatLng;
	}
	
	 /**==========================================================================================================
     ** called everytime the location of the device changes
     ** @param location : a Location object that have all the information about the current location
     **========================================================================================================*/
	@Override
	public void onLocationChanged(Location location) {
		userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		userMarker.setPosition(userLatLng);
	}

	 /**==========================================================================================================
     ** Other methods of LocationListener
     **========================================================================================================*/
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
