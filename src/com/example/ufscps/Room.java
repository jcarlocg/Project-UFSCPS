package com.example.ufscps;

import android.content.Context;
import android.database.Cursor;

public class Room {
	private String roomId;
	private String roomDescription;
	private String roomFloor;
	private String roomBuilding;
	private double latitude;
	private double longitude;

	private TestAdapter mDbHelper;
	private Cursor cursor;
	
	public Room(Context context, String _id) {
        mDbHelper = new TestAdapter(context);  //inicializa a interface com a database
        mDbHelper.open(); // abre a database
        
        if(_id != null) {
	        cursor = mDbHelper.Search(_id);
	        
	    	if (cursor.moveToFirst()) {
	    		roomId = cursor.getString(cursor.getColumnIndex("_id"));
	    		roomDescription = cursor.getString(cursor.getColumnIndex("description"));
	    		latitude  = cursor.getDouble(cursor.getColumnIndex("latitude"));
	    		longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
	    		roomFloor = cursor.getString(cursor.getColumnIndex("floor"));
	    		roomBuilding = cursor.getString(cursor.getColumnIndex("building"));
	        }
    	}
	}
	
	public String getRoomId() {
		return roomId;
	}
	public String getRoomDescription() {
		return roomDescription;
	}
	public String getRoomFloor() {
		return roomFloor;
	}
	public String getRoomBuilding() {
		return roomBuilding;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
}
