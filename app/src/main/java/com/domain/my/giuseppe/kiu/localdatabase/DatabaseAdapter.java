package com.domain.my.giuseppe.kiu.localdatabase;

/**
 * Created by giuseppe on 21/06/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.domain.my.giuseppe.kiu.utils.PlaceView;

public class DatabaseAdapter
{
    @SuppressWarnings("unused")
    private static final String LOG_TAG = DatabaseAdapter.class.getSimpleName();

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    // Database fields
    private static final String DATABASE_TABLE = "history";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_COUNT = "count";

    public DatabaseAdapter(Context context) {this.context = context;}

    public DatabaseAdapter open() throws SQLException
    {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {dbHelper.close();}

    public ContentValues createContentValues(int id, String name, String address, double lat,
                                             double lng, int value)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_NAME, name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_LAT, lat);
        values.put(KEY_LNG, lng);
        values.put(KEY_COUNT, value);

        return values;
    }

    public ContentValues createContentValuesNoId(String name, String address, double lat,
                                             double lng, int value)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_LAT, lat);
        values.put(KEY_LNG, lng);
        values.put(KEY_COUNT, value);

        return values;
    }

    //create a contact
    public long createPlace(ContentValues cv)
    {return database.insertOrThrow(DATABASE_TABLE, null, cv);}

    public PlaceView isPlaceAlreadyExist(String address)
    {
        Cursor c = fetchPlacesByFilter(address);
        c.moveToFirst();
        if(c.getCount()==0)
            return new PlaceView();

        else
            return new PlaceView(c.getInt(c.getColumnIndex(KEY_ID)),
                    c.getString(c.getColumnIndex(KEY_NAME)),
                    c.getString(c.getColumnIndex(KEY_ADDRESS)),
                    c.getDouble(c.getColumnIndex(KEY_LAT)), c.getDouble(c.getColumnIndex(KEY_LNG)),
                    c.getInt(c.getColumnIndex(KEY_COUNT)));
    }

    //update a contact
    public boolean updatePlace(ContentValues cv)
    {
        PlaceView pw = isPlaceAlreadyExist(cv.getAsString(KEY_ADDRESS));
        if(pw.isEmpty())
        {
            createPlace(cv);
            return false;
        }
        else
        {
            ContentValues updateValues = createContentValues(pw.getId(), pw.getTitle(), pw.getAddress(),
                    pw.getLat(), pw.getLng(), pw.getCount()+1);
            return database.update(DATABASE_TABLE, updateValues, KEY_ID + "=" + updateValues.getAsInteger(KEY_ID), null)
                    > 0;
        }
    }

    public boolean update(ContentValues cv)
    {
            return database.update(DATABASE_TABLE, cv, KEY_ID + "=" + cv.getAsInteger(KEY_ID), null)
                    > 0;
    }

    //delete a contact
    public boolean deletePlace(String name)
    {return database.delete(DATABASE_TABLE, KEY_NAME + "=" + name, null) > 0;}

    //fetch all contacts
    public Cursor fetchAllPlaces()
    {
        return database.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_LAT, KEY_LNG,
                KEY_COUNT}, null, null, null, null, DatabaseAdapter.KEY_COUNT+" DESC");
    }

    //fetch contacts filter by a string
    public Cursor fetchPlacesByFilter(String filter)
    {
        String[] from = {KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_LAT, KEY_LNG, KEY_COUNT};
        String where = KEY_ADDRESS + "=?";
        String[] whreArgs = {filter};

        Cursor mCursor = database.query(DATABASE_TABLE, from, where, whreArgs, null, null, null);

        return mCursor;
    }

    public ContentValues fetchPlacesById(int id)
    {
        String[] from = {KEY_ID, KEY_NAME, KEY_ADDRESS, KEY_LAT, KEY_LNG, KEY_COUNT};
        String where = KEY_ID + "=?";
        String[] whreArgs = {String.valueOf(id)};

        Cursor mCursor = database.query(DATABASE_TABLE, from, where, whreArgs, null, null, null);
        ContentValues content = null;
        if(mCursor.moveToNext())
        {
            content = createContentValues(mCursor.getInt(mCursor.getColumnIndex(KEY_ID)),
                    mCursor.getString(mCursor.getColumnIndex(KEY_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(KEY_ADDRESS)),
                    mCursor.getDouble(mCursor.getColumnIndex(KEY_LAT)),
                    mCursor.getDouble(mCursor.getColumnIndex(KEY_LNG)),
                    mCursor.getInt(mCursor.getColumnIndex(KEY_COUNT)));
        }

        return content;
    }
}
