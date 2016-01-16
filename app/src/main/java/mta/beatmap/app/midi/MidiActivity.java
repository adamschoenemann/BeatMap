package mta.beatmap.app.midi;

import java.io.*;
import java.util.ArrayList;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;
import mta.beatmap.app.R;
import com.leff.midi.*;

public class MidiActivity extends Activity {

    private String file = "example_midi.mid";
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midi);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        createMIDIFileWithLib();
        playNewMIDIFile();
    }

    public void createMIDIFileWithLib() {
        // create some MIDI tracks
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack  = new MidiTrack();

        // add events to the track
        // Track 0 is tempo map
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo.setBpm(228);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

        // add some notes to track 1
        final int NOTE_COUNT = 80;
        for(int i = 0; i < NOTE_COUNT; i++) {
            int channel = 0;
            int pitch = 1 + i;
            int velocity = 100;
            long tick = i * 480;
            long duration = 120;

            noteTrack.insertNote(channel, pitch, velocity, tick, duration);
        }

        // create a midi file
        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        // write to file
        try {
            FileOutputStream fout = openFileOutput(file, MODE_PRIVATE);
            midi.writeToFileStream(fout);
        } catch(IOException e) {
            System.err.println("Midi file could not be written");
            e.printStackTrace();
        }
    }


    public void play(View view) {
  /* Triggered by a button defined in activity_main.xml as
  <Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:onClick="play"
    android:text="Play MIDI" />
  */
        playNewMIDIFile();
    }

    public void playNewMIDIFile() {
        try {
            String filename = getFilesDir() + "/" + file;
            File midifile = new File(filename);
            FileInputStream inputStream = new FileInputStream(midifile);
            FileDescriptor fileDescriptor = inputStream.getFD();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(fileDescriptor);
            mediaPlayer.setDa
            inputStream.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(View view) {
        mediaPlayer.stop();
    }
}
