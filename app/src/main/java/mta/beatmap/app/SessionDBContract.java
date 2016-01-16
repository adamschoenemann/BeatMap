package mta.beatmap.app;

import android.provider.BaseColumns;

/**
 * Created by Wolf on 16/01/2016.
 */

public final class SessionDBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public SessionDBContract() {}

    /* Inner class that defines the table contents */
    public static abstract class SessionsTable implements BaseColumns {
        public static final String TABLE_NAME = "sessions";
        public static final String COLUMN_NAME_SESSION_ID = "sessionid";
        public static final String COLUMN_NAME_BEAT_ID = "beatid";
        public static final String COLUMN_NAME_BPM = "bpm";
        public static final String COLUMN_NAME_METER_NUMERATOR = "meter_numerator";
        public static final String COLUMN_NAME_METER_DENOMINATOR = "meter_denominator";
        public static final String COLUMN_NAME_BARS = "bars"; // number of bars
    }
}
