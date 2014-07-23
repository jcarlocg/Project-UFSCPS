package com.example.ufscps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class ConfigActivity extends Activity {

	DataBaseAdapter mDbHelper;
	EditText distanceText;
	Switch alarmStateSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.activity_config); // show activity_list on the screen
	     
        /**==========================================================================================================
         ** initialization of the database
         **========================================================================================================*/
        
        mDbHelper = new DataBaseAdapter(this);
        
        mDbHelper.createDatabase();   
        mDbHelper.open();
        
        /**==========================================================================================================
         ** get and initialize the elements of the screen
         **========================================================================================================*/
        
        distanceText = (EditText) findViewById(R.id.distTxtField);
        alarmStateSwitch = (Switch) findViewById(R.id.alarmSwitch);
        
        distanceText.setText("" + mDbHelper.GetAlarmConfigDistance());
        alarmStateSwitch.setChecked(mDbHelper.GetAlarmConfigState());
        
	}
	
	
	 /**==========================================================================================================
     ** Update all the values of the configuration screen to the database
     ** then returns to the previous activity
     **========================================================================================================*/
	public void OnSave(View view) {
		String tempDist = distanceText.getText().toString();
		
		String alarmState = (alarmStateSwitch.isChecked() ? "On" : "Off");
		
		if(tempDist.length() > 0) {
			mDbHelper.updateConfig(alarmState, tempDist);
		}
		
		onBackPressed();
	}

}
