package com.domain.my.giuseppe.kiu.localdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by giuseppe on 21/06/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String DATABASE_NAME = "kiu.db";
    private static final int DATABASE_VERSION = 2;

    // Lo statement SQL di creazione del database
    private static final String DATABASE_CREATE_HISTORY =
            "create table history " +
                    "(" +
                        "id integer primary key autoincrement, " +
                        "name text not null, " +
                        "address text not null, " +
                        "lat real not null, " +
                        "lng real not null, " +
                        "count integer not null" +
                    ");";
    // Lo statement SQL di creazione del database
    private static final String DATABASE_CREATE_HELPER =
            "CREATE TABLE IF NOT EXISTS helper " +
                    "(" +
                    "_id integer primary key autoincrement, "+
                    "username_profile text not null,"+
                    "username_helper text not null, " +
                    "date text not null, " +
                    "address text not null, " +
                    "time not null, " +
                    "money float null " +
                    ");";

    //Donato----
    private static final String DATABASE_CREATE_KIUER =
            "CREATE TABLE IF NOT EXISTS kiuer " +
                    "(" +
                    "id integer primary key autoincrement, " +
                    "token text not null, " +
                    "feedback text null, " +
                    "local_place text not null, " +
                    "local_address text not null, " +
                    "local_date text not null, " +
                    "local_time text not null, " +
                    "price text null, " +
                    "note text , " +
                    "latitude text not null, " +
                    "longitude text not null, " +
                    "visibility integer, " +
                    "accepted integer" +
                    ");";
    //End Donato
    // Costruttore
    public DatabaseHelper(Context context)
    {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    // Questo metodo viene chiamato durante la creazione del database
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(DATABASE_CREATE_HISTORY);
        database.execSQL(DATABASE_CREATE_HELPER);
        database.execSQL(DATABASE_CREATE_KIUER);//Donato
    }

    // Questo metodo viene chiamato durante l'upgrade del database, ad esempio quando viene incrementato il numero di versione
    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion )
    {
        database.execSQL("DROP TABLE IF EXISTS history");
        database.execSQL("DROP TABLE IF EXISTS helper");
        database.execSQL("DROP TABLE IF EXISTS kiuer");//Donato
        onCreate(database);
    }


    /*public void onDowngrade(SQLiteDatabase database,
                            int oldVersion,
                            int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS history");
        database.execSQL("DROP TABLE IF EXISTS helper");
        onCreate(database);
    }*/
}
