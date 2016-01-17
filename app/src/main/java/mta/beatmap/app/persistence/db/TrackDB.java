package mta.beatmap.app.persistence.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wolf on 16/01/2016.
 * But taken from https://developer.android.com/training/basics/data-storage/databases.html
 */

public class TrackDB extends SQLiteOpenHelper {

    private static final String UINT_TYPE = " INTEGER";
    private static final String ID_TYPE = " INTEGER";
    private static final String STRING_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_SEQUENCES =
            "CREATE TABLE " + DBContract.SequenceTable.TABLE_NAME + " (" +
                    DBContract.SequenceTable._ID + ID_TYPE + " PRIMARY KEY," +
                    DBContract.SequenceTable.COLUMN_NAME_TRACK_ID + ID_TYPE + COMMA_SEP +
                    DBContract.SequenceTable.COLUMN_NAME_BARS + UINT_TYPE + COMMA_SEP +
                    DBContract.SequenceTable.COLUMN_NAME_METER_NUMERATOR + UINT_TYPE + COMMA_SEP +
                    DBContract.SequenceTable.COLUMN_NAME_METER_DENOMINATOR + UINT_TYPE + COMMA_SEP +
                    DBContract.SequenceTable.COLUMN_NAME_BPM + UINT_TYPE +
            " )";

    private static final String SQL_CREATE_TRACKS =
            "CREATE TABLE " + DBContract.TrackTable.TABLE_NAME + " (" +
                    DBContract.TrackTable._ID + ID_TYPE + " PRIMARY KEY," +
                    DBContract.TrackTable.COLUMN_NAME_TITLE + STRING_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.SequenceTable.TABLE_NAME + ";" +
            "DROP TABLE IF EXISTS " + DBContract.TrackTable.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "BeatMap.db";

    public TrackDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        System.out.println("SQL CREATE ENTRIES stuff");
        System.out.println(SQL_CREATE_SEQUENCES);
        System.out.println(SQL_CREATE_TRACKS);

        db.execSQL(SQL_CREATE_SEQUENCES);
        db.execSQL(SQL_CREATE_TRACKS);
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
