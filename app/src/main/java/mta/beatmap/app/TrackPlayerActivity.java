package mta.beatmap.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import mta.beatmap.app.metro.config.Beat;
import mta.beatmap.app.metro.config.Meter;
import mta.beatmap.app.track.Track;
import mta.beatmap.app.track.TrackPlayer;
import mta.beatmap.app.metro.SimpleMetronome;

@Deprecated
/**
 * @deprecated Used for testing only!
 */
public class TrackPlayerActivity extends AppCompatActivity implements TrackPlayer.TrackFinishedListener {

    private Track track;
    private TrackPlayer trackPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);

        track = new Track();
        track.appendSequence(new Beat(new Meter(4,4), 120), 4);
        track.appendSequence(new Beat(new Meter(3,4), 200), 4);
        track.appendSequence(new Beat(new Meter(3,4), 200), 8);
        track.appendSequence(new Beat(new Meter(2,8), 200), 3);
        trackPlayer = new TrackPlayer(track, new SimpleMetronome(this));
        trackPlayer.setOnTrackFinishedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toggleButton(boolean shouldplay)
    {
        if (shouldplay) {
            ((Button)findViewById(R.id.playTrack)).setText("Stop Track");
            trackPlayer.play();
        } else {
            ((Button)findViewById(R.id.playTrack)).setText("Play Track");
            trackPlayer.stop();
        }
    }

    public void playTrack(View view) {
        toggleButton(!trackPlayer.isPlaying());
    }

    @Override
    public void onFinished(int sequenceIndex, int beats, int bars) {
        Log.i(getPackageName(), "track finished");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((Button)findViewById(R.id.playTrack)).setText("Play Track");
            }
        });
    }
}
