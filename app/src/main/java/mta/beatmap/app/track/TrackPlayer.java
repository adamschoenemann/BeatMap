//
// Translated by CS2J (http://www.cs2j.com): 2014-07-29 22:02:12
//

package mta.beatmap.app.track;


import mta.beatmap.app.metro.Metronome;
import mta.beatmap.app.metro.config.Beat;

public class TrackPlayer implements Metronome.OnBeatEventListener {
    private Metronome metro;

    private int sequenceIndex = 0;
    private Thread thread;
    private int nextSequenceBar;

    private TrackFinishedListener onTrackFinishedListener;

    public final Track track;

    public TrackPlayer(Track track, Metronome metro) {
        this.track = track;
        this.metro = metro;
        metro.onBeatEvent(this);
    }

    public void setOnTrackFinishedListener(TrackFinishedListener onTrackFinishedListener) {
        this.onTrackFinishedListener = onTrackFinishedListener;
    }

    public TrackPlayer(Track track) {
        this(track, new Metronome());
    }

    public int getSequenceIndex() {
        return sequenceIndex;
    }

    public void playAsync() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                play();
            }
        });
        thread.start();
    }

    public boolean isPlaying() {
        return metro.isPlaying();
    }

    public synchronized void play() {
        metro.reset();
        Sequence[] sequences = track.getSequences();
        Sequence seq = sequences[getSequenceIndex()];
        Beat beat = seq.getBeat();
        nextSequenceBar = seq.getBars();
        metro.setBeat(beat);

        metro.start();
    }

    @Override
    public void onBeatEvent(int beatIndex, int beats, int bars) {
        System.out.println("beatIndex: " + beatIndex + ", beats: " + beats + ", bars: " + bars);
        System.out.println("sequenceIndex: " + sequenceIndex + ", nextSequenceBar: " + nextSequenceBar);

        if (shouldAdvanceSequence(bars)) {
            this.sequenceIndex++;
            if (isFinished()) {
                onTrackFinished(this.sequenceIndex, beats, bars);
                stop();
                return;
            }
            advanceSequence();
        }
    }

    private void advanceSequence() {
        Sequence seq = track.getSequences()[sequenceIndex];
        Beat beat = seq.getBeat();
        metro.setBeat(beat);
        nextSequenceBar += seq.getBars();
    }

    private boolean isFinished() {
        return sequenceIndex >= track.getSequences().length;
    }

    private boolean shouldAdvanceSequence(int bars) {
        return bars == nextSequenceBar;
    }

    public void stop() {
        metro.stop();
        metro.reset();
        this.sequenceIndex = 0;
    }

    private void onTrackFinished(int sequenceIndex, int beats, int bars) {
        onTrackFinishedListener.onFinished(sequenceIndex, beats, bars);
    }

    private int totalSequenceBars() {
        int sum = 0;
        for (Sequence seq :
                track.getSequences()) {
            sum += seq.getBars();
        }
        return sum;
    }

    public interface TrackFinishedListener {
       void onFinished(int sequenceIndex, int beats, int bars);
    }
}


