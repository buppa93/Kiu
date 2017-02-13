package com.domain.my.giuseppe.kiu.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by User on 09/09/2016.
 */
public class DatabaseListKiuer {

    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private static final String DATABASE_TABLE= "kiuer";

    public static final String KEY_ID = "id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_FEEDBACK = "feedback";
    public static final String KEY_LOCAL_PLACE = "local_place";
    public static final String KEY_LOCAL_ADDRESS = "local_address";
    public static final String KEY_LOCAL_DATE = "local_date";
    public static final String KEY_LOCAL_TIME = "local_time";
    public static final String KEY_PRICE = "price";
    public static final String KEY_NOTE = "note";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_VISIBILITY = "visibility";
    public static final String KEY_ACCEPTED = "accepted";

    public DatabaseListKiuer(Context context) {
        this.context = context;
    }

    public DatabaseListKiuer open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public DatabaseListKiuer read() throws  SQLException{
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getReadableDatabase();
        return this;
    }

    public void close() {databaseHelper.close();}

    public ContentValues createContentValues(int id, String token, String feedback, String local_place,
                                             String local_address, String local_date, String local_time,
                                             String price, String note, String latitude, String longitude, int visibility, int accepted)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_TOKEN, token);
        values.put(KEY_FEEDBACK,feedback);
        values.put(KEY_LOCAL_PLACE, local_place);
        values.put(KEY_LOCAL_ADDRESS, local_address);
        values.put(KEY_LOCAL_DATE, local_date);
        values.put(KEY_LOCAL_TIME, local_time);
        values.put(KEY_PRICE, price);
        values.put(KEY_NOTE, note);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_VISIBILITY,visibility);
        values.put(KEY_ACCEPTED, accepted);

        return values;
    }

    public ContentValues createContentValuesNoId( String token, String feedback, String local_place,
                                                  String local_address, String local_date, String local_time,
                                                  String price, String note, String latitude, String longitude, int visibility, int accepted)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, token);
        values.put(KEY_FEEDBACK,feedback);
        values.put(KEY_LOCAL_PLACE, local_place);
        values.put(KEY_LOCAL_ADDRESS, local_address);
        values.put(KEY_LOCAL_DATE, local_date);
        values.put(KEY_LOCAL_TIME, local_time);
        values.put(KEY_PRICE, price);
        values.put(KEY_NOTE, note);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_VISIBILITY,visibility);
        values.put(KEY_ACCEPTED, accepted);

        return values;
    }

    //TODO implementare metodi di aggiornamento di un singolo attributo in un record
    //create kiuer
    public long createKiuer(int id, String token, String feedback, String local_place,
                            String local_address, String local_date, String local_time,
                            String price, String note, String latitude, String longitude, int visibility, int accepted){
        ContentValues initial_values = createContentValues(id, token, feedback, local_place, local_address, local_date, local_time,
                price, note, latitude, longitude, visibility, accepted);
        return sqLiteDatabase.insertOrThrow(DATABASE_TABLE, null, initial_values);
    }

    //update kiuer
    public boolean updateKiuer(int id, String token, String feedback, String local_place,
                               String local_address, String local_date, String local_time,
                               String price, String note, String latitude, String longitude, int visibility, int accepted){
        ContentValues update_values = createContentValues(id, token, feedback, local_place, local_address, local_date, local_time,
                price, note, latitude, longitude, visibility, accepted);
        return sqLiteDatabase.update(DATABASE_TABLE, update_values, KEY_ID + "=" + id, null ) > 0;
    }

    //delete kiuer
    public boolean deleteKiuer(int id){
        return sqLiteDatabase.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    //fetch all kiuer
    public Cursor fetchAllKiuer(){
        return sqLiteDatabase.rawQuery("select rowid _id,* from kiuer", null);
    }

    //count kiuer request
    public Cursor countRequestSeenFalse(){
        final String sql = "SELECT COUNT(*) FROM kiuer where visibility = 0";

        Cursor c = sqLiteDatabase.rawQuery(sql, null);

        return c;
    }

    //set all request seen to 1
    public void setRequestToTrue(int id){
        ContentValues cv = new ContentValues();
        cv.put(KEY_VISIBILITY,1);
        sqLiteDatabase.update(DATABASE_TABLE,cv,KEY_VISIBILITY + " = 0 and " + KEY_ID + " = " + id,null);
    }

    //set request state accepted
    public void setRequestAccepted(int id){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ACCEPTED,1);
        sqLiteDatabase.update(DATABASE_TABLE, cv, KEY_ACCEPTED + " = 0 and " + KEY_ID + " = " + id, null);
    }

    //fetch kiuer filter by accepted column
    public Cursor fetchAcceptedKiuer(){
        return sqLiteDatabase.rawQuery("select rowid _id,* from kiuer where accepted = 1", null);
    }

    /*//fetch kiuer filter by a string
    public Cursor fetchKiuersByMail(String filter) {
        Cursor mCursor = sqLiteDatabase.query(true, DATABASE_TABLE, new String[] {
                        KEY_ID,KEY_NAME, KEY_FEEDBACK, KEY_LOCATION, KEY_DATE, KEY_HOUR, KEY_PRICE},
                KEY_NAME + " like '%"+ filter + "%'", null, null,null, null, null);

        return mCursor;
    }*/
}
