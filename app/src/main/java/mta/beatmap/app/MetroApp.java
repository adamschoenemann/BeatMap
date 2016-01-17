package mta.beatmap.app;

import android.app.Application;
import android.util.Log;
import junit.framework.Assert;
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

        tdh.truncate();
        String trackName = "Track1";
        Track track = new Track();
        track.appendSequence(new Sequence(100, 4, 4, 10));
        track.appendSequence(new Sequence(150, 3, 8, 20));
        tdh.upsertTrack(trackName, track);

        trackName = "Track2";
        track = new Track();
        track.appendSequence(new Sequence(100, 4, 8, 4));
        track.appendSequence(new Sequence(160, 3, 4, 4));
        tdh.upsertTrack(trackName, track);

        trackName = "Track3";
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
        Assert.assertNotNull(tm);
        Assert.assertEquals(tm.id, tracks.get(0).id);
        Assert.assertEquals(tm.getTitle(), "Track1");

        Sequence[] seqs = tm.getSequences();
        Assert.assertEquals(2, seqs.length);
        Assert.assertEquals(seqs[0].getBeat().getBPM(), 100);
        Assert.assertEquals(seqs[0].getBeat().getMeter().getUpper(), 4);
        Assert.assertEquals(seqs[0].getBeat().getMeter().getLower(), 4);
        Assert.assertEquals(seqs[0].getBars(), 10);

        Assert.assertEquals(seqs[1].getBeat().getBPM(), 150);
        Assert.assertEquals(seqs[1].getBeat().getMeter().getUpper(), 3);
        Assert.assertEquals(seqs[1].getBeat().getMeter().getLower(), 8);
        Assert.assertEquals(seqs[1].getBars(), 20);
    }
}
