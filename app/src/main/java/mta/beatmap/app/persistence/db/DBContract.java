package mta.beatmap.app.persistence.db;

import android.provider.BaseColumns;

/**
 * Created by Wolf on 16/01/2016.
 */

public final class DBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DBContract() {}

    /* Sessions table contents */
    public static abstract class SequenceTable implements BaseColumns {
        public static final String TABLE_NAME = "sequences";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TRACK_ID = "track_id";
        public static final String COLUMN_NAME_BPM = "bpm";
        public static final String COLUMN_NAME_METER_NUMERATOR = "meter_numerator";
        public static final String COLUMN_NAME_METER_DENOMINATOR = "meter_denominator";
        public static final String COLUMN_NAME_BARS = "bars"; // number of bars
    }

    public static abstract class TrackTable implements BaseColumns {
        public static final String TABLE_NAME = "tracks";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
