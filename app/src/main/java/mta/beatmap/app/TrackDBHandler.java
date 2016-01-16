package mta.beatmap.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import dk.aschoen.beatplanner.core.Beat;
import dk.aschoen.beatplanner.core.Meter;
import dk.aschoen.beatplanner.core.Sequence;
import dk.aschoen.beatplanner.core.Track;

/**
 * Created by Wolf on 16/01/2016.
 */
public class TrackDBHandler {

    private SQLiteDatabase db;
    private final String[] PROJECTION = {
            DBContract.TrackTable.COLUMN_NAME_BPM,
            DBContract.TrackTable.COLUMN_NAME_METER_NUMERATOR,
            DBContract.TrackTable.COLUMN_NAME_METER_DENOMINATOR,
            DBContract.TrackTable.COLUMN_NAME_BARS
    };
    private final String TABLE_NAME = DBContract.TrackTable.TABLE_NAME;
    private final String WHERE_SESSION = DBContract.TrackTable.COLUMN_NAME_SESSION_ID + "=?";

    private final String BPM_NAME = DBContract.TrackTable.COLUMN_NAME_BPM;
    private final String METER_NUMERATOR_NAME = DBContract.TrackTable.COLUMN_NAME_METER_NUMERATOR;
    private final String METER_DENOMINATOR_NAME = DBContract.TrackTable.COLUMN_NAME_METER_DENOMINATOR;
    private final String BARS_NAME = DBContract.TrackTable.COLUMN_NAME_BARS;


    public TrackDBHandler(Context context) {
        TrackDB mDbHelper = new TrackDB(context);
        db = mDbHelper.getWritableDatabase();
    }

    public void upsert(String session_id, Track track) {
        delete(session_id);
        insert(session_id, track);
    }

    private void delete(String session_id) {
        db.delete(TABLE_NAME, WHERE_SESSION, new String[]{session_id});
    }

    private void insert(String session_id, Track track) {

        for (int i = 0; i < track.size(); i++) {
            ContentValues values = extractContent(track.get(i));

            values.put(DBContract.TrackTable.COLUMN_NAME_SESSION_ID, session_id);
            values.put(DBContract.TrackTable.COLUMN_NAME_BEAT_ID, i);

            db.insert(TABLE_NAME, null, values);
        }
    }

    private ContentValues extractContent(Sequence seq) {
        ContentValues values = new ContentValues();

        Beat beat = seq.getBeat();
        Meter meter = beat.getMeter();

        values.put(BPM_NAME, beat.getBPM());
        values.put(METER_NUMERATOR_NAME, meter.getNumerator());
        values.put(METER_DENOMINATOR_NAME, meter.getDenominator());
        values.put(BARS_NAME, seq.getBars());

        return values;
    }

    public Track getSession(String session_id) {

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

        c.moveToPosition(-1); // Start just before the first value

        Track track = new Track();

        // Loop while there is a next row
        while (c.moveToNext()) {
            bpm = c.getInt(bpm_index);
            meter_numerator = c.getInt(meter_numerator_index);
            meter_denominator = c.getInt(meter_denominator_index);
            bars = c.getInt(bars_index);

            Sequence seq = new Sequence(bpm, meter_numerator, meter_denominator, bars);
            track.appendSequence(seq);
        }

        return track;
    }

}
