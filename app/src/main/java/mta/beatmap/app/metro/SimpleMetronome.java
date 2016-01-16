package mta.beatmap.app.metro;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import dk.aschoen.beatplanner.core.Beat;
import dk.aschoen.beatplanner.core.Meter;
import dk.aschoen.beatplanner.core.Metronome;

/**
 * Created by Adam on 2016-01-16.
 */
public class SimpleMetronome extends Metronome {

    private final SoundPool soundPool;
    private int claveId;
    private int claveLowId;

    public SimpleMetronome(Context ctx)
    {
        this(ctx, new Beat(new Meter(4,4), 120));
    }

    public SimpleMetronome(Context ctx, Beat beat)
    {
        super(beat);
//        final Meter meter = new Meter(3, 4);
//        final Beat beat = new Beat(meter, 200);

        soundPool = createSoundPool();

        loadSounds(ctx, soundPool);

        this.onBeatEvent(new Metronome.OnBeatEventListener() {
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

    private void loadSounds(Context ctx, SoundPool sp) {
        claveId = sp.load(ctx, getSoundRes(ctx, "llfclave"), 1);
        claveLowId = sp.load(ctx, getSoundRes(ctx, "llfclave_low"), 1);
    }

    private SoundPool createSoundPool() {
        final SoundPool.Builder builder = new SoundPool.Builder();
        final AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        builder.setAudioAttributes(attrs);

        return builder.build();
    }

    private int getSoundRes(Context ctx, String id)
    {
        final String sr = ((AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE)).getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        final String pkgName = ctx.getApplicationContext().getPackageName();
        Log.i("SAMPLE_RATE", "Sample rate: " + sr);
        return ctx.getResources().getIdentifier(id + "_" + sr, "raw", pkgName);
    }


}
