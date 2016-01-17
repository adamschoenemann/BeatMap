//
// Translated by CS2J (http://www.cs2j.com): 2014-07-29 22:02:11
//

package mta.beatmap.app.metro;

import mta.beatmap.app.metro.config.Beat;
import mta.beatmap.app.metro.config.Meter;

import java.util.ArrayList;


public class Metronome
{

    private static class BeatEnumerator
    {
        public static class Info   
        {
            public final int index;
            public final long duration;
            public Info(int i, long d) {
                index = i;
                duration = d;
            }
        
        }

        public final Beat beat;
        private int index = 1;
        public BeatEnumerator(Beat beat) {
            this.beat = beat;
        }

        public Info next() {
            int retIndex = index;
            index++;
            if (index > beat.getMeter().getUpper())
                index = 1;
             
            long fourthDur = 60000 / beat.getBPM();
            long dur = (long)(fourthDur * (4 / (float) beat.getMeter().getLower()));
            return new Info(retIndex,dur);
        }
    
    }

    private int beats = 0;
    public int getBeats() {
        return beats;
    }

    public synchronized void setBeats(int value) {
        beats = value;
    }

    private int bars = 0;
    public int getBars() {
        return bars;
    }

    public synchronized void setBars(int value) {
        bars = value;
    }

    public Beat getBeat() {
        return bEnum.beat;
    }

    public synchronized void setBeat(Beat value) {
        bEnum = new BeatEnumerator(value);
    }

    public int getBPM() {
        return getBeat().getBPM();
    }


    public synchronized void setBPM(int value) {
        int oldBPM = getBeat().getBPM();
        this.setBeat(new Beat(getBeat().getMeter(), value));
        for(OnBPMChangedListener h : onBpmChangedListeners)
            h.onBPMChanged(oldBPM, value);
    }

    public Meter getMeter() {
        return getBeat().getMeter();
    }

    public synchronized void setMeter(Meter value) {
        setBeat(new Beat(value, getBeat().getBPM()));
    }

    private Thread thread = new Thread();
    private BeatEnumerator bEnum;
    private boolean shouldStop = false;

    // -------------------------------------------------------- //
    public static interface OnBeatEventListener {
        public void onBeatEvent(int index, int beats, int bars);
    }
    private ArrayList<OnBeatEventListener> onBeatEventListeners = new ArrayList<OnBeatEventListener>();
    public void onBeatEvent(OnBeatEventListener h) {
        onBeatEventListeners.add(h);
    }

    // -------------------------------------------------------- //
    public static interface OnBPMChangedListener {
        public void onBPMChanged(int oldBPM, int newBPM);
    }
    private ArrayList<OnBPMChangedListener> onBpmChangedListeners = new ArrayList<OnBPMChangedListener>();
    public void onBPMChangedEvent(OnBPMChangedListener h) {
        onBpmChangedListeners.add(h);
    }
    // -------------------------------------------------------- //



    public Metronome(Beat beat) {

        bEnum = new BeatEnumerator(beat);
        setBeats(0);
    }

    public Metronome() {
        this(new Beat(Meter.Common,60));
    }

    public boolean isPlaying() {
        return (thread != null && thread.isAlive());
    }

    public void start() {
        if (thread != null && thread.isAlive())
            return ;
         
        // already running
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                loop();
            }
        });
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    public synchronized boolean toggle() {
        if (isPlaying())
        {
            stop();
            return false;
        }
         
        start();
        return true;
    }

    public synchronized void stop() {
        shouldStop = true;
        return;
    }

    public void reset() {
        setBeats(0);
        setBars(0);
        bEnum = new BeatEnumerator(bEnum.beat);
    }

    public void removeBeatEventListeners() {
        this.onBeatEventListeners.clear();
    }

    public void restart() {
        reset();
        if (thread.isAlive() == false)
            start();
         
    }

    private void loop() {
        //Thread.Sleep(200); // HACK to get first beat audible
        Metronome.BeatEnumerator.Info info;
        while (shouldStop == false) {
            info = bEnum.next();
            if (info.index == getBeat().getMeter().getUpper()) {
                bars++;
            }
            beats++;
            for(OnBeatEventListener h : onBeatEventListeners)
                h.onBeatEvent(info.index, getBeats(), getBars());
             
//            Console.WriteLine(sw.ElapsedMilliseconds + ", index: " + info.index + ", Bars: " + getBars());
            try {
                Thread.sleep(info.duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Not very accurate, unfortunately. Alternatives appear limited
        shouldStop = false;
    }

}


