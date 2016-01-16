//
// Translated by CS2J (http://www.cs2j.com): 2014-07-29 22:02:11
//

package dk.aschoen.beatplanner.core;


import dk.aschoen.beatplanner.core.Beat;

public class Sequence
{
    private final Beat beat;
    public final int bars;

    public Sequence(Beat beat, int bars) {
        this.beat = beat;
        this.bars = bars;
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
}


