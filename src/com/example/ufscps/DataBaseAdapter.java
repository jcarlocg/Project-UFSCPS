package com.example.ufscps;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListAdapter;

public class DataBaseAdapter 
{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;
    
	protected Cursor cursor;
	protected ListAdapter adapter;
	
    public DataBaseAdapter(Context context) 
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }   
    
    /**==========================================================================================================
     ** create the database
     **========================================================================================================*/
    public DataBaseAdapter createDatabase() throws SQLException 
    { 
        try 
        {
            mDbHelper.createDataBase();
        } 
        catch (IOException mIOException) 
        {
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    /**==========================================================================================================
     ** open the database
     **========================================================================================================*/
    public DataBaseAdapter open() throws SQLException 
    {
        try 
        {
            mDbHelper.openDataBase();
            mDb = mDbHelper.getReadableDatabase();
        } 
        catch (SQLException mSQLException) 
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }
    
    /**==========================================================================================================
     ** Check if the Database exists in the directory
     **========================================================================================================*/
    public boolean CheckExistDatabase() {
    	if(mDbHelper.checkDataBase()) {
    		return true;
    	}
    	else {
    		return false;
    	}

    }

    /**==========================================================================================================
     ** close the database
     **========================================================================================================*/
    public void close() 
    {
        mDbHelper.close();
    }
    
    /**==========================================================================================================
     ** search the database for entries that match the txtString
     **
     ** @param  txtString : string with the _id of the room that we want to retrieve from the database 
     ** @return a cursor with all the entries that match txtString
     **========================================================================================================*/
    public Cursor Search(String txtString) {
    	 
		cursor = mDb.rawQuery("select * from Location where _id like '%" + txtString + "%'", null);
		
		return cursor;
    }
}
