package com.example.ufscps;

import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListAdapter;

public class TestAdapter 
{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;
    
	protected Cursor cursor;
	protected ListAdapter adapter;

    public TestAdapter(Context context) 
    {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public TestAdapter createDatabase() throws SQLException 
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

    public TestAdapter open() throws SQLException 
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
    
    public boolean CheckExistDatabase() {
    	if(mDbHelper.checkDataBase()) {
    		return true;
    	}
    	else {
    		return false;
    	}

    }

    public void close() 
    {
        mDbHelper.close();
    }
     
     public Cursor Search(String txtString) {
    	 
		cursor = mDb.rawQuery("select * from Location where _id like '%" + txtString + "%'", null);
		
		return cursor;
     }
}
