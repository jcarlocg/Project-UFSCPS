package com.example.ufscps;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class GPSAdapter extends FragmentActivity implements LocationListener {
	
	private LocationManager locationManager;
	private LatLng userLatLng;
	
	public GPSAdapter(Context c) {
        locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        
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
       
	    locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 100, 10, this);
	    
	    userLatLng = new LatLng(-27.6012, -48.5197); // coordenadas da reitoria
	}

	public LatLng getUserLatLng() {
		return userLatLng;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
	}

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
