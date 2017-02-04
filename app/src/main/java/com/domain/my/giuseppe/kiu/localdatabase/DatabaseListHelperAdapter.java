package com.domain.my.giuseppe.kiu.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ivasco92 on 31/08/16.
 */
public class DatabaseListHelperAdapter {
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private static final String DATABASE_TABLE= "helper";

    public static final String KEY_ID="_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_DATE = "date";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TIME = "time";
    public static final String KEY_MONEY = "money";

    public DatabaseListHelperAdapter(Context context)
    {
        this.context = context;
    }

    public DatabaseListHelperAdapter open() throws SQLException
    {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    private ContentValues createContentValues(String username, String date, String address, String time, String money ) {
        ContentValues values = new ContentValues();
        values.put( KEY_USERNAME, username );
        values.put( KEY_DATE, date );
        values.put( KEY_ADDRESS, address );
        values.put( KEY_TIME, time );
        values.put( KEY_MONEY, money);

        return values;
    }

    //TODO implementare metodi di aggiornamento di un singolo attributo in un record
    //create a contact
    public long createContact(String username, String date, String address, String time, String money)
    {
        ContentValues initialValues =  createContentValues(username, date, address, time, money );
        return database.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

    //update a contact
    public boolean updateContact( int id, String username, String date, String address, String time, String money ) {
        ContentValues updateValues = createContentValues(username, date, address, time, money );
        return database.update(DATABASE_TABLE, updateValues, KEY_USERNAME + "=" + username, null) > 0;
    }

    //delete a contact
    public boolean deleteContact(String id) {
        return database.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    //fetch all contacts
    public Cursor fetchAllContacts() {
       //return database.rawQuery( "select rowid _id,* from helper", null);
       // return database.rawQuery( "select * from helper", null);
        return database.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_USERNAME, KEY_DATE, KEY_ADDRESS, KEY_TIME,
                KEY_MONEY}, null, null, null, null, null);
        // return database.rawQuery(DATABASE_TABLE, new String[] { KEY_USERNAME, KEY_DATE, KEY_ADDRESS, KEY_TIME, KEY_MONEY});
        //return database.rawQuery(DATABASE_TABLE, new String[] { KEY_USERNAME, KEY_DATE, KEY_ADDRESS, KEY_TIME, KEY_MONEY}, null, null, null, null, null);
       // return database.query(DATABASE_TABLE, new String[] { KEY_USERNAME, KEY_DATE, KEY_ADDRESS, KEY_TIME, KEY_MONEY}, null, null, null, null, null);
    }

    //fetch contacts filter by a string
    public Cursor fetchContactsByUsername(String filter) {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
                KEY_ID, KEY_USERNAME, KEY_DATE, KEY_ADDRESS, KEY_TIME, KEY_MONEY },
                KEY_USERNAME + " like '%"+ filter + "%'", null, null, null, null, null);

        return mCursor;
    }

}
