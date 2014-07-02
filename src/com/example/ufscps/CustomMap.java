package com.example.ufscps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import java.lang.Number;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomMap extends FragmentActivity{

	private Context context;
	private GoogleMap map;
	private Marker userMarker, roomMarker;
	private Room searchedRoom = null;
	
	public CustomMap(Context c, FragmentManager f) {
	    map = ((SupportMapFragment) f.findFragmentById(R.id.fragment1)).getMap();
	    
	    context = c;
	    
		if(userMarker!=null) userMarker.remove();
	    userMarker = map.addMarker(new MarkerOptions()
	    .position(new LatLng(0,0))
	    .title("You are here")
	    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marvin))
	    .snippet("Your current location"));
	    
	    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
	    	@Override
	    	public void onInfoWindowClick(Marker marker) {
	    		if(marker.equals(roomMarker)) {
	    			InfoWidowOnClick();	
	    		}
	    	}
	    });
	}
	
	public Marker getUserMarker() {
		return userMarker;
	}
	
	public void InsertRoomMarker(Room room) {
		searchedRoom = room;
		
		roomMarker = map.addMarker(new MarkerOptions()
        .position(new LatLng(room.getLatitude(), room.getLongitude()))
        .title(room.getRoomId())
        .snippet(room.getRoomBuilding() + " - " + room.getRoomFloor())
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.room_marker)));
	}
	
	public void moveMapTo(LatLng userPosition, float zoom) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, zoom));
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
    int CalculateDistance(double lat1, double lat2, double lon1, double lon2) {
        // TODO Auto-generated method stub
        final int R = 6371; // Radious of the earth
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + 
                   Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c * 1000;
               
        return (int) (distance - (distance%1)); 
    }
     
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }
    
    //=-=-==-=-=-=-=-=-
    
    public void InfoWidowOnClick(){
    	
    	if(searchedRoom == null) return;
    	
        String infoString = searchedRoom.getRoomDescription() + "\n\n" +
        					"Prédio: " + searchedRoom.getRoomBuilding() + "\n" +
                            "Andar: " +  searchedRoom.getRoomFloor() + "\n\n" +
        					"Distância: " + CalculateDistance(searchedRoom.getLatitude(), userMarker.getPosition().latitude, searchedRoom.getLongitude(), userMarker.getPosition().longitude);
    	
         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

  			// set title
  			alertDialogBuilder.setTitle(searchedRoom.getRoomId());

  			// set dialog message
  			alertDialogBuilder
  				.setMessage(infoString)
  				.setCancelable(false)
  				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
  					public void onClick(DialogInterface dialog,int id) {
  						// if this button is clicked, close
  						// current activity
  						dialog.cancel();
  					}
  				  });

  				// create alert dialog
  				AlertDialog alertDialog = alertDialogBuilder.create();

  				// show it
  				alertDialog.show();
  	}
}
