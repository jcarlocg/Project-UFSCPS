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
	TestAdapter mDbHelper; // interface com a database
	protected EditText searchText; //campo de busca na GUI
	protected ListView roomListView;
	Cursor cursor = null;
	SimpleCursorAdapter adapter;
	OnKeyListener keyListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list); // mostra a tela activityList
       
        searchText = (EditText) findViewById(R.id.editText);
        roomListView = (ListView) findViewById(R.id.listView);
        
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
        
        mDbHelper = new TestAdapter(this);  //inicializa a interface coom a database
        
        mDbHelper.createDatabase(); // copia a database que está na pasta assets pra pasta final no dispositivo   
        mDbHelper.open(); // abre a database
        
        searchText.setOnKeyListener(keyListener);
        roomListView.setOnItemClickListener(clkListener);
    }
    
    @SuppressWarnings("deprecation")
	public void OnSearch(View view) {
    	String srcStr = searchText.getText().toString();
    	if(srcStr.length() > 0) {
	    	cursor = mDbHelper.Search(srcStr);
	  
	    	String[] arrayColumns = new String[]{"_id","building"};
	    	int[] arrayViewIDs = new int[]{R.id.textViewRoomId,R.id.textViewBuilding};
	    	
	    	adapter = new SimpleCursorAdapter(this, R.layout.location_list_item, cursor, arrayColumns, arrayViewIDs);
	    	roomListView.setAdapter(adapter);
    	}
    }
}
