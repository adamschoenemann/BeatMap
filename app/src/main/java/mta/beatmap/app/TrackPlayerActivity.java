package mta.beatmap.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import dk.aschoen.beatplanner.core.Beat;
import dk.aschoen.beatplanner.core.Meter;
import dk.aschoen.beatplanner.core.Track;
import dk.aschoen.beatplanner.core.TrackPlayer;
import mta.beatmap.app.metro.SimpleMetronome;

public class TrackPlayerActivity extends AppCompatActivity {

    private Track track;
    private TrackPlayer trackPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);

        track = new Track();
        track.appendSequence(new Beat(new Meter(4,4), 120), 4);
        track.appendSequence(new Beat(new Meter(3,4), 120), 4);
        track.appendSequence(new Beat(new Meter(3,4), 200), 8);
        track.appendSequence(new Beat(new Meter(2,8), 200), 3);
        trackPlayer = new TrackPlayer(track, new SimpleMetronome(this));
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

    public void playTrack(View view) {
        if (trackPlayer.isPlaying()) {
            ((Button)view).setText("Play Track");
            trackPlayer.stop();
        } else {
            ((Button)view).setText("Stop Track");
            trackPlayer.playAsync();
        }
    }
}
