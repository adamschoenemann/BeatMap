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

    // packed is {BPM, meter_num, meter_denom, n_bars}
    public Sequence(int[] packed) {
        this(packed[0], packed[1], packed[2], packed[3]);
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

    public int[] pack() {
        return new int[]{this.beat.getBPM(),
                         this.beat.getMeter().getNumerator(),
                         this.beat.getMeter().getDenominator(),
                         this.bars
        };
    }

    @Override
    public String toString() {
        return beat.toString() + " bars: " + bars;
    }
}


