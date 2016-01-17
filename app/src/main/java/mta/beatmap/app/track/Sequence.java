//
// Translated by CS2J (http://www.cs2j.com): 2014-07-29 22:02:11
//

package mta.beatmap.app.track;


import mta.beatmap.app.metro.config.Beat;
import mta.beatmap.app.metro.config.Meter;

public class Sequence
{
    private final Beat beat;
    public final int bars;

    public Sequence(Beat beat, int bars) {
        this.beat = beat;
        this.bars = bars;
    }

    public Sequence(int BPM, int meter_numerator, int meter_denominator, int bars) {
        this(new Beat(new Meter(meter_numerator, meter_denominator), BPM), bars);
    }

    public Sequence setBars(int bars) {
        return new Sequence(this.getBeat(), bars);
    }

    public  Sequence setBeat(Beat beat) {
        return new Sequence(beat, this.getBars());
    }

    public int getBars()
    {
        return bars;
    }

    public Beat getBeat() {
        return beat;
    }

    @Override
    public String toString() {
        return beat.toString() + " bars: " + bars;
    }
}


