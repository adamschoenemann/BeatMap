//
// Translated by CS2J (http://www.cs2j.com): 2014-07-29 22:02:11
//

package dk.aschoen.beatplanner.core;



public class Meter {
    private final int upper;
    private final int lower;

    public Meter(int u, int l) {
        upper = u;
        if (l == 2 || l == 4 || l == 8 || l == 16 || l == 32)
            lower = l;
        else
            throw new IllegalArgumentException("Invalid lower meter given");
    }

    public Meter(Meter m) {
        this(m.getUpper(), m.getLower());
    }

    @Override
    public String toString() {
        try {
            return String.format("%d/%d", getUpper(), getLower());
        } catch (RuntimeException e) {
            throw e;
        }

    }

    public static final Meter Common = new Meter(4, 4);

    public int getUpper() {
        return upper;
    }

    public int getNumerator() {
        return getUpper();
    }

    public int getLower() {
        return lower;
    }

    public int getDenominator() {
        return getLower();
    }

    public Meter setUpper(int val) {
        return new Meter(val, this.getLower());
    }
    public Meter setLower(int val) {
        return new Meter(this.getUpper(), val);
    }
}


