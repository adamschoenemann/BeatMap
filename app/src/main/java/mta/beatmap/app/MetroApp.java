package mta.beatmap.app;

import android.app.Application;
import android.util.Log;
import mta.beatmap.app.persistence.db.DBHandler;
import mta.beatmap.app.persistence.db.models.TrackModel;
import mta.beatmap.app.track.Sequence;
import mta.beatmap.app.track.Track;

import java.util.List;

/**
 * Created by Adam on 2016-01-17.
 */
public class MetroApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MetroApp", "Application Created");

        populateTestData();
    }

    private void populateTestData() {
        DBHandler tdh = new DBHandler(this);

        String trackName = "1";
        Track track = new Track();
        track.appendSequence(new Sequence(100, 4, 4, 10));
        track.appendSequence(new Sequence(150, 3, 8, 20));
        tdh.upsertTrack(trackName, track);

        trackName = "2";
        track = new Track();
        track.appendSequence(new Sequence(100, 4, 8, 4));
        track.appendSequence(new Sequence(160, 3, 4, 4));
        tdh.upsertTrack(trackName, track);

        trackName = "3";
        track = new Track();
        track.appendSequence(new Sequence(100, 6, 8, 2));
        track.appendSequence(new Sequence(160, 4, 4, 2));
        track.appendSequence(new Sequence(220, 4, 8, 10));
        tdh.upsertTrack(trackName, track);

        List<TrackModel> tracks = tdh.getAllTracks();
        Log.d("DBDEBUG", "tracks in DB: " + tracks.size());
        for (TrackModel dbTrack : tracks) {
            Log.d("DBDEBUG", "Track " + dbTrack.id + " has " + dbTrack.getSequences().length + " sequences.");
        }

        int id = tracks.get(0).id;

        // get a single track
        TrackModel tm = tdh.getTrack(id);
        assert tm != null;
        assert tm.id == 1;
        assert tm.getTitle() == "1";
        Sequence[] seqs = tm.getSequences();
        assert seqs.length == 2;
        assert seqs[0].getBeat().getBPM() == 100;
        assert seqs[0].getBeat().getMeter().getUpper() == 4;
        assert seqs[0].getBeat().getMeter().getLower() == 4;
        assert seqs[0].getBars() == 4;
    }
}
