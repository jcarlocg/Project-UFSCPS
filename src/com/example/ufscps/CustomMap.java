package com.example.ufscps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

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
		
		/**==========================================================================================================
	     ** Associate the UI map to the GoogleMap map object
	     **=========================================================================================================*/
	    map = ((SupportMapFragment) f.findFragmentById(R.id.fragment1)).getMap();
	    
	    context = c;
	    
	    /**==========================================================================================================
	     ** Setup and configure the Marker that represents the user in the map
	     **========================================================================================================*/
		if(userMarker!=null) userMarker.remove();
	    userMarker = map.addMarker(new MarkerOptions()
	    .position(new LatLng(0,0))
	    .title("You are here")
	    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marvin))
	    .snippet("Your current location"));
	    
	    /**==========================================================================================================
	     ** Setup the click listener in the info window of the room marker
	     **========================================================================================================*/
	    map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
	    	@Override
	    	public void onInfoWindowClick(Marker marker) {
	    		if(marker.equals(roomMarker)) {
	    			InfoWidowOnClick();	
	    		}
	    	}
	    });
	}
	
    /**==========================================================================================================
     ** @return the Marker object that represents the user.
     **========================================================================================================*/
	public Marker getUserMarker() {
		return userMarker;
	}
	
    /**==========================================================================================================
     ** @param room : a Room type object that contains all the information about the searched room
     **========================================================================================================*/
	public void InsertRoomMarker(Room room) {
		searchedRoom = room;
		
		roomMarker = map.addMarker(new MarkerOptions()
        .position(new LatLng(room.getLatitude(), room.getLongitude()))
        .title(room.getRoomId())
        .snippet(room.getRoomBuilding() + " - " + room.getRoomFloor())
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.room_marker)));
	}
	
    /**==========================================================================================================
     ** Moves the map to a certain position with a certain zoom
     ** @param position : a LatLng structure that holds the position that the map will move to.
     ** @param zoom     : the zoom that the camera of the map will stay; 16 shows the entire UFSC terrain;
     **========================================================================================================*/
	public void moveMapTo(LatLng position, float zoom) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
	}
    
	
    
    /**==========================================================================================================
     ** This is the implementation Haversine Distance Algorithm between two places
     ** @param lat1 : a Double value that represents the latitude of the first position
     ** @param lat2 : a Double value that represents the latitude of the second position
     ** @param lon1 : a Double value that represents the longitude of the first position
     ** @param lon2 : a Double value that represents the longitude of the second position
     **
     ** @return distance: the distance between the two positions
     **
     **
     **	R = earth’s radius (mean radius = 6,371km)
	 ** [DELTAlat = lat2- lat1
	 ** [DELTA]long = long2- long1
	 ** a = sin²([DELTA]lat/2) + cos(lat1).cos(lat2).sin²([DELTA]long/2)
	 ** c = 2.atan2([SQRT]a, [SQRT](1-a))
	 ** d = R.c
     **
     **========================================================================================================*/
    int CalculateDistance(double lat1, double lat2, double lon1, double lon2) {
        // TODO Auto-generated method stub
        final int R = 6371; // Radius of the earth
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
    
    /**==========================================================================================================
     ** Called when the user clicks in the info window of the roomMarker
     **========================================================================================================*/
    public void InfoWidowOnClick(){
    	
    	if(searchedRoom == null) return;
    	
	    /**==========================================================================================================
	     ** The string with all the information about the searched room
	     **========================================================================================================*/
        String infoString = searchedRoom.getRoomDescription() + "\n\n" +
        					"Prédio: " + searchedRoom.getRoomBuilding() + "\n" +
                            "Andar: " +  searchedRoom.getRoomFloor() + "\n\n" +
        					"Distância: " + CalculateDistance(searchedRoom.getLatitude(), userMarker.getPosition().latitude, searchedRoom.getLongitude(), userMarker.getPosition().longitude) + " metros";
    	
         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

 	    /**==========================================================================================================
 	     ** Configuration of the alert window
 	     **========================================================================================================*/	     
		alertDialogBuilder
			.setTitle(searchedRoom.getRoomId())
			.setMessage(infoString)
			.setCancelable(false)
			.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					dialog.cancel();
				}
			  });

		/**==========================================================================================================
		 ** Create and show the alert dialog previously configurated
		 **========================================================================================================*/
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
  	}
}
