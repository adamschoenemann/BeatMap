package mta.beatmap.app.persistence.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mta.beatmap.app.metro.config.Beat;
import mta.beatmap.app.metro.config.Meter;
import mta.beatmap.app.persistence.db.models.TrackModel;
import mta.beatmap.app.track.Sequence;
import mta.beatmap.app.track.Track;

import java.util.ArrayList;
import java.util.List;

import static mta.beatmap.app.persistence.db.DBContract.*;

/**
 * Created by Wolf on 16/01/2016.
 */
public class DBHandler {

    private SQLiteDatabase db;
    private final String[] SEQUENCE_PROJECTION = {
            SequenceTable.COLUMN_NAME_BPM,
            SequenceTable.COLUMN_NAME_METER_NUMERATOR,
            SequenceTable.COLUMN_NAME_METER_DENOMINATOR,
            SequenceTable.COLUMN_NAME_BARS
    };
    private final String WHERE_TRACK_ID = TrackTable.COLUMN_NAME_ID + "=?";

    private final String BPM_NAME = SequenceTable.COLUMN_NAME_BPM;
    private final String METER_NUMERATOR_NAME = SequenceTable.COLUMN_NAME_METER_NUMERATOR;
    private final String METER_DENOMINATOR_NAME = SequenceTable.COLUMN_NAME_METER_DENOMINATOR;
    private final String BARS_NAME = SequenceTable.COLUMN_NAME_BARS;


    public DBHandler(Context context) {
        TrackDB mDbHelper = new TrackDB(context);
        db = mDbHelper.getWritableDatabase();
    }

    public void upsertTrack(String trackID, Track track) {
        deleteTrack(trackID);
        insertTrack(trackID, track);
    }

    private void deleteTrack(String trackID) {
        db.delete(TrackTable.TABLE_NAME, WHERE_TRACK_ID, new String[]{trackID});
    }

    private void insertTrack(String title, Track track) {
        ContentValues values = new ContentValues();
        values.put(TrackTable.COLUMN_NAME_TITLE, title);

        long trackId = db.insert(TrackTable.TABLE_NAME, null, values);
        for (Sequence seq : track.getSequences()) {
            ContentValues sv = new ContentValues();
            sv.put(SequenceTable.COLUMN_NAME_BARS, seq.getBars());
            sv.put(SequenceTable.COLUMN_NAME_BPM, seq.getBeat().getBPM());
            sv.put(SequenceTable.COLUMN_NAME_TRACK_ID, trackId);
            sv.put(SequenceTable.COLUMN_NAME_METER_DENOMINATOR, seq.getBeat().getMeter().getDenominator());
            sv.put(SequenceTable.COLUMN_NAME_METER_NUMERATOR, seq.getBeat().getMeter().getNumerator());
            db.insert(SequenceTable.TABLE_NAME, null, sv);
        }

/*        for (int i = 0; i < track.size(); i++) {
            ContentValues values = extractContent(track.get(i));

            values.put(SequenceTable.COLUMN_NAME_ID, title);
            values.put(SequenceTable.COLUMN_NAME_BEAT_ID, i);

            db.insert(TABLE_NAME, null, values);
        }*/
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

    public List<TrackModel> getAllTracks() {
        Cursor c = db.query(
                TrackTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        c.moveToPosition(-1);

        List<TrackModel> tracks = new ArrayList();
        while(c.moveToNext()) {
            int id = c.getInt(0);
            String title = c.getString(1);
            TrackModel track = new TrackModel(id, title);
            for (Sequence seq : getSequences(id)) {
                track.appendSequence(seq);
            }
            tracks.add(track);
        }
        return tracks;
    }

    public List<Sequence> getSequences(int trackId) {

        Cursor seqCursor = db.query(
                SequenceTable.TABLE_NAME,
                null,
                SequenceTable.COLUMN_NAME_TRACK_ID + "=?",
                new String[]{String.valueOf(trackId)},
                null,
                null,
                null
        );

        List<Sequence> seqs = new ArrayList<Sequence>();

        seqCursor.moveToPosition(-1);
        while(seqCursor.moveToNext()) {
            int bpm_index = seqCursor.getColumnIndexOrThrow(BPM_NAME);
            int meter_numerator_index = seqCursor.getColumnIndexOrThrow(METER_NUMERATOR_NAME);
            int meter_denominator_index = seqCursor.getColumnIndexOrThrow(METER_DENOMINATOR_NAME);
            int bars_index = seqCursor.getColumnIndexOrThrow(BARS_NAME);

            int bpm = seqCursor.getInt(bpm_index);
            int meter_numerator = seqCursor.getInt(meter_numerator_index);
            int meter_denominator = seqCursor.getInt(meter_denominator_index);
            int bars = seqCursor.getInt(bars_index);

            Sequence seq = new Sequence(bpm, meter_numerator, meter_denominator, bars);
            seqs.add(seq);
        }
        return seqs;
    }


    public Track getTrack(String trackID) {

        String[] selectionVals = {trackID};

        Cursor c = db.query(
                TrackTable.TABLE_NAME,    // The table to query
                SEQUENCE_PROJECTION,    // The columns to return
                WHERE_TRACK_ID, // The string for the WHERE clause
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
