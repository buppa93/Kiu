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
    public static final String KEY_MAIL = "mail";
    public static final String KEY_FEEDBACK = "feedback";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_DATE = "date";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_RATE = "rate";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_SEEN = "seen";

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

    public ContentValues createContentValues(int id, String mail,float feedback, String address, String date,
                                             String hour, float rate, float distance, int seen)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_MAIL, mail);
        values.put(KEY_FEEDBACK,feedback);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_DATE, date);
        values.put(KEY_HOUR, hour);
        values.put(KEY_RATE, rate);
        values.put(KEY_DISTANCE,distance);
        values.put(KEY_SEEN,seen);

        return values;
    }

    public ContentValues createContentValuesNoId( String mail,float feedback, String address, String date,
                                             String hour, float rate, float distance, int seen)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_MAIL, mail);
        values.put(KEY_FEEDBACK,feedback);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_DATE, date);
        values.put(KEY_HOUR, hour);
        values.put(KEY_RATE, rate);
        values.put(KEY_DISTANCE,distance);
        values.put(KEY_SEEN,seen);

        return values;
    }

    //TODO implementare metodi di aggiornamento di un singolo attributo in un record
    //create kiuer
    public long createKiuer(int id, String mail,float feedback, String address, String date,
                            String hour, float rate, float distance, int seen){
        ContentValues initial_values = createContentValues(id, mail, feedback, address, date, hour, rate, distance, seen);
        return sqLiteDatabase.insertOrThrow(DATABASE_TABLE, null, initial_values);
    }

    //update kiuer
    public boolean updateKiuer(int id, String mail,float feedback, String address, String date,
                               String hour, float rate, float distance, int seen){
        ContentValues update_values = createContentValues(id, mail, feedback, address, date, hour, rate, distance, seen);
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
        final String sql = "SELECT COUNT(*) FROM kiuer where seen = 0";

        Cursor c = sqLiteDatabase.rawQuery(sql, null);

        return c;
    }

    //set all request seen to 1
    public void setRequestToTrue(int id){
        ContentValues cv = new ContentValues();
        cv.put(KEY_SEEN,1);
        sqLiteDatabase.update(DATABASE_TABLE,cv,KEY_SEEN + " = 0 and " + KEY_ID + " = " + id,null);
    }

    //fetch kiuer filter by a string
    public Cursor fetchKiuersByMail(String filter) {
        Cursor mCursor = sqLiteDatabase.query(true, DATABASE_TABLE, new String[] {
                        KEY_ID,KEY_MAIL, KEY_FEEDBACK, KEY_ADDRESS, KEY_DATE, KEY_HOUR, KEY_RATE, KEY_DISTANCE },
                KEY_MAIL + " like '%"+ filter + "%'", null, null,null, null, null);

        return mCursor;
    }
}
