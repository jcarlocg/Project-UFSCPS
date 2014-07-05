package com.example.ufscps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Main extends Activity {
	/** MAIN ACTIVITY **/
    /** Called when the activity is first created. */
	
	
	Context context = this;
	DataBaseAdapter mDbHelper; 
	protected EditText searchText;
	protected ListView roomListView;
	Cursor cursor = null;
	SimpleCursorAdapter adapter;
	OnKeyListener keyListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list); // show activity_list on the screen
       
        /**==========================================================================================================
         ** get the elements of the screen
         **========================================================================================================*/
       
        searchText = (EditText) findViewById(R.id.editText);
        roomListView = (ListView) findViewById(R.id.listView);
        
        /**==========================================================================================================
         ** Config of the key and click listeners that will be used by the EditText and listView 
         **========================================================================================================*/
        
        keyListener =  new OnKeyListener() {
        	public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                	OnSearch(null);
                	return true;
                }
				return false;
            }
        };
        
        OnItemClickListener clkListener = new OnItemClickListener() {  
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {  
                Intent intent = new Intent(context, MapActivity.class);
                Cursor cursor = (Cursor) adapter.getItem(position);
            	intent.putExtra("ROOM_ID", cursor.getString(cursor.getColumnIndex("_id")));
            	startActivity(intent);
            }
        };
        
        /**==========================================================================================================
         ** initialization of the database
         **========================================================================================================*/
        
        mDbHelper = new DataBaseAdapter(this);
        
        mDbHelper.createDatabase();   
        mDbHelper.open();
        
        /**==========================================================================================================
         ** Set the key and click listeners EditText and listView
         **========================================================================================================*/
        searchText.setOnKeyListener(keyListener);
        roomListView.setOnItemClickListener(clkListener); 
    }
    
    /**
     * when the user types enter or presses the button Ok, this method is called and execute the entire search routine
     * @param view - represent the current view.
     */    
    
    @SuppressWarnings("deprecation")
	public void OnSearch(View view) {
    	String srcStr = searchText.getText().toString(); 	// string typed on the screen
    	if(srcStr.length() > 0) { 							// to assure that the string isn't empty
	    	cursor = mDbHelper.Search(srcStr); 				// search the database for the room id and stores the result in cursor
	  
	    	String[] arrayColumns = new String[]{"_id","building"};
	    	int[] arrayViewIDs = new int[]{R.id.textViewRoomId,R.id.textViewBuilding};
	    	
	    	adapter = new SimpleCursorAdapter(this, R.layout.location_list_item, cursor, arrayColumns, arrayViewIDs);
	    	roomListView.setAdapter(adapter);	// SimpleCursorAdapter is used to show the query results in the listView 
    	}
    }
}
