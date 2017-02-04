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
                    "username text not null, " +
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
                    "mail text not null, " +
                    "feedback float null, " +
                    "address text not null, " +
                    "date text not null, " +
                    "hour text not null, " +
                    "rate float null, " +
                    "distance float null, " +
                    "seen integer " +
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
