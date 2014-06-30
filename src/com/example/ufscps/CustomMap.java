package com.example.ufscps;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomMap extends FragmentActivity{

	private GoogleMap map;
	private Marker userMarker;
	
	public CustomMap(FragmentManager f) {
	    map = ((SupportMapFragment) f.findFragmentById(R.id.fragment1)).getMap();
	}
	
	public void InsertRoomMarker(Room room) {
        map.addMarker(new MarkerOptions()
        .position(new LatLng(room.getLatitude(), room.getLongitude()))
        .title(room.getRoomId())
        .snippet(room.getRoomBuilding() + " - " + room.getRoomFloor())
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.skyrim)));
	}
	
	public void moveMapTo(LatLng userPosition, float zoom) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, zoom));

		if(userMarker!=null) userMarker.remove();
	    userMarker = map.addMarker(new MarkerOptions()
	    .position(userPosition)
	    .title("You are here")
	    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marvin))
	    .snippet("Your current location"));
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
    double CalculateDistance(double lat1, double lat2, double lon1, double lon2) {
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
