//
// Translated by CS2J (http://www.cs2j.com): 2014-07-29 22:02:12
//

package mta.beatmap.app.track;

import mta.beatmap.app.metro.config.Beat;

import java.util.ArrayList;

public class Track
{
    private final ArrayList<Sequence> sequences = new ArrayList<Sequence>();
    public Sequence[] getSequences() {
        Sequence[] result = new Sequence[sequences.size()];
        sequences.toArray(result);
        return result;
    }

    public void appendSequence(Beat beat, int reps) {
        appendSequence(new Sequence(beat, reps));
    }

    public void appendSequence(Sequence sequence) { sequences.add(sequence); }

    public void prependSequence(Beat beat, int reps) {
        sequences.add(0, new Sequence(beat, reps));
    }

    public void insertSequence(Beat beat, int reps, int i) {
        sequences.add(i, new Sequence(beat, reps));
    }

    public void set(int index, Sequence sequence) {
        this.sequences.set(index, sequence);
    }

    public void remove(int index) {
        this.sequences.remove(index);
    }


    // METER BPM REPS
    // 4/4   180 4
    private static Sequence parseSequence(String line) {
        String[] splits = line.split(" ");
        Beat beat = Beat.parse(splits);
        int reps = Integer.valueOf(splits[2]);
        return new Sequence(beat, reps);
    }

    public static Track parse(String[] lines) {
        Track track = new Track();
        Sequence seq;
        for (String line : lines) {
            seq = parseSequence(line);
            track.appendSequence(seq.getBeat(), seq.getBars());
        }
        return track;
    }

    public static Track parse(String input) {
        return parse(input.split(" "));
    }

    public String compose() {
        StringBuilder sb = new StringBuilder();
        for (Object __dummyForeachVar0 : sequences)
        {
            Sequence seq = (Sequence)__dummyForeachVar0;
            sb.append(String.format("%s %d %d\n", seq.getBeat().getMeter(), seq.getBeat().getBPM(), seq.getBars()));
        }
        return sb.toString();
    }

    public int size() {
        return sequences.size();
    }

    public Sequence get(int index) {
        return sequences.get(index);
    }

}


