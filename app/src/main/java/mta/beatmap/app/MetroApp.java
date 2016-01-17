package mta.beatmap.app;

import android.app.Application;
import android.util.Log;
import mta.beatmap.app.metro.Metronome;
import mta.beatmap.app.persistence.db.TrackDBHandler;
import mta.beatmap.app.track.Sequence;
import mta.beatmap.app.track.Track;

/**
 * Created by Adam on 2016-01-17.
 */
public class MetroApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MetroApp", "Application Created");

    }

    private void populateTestData() {
        TrackDBHandler tdh = new TrackDBHandler(this);

        String trackID = "1";
        Track track = new Track();
        track.appendSequence(new Sequence(100, 4, 4, 10));
        track.appendSequence(new Sequence(150, 3, 8, 20));
        tdh.upsert(trackID, track);

        trackID = "2";
        track = new Track();
        track.appendSequence(new Sequence(100, 4, 8, 4));
        track.appendSequence(new Sequence(160, 3, 4, 4));
        tdh.upsert(trackID, track);

        trackID = "3";
        track = new Track();
        track.appendSequence(new Sequence(100, 6, 8, 2));
        track.appendSequence(new Sequence(160, 4, 4, 2));
        track.appendSequence(new Sequence(220, 4, 8, 10));
        tdh.upsert(trackID, track);

    }
}
