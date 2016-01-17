package mta.beatmap.app.sequenceListRV;

import mta.beatmap.app.track.Sequence;

/**
 * Created by mickneupart on 17/01/16.
 */
public class SequenceVM{

    private int imgBar;
    private int imgMeter;
    private int imgBpm;
    private Sequence seq;

    public SequenceVM(Sequence seq, int imgBar, int imgMeter, int imgBpm){
        this.seq = seq;
        this.imgBar = imgBar;
        this.imgMeter = imgMeter;
        this.imgBpm = imgBpm;
    }

    public int getImgBar() {
        return imgBar;
    }

    public int getImgMeter() {
        return imgMeter;
    }

    public int getImgBpm() {
        return imgBpm;
    }

    public Sequence getSeq(){
        return seq;
    }

}
