package mta.beatmap.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Wolf on 16/01/2016.
 */
public class SessionDBHandler {

    private SQLiteDatabase db;
    private final String[] PROJECTION = {
            DBContract.SessionsTable.COLUMN_NAME_BPM,
            DBContract.SessionsTable.COLUMN_NAME_METER_NUMERATOR,
            DBContract.SessionsTable.COLUMN_NAME_METER_DENOMINATOR,
            DBContract.SessionsTable.COLUMN_NAME_BARS
    };
    private final String TABLE_NAME = DBContract.SessionsTable.TABLE_NAME;
    private final String WHERE_SESSION = DBContract.SessionsTable.COLUMN_NAME_SESSION_ID + "=?";

    private final String BPM_NAME = DBContract.SessionsTable.COLUMN_NAME_BPM;
    private final String METER_NUMERATOR_NAME = DBContract.SessionsTable.COLUMN_NAME_METER_NUMERATOR;
    private final String METER_DENOMINATOR_NAME = DBContract.SessionsTable.COLUMN_NAME_METER_DENOMINATOR;
    private final String BARS_NAME = DBContract.SessionsTable.COLUMN_NAME_BARS;


    public SessionDBHandler(Context context) {
        SessionDB mDbHelper = new SessionDB(context);
        db = mDbHelper.getWritableDatabase();
    }

    public void upsert(String session_id, String[] beat_placeholder) {
        delete(session_id);
        insert(session_id, beat_placeholder);
    }

    private void delete(String session_id) {
        db.delete(TABLE_NAME, WHERE_SESSION, new String[]{session_id});
    }

    private void insert(String session_id, String[] beat_placeholder) {

        for (int i = 0; i < beat_placeholder.length; i++) {
            ContentValues values = extractContent(beat_placeholder[i]);

            values.put(DBContract.SessionsTable.COLUMN_NAME_SESSION_ID, session_id);
            values.put(DBContract.SessionsTable.COLUMN_NAME_BEAT_ID, i);

            db.insert(TABLE_NAME, null, values);
        }
    }

    private ContentValues extractContent(String beat_placeholder) {
        ContentValues values = new ContentValues();

        // TODO extract actual values from beat object
        values.put(BPM_NAME, 120);
        values.put(METER_NUMERATOR_NAME, 5);
        values.put(METER_DENOMINATOR_NAME, 8);
        values.put(BARS_NAME, 10);

        return values;
    }

    public String[] getSession(String session_id) {

        String[] selectionVals = {session_id};

        Cursor c = db.query(
                TABLE_NAME,    // The table to query
                PROJECTION,    // The columns to return
                WHERE_SESSION, // The string for the WHERE clause
                selectionVals, // The values for the WHERE clause
                null,          // don't group the rows
                null,          // don't filter by row groups
                null           // Don't sort: No sort order
        );

        int bpm_index = c.getColumnIndexOrThrow(BPM_NAME);
        int meter_numerator_index = c.getColumnIndexOrThrow(METER_NUMERATOR_NAME);
        int meter_denominator_index = c.getColumnIndexOrThrow(METER_DENOMINATOR_NAME);
        int bars_index = c.getColumnIndexOrThrow(BARS_NAME);

        int bpm;
        int meter_numerator;
        int meter_denominator;
        int bars;

        int pos = -1;
        c.moveToPosition(pos);
        String[] beats = new String[c.getCount()];

        while (c.moveToNext()) {
            pos++;
            // TODO Convert entries to Beats

            bpm = c.getInt(bpm_index);
            meter_numerator = c.getInt(meter_numerator_index);
            meter_denominator = c.getInt(meter_denominator_index);
            bars = c.getInt(bars_index);

            String beat_placeholder = bpm + " " + meter_numerator + " "
                                    + meter_denominator + " " + bars;

            beats[pos] = beat_placeholder;
        }

        return beats;
    }

}
