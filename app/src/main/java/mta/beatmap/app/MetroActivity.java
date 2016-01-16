package mta.beatmap.app;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import dk.aschoen.beatplanner.core.Beat;
import dk.aschoen.beatplanner.core.Meter;
import dk.aschoen.beatplanner.core.Metronome;

public class MetroActivity extends AppCompatActivity {

    private Metronome metronome;
    private SoundPool soundPool;
    private int claveId;
    private int claveLowId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro);

        final Meter meter = new Meter(3, 4);
        final Beat beat = new Beat(meter, 200);
        this.metronome = new Metronome(beat);
        final SoundPool.Builder builder = new SoundPool.Builder();
        final AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        builder.setAudioAttributes(attrs);

        soundPool = builder.build();


        final Context context = this.getApplicationContext();

        claveId = soundPool.load(context, getSoundRes("llfclave"), 1);
        claveLowId = soundPool.load(context, getSoundRes("llfclave_low"), 1);
    }

    private int getSoundRes(String id)
    {
        final String sr = ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        final String pkgName = getApplicationContext().getPackageName();
        Log.i("SAMPLE_RATE", "Sample rate: " + sr);
        return getResources().getIdentifier(id + "_" + sr, "raw", pkgName);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_metro, menu);
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

    public void startMetronome(View view) {
        metronome.start();
        metronome.onBeatEvent(new Metronome.OnBeatEventListener() {
            @Override
            public void onBeatEvent(int index, int beats, int bars) {
                int sid;
                if (index == 1)
                    sid = claveId;
                else
                    sid = claveLowId;
                soundPool.play(sid, 1.0f, 1.0f, 1, 0, 1.0f);
            }
        });
    }

    public void stopMetronome(View view) {
        metronome.stop();
        metronome.removeBeatEventListeners();
    }
}
