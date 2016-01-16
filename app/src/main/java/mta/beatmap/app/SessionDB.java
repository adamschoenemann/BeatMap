package mta.beatmap.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wolf on 16/01/2016.
 * But taken from https://developer.android.com/training/basics/data-storage/databases.html
 */

public class SessionDB extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String UINT_TYPE = " INTEGER";
    private static final String ID_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SessionDBContract.SessionsTable.TABLE_NAME + " (" +
                    SessionDBContract.SessionsTable._ID + ID_TYPE + " PRIMARY KEY," +
                    SessionDBContract.SessionsTable.COLUMN_NAME_SESSION_ID + ID_TYPE + COMMA_SEP +
                    SessionDBContract.SessionsTable.COLUMN_NAME_BEAT_ID + ID_TYPE + COMMA_SEP +
                    SessionDBContract.SessionsTable.COLUMN_NAME_BARS + UINT_TYPE + COMMA_SEP +
                    SessionDBContract.SessionsTable.COLUMN_NAME_METER_NUMERATOR + UINT_TYPE + COMMA_SEP +
                    SessionDBContract.SessionsTable.COLUMN_NAME_METER_DENOMINATOR + UINT_TYPE + COMMA_SEP +
                    SessionDBContract.SessionsTable.COLUMN_NAME_BPM + UINT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SessionDBContract.SessionsTable.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public SessionDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        System.out.println("SQL CREATE ENTRIES stuff");
        System.out.println(SQL_CREATE_ENTRIES);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
